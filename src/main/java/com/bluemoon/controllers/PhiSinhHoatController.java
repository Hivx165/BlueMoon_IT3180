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

public class PhiSinhHoatController {

    @FXML private TableView<PhiSinhHoatDisplay> tablePhiSinhHoat;
    @FXML private TableColumn<PhiSinhHoatDisplay, String> colMaHo;
    @FXML private TableColumn<PhiSinhHoatDisplay, Integer> colThang;
    @FXML private TableColumn<PhiSinhHoatDisplay, Integer> colNam;
    @FXML private TableColumn<PhiSinhHoatDisplay, Float> colDien;
    @FXML private TableColumn<PhiSinhHoatDisplay, Float> colNuoc;
    @FXML private TableColumn<PhiSinhHoatDisplay, Float> colInternet;
    @FXML private TableColumn<PhiSinhHoatDisplay, Float> colTong;

    @FXML private TextField searchField;
    private ObservableList<PhiSinhHoatDisplay> list = FXCollections.observableArrayList();

    public void initialize() {
        colMaHo.setCellValueFactory(new PropertyValueFactory<>("maHo"));
        colThang.setCellValueFactory(new PropertyValueFactory<>("thang"));
        colNam.setCellValueFactory(new PropertyValueFactory<>("nam"));
        colDien.setCellValueFactory(new PropertyValueFactory<>("tienDien"));
        colNuoc.setCellValueFactory(new PropertyValueFactory<>("tienNuoc"));
        colInternet.setCellValueFactory(new PropertyValueFactory<>("tienNet"));
        colTong.setCellValueFactory(new PropertyValueFactory<>("tongCong"));

        loadData();
    }

    private void loadData() {
        list.clear();
        // Giả sử bảng tên là 'phisinhhoat_chitiet' hoặc 'capnhat_phisinhhoat'
        String sql = "SELECT * FROM phisinhhoat"; 

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                float d = rs.getFloat("TienDien");
                float n = rs.getFloat("TienNuoc");
                float i = rs.getFloat("TienInternet");
                
                list.add(new PhiSinhHoatDisplay(
                        rs.getString("MaHoKhau"),
                        rs.getInt("Thang"),
                        rs.getInt("Nam"),
                        d, n, i, (d + n + i)
                ));
            }
            tablePhiSinhHoat.setItems(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearch() {
        String key = searchField.getText().toLowerCase();
        ObservableList<PhiSinhHoatDisplay> filter = FXCollections.observableArrayList();
        for (PhiSinhHoatDisplay p : list) {
            if (p.getMaHo().toLowerCase().contains(key)) filter.add(p);
        }
        tablePhiSinhHoat.setItems(filter);
    }
    
    @FXML private void handleRefresh() { loadData(); }
    @FXML private void handleAdd() { ControllerUtil.showSuccess("Info", "Tính năng thêm hóa đơn đang cập nhật."); }

    public static class PhiSinhHoatDisplay {
        private final SimpleStringProperty maHo;
        private final SimpleIntegerProperty thang;
        private final SimpleIntegerProperty nam;
        private final SimpleFloatProperty tienDien;
        private final SimpleFloatProperty tienNuoc;
        private final SimpleFloatProperty tienNet;
        private final SimpleFloatProperty tongCong;

        public PhiSinhHoatDisplay(String ma, int t, int n, float d, float nc, float net, float tong) {
            this.maHo = new SimpleStringProperty(ma);
            this.thang = new SimpleIntegerProperty(t);
            this.nam = new SimpleIntegerProperty(n);
            this.tienDien = new SimpleFloatProperty(d);
            this.tienNuoc = new SimpleFloatProperty(nc);
            this.tienNet = new SimpleFloatProperty(net);
            this.tongCong = new SimpleFloatProperty(tong);
        }
        // Getters...
        public String getMaHo() { return maHo.get(); }
        public int getThang() { return thang.get(); }
        public int getNam() { return nam.get(); }
        public float getTienDien() { return tienDien.get(); }
        public float getTienNuoc() { return tienNuoc.get(); }
        public float getTienNet() { return tienNet.get(); }
        public float getTongCong() { return tongCong.get(); }
    }
}