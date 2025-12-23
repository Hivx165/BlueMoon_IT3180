package com.bluemoon.controllers;

import com.bluemoon.models.PhiSinhHoatModel; // Giả định bạn đã tạo Model này
import com.bluemoon.services.DatabaseConnection;
import com.bluemoon.utils.ControllerUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class PhiSinhHoatController {

    @FXML private TableView<PhiSinhHoatModel> tablePhiSinhHoat;
    @FXML private TableColumn<PhiSinhHoatModel, String> colMaHo;
    @FXML private TableColumn<PhiSinhHoatModel, Double> colDienTich;
    @FXML private TableColumn<PhiSinhHoatModel, Double> colDonGia;
    @FXML private TableColumn<PhiSinhHoatModel, Double> colTongTien;
    @FXML private TableColumn<PhiSinhHoatModel, String> colTrangThai;

    @FXML private TextField txtSearchMaHo;
    @FXML private Label lblThongBao;

    private ObservableList<PhiSinhHoatModel> dataList = FXCollections.observableArrayList();

    public void initialize() {
        // 1. Cấu hình các cột
        colMaHo.setCellValueFactory(new PropertyValueFactory<>("maHoKhau"));
        colDienTich.setCellValueFactory(new PropertyValueFactory<>("dienTich"));
        colDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));
        colTongTien.setCellValueFactory(new PropertyValueFactory<>("tongTien"));
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));

        // 2. Tải dữ liệu từ DB
        loadData();
    }

    private void loadData() {
        dataList.clear();
        // SQL JOIN giữa bảng HoKhau (để lấy diện tích) và bảng PhiSinhHoat
        String sql = "SELECT h.MaHoKhau, h.DienTichHo, p.DonGia, p.TrangThai " +
                "FROM hokhau h LEFT JOIN PhiSinhHoat p ON h.MaHoKhau = p.MaHoKhau";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                double dienTich = rs.getDouble("DienTichHo");
                double donGia = rs.getDouble("DonGia") == 0 ? 10000 : rs.getDouble("DonGia"); // Mặc định 10k/m2 nếu chưa có
                String trangThai = rs.getString("TrangThai") == null ? "Chưa thu" : rs.getString("TrangThai");

                dataList.add(new PhiSinhHoatModel(
                        rs.getString("MaHoKhau"),
                        dienTich,
                        donGia,
                        trangThai
                ));
            }
            tablePhiSinhHoat.setItems(dataList);

        } catch (Exception e) {
            e.printStackTrace();
            ControllerUtil.showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải dữ liệu phí sinh hoạt!");
        }
    }

    @FXML
    private void handleCalculateAll() {
        // Logic tính toán lại phí cho toàn bộ các hộ dựa trên diện tích hiện tại
        lblThongBao.setText("Đang tính toán lại phí cho toàn bộ chung cư...");
        // Thực hiện cập nhật DB tại đây
        loadData();
    }

    @FXML
    private void handleSearch() {
        String keyword = txtSearchMaHo.getText().toLowerCase();
        ObservableList<PhiSinhHoatModel> filteredData = FXCollections.observableArrayList();

        for (PhiSinhHoatModel item : dataList) {
            if (item.getMaHoKhau().toLowerCase().contains(keyword)) {
                filteredData.add(item);
            }
        }
        tablePhiSinhHoat.setItems(filteredData);
    }
}