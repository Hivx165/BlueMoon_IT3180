package com.bluemoon;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Load file giao diện LoginView.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bluemoon/views/LoginView.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setTitle("BlueMoon Apartment Management - Login");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}