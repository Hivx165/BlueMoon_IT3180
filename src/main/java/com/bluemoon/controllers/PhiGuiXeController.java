package com.bluemoon.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class PhiGuiXeController {
    @FXML private TextField txtMaHo, txtSoXeMay, txtSoOto;
    @FXML private Label lblTongPhi;

    @FXML
    private void handleCalculate() {
        try {
            int xeMay = Integer.parseInt(txtSoXeMay.getText());
            int oto = Integer.parseInt(txtSoOto.getText());
            // Giả định đơn giá: Xe máy 100k, Oto 1tr
            long total = (xeMay * 100000L) + (oto * 1000000L);
            lblTongPhi.setText(String.format("%, d VND", total));
        } catch (NumberFormatException e) {
            lblTongPhi.setText("Dữ liệu nhập sai!");
        }
    }
}