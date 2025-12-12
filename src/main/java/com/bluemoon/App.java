package com.bluemoon;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        try {
            // 1. Kiểm tra xem có tìm thấy file không
            String fxmlPath = "/com/bluemoon/views/LoginView.fxml";
            URL resource = getClass().getResource(fxmlPath);

            if (resource == null) {
                // Nếu null nghĩa là không tìm thấy -> In báo lỗi đỏ ra màn hình console
                System.err.println("❌ LỖI NGHIÊM TRỌNG: Không tìm thấy file FXML!");
                System.err.println("👉 Chương trình đang tìm tại: " + fxmlPath);
                System.err.println("👉 Hãy kiểm tra lại thư mục 'out/production/BlueMoonApartment/com/bluemoon/views/' xem có file LoginView.fxml chưa.");
                return; // Dừng chương trình
            }

            // 2. Nếu tìm thấy thì nạp
            System.out.println("✅ Đã tìm thấy file FXML tại: " + resource);
            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setTitle("BlueMoon Apartment Management - Login");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}