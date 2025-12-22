package com.bluemoon.controllers;

import com.bluemoon.models.PhiSinhHoatModel;
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

public class PhiSinhHoatController {

    @FXML private TableView<PhiSinhHoatModel> table;
    @FXML private TableColumn<PhiSinhHoatModel, String> colMaHo;
    @FXML private TableColumn<PhiSinhHoatModel, Integer> colNam;
    // Ví dụ hiển thị 3 tháng đầu năm, bạn có thể thêm đủ 12 tháng
    @FXML private TableColumn<PhiSinhHoatModel, Float> colT1;
    @FXML private TableColumn<PhiSinhHoatModel, Float> colT2;
    @FXML private TableColumn<PhiSinhHoatModel, Float> colT3;

    private ObservableList<PhiSinhHoatModel> list = FXCollections.observableArrayList();

    public void initialize() {
        colMaHo.setCellValueFactory(new PropertyValueFactory<>("maHoKhau"));
        colNam.setCellValueFactory(new PropertyValueFactory<>("nam"));
        colT1.setCellValueFactory(new PropertyValueFactory<>("thang1"));
        colT2.setCellValueFactory(new PropertyValueFactory<>("thang2"));
        colT3.setCellValueFactory(new PropertyValueFactory<>("thang3"));

        loadData();
    }

    private void loadData() {
        list.clear();
        // Giả sử bảng CSDL tên là 'phisinhhoat_tonghop' hoặc tương tự
        // Dựa vào Model, bạn có các cột Thang1...Thang12
        String sql = "SELECT * FROM phisinhhoat"; 
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                PhiSinhHoatModel p = new PhiSinhHoatModel();
                p.setId(rs.getInt("ID"));
                p.setMaHoKhau(rs.getString("MaHoKhau"));
                p.setNam(rs.getInt("Nam"));
                p.setThang1(rs.getFloat("Thang1"));
                p.setThang2(rs.getFloat("Thang2"));
                p.setThang3(rs.getFloat("Thang3"));
                // ... set tiếp các tháng còn lại nếu cần
                
                list.add(p);
            }
            table.setItems(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}