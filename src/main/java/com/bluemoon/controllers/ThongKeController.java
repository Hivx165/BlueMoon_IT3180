package com.bluemoon.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.bluemoon.services.DatabaseConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ThongKeController {

    @FXML private TableView<SimpleNhanKhau> tableThongKe;
    @FXML private TableColumn<SimpleNhanKhau, String> colHoTen;
    @FXML private TableColumn<SimpleNhanKhau, String> colCCCD;
    @FXML private TableColumn<SimpleNhanKhau, String> colGioiTinh;
    @FXML private TableColumn<SimpleNhanKhau, String> colDiaChi;
    @FXML private Label lblTongSo;

    private ObservableList<SimpleNhanKhau> list = FXCollections.observableArrayList();

    public void initialize() {
        // Cấu hình cột
        colHoTen.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        colCCCD.setCellValueFactory(new PropertyValueFactory<>("cccd"));
        colGioiTinh.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
        colDiaChi.setCellValueFactory(new PropertyValueFactory<>("diaChi"));

        // Tự động tải dữ liệu khi mở
        loadData();
    }

    @FXML
    public void loadData() {
        list.clear();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Lấy thông tin người + địa chỉ nhà họ đang ở
            String sql = "SELECT nk.HoTen, nk.SoCMND_CCCD, nk.GioiTinh, hk.DiaChi " +
                    "FROM nhankhau nk " +
                    "JOIN hokhau hk ON nk.MaHoKhau = hk.MaHoKhau";

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(new SimpleNhanKhau(
                        rs.getString("HoTen"),
                        rs.getString("SoCMND_CCCD"),
                        rs.getString("GioiTinh"),
                        rs.getString("DiaChi")
                ));
            }

            tableThongKe.setItems(list);
            lblTongSo.setText(String.valueOf(list.size())); // Đếm tổng số

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Class đơn giản để hiển thị
    public static class SimpleNhanKhau {
        private final SimpleStringProperty hoTen;
        private final SimpleStringProperty cccd;
        private final SimpleStringProperty gioiTinh;
        private final SimpleStringProperty diaChi;

        public SimpleNhanKhau(String ht, String cm, String gt, String dc) {
            this.hoTen = new SimpleStringProperty(ht);
            this.cccd = new SimpleStringProperty(cm);
            this.gioiTinh = new SimpleStringProperty(gt);
            this.diaChi = new SimpleStringProperty(dc);
        }

        public String getHoTen() { return hoTen.get(); }
        public String getCccd() { return cccd.get(); }
        public String getGioiTinh() { return gioiTinh.get(); }
        public String getDiaChi() { return diaChi.get(); }
    }
}