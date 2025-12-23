package com.bluemoon.controllers;

import com.bluemoon.models.NhanKhauModel;
import com.bluemoon.services.DatabaseConnection;
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

    @FXML
    private TableView<NhanKhauModel> nhanKhauTable;

    // Các cột tương ứng với Model
    @FXML private TableColumn<NhanKhauModel, String> colCCCD;
    @FXML private TableColumn<NhanKhauModel, String> colHoTen;
    @FXML private TableColumn<NhanKhauModel, Integer> colTuoi;
    @FXML private TableColumn<NhanKhauModel, String> colGioiTinh;
    @FXML private TableColumn<NhanKhauModel, String> colSDT;
    @FXML private TableColumn<NhanKhauModel, String> colMaHoKhau;
    @FXML private TableColumn<NhanKhauModel, String> colQuanHe;

    @FXML
    private TextField searchField;

    private ObservableList<NhanKhauModel> nhanKhauList = FXCollections.observableArrayList();

    public void initialize() {
        // 1. Cấu hình cột (Phải trùng tên biến trong NhanKhauModel)
        colCCCD.setCellValueFactory(new PropertyValueFactory<>("soCMND_CCCD"));
        colHoTen.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        colTuoi.setCellValueFactory(new PropertyValueFactory<>("tuoi"));
        colGioiTinh.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
        colSDT.setCellValueFactory(new PropertyValueFactory<>("soDT"));
        colMaHoKhau.setCellValueFactory(new PropertyValueFactory<>("maHoKhau"));
        colQuanHe.setCellValueFactory(new PropertyValueFactory<>("quanHe"));

        // 2. Load dữ liệu ban đầu
        loadDataFromDB();
    }

    // --- TẢI DỮ LIỆU TỪ SQL ---
    private void loadDataFromDB() {
        nhanKhauList.clear();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = "SELECT * FROM nhankhau";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                NhanKhauModel nk = new NhanKhauModel(
                        rs.getString("SoCMND_CCCD"),
                        rs.getString("MaHoKhau"),
                        rs.getString("HoTen"),
                        rs.getInt("Tuoi"), // Lưu ý: Nếu cột SQL là DateOfBirth thì cần tính tuổi
                        rs.getString("GioiTinh"),
                        rs.getString("SoDT"),
                        rs.getString("QuanHe"),
                        rs.getInt("TamTru"),
                        rs.getInt("TamVang")
                );
                nhanKhauList.add(nk);
            }
            nhanKhauTable.setItems(nhanKhauList);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể tải dữ liệu: " + e.getMessage());
        }
    }

    // --- TÌM KIẾM ---
    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().toLowerCase();
        ObservableList<NhanKhauModel> filteredList = FXCollections.observableArrayList();

        for (NhanKhauModel nk : nhanKhauList) {
            if (nk.getHoTen().toLowerCase().contains(keyword) ||
                    nk.getSoCMND_CCCD().toLowerCase().contains(keyword)) {
                filteredList.add(nk);
            }
        }
        nhanKhauTable.setItems(filteredList);
    }

    @FXML
    private void handleRefresh() {
        loadDataFromDB();
        searchField.clear();
    }

    // --- THÊM MỚI (Mở Form ThemNhanKhau.fxml) ---
    @FXML
    private void handleAdd() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bluemoon/views/ThemNhanKhau.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Thêm Nhân Khẩu Mới");
            stage.setScene(new Scene(root));

            // WINDOW_MODAL: Bắt buộc người dùng xử lý cửa sổ này trước khi quay lại màn hình chính
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(nhanKhauTable.getScene().getWindow());

            stage.showAndWait(); // Chờ đến khi cửa sổ đóng lại

            // Sau khi đóng form, tải lại dữ liệu để cập nhật người vừa thêm
            handleRefresh();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể mở form thêm mới: " + e.getMessage());
        }
    }

    // --- XÓA NHÂN KHẨU ---
    @FXML
    private void handleDelete() {
        NhanKhauModel selectedItem = nhanKhauTable.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            showAlert("Cảnh báo", "Vui lòng chọn nhân khẩu cần xóa!");
            return;
        }

        // Hiện hộp thoại xác nhận
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa");
        alert.setHeaderText(null);
        alert.setContentText("Bạn có chắc chắn muốn xóa: " + selectedItem.getHoTen() + " ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteNhanKhau(selectedItem.getSoCMND_CCCD());
        }
    }

    private void deleteNhanKhau(String cccd) {
        String sql = "DELETE FROM nhankhau WHERE SoCMND_CCCD = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cccd);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                showAlert("Thành công", "Đã xóa thành công!");
                handleRefresh(); // Tải lại bảng
            } else {
                showAlert("Lỗi", "Không tìm thấy dữ liệu để xóa.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi SQL", "Không thể xóa: " + e.getMessage());
        }
    }

    // --- HÀM TIỆN ÍCH ---
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}