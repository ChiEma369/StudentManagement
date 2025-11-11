package com.gm.student_management;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TableView;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.gm.student_management.StudentCSV.loadCsv;


public class TabSV extends Tab {

    private TableView<Sinhvien> table;
    private ObservableList<Sinhvien> sinhvien;

    public TabSV(ObservableList<Sinhvien> sinhvien, Stage stage) {
        this.sinhvien = sinhvien;
        setText("Quản lý sinh viên");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);

        Label title = new Label("THÊM THÔNG TIN SINH VIÊN");
        Label lbten = new Label("Họ và tên:");
        ;
        TextField ten = new TextField();

        Label lbsex = new Label("Giới tính:");
        ComboBox<String> gt = new ComboBox<>();
        gt.getItems().addAll("Nữ", "Nam", "Khác");
        gt.setValue("Nữ");

        Label lbbirth = new Label("Ngày sinh:");
        TextField birth = new TextField();
        Label lbmasv = new Label("Mã sinh viên:");
        TextField masv = new TextField();
        Label lblop = new Label("Lớp:");
        TextField lop = new TextField();


        grid.add(title, 0, 0);
        HBox h1 = new HBox(5, lbten, ten);
        HBox h2 = new HBox(5, lbsex, gt);
        HBox h3 = new HBox(5, lbbirth, birth);
        HBox h4 = new HBox(5, lbmasv, masv);
        HBox h5 = new HBox(5, lblop, lop);

        grid.add(h1, 0, 1, 2, 1);
        grid.add(h2, 0, 2, 2, 1);
        grid.add(h3, 0, 3, 2, 1);
        grid.add(h4, 0, 4, 2, 1);
        grid.add(h5, 0, 5, 2, 1);

        table = new TableView<>(sinhvien);
        createColumns();

        Button xoahang = new Button("Xóa");
        xoahang.setOnAction(e -> {
            Sinhvien sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) sinhvien.remove(sel);
        });
        table.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                TablePosition p = table.getSelectionModel().getSelectedCells().get(0);
                table.edit(p.getRow(), p.getTableColumn());
            }
        });
        table.setEditable(true);

        Button add = new Button("Thêm mới");
        Button huy = new Button("Hủy");
        HBox button = new HBox(10);
        Button savefi = new Button("Lưu bảng");
        Button loadfi = new Button("Mở bảng");
        button.getChildren().addAll(add, huy, savefi, loadfi, xoahang);
        grid.add(button, 0, 6);

        String path = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "svien.csv";

        if (new File(path).exists()) {
            try {
                List<Sinhvien> list = StudentCSV.loadCsv(path, sinhvien);
                sinhvien.clear();
                sinhvien.addAll(list);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        savefi.setOnAction(e -> {
            try {
                ;
                StudentCSV.saveCsv(path, sinhvien);
            } catch (Exception ex) {
                ex.printStackTrace();    // tranh tat may khi gap loi
            }
        });
        loadfi.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Chọn file CSV để mở");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files", "*.csv"));

            File file = fc.showOpenDialog(stage);
            if (file != null) {
                try {
                    List<Sinhvien> list = loadCsv(file.getAbsolutePath(), table.getItems());
                    sinhvien.clear();
                    sinhvien.addAll(list);
                    Alert load = new Alert(Alert.AlertType.INFORMATION, "Đã load file");
                    load.show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        add.setOnAction(e -> {
            addSvien(masv.getText(), ten.getText(), birth.getText(), gt.getValue(), lop.getText());
        });
        huy.setOnAction(e -> clearF(ten, birth, masv, lop));

        BorderPane base = new BorderPane();
        base.setLeft(grid);
        base.setCenter(table);
        grid.setPrefWidth(350);
        base.setPadding(new Insets(10));

        setContent(base);

    }

    private void clearF(TextField... fields) {
        for (TextField f : fields) f.clear();
    }

    private void createColumns() {
        TableColumn<Sinhvien, String> colid = new TableColumn<>("Mã sinh viên");
        colid.setCellValueFactory(new PropertyValueFactory<>("masv"));  // phai trung ten ben s
        colid.setCellFactory(TextFieldTableCell.forTableColumn());
        colid.setOnEditCommit(e -> e.getRowValue().setMasv(e.getNewValue()));

        TableColumn<Sinhvien, String> colten = new TableColumn<>("Họ và tên");
        colten.setCellValueFactory(new PropertyValueFactory<>("ten"));
        colten.setPrefWidth(250);   //dat chieu ngang cho cot
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
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void addSvien(String id, String name, String birth, String sex, String lop) {
        if (id.isEmpty() || name.isEmpty() || birth.isEmpty() || lop.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Lỗi nhập dữ liệu!");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng nhập đầy đủ thông tin.");
            alert.show();
            return;
        }

        sinhvien.add(new Sinhvien(id, name, birth, sex, lop));

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Nhập thành công");
        alert.setHeaderText(null);
        alert.setContentText("Đã thêm sinh viên");
        alert.show();
    }
}