package com.bluemoon.controllers;

import com.bluemoon.models.TamVangModel;
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

public class TamVangController {

    @FXML private TableView<TamVangModel> table;
    @FXML private TableColumn<TamVangModel, String> colMa;
    @FXML private TableColumn<TamVangModel, String> colCCCD;
    @FXML private TableColumn<TamVangModel, String> colNoiDen;
    @FXML private TableColumn<TamVangModel, Date> colTuNgay;
    @FXML private TableColumn<TamVangModel, Date> colDenNgay;

    private ObservableList<TamVangModel> list = FXCollections.observableArrayList();

    public void initialize() {
        colMa.setCellValueFactory(new PropertyValueFactory<>("maTamVang"));
        colCCCD.setCellValueFactory(new PropertyValueFactory<>("soCMND_CCCD"));
        colNoiDen.setCellValueFactory(new PropertyValueFactory<>("noiTamTru")); // Lưu ý: Model bạn đặt tên biến là noiTamTru cho nơi đến
        colTuNgay.setCellValueFactory(new PropertyValueFactory<>("tuNgay"));
        colDenNgay.setCellValueFactory(new PropertyValueFactory<>("denNgay"));

        loadData();
    }

    private void loadData() {
        list.clear();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            ResultSet rs = stmt.executeQuery("SELECT * FROM tamvang");
            while (rs.next()) {
                list.add(new TamVangModel(
                    rs.getString("MaTamVang"),
                    rs.getString("SoCMND_CCCD"),
                    rs.getString("NoiTamTru"),
                    rs.getDate("TuNgay"),
                    rs.getDate("DenNgay")
                ));
            }
            table.setItems(list);
        } catch (Exception e) {
            e.printStackTrace();
            ControllerUtil.showError("Lỗi", "Không thể tải dữ liệu tạm vắng.");
        }
    }
}