package com.bluemoon.controllers;

import com.bluemoon.dao.ThongKeDAO;
import com.bluemoon.models.ThongKeHoKhauModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class ThongKeController {

    // Các thành phần của Biểu đồ
    @FXML private PieChart pieChartNhanKhau;
    @FXML private BarChart<String, Number> barChartKhoanThu;
    @FXML private Label lblTongThu;

    // Các thành phần của Bảng chi tiết
    @FXML private TableView<ThongKeHoKhauModel> tableChiTiet;
    @FXML private TableColumn<ThongKeHoKhauModel, String> colMaHoKhau;
    @FXML private TableColumn<ThongKeHoKhauModel, Double> colPhiSinhHoat;
    @FXML private TableColumn<ThongKeHoKhauModel, Double> colPhiDichVu;
    @FXML private TableColumn<ThongKeHoKhauModel, Double> colPhiDongGop;
    @FXML private TableColumn<ThongKeHoKhauModel, Double> colTongCong;

    @FXML
    public void initialize() {
        // Setup Bảng
        setupTable();

        // Load toàn bộ dữ liệu
        loadData();
    }

    private void setupTable() {
        colMaHoKhau.setCellValueFactory(new PropertyValueFactory<>("maHoKhau"));
        colPhiSinhHoat.setCellValueFactory(new PropertyValueFactory<>("phiSinhHoat"));
        colPhiDichVu.setCellValueFactory(new PropertyValueFactory<>("phiDichVu"));
        colPhiDongGop.setCellValueFactory(new PropertyValueFactory<>("phiDongGop"));
        colTongCong.setCellValueFactory(new PropertyValueFactory<>("tongCong"));

        // Định dạng hiển thị số tiền (Ví dụ: 1,000,000 đ)
        NumberFormat vnFormat = NumberFormat.getInstance(new Locale("vi", "VN"));

        formatCurrencyColumn(colPhiSinhHoat, vnFormat);
        formatCurrencyColumn(colPhiDichVu, vnFormat);
        formatCurrencyColumn(colPhiDongGop, vnFormat);
        formatCurrencyColumn(colTongCong, vnFormat);
    }

    // Helper để format cột tiền tệ
    private void formatCurrencyColumn(TableColumn<ThongKeHoKhauModel, Double> col, NumberFormat format) {
        col.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(format.format(item));
                }
            }
        });
    }

    private void loadData() {
        // 1. Load Biểu đồ Nhân khẩu
        Map<String, Integer> statsNK = ThongKeDAO.getNhanKhauStats();
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                new PieChart.Data("Thường trú", statsNK.get("ThuongTru")),
                new PieChart.Data("Tạm trú", statsNK.get("TamTru")),
                new PieChart.Data("Tạm vắng", statsNK.get("TamVang"))
        );
        pieChartNhanKhau.setData(pieData);
        pieChartNhanKhau.setTitle("Tỷ lệ Cư trú");

        // 2. Load Biểu đồ Tài chính
        Map<String, Double> statsThu = ThongKeDAO.getKhoanThuStats();
        barChartKhoanThu.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Tổng thu (VNĐ)");
        series.getData().add(new XYChart.Data<>("Sinh Hoạt", statsThu.get("PhiSinhHoat")));
        series.getData().add(new XYChart.Data<>("Dịch Vụ", statsThu.get("PhiDichVu")));
        series.getData().add(new XYChart.Data<>("Đóng Góp", statsThu.get("PhiDongGop")));
        barChartKhoanThu.getData().add(series);

        // Hiển thị tổng tiền text
        double total = statsThu.get("PhiSinhHoat") + statsThu.get("PhiDichVu") + statsThu.get("PhiDongGop");
        NumberFormat vnFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        lblTongThu.setText("TỔNG DÒNG TIỀN: " + vnFormat.format(total) + " VNĐ");

        // 3. Load Bảng Chi Tiết (Theo Hộ Khẩu)
        ObservableList<ThongKeHoKhauModel> tableList = FXCollections.observableArrayList(ThongKeDAO.getThongKeTheoHoKhau());
        tableChiTiet.setItems(tableList);
    }

    @FXML
    private void handleRefresh() {
        loadData();
    }
}