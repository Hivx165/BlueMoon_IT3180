package com.bluemoon.controllers;

import com.bluemoon.services.DatabaseConnection;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class HoKhauController {

    @FXML
    private TableView<SimpleHoKhau> hoKhauTable;
    @FXML
    private TableColumn<SimpleHoKhau, String> colMaHoKhau;
    @FXML
    private TableColumn<SimpleHoKhau, String> colTenChuHo;
    @FXML
    private TableColumn<SimpleHoKhau, String> colDiaChi;
    @FXML
    private TableColumn<SimpleHoKhau, String> colDienTich;

    private ObservableList<SimpleHoKhau> list = FXCollections.observableArrayList();

    public void initialize() {
        System.out.println(">> HoKhauController đã được khởi tạo!");

        // Cấu hình cột
        colMaHoKhau.setCellValueFactory(new PropertyValueFactory<>("maHo"));
        colTenChuHo.setCellValueFactory(new PropertyValueFactory<>("chuHo"));
        colDiaChi.setCellValueFactory(new PropertyValueFactory<>("diaChi"));
        colDienTich.setCellValueFactory(new PropertyValueFactory<>("dienTich"));

        // Load dữ liệu
        loadData();
    }

    private void loadData() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = "SELECT * FROM hokhau";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                list.add(new SimpleHoKhau(
                        rs.getString("MaHoKhau"),
                        "Đang cập nhật", // Tạm thời để string này
                        rs.getString("DiaChi"),
                        String.valueOf(rs.getFloat("DienTichHo"))
                ));
            }
            hoKhauTable.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Class nội bộ để hiển thị nhanh
    public static class SimpleHoKhau {
        private final SimpleStringProperty maHo;
        private final SimpleStringProperty chuHo;
        private final SimpleStringProperty diaChi;
        private final SimpleStringProperty dienTich;

        public SimpleHoKhau(String ma, String ten, String dc, String dt) {
            this.maHo = new SimpleStringProperty(ma);
            this.chuHo = new SimpleStringProperty(ten);
            this.diaChi = new SimpleStringProperty(dc);
            this.dienTich = new SimpleStringProperty(dt);
        }

        public String getMaHo() { return maHo.get(); }
        public String getChuHo() { return chuHo.get(); }
        public String getDiaChi() { return diaChi.get(); }
        public String getDienTich() { return dienTich.get(); }
    }
}