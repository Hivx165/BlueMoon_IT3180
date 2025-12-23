package com.bluemoon.controllers;

import com.bluemoon.models.NhanKhauModel;
import com.bluemoon.services.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class NhanKhauController {

    @FXML
    private TableView<NhanKhauModel> nhanKhauTable; // Bảng hiển thị

    @FXML
    private TableColumn<NhanKhauModel, String> colCCCD;
    @FXML
    private TableColumn<NhanKhauModel, String> colHoTen;
    @FXML
    private TableColumn<NhanKhauModel, Integer> colTuoi;
    @FXML
    private TableColumn<NhanKhauModel, String> colGioiTinh;
    @FXML
    private TableColumn<NhanKhauModel, String> colSDT;
    @FXML
    private TableColumn<NhanKhauModel, String> colMaHoKhau;
    @FXML
    private TableColumn<NhanKhauModel, String> colQuanHe;

    @FXML
    private TextField searchField;

    // Danh sách chứa dữ liệu nhân khẩu
    private ObservableList<NhanKhauModel> nhanKhauList = FXCollections.observableArrayList();

    // Hàm này tự động chạy khi giao diện được mở
    public void initialize() {
        // 1. Cấu hình các cột trong bảng (Map với tên thuộc tính trong Model)
        colCCCD.setCellValueFactory(new PropertyValueFactory<>("soCMND_CCCD"));
        colHoTen.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        colTuoi.setCellValueFactory(new PropertyValueFactory<>("tuoi"));
        colGioiTinh.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
        colSDT.setCellValueFactory(new PropertyValueFactory<>("soDT"));
        colMaHoKhau.setCellValueFactory(new PropertyValueFactory<>("maHoKhau"));
        colQuanHe.setCellValueFactory(new PropertyValueFactory<>("quanHe"));

        // 2. Tải dữ liệu từ CSDL lên bảng
        loadDataFromDB();
    }

    private void loadDataFromDB() {
        nhanKhauList.clear(); // Xóa dữ liệu cũ
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Câu lệnh SQL lấy toàn bộ nhân khẩu
            String sql = "SELECT * FROM nhankhau";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                NhanKhauModel nk = new NhanKhauModel(
                        rs.getString("SoCMND_CCCD"),
                        rs.getString("MaHoKhau"),
                        rs.getString("HoTen"), // Chú ý: SQL Server dùng Nvarchar nên Java tự hiểu UTF-8
                        rs.getInt("Tuoi"),
                        rs.getString("GioiTinh"),
                        rs.getString("SoDT"),
                        rs.getString("QuanHe"),
                        rs.getInt("TamTru"),
                        rs.getInt("TamVang")
                );
                nhanKhauList.add(nk);
            }
            // Đưa list vào bảng
            nhanKhauTable.setItems(nhanKhauList);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể tải dữ liệu: " + e.getMessage());
        }
    }

    @FXML
    private void handleRefresh() {
        loadDataFromDB();
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().toLowerCase();
        // Lọc dữ liệu trên list hiện có (đơn giản hơn query lại DB)
        ObservableList<NhanKhauModel> filteredList = FXCollections.observableArrayList();

        for (NhanKhauModel nk : nhanKhauList) {
            if (nk.getHoTen().toLowerCase().contains(keyword) ||
                    nk.getSoCMND_CCCD().contains(keyword)) {
                filteredList.add(nk);
            }
        }
        nhanKhauTable.setItems(filteredList);
    }

    @FXML
    private void handleAdd() {
        showAlert("Thông báo", "Chức năng Thêm mới sẽ được phát triển ở bước tiếp theo.");
        // Logic mở form thêm mới sẽ viết sau
    }

    @FXML
    private void handleDelete() {
        NhanKhauModel selectedItem = nhanKhauTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Cảnh báo", "Vui lòng chọn nhân khẩu cần xóa!");
            return;
        }
        showAlert("Thông báo", "Bạn đang chọn xóa: " + selectedItem.getHoTen());
        // Logic xóa DB sẽ viết sau
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}