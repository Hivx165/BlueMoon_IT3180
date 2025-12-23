package com.bluemoon.controllers;

import com.bluemoon.services.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.*;

public class PhiDichVuController {
    @FXML private TableView<PhiDichVuModel> tableDichVu;
    @FXML private TableColumn<PhiDichVuModel, String> colMaHo, colLoaiPhi;
    @FXML private TableColumn<PhiDichVuModel, Double> colChiSoCu, colChiSoMoi, colThanhTien;

    private ObservableList<PhiDichVuModel> list = FXCollections.observableArrayList();

    public void initialize() {
        colMaHo.setCellValueFactory(new PropertyValueFactory<>("maHo"));
        colLoaiPhi.setCellValueFactory(new PropertyValueFactory<>("loaiPhi"));
        colChiSoCu.setCellValueFactory(new PropertyValueFactory<>("chiSoCu"));
        colChiSoMoi.setCellValueFactory(new PropertyValueFactory<>("chiSoMoi"));
        colThanhTien.setCellValueFactory(new PropertyValueFactory<>("thanhTien"));
        loadData();
    }

    private void loadData() {
        list.clear();
        String sql = "SELECT * FROM PhiDichVu"; // Giả định tên bảng trong DB
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new PhiDichVuModel(
                        rs.getString("MaHoKhau"), rs.getString("LoaiPhi"),
                        rs.getDouble("ChiSoCu"), rs.getDouble("ChiSoMoi"),
                        rs.getDouble("DonGia")
                ));
            }
            tableDichVu.setItems(list);
        } catch (Exception e) { e.printStackTrace(); }
    }
}