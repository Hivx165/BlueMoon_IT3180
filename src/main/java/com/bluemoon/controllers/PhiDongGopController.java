package com.bluemoon.controllers;

import com.bluemoon.models.PhiDongGopModel;
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
import java.util.Date;

public class PhiDongGopController {

    @FXML private TableView<PhiDongGopModel> table;
    @FXML private TableColumn<PhiDongGopModel, String> colMaHo;
    @FXML private TableColumn<PhiDongGopModel, String> colTenPhi;
    @FXML private TableColumn<PhiDongGopModel, Float> colSoTien;
    @FXML private TableColumn<PhiDongGopModel, Date> colNgay;

    private ObservableList<PhiDongGopModel> list = FXCollections.observableArrayList();

    public void initialize() {
        colMaHo.setCellValueFactory(new PropertyValueFactory<>("maHoKhau"));
        colTenPhi.setCellValueFactory(new PropertyValueFactory<>("tenPhi"));
        colSoTien.setCellValueFactory(new PropertyValueFactory<>("soTien"));
        colNgay.setCellValueFactory(new PropertyValueFactory<>("ngayDongGop"));

        loadData();
    }

    private void loadData() {
        list.clear();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            ResultSet rs = stmt.executeQuery("SELECT * FROM phidonggop");
            while (rs.next()) {
                PhiDongGopModel p = new PhiDongGopModel();
                p.setId(rs.getInt("ID"));
                p.setMaHoKhau(rs.getString("MaHoKhau"));
                p.setTenPhi(rs.getString("TenPhi"));
                p.setSoTien(rs.getFloat("SoTien"));
                p.setNgayDongGop(rs.getDate("NgayDongGop"));
                
                list.add(p);
            }
            table.setItems(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}