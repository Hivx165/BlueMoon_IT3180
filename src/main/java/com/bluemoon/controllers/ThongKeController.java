package com.bluemoon.controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

public class ThongKeController {
    @FXML private BarChart<String, Number> revenueChart;

    public void initialize() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu 2025");

        // Dữ liệu mẫu (Thực tế sẽ lấy từ Database bằng SUM(TotalAmount))
        series.getData().add(new XYChart.Data<>("T1", 150000000));
        series.getData().add(new XYChart.Data<>("T2", 120000000));
        series.getData().add(new XYChart.Data<>("T3", 185000000));

        revenueChart.getData().add(series);
    }
}