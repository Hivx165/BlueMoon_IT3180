package com.bluemoon.controllers;

import com.bluemoon.models.ThanhToanModel;
import com.bluemoon.services.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

public class ThanhToanController {

    @FXML private TableView<ThanhToanModel> table;
    @FXML private TableColumn<ThanhToanModel, String> colMaHo;
    @FXML private TableColumn<ThanhToanModel, Float> colSoTien;
    @FXML private TableColumn<ThanhToanModel, String> colNoiDung;
    @FXML private TableColumn<ThanhToanModel, Timestamp> colNgay;

    private ObservableList<ThanhToanModel> list = FXCollections.observableArrayList();

    public void initialize() {
        colMaHo.setCellValueFactory(new PropertyValueFactory<>("maHoKhau"));
        colSoTien.setCellValueFactory(new PropertyValueFactory<>("soTienThanhToan"));
        colNoiDung.setCellValueFactory(new PropertyValueFactory<>("noiDung"));
        colNgay.setCellValueFactory(new PropertyValueFactory<>("ngayThanhToan"));

        loadData();
    }

    private void loadData() {
        list.clear();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
             
            String sql = "SELECT * FROM lichsu_thanhtoan"; // Tên bảng giả định
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                ThanhToanModel tt = new ThanhToanModel();
                tt.setId(rs.getInt("ID"));
                tt.setMaHoKhau(rs.getString("MaHoKhau"));
                tt.setSoTienThanhToan(rs.getFloat("SoTien"));
                tt.setNoiDung(rs.getString("NoiDung"));
                tt.setNgayThanhToan(rs.getTimestamp("NgayThanhToan"));
                list.add(tt);
            }
            table.setItems(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}