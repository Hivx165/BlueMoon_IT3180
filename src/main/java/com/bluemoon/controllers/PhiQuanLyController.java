package com.bluemoon.controllers;

import com.bluemoon.services.DatabaseConnection;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class PhiQuanLyController {

    @FXML private TableView<PhiQuanLyDisplay> tablePhiQuanLy;
    @FXML private TableColumn<PhiQuanLyDisplay, String> colMaHo;
    @FXML private TableColumn<PhiQuanLyDisplay, Float> colDienTich;
    @FXML private TableColumn<PhiQuanLyDisplay, Float> colDonGia;
    @FXML private TableColumn<PhiQuanLyDisplay, Float> colTongTien;
    @FXML private TableColumn<PhiQuanLyDisplay, Integer> colNam;

    @FXML private TextField searchField;

    private ObservableList<PhiQuanLyDisplay> list = FXCollections.observableArrayList();

    public void initialize() {
        // Cấu hình cột
        colMaHo.setCellValueFactory(new PropertyValueFactory<>("maHo"));
        colDienTich.setCellValueFactory(new PropertyValueFactory<>("dienTich"));
        colDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));
        colTongTien.setCellValueFactory(new PropertyValueFactory<>("tongTien"));
        colNam.setCellValueFactory(new PropertyValueFactory<>("nam"));

        loadData();
    }

    private void loadData() {
        list.clear();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // JOIN bảng phiquanly và hokhau
            String sql = "SELECT pql.MaHoKhau, hk.DienTichHo, pql.GiaPhi, pql.TienNopMoiThang, pql.Nam " +
                    "FROM phiquanly pql " +
                    "JOIN hokhau hk ON pql.MaHoKhau = hk.MaHoKhau";

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(new PhiQuanLyDisplay(
                        rs.getString("MaHoKhau"),
                        rs.getFloat("DienTichHo"),
                        rs.getFloat("GiaPhi"),
                        rs.getFloat("TienNopMoiThang"),
                        rs.getInt("Nam")
                ));
            }
            tablePhiQuanLy.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearch() {
        String key = searchField.getText().toLowerCase();
        ObservableList<PhiQuanLyDisplay> filter = FXCollections.observableArrayList();
        for (PhiQuanLyDisplay p : list) {
            if (p.getMaHo().toLowerCase().contains(key)) {
                filter.add(p);
            }
        }
        tablePhiQuanLy.setItems(filter);
    }

    @FXML private void handleRefresh() { loadData(); }

    @FXML private void handleAdd() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText("Tính năng tạo phí quản lý tự động sẽ được cập nhật sau.");
        alert.showAndWait();
    }

    // Class nội bộ để hiển thị
    public static class PhiQuanLyDisplay {
        private final SimpleStringProperty maHo;
        private final SimpleFloatProperty dienTich;
        private final SimpleFloatProperty donGia;
        private final SimpleFloatProperty tongTien;
        private final SimpleIntegerProperty nam;

        public PhiQuanLyDisplay(String ma, float dt, float dg, float tong, int nam) {
            this.maHo = new SimpleStringProperty(ma);
            this.dienTich = new SimpleFloatProperty(dt);
            this.donGia = new SimpleFloatProperty(dg);
            this.tongTien = new SimpleFloatProperty(tong);
            this.nam = new SimpleIntegerProperty(nam);
        }

        public String getMaHo() { return maHo.get(); }
        public float getDienTich() { return dienTich.get(); }
        public float getDonGia() { return donGia.get(); }
        public float getTongTien() { return tongTien.get(); }
        public int getNam() { return nam.get(); }
    }
}