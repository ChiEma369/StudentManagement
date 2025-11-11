package com.gm.student_management;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class Mainapp extends Application {

    private static ObservableList<Sinhvien> sinhvien = FXCollections.observableArrayList();

    public static ObservableList<Sinhvien> getSinhvien() {
        return sinhvien;
    }

    @Override
    public void start(Stage stage) {
        System.out.println("Start Mainapp");

        TabSV tabSV = new TabSV(sinhvien, stage);
        TabDiem tabDiem = new TabDiem(sinhvien, stage);

        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(tabSV, tabDiem);

        Scene scene = new Scene(tabPane, 1200, 700);
        stage.setScene(scene);
        stage.setTitle("Student Management");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
