package com.bluemoon.controllers;

import com.bluemoon.services.DatabaseConnection;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

public class NhanKhauController {

    @FXML private TableView<NhanKhauDisplay> nhanKhauTable;
    @FXML private TableColumn<NhanKhauDisplay, String> colMaHo;
    @FXML private TableColumn<NhanKhauDisplay, String> colHoTen;
    @FXML private TableColumn<NhanKhauDisplay, String> colNgaySinh;
    @FXML private TableColumn<NhanKhauDisplay, String> colGioiTinh;
    @FXML private TableColumn<NhanKhauDisplay, String> colCCCD;
    @FXML private TableColumn<NhanKhauDisplay, String> colSDT;
    @FXML private TableColumn<NhanKhauDisplay, String> colQuanHe;
    @FXML private TableColumn<NhanKhauDisplay, String> colTrangThai;
    @FXML private TableColumn<NhanKhauDisplay, String> colGhiChu;

    @FXML private TextField searchField;

    private ObservableList<NhanKhauDisplay> listNhanKhau = FXCollections.observableArrayList();

    public void initialize() {
        // Cấu hình cột bảng
        colMaHo.setCellValueFactory(new PropertyValueFactory<>("maHo"));
        colHoTen.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        colNgaySinh.setCellValueFactory(new PropertyValueFactory<>("ngaySinh"));
        colGioiTinh.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
        colCCCD.setCellValueFactory(new PropertyValueFactory<>("cccd"));
        colSDT.setCellValueFactory(new PropertyValueFactory<>("sdt"));
        colQuanHe.setCellValueFactory(new PropertyValueFactory<>("quanHe"));
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
        colGhiChu.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));

        loadData();
    }

    // --- TẢI DỮ LIỆU TỪ SQL ---
    private void loadData() {
        listNhanKhau.clear();
        String sql = "SELECT * FROM nhankhau";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Xử lý logic Trạng thái cư trú
                int tamTru = rs.getInt("TamTru");
                int tamVang = rs.getInt("TamVang");
                String trangThai = "Thường trú";

                if (tamVang == 1) {
                    trangThai = "Tạm vắng";
                } else if (tamTru == 1) {
                    trangThai = "Tạm trú";
                }

                listNhanKhau.add(new NhanKhauDisplay(
                        rs.getString("MaHoKhau"),
                        rs.getString("HoTen"),
                        rs.getDate("NgaySinh").toString(), // Chuyển Date sang String
                        rs.getString("GioiTinh"),
                        rs.getString("SoCMND_CCCD"),
                        rs.getString("SoDT"),
                        rs.getString("QuanHe"),
                        trangThai,
                        rs.getString("GhiChu")
                ));
            }
            nhanKhauTable.setItems(listNhanKhau);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể tải dữ liệu: " + e.getMessage());
        }
    }

    // --- TÌM KIẾM ---
    @FXML
    private void handleSearch() {
        String key = searchField.getText().toLowerCase();
        ObservableList<NhanKhauDisplay> filter = FXCollections.observableArrayList();

        for (NhanKhauDisplay nk : listNhanKhau) {
            if (nk.getHoTen().toLowerCase().contains(key) ||
                    nk.getCccd().toLowerCase().contains(key) ||
                    (nk.getSdt() != null && nk.getSdt().contains(key))) {
                filter.add(nk);
            }
        }
        nhanKhauTable.setItems(filter);
    }

    @FXML
    private void handleRefresh() {
        loadData();
        searchField.clear();
    }

    // --- THÊM NHÂN KHẨU (Mở form ThemNhanKhau.fxml) ---
    @FXML
    private void handleAdd() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bluemoon/views/ThemNhanKhau.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Thêm Nhân Khẩu Mới");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);

            if (nhanKhauTable.getScene() != null) {
                stage.initOwner(nhanKhauTable.getScene().getWindow());
            }

            stage.showAndWait();
            handleRefresh(); // Tải lại sau khi thêm

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể mở form thêm: " + e.getMessage());
        }
    }

    // --- XÓA NHÂN KHẨU ---
    @FXML
    private void handleDelete() {
        NhanKhauDisplay selected = nhanKhauTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Cảnh báo", "Vui lòng chọn nhân khẩu cần xóa!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa");
        alert.setHeaderText(null);
        alert.setContentText("Bạn có chắc muốn xóa: " + selected.getHoTen() + " (" + selected.getCccd() + ")?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteNhanKhau(selected.getCccd());
        }
    }

    private void deleteNhanKhau(String cccd) {
        String sql = "DELETE FROM nhankhau WHERE SoCMND_CCCD = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cccd);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                showAlert("Thành công", "Đã xóa nhân khẩu thành công!");
                handleRefresh();
            } else {
                showAlert("Lỗi", "Không tìm thấy dữ liệu để xóa.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi SQL", "Không thể xóa: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // --- LỚP NỘI BỘ ĐỂ HIỂN THỊ DỮ LIỆU (DTO) ---
    public static class NhanKhauDisplay {
        private final SimpleStringProperty maHo;
        private final SimpleStringProperty hoTen;
        private final SimpleStringProperty ngaySinh;
        private final SimpleStringProperty gioiTinh;
        private final SimpleStringProperty cccd;
        private final SimpleStringProperty sdt;
        private final SimpleStringProperty quanHe;
        private final SimpleStringProperty trangThai; // Hiển thị: Tạm trú/Tạm vắng/Thường trú
        private final SimpleStringProperty ghiChu;

        public NhanKhauDisplay(String ma, String ten, String ngay, String gt, String cmnd, String dt, String qh, String tt, String gc) {
            this.maHo = new SimpleStringProperty(ma);
            this.hoTen = new SimpleStringProperty(ten);
            this.ngaySinh = new SimpleStringProperty(ngay);
            this.gioiTinh = new SimpleStringProperty(gt);
            this.cccd = new SimpleStringProperty(cmnd);
            this.sdt = new SimpleStringProperty(dt);
            this.quanHe = new SimpleStringProperty(qh);
            this.trangThai = new SimpleStringProperty(tt);
            this.ghiChu = new SimpleStringProperty(gc);
        }

        public String getMaHo() { return maHo.get(); }
        public String getHoTen() { return hoTen.get(); }
        public String getNgaySinh() { return ngaySinh.get(); }
        public String getGioiTinh() { return gioiTinh.get(); }
        public String getCccd() { return cccd.get(); }
        public String getSdt() { return sdt.get(); }
        public String getQuanHe() { return quanHe.get(); }
        public String getTrangThai() { return trangThai.get(); }
        public String getGhiChu() { return ghiChu.get(); }
    }
}