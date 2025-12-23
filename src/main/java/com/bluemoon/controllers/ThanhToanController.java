package com.bluemoon.controllers;

import com.bluemoon.services.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;

public class ThanhToanController {
    @FXML private TextField txtMaHoaDon;
    @FXML private ComboBox<String> cbPthucThanhToan; // Tiền mặt, Chuyển khoản

    @FXML
    private void handleConfirmPayment() {
        String sql = "UPDATE Invoices SET Status = 'Paid', PaymentDate = GETDATE() WHERE InvoiceID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, txtMaHoaDon.getText());
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                new Alert(Alert.AlertType.INFORMATION, "Thanh toán thành công!").show();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}