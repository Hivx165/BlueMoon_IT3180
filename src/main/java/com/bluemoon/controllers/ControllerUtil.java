package com.bluemoon.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.IOException;

public class ControllerUtil {

    // Hàm hiển thị thông báo nhanh
    public static void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Hàm hỗ trợ chuyển Scene
    public static void navigateTo(String fxmlPath, String title, Stage stage) {
        try {
            Parent root = FXMLLoader.load(ControllerUtil.class.getResource(fxmlPath));
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}