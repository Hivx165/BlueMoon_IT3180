package com.bluemoon.controllers;

import com.bluemoon.models.HoKhauModel;
import com.bluemoon.services.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleFloatProperty;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class PhiGuiXeController {

    @FXML private TableView<HoKhauModel> table;
    @FXML private TableColumn<HoKhauModel, String> colMaHo;
    @FXML private TableColumn<HoKhauModel, Integer> colXeMay;
    @FXML private TableColumn<HoKhauModel, Integer> colOTo;
    @FXML private TableColumn<HoKhauModel, Integer> colXeDap;
    @FXML private TableColumn<HoKhauModel, Float> colTongTien; // Cột tính toán

    // Đơn giá giả định
    private final float GIA_XE_MAY = 70000;
    private final float GIA_OTO = 1200000;
    private final float GIA_XE_DAP = 30000;

    private ObservableList<HoKhauModel> list = FXCollections.observableArrayList();

    public void initialize() {
        colMaHo.setCellValueFactory(new PropertyValueFactory<>("maHoKhau"));
        colXeMay.setCellValueFactory(new PropertyValueFactory<>("soXeMay"));
        colOTo.setCellValueFactory(new PropertyValueFactory<>("soOTo"));
        colXeDap.setCellValueFactory(new PropertyValueFactory<>("soXeDap"));
        
        // Tính tổng tiền động cho từng dòng
        colTongTien.setCellValueFactory(cellData -> {
            HoKhauModel hk = cellData.getValue();
            float tong = (hk.getSoXeMay() * GIA_XE_MAY) + 
                         (hk.getSoOTo() * GIA_OTO) + 
                         (hk.getSoXeDap() * GIA_XE_DAP);
            return new SimpleFloatProperty(tong).asObject();
        });

        loadData();
    }

    private void loadData() {
        list.clear();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM hokhau");
            while (rs.next()) {
                HoKhauModel hk = new HoKhauModel();
                hk.setMaHoKhau(rs.getString("MaHoKhau"));
                hk.setSoXeMay(rs.getInt("SoXeMay"));
                hk.setSoOTo(rs.getInt("SoOTo"));
                hk.setSoXeDap(rs.getInt("SoXeDap"));
                list.add(hk);
            }
            table.setItems(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}