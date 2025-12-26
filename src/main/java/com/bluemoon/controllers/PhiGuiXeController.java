package com.bluemoon.controllers;

import com.bluemoon.services.DatabaseConnection;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class PhiGuiXeController {

    @FXML private TableView<PhiGuiXeDisplay> tablePhiGuiXe;
    @FXML private TableColumn<PhiGuiXeDisplay, String> colMaHo;
    @FXML private TableColumn<PhiGuiXeDisplay, Integer> colXeMay;
    @FXML private TableColumn<PhiGuiXeDisplay, Integer> colOto;
    @FXML private TableColumn<PhiGuiXeDisplay, Integer> colXeDap;
    @FXML private TableColumn<PhiGuiXeDisplay, Float> colTongTien;

    @FXML private TextField searchField;

    // Đơn giá giả định (VND)
    private final float GIA_XE_MAY = 70000;
    private final float GIA_OTO = 1200000;
    private final float GIA_XE_DAP = 30000;

    private ObservableList<PhiGuiXeDisplay> list = FXCollections.observableArrayList();

    public void initialize() {
        colMaHo.setCellValueFactory(new PropertyValueFactory<>("maHo"));
        colXeMay.setCellValueFactory(new PropertyValueFactory<>("soXeMay"));
        colOto.setCellValueFactory(new PropertyValueFactory<>("soOto"));
        colXeDap.setCellValueFactory(new PropertyValueFactory<>("soXeDap"));
        colTongTien.setCellValueFactory(new PropertyValueFactory<>("tongTien"));

        loadData();
    }

    private void loadData() {
        list.clear();
        // Lấy số lượng xe từ bảng Hộ khẩu để tính tiền
        String sql = "SELECT MaHoKhau, SoXeMay, SoOTo, SoXeDap FROM hokhau";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int xm = rs.getInt("SoXeMay");
                int oto = rs.getInt("SoOTo");
                int xd = rs.getInt("SoXeDap");
                
                // Tính tổng tiền
                float tong = (xm * GIA_XE_MAY) + (oto * GIA_OTO) + (xd * GIA_XE_DAP);

                list.add(new PhiGuiXeDisplay(rs.getString("MaHoKhau"), xm, oto, xd, tong));
            }
            tablePhiGuiXe.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
            ControllerUtil.showError("Lỗi", "Không thể tải dữ liệu gửi xe.");
        }
    }

    @FXML
    private void handleSearch() {
        String key = searchField.getText().toLowerCase();
        ObservableList<PhiGuiXeDisplay> filter = FXCollections.observableArrayList();
        for (PhiGuiXeDisplay p : list) {
            if (p.getMaHo().toLowerCase().contains(key)) {
                filter.add(p);
            }
        }
        tablePhiGuiXe.setItems(filter);
    }

    @FXML private void handleRefresh() { loadData(); }

    // Class hiển thị
    public static class PhiGuiXeDisplay {
        private final SimpleStringProperty maHo;
        private final SimpleIntegerProperty soXeMay;
        private final SimpleIntegerProperty soOto;
        private final SimpleIntegerProperty soXeDap;
        private final SimpleFloatProperty tongTien;

        public PhiGuiXeDisplay(String ma, int xm, int oto, int xd, float tong) {
            this.maHo = new SimpleStringProperty(ma);
            this.soXeMay = new SimpleIntegerProperty(xm);
            this.soOto = new SimpleIntegerProperty(oto);
            this.soXeDap = new SimpleIntegerProperty(xd);
            this.tongTien = new SimpleFloatProperty(tong);
        }
        // Getters
        public String getMaHo() { return maHo.get(); }
        public int getSoXeMay() { return soXeMay.get(); }
        public int getSoOto() { return soOto.get(); }
        public int getSoXeDap() { return soXeDap.get(); }
        public float getTongTien() { return tongTien.get(); }
    }
}