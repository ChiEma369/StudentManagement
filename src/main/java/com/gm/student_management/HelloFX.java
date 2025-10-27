package com.gm.student_management;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloFX {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onWelcomButtonClick() {
        welcomeText.setText("Welcome to Phenikaa Student Management System ");
    }
}
