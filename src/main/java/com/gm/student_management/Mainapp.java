package com.gm.student_management;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
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
        ComboBox<String> gt = new ComboBox<>();
        gt.getItems().addAll("Nữ", "Nam", "Khác");
        gt.setValue("Nữ");

        Label lbbirth = new Label("Ngày sinh:");
        TextField birth = new TextField();
        Label lbmasv= new Label("Mã sinh viên:");
        TextField masv = new TextField();
        Label lblop = new Label("Lớp:");
        TextField lop = new TextField();


        Button add = new Button("Thêm mới");
        Button huy = new Button("Hủy");
        HBox button = new HBox(10);
        button.getChildren().addAll(add, huy);


        grid.add(title,0,0);
        grid.add(lbten,0,1);  grid.add(ten,1,1);
        grid.add(lbsex,0,2);  grid.add(gt, 1, 2);
        grid.add(lbbirth,0,3);  grid.add(birth, 1, 3);
        grid.add(lbmasv,0,4); grid.add(masv,1,4);
        grid.add(lblop,0,5); grid.add(lop,1,5);
        grid.add(button, 1, 6);

        TableView<Sinhvien> table = new TableView<>(sinhvien);
        TableColumn<Sinhvien, String> colid = new TableColumn<>("Mã sinh viên");
        colid.setCellValueFactory(new PropertyValueFactory<>("masv"));  // phai trung ten ben s
        colid.setCellFactory(TextFieldTableCell.forTableColumn());
        colid.setOnEditCommit(e -> e.getRowValue().setMasv(e.getNewValue()));

        TableColumn<Sinhvien, String> colten = new TableColumn<>("Họ và tên");
        colten.setCellValueFactory(new PropertyValueFactory<>("ten"));
        colten.setCellFactory(TextFieldTableCell.forTableColumn());
        colten.setOnEditCommit(e -> e.getRowValue().setTen(e.getNewValue()));

        TableColumn<Sinhvien, String> colbirth = new TableColumn<>("Ngày sinh");
        colbirth.setCellValueFactory(new PropertyValueFactory<>("birth"));
        colbirth.setCellFactory(TextFieldTableCell.forTableColumn());
        colbirth.setOnEditCommit(e -> e.getRowValue().setBirth(e.getNewValue()));

        TableColumn<Sinhvien, String> colsex = new TableColumn<>("Giới tính");
        colsex.setCellValueFactory(new PropertyValueFactory<>("sex"));
        colsex.setCellFactory(TextFieldTableCell.forTableColumn());
        colsex.setOnEditCommit(e -> e.getRowValue().setSex(e.getNewValue()));

        TableColumn<Sinhvien, String> collop = new TableColumn<>("Lớp");
        collop.setCellValueFactory(new PropertyValueFactory<>("lop")); // ten phai giong trong class sinhvien
        collop.setCellFactory(TextFieldTableCell.forTableColumn());
        collop.setOnEditCommit(e -> e.getRowValue().setLop(e.getNewValue()));

        table.getColumns().addAll(colid, colten, colbirth, colsex, collop);
        table.setPrefHeight(200);

        Button xoahang = new Button("Xóa");
        xoahang.setOnAction(e -> {
                    Sinhvien sel = table.getSelectionModel().getSelectedItem();
                    if (sel != null) sinhvien.remove(sel);
        });
        table.setOnMouseClicked(e -> {
            if(e.getClickCount() == 2){
                TablePosition p = table.getSelectionModel().getSelectedCells().get(0);
                table.edit(p.getRow(), p.getTableColumn());
            }
        });


        add.setOnAction(e -> {
            String id = masv.getText().trim();
            String name = ten.getText();
            String ns = birth.getText();
            String lp = lop.getText().trim();

            if(name.isEmpty() || ns.isEmpty() || id.isEmpty() || lp.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Lỗi nhập dữ liệu!");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng nhập đầy đủ thông tin.");
                alert.show();
                return;
            }
            sinhvien.add(new Sinhvien(id, name, ns, gt.getValue(), lp));  //cai nay cung can dung thu tu cot
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Nhập thành công");
            alert.setHeaderText(null);
            alert.setContentText("Đã thêm sinh viên");
            alert.show();

            clearF(ten, birth, masv, lop);
        });

        huy.setOnAction(e -> clearF(ten, birth, masv, lop));

        VBox bang = new VBox(10, grid, table);
        bang.setPadding(new Insets(10));
        Scene scene = new Scene(bang,600,400);  //dung bang
        stage.setScene(scene);
        stage.setTitle("Student Management");
        stage.show();

    }
    public static void main(String[] args) {
        launch();
    }
}