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

import java.sql.SQLException;


public class TabSV extends Tab {

    private TableView<Sinhvien> table;
    private ObservableList<Sinhvien> sinhvien;

    private void loadData() {
        try {
            sinhvien.clear(); // Xóa dữ liệu cũ trên giao diện
            // SỬA: Gọi hàm getAll() từ lớp DAO
            sinhvien.addAll(sinhvienDb.getAll());

        } catch (SQLException e) {
            // Xử lý nếu kết nối DB thất bại
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi CSDL");
            alert.setHeaderText("Không thể tải dữ liệu sinh viên!");
            alert.setContentText("Chi tiết lỗi: " + e.getMessage());
            alert.show();
            e.printStackTrace();
        }
    }
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

        table.setEditable(true);

        Button add = new Button("Thêm mới");
        Button huy = new Button("Hủy");
        Button xoahang = new Button("Xóa");
        HBox button = new HBox(10);
        Button savefi = new Button("Lưu bảng");
        Button loadfi = new Button("Mở bảng");
        button.getChildren().addAll(add, huy, savefi, loadfi, xoahang);
        grid.add(button, 0, 6);


        BorderPane pane =  new BorderPane();
        pane.setLeft(grid);
        pane.setCenter(table);

        BorderPane.setMargin(grid, new Insets(0,20, 0, 0));
        setContent(pane);

        // load du lieu
        loadData();

        add.setOnAction(e -> {
            Sinhvien sv = new Sinhvien(
                    masv.getText(),
                    ten.getText(),
                    birth.getText(),
                    gt.getValue(),
                    lop.getText()
            );

            sinhvien.add(sv);
            sinhvienDb.insert(sv);

            clearF(ten, birth, masv, lop);
        });

        xoahang.setOnAction(e -> {
            Sinhvien sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) {
                try {
                    sinhvienDb.delete(sel); // <<< GỌI HÀM XÓA TRONG CSDL
                    sinhvien.remove(sel);    // Xóa trên giao diện
                    new Alert(Alert.AlertType.INFORMATION, "Đã xóa sinh viên thành công.").show();
                } catch (SQLException ex) {
                    new Alert(Alert.AlertType.ERROR, "Lỗi: Không thể xóa sinh viên khỏi CSDL. " + ex.getMessage()).show();
                    ex.printStackTrace();
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "Vui lòng chọn một hàng để xóa.").show();
            }
        });

        huy.setOnAction(e -> clearF(ten, birth, masv, lop));

        savefi.setOnAction(e -> {
            new Alert(Alert.AlertType.INFORMATION,
                    "Dữ liệu đã lưu tự động vào MySQL").show();
        });

        loadfi.setOnAction(e -> {
            loadData();
            new Alert(Alert.AlertType.INFORMATION, "Đã nạp lại dữ liệu từ MySQL").show();
        });
    }

        public void clearF(TextField... fields) {
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