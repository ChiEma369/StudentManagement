package com.gm.student_management;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class Mainapp extends Application {
    @Override
    public void start(Stage stage) {
        Label label = new Label("Hello, Chi Ema");
        Scene scene = new Scene(label, 300, 200);
        stage.setTitle("My first app");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}
