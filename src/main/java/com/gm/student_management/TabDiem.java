package com.gm.student_management;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.sql.SQLException;

public class TabDiem extends Tab {
    private ObservableList<Diem> listDiem = FXCollections.observableArrayList();
    private ObservableList<Sinhvien> sinhvien;

    public void loadDiemData() { // Bỏ 'throws SQLException' vì đã try-catch bên trong
        try {
            listDiem.clear();
            listDiem.addAll(DB.loadAllDiem(this.sinhvien));
        } catch (SQLException ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Lỗi tải dữ liệu điểm: " + ex.getMessage()).show();
        }
    }

    public TabDiem(ObservableList<Sinhvien> sinhvien, Stage stage) {
        setText("Quản lý điểm");
        this.sinhvien = sinhvien;

        BorderPane bp = new BorderPane();
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        ComboBox<Sinhvien> cbSV = new ComboBox<>();
        cbSV.setItems(sinhvien);

        ComboBox<String> cbMon = new ComboBox<>();
        cbMon.getItems().addAll("Nhập môn công nghệ thông tin", "Lập trình C", "OOP", "Cơ sở dữ liệu",
                "Hệ điều hành", "Mạng máy tính", "Trí tuệ nhân tạo", "Lập trình Web", "Lập trình Python",
                "Lập trình Java", "Lập trình C++", "Thiết kế phần mềm", "Đại số tuyến tính", "Giải tích",
                "Toán rời rạc", "Lý thuyết xác suất thống kê", "Triết học Mác_Lênin");

        cbSV.setConverter(new StringConverter<Sinhvien>() {
            @Override
            public String toString(Sinhvien sv) {
                if (sv == null) return "";
                return sv.getMasv() + " - " + sv.getTen();
            }
            @Override
            public Sinhvien fromString(String s) { return null; }
        });

        TextField txtDiem = new TextField();
        TextField txtNamhoc = new TextField();

        Button Them = new Button("Thêm điểm");
        Button xoa = new Button("Xóa");
        HBox button = new HBox(10);
        button.getChildren().addAll(Them, xoa);

        grid.add(new Label("Sinh viên:"), 0, 0); grid.add(cbSV, 1, 0);
        grid.add(new Label("Môn:"), 0, 1); grid.add(cbMon, 1, 1);
        grid.add(new Label("Điểm:"), 0, 2); grid.add(txtDiem, 1, 2);
        Label lbNam = new Label("Năm học:");
        grid.add(lbNam, 0, 3); grid.add(txtNamhoc, 1, 3);

        grid.add(button, 1, 4);

        TableView<Diem> tableDiem = new TableView<>(listDiem);
        tableDiem.setEditable(true);

        TableColumn<Diem, String> colMsv = new TableColumn<>("Mã sinh viên");
        colMsv.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getSv().getMasv()));

        TableColumn<Diem, String> colten = new TableColumn<>("Họ và tên");
        colten.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getSv().getTen()));

        TableColumn<Diem, String> colmon = new TableColumn<>("Môn học");
        colmon.setCellValueFactory(new PropertyValueFactory<>("mon"));

        TableColumn<Diem, String> colNam = new TableColumn<>("Năm học");
        colNam.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNamhoc()));

        TableColumn<Diem, Double> coldiem = new TableColumn<>("Điểm");
        coldiem.setCellValueFactory(new PropertyValueFactory<>("diem"));

        coldiem.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Double>() {
            @Override
            public String toString(Double object) {
                return object == null ? "" : object.toString();
            }
            @Override
            public Double fromString(String string) {
                try {
                    double d = Double.parseDouble(string);
                    if (d < 0 || d > 10) return null; // Logic kiểm tra 0-10
                    return d;
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }));

        coldiem.setOnEditCommit(e -> {
            Double newValue = e.getNewValue();
            Diem rowData = e.getRowValue();

            if (newValue == null) {
                e.getTableView().refresh();
                new Alert(Alert.AlertType.WARNING, "Điểm phải là số từ 0 đến 10!").show();
                return;
            }

            try {
                rowData.setDiem(newValue);
                DB.updateDiem(rowData);
                e.getTableView().refresh(); // Refresh để tính lại GPA
            } catch (Exception ex) {
                ex.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Lỗi cập nhật CSDL: " + ex.getMessage()).show();
                loadDiemData();
            }
        });

        TableColumn<Diem, String> colgpa = new TableColumn<>("GPA");
        colgpa.setCellValueFactory(c -> new SimpleStringProperty(String.format("%.2f", c.getValue().getGPA())));

        tableDiem.getColumns().addAll(colMsv, colten, colmon, colNam, coldiem, colgpa);
        tableDiem.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // cac nut
        Them.setOnAction(e -> {
            Sinhvien sv = cbSV.getValue();
            String mon = cbMon.getValue();
            String namhoc = txtNamhoc.getText().trim();
            String diemStr = txtDiem.getText().trim();

            if (sv == null || mon == null || diemStr.isEmpty() || namhoc.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Vui lòng nhập đủ thông tin!").show();
                return;
            }

            double val;
            try {
                val = Double.parseDouble(diemStr);
                if (val < 0 || val > 10) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR, "Điểm phải là số từ 0-10!").show();
                return;
            }

            Diem d = new Diem(sv, mon, namhoc, val);
            try {
                DB.insertDiem(d);
                listDiem.add(d);
                tableDiem.refresh();

            } catch (Exception ex) {
                ex.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Lỗi lưu database (Có thể trùng môn/năm học)!").show();
            }
        });

        xoa.setOnAction(e -> {
            Diem sel = tableDiem.getSelectionModel().getSelectedItem();
            if (sel != null) {
                try {
                    DB.deleteDiem(sel);
                    listDiem.remove(sel);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Lỗi xóa database!").show();
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "Vui lòng chọn dòng để xóa!").show();
            }
        });

        loadDiemData();

        bp.setLeft(grid);
        bp.setCenter(tableDiem);
        setContent(bp);
    }

    public ObservableList<Diem> getListDiem(){
        return listDiem;
    }
}