package com.bluemoon.controllers;

import com.bluemoon.models.TamTruModel;
import com.bluemoon.services.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Date;

public class TamTruController {

    @FXML private TableView<TamTruModel> tableTamTru;
    @FXML private TableColumn<TamTruModel, String> colMa;
    @FXML private TableColumn<TamTruModel, String> colCCCD;
    @FXML private TableColumn<TamTruModel, String> colLyDo;
    @FXML private TableColumn<TamTruModel, Date> colTuNgay;
    @FXML private TableColumn<TamTruModel, Date> colDenNgay;
    @FXML private TextField searchField;

    private ObservableList<TamTruModel> list = FXCollections.observableArrayList();

    public void initialize() {
        colMa.setCellValueFactory(new PropertyValueFactory<>("maTamTru"));
        colCCCD.setCellValueFactory(new PropertyValueFactory<>("soCMND_CCCD"));
        colLyDo.setCellValueFactory(new PropertyValueFactory<>("lyDo"));
        colTuNgay.setCellValueFactory(new PropertyValueFactory<>("tuNgay"));
        colDenNgay.setCellValueFactory(new PropertyValueFactory<>("denNgay"));
        loadData();
    }

    private void loadData() {
        list.clear();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            ResultSet rs = stmt.executeQuery("SELECT * FROM tamtru");
            while (rs.next()) {
                list.add(new TamTruModel(
                        rs.getString("MaTamTru"),
                        rs.getString("SoCMND_CCCD"),
                        rs.getString("LyDo"),
                        rs.getDate("TuNgay"),
                        rs.getDate("DenNgay")
                ));
            }
            tableTamTru.setItems(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML private void handleRefresh() { loadData(); }
    
    @FXML private void handleDelete() {
        TamTruModel selected = tableTamTru.getSelectionModel().getSelectedItem();
        if (selected == null) {
            ControllerUtil.showError("Lỗi", "Vui lòng chọn dòng cần xóa");
            return;
        }
        if (ControllerUtil.showConfirm("Xác nhận", "Xóa đơn tạm trú mã: " + selected.getMaTamTru() + "?")) {
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM tamtru WHERE MaTamTru = ?")) {
                stmt.setString(1, selected.getMaTamTru());
                stmt.executeUpdate();
                loadData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}