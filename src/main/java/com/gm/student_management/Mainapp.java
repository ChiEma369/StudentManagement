package com.gm.student_management;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Mainapp extends Application {
    private void clearF(TextField... fields) {
        for (TextField f : fields) f.clear();
    }
        private ObservableList<Sinhvien> sinhvien = FXCollections.observableArrayList();
    @Override
    public void start(Stage stage){
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);

        Label title = new Label("THÊM THÔNG TIN SINH VIÊN");
        Label lbten = new Label("Họ và tên:");;
        TextField ten = new TextField();
        Label lbsex = new Label("Giới tính:");
        TextField sex = new TextField();
        Label lbbirth = new Label("Ngày sinh:");
        TextField birth = new TextField();
        Label lbmasv= new Label("Mã sinh viên:");
        TextField masv = new TextField();
        Label lbnganh = new Label("Ngành học:");
        TextField nganh = new TextField();
        Label lblop = new Label("Lớp:");
        TextField lop = new TextField();


        Button add = new Button("Thêm mới");
        Button huy = new Button("Hủy");
        HBox button = new HBox(10);
        button.getChildren().addAll(add, huy);


        grid.add(title,0,0);
        grid.add(lbten,0,1);  grid.add(ten,1,1);
        grid.add(lbsex,0,2);  grid.add(sex, 1, 2);
        grid.add(lbmasv,0,3); grid.add(masv,1,3);
        grid.add(lblop,0,5); grid.add(lop,1,5);
        grid.add(button, 1, 6);

        TableView<Sinhvien> table = new TableView<>(sinhvien);

        add.setOnAction(e -> {
            String name = ten.getText();
            String gt = sex.getText();
            String id = masv.getText().trim();
            String nh = nganh.getText().trim();
            String lp = lop.getText().trim();

            if(name.isEmpty() || gt.isEmpty() || id.isEmpty() || nh.isEmpty() || lp.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Lỗi nhập dữ liệu!");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng nhập đầy đủ thông tin.");
                alert.show();
                return;
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Nhập thành công");
            alert.setHeaderText(null);
            alert.setContentText("Đã thêm sinh viên");
            alert.show();

            clearF(ten, sex, masv, nganh, lop);
        });

        huy.setOnAction(e -> clearF(ten, sex, masv, nganh, lop));
        Scene scene = new Scene(grid,600,400);
        stage.setScene(scene);
        stage.setTitle("Student Management");
        stage.show();

    }
    public static void main(String[] args) {
        launch();
    }
}