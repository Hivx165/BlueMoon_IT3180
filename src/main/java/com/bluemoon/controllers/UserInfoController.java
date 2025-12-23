package com.bluemoon.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class UserInfoController {
    @FXML private Label lblUsername, lblFullName, lblRole;

    public void setUserData(String user, String name, String role) {
        lblUsername.setText(user);
        lblFullName.setText(name);
        lblRole.setText(role);
    }
}