package com.bluemoon.controllers;

import com.bluemoon.services.DatabaseConnection;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;

public class PhiDongGopController {

    @FXML private TableView<PhiDongGopDisplay> tablePhiDongGop;
    @FXML private TableColumn<PhiDongGopDisplay, String> colMaHo;
    @FXML private TableColumn<PhiDongGopDisplay, String> colTenKhoanThu;
    @FXML private TableColumn<PhiDongGopDisplay, Float> colSoTien;
    @FXML private TableColumn<PhiDongGopDisplay, Date> colNgayDong;

    @FXML private TextField searchField;

    private ObservableList<PhiDongGopDisplay> list = FXCollections.observableArrayList();

    public void initialize() {
        // Cấu hình cột
        colMaHo.setCellValueFactory(new PropertyValueFactory<>("maHo"));
        colTenKhoanThu.setCellValueFactory(new PropertyValueFactory<>("tenKhoanThu"));
        colSoTien.setCellValueFactory(new PropertyValueFactory<>("soTien"));
        colNgayDong.setCellValueFactory(new PropertyValueFactory<>("ngayDong"));

        loadData();
    }

    private void loadData() {
        list.clear();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Lưu ý: Nếu tên bảng trong SQL của bạn khác (ví dụ: phidonggop), hãy sửa lại dòng này
            String sql = "SELECT * FROM donggop";

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(new PhiDongGopDisplay(
                        rs.getString("MaHoKhau"),
                        rs.getString("TenKhoanDongGop"),
                        rs.getFloat("SoTien"),
                        rs.getDate("NgayDong")
                ));
            }
            tablePhiDongGop.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearch() {
        String key = searchField.getText().toLowerCase();
        ObservableList<PhiDongGopDisplay> filter = FXCollections.observableArrayList();
        for (PhiDongGopDisplay p : list) {
            if (p.getTenKhoanThu().toLowerCase().contains(key) ||
                    p.getMaHo().toLowerCase().contains(key)) {
                filter.add(p);
            }
        }
        tablePhiDongGop.setItems(filter);
    }

    @FXML private void handleRefresh() { loadData(); }

    @FXML private void handleAdd() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText("Chức năng thêm phí đóng góp đang phát triển.");
        alert.showAndWait();
    }

    // Class nội bộ
    public static class PhiDongGopDisplay {
        private final SimpleStringProperty maHo;
        private final SimpleStringProperty tenKhoanThu;
        private final SimpleFloatProperty soTien;
        private final Date ngayDong;

        public PhiDongGopDisplay(String ma, String ten, float tien, Date ngay) {
            this.maHo = new SimpleStringProperty(ma);
            this.tenKhoanThu = new SimpleStringProperty(ten);
            this.soTien = new SimpleFloatProperty(tien);
            this.ngayDong = ngay;
        }

        public String getMaHo() { return maHo.get(); }
        public String getTenKhoanThu() { return tenKhoanThu.get(); }
        public float getSoTien() { return soTien.get(); }
        public Date getNgayDong() { return ngayDong; }
    }
}