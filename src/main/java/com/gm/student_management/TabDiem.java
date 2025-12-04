package com.gm.student_management;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.util.List;

public class TabDiem extends Tab {
    private ObservableList<Diem> listDiem = FXCollections.observableArrayList();
    private ObservableList<Sinhvien> sinhvien;
    private TableView<Diem> tableDiem;

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
            public Sinhvien fromString(String s) {
                return null;
            }
        });

        TextField txtDiem = new TextField();
        ComboBox<String> cbNamhoc = new ComboBox<>();
        cbNamhoc.setEditable(true);  // vua chon vua nhap moi
        cbNamhoc.setPrefWidth(150);

        try {
            // Lấy danh sách năm từ db
            cbNamhoc.setItems(FXCollections.observableArrayList(DB.getDsNamhoc()));

            if (!cbNamhoc.getItems().isEmpty()) {
                cbNamhoc.getSelectionModel().selectFirst();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Label title = new Label("THÊM THÔNG TIN ĐIỂM SINH VIÊN");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;"); // Làm đậm cho đẹp
        grid.add(title, 0, 0, 2, 1);

        grid.add(new Label("Sinh viên:"), 0, 1);
        grid.add(cbSV, 1, 1);
        grid.add(new Label("Môn:"), 0, 2);
        grid.add(cbMon, 1, 2);
        grid.add(new Label("Điểm:"), 0, 3);
        grid.add(txtDiem, 1, 3);
        Label lbNam = new Label("Năm học:");
        grid.add(lbNam, 0, 4);
        grid.add(cbNamhoc, 1, 4);


        tableDiem = new TableView<>(listDiem); // <<< SỬA DÒNG NÀY
        tableDiem.setEditable(true);
        createColumns();

        // cac nut
        Button Them = new Button("Thêm điểm");
        Button xoa = new Button("Xóa");
        HBox button = new HBox(10);
        button.getChildren().addAll(Them, xoa);
        grid.add(button, 1, 5);


        Them.setOnAction(e -> {
            Sinhvien sv = cbSV.getValue();
            String mon = cbMon.getValue();

            String namhoc = "";
            if (cbNamhoc.getValue() != null) {
                namhoc = cbNamhoc.getValue().trim();
            }

            String diemStr = txtDiem.getText().trim();

            if (sv == null || mon == null || diemStr.isEmpty() || namhoc.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Vui lòng nhập đủ thông tin (Sinh viên, Môn, Năm, Điểm)!").show();
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

                if (!cbNamhoc.getItems().contains(namhoc)) {
                    cbNamhoc.getItems().add(0, namhoc); // Thêm vào đầu danh sách
                    cbNamhoc.getSelectionModel().select(namhoc);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Lỗi: Sinh viên này đã có điểm môn " + mon + " trong năm " + namhoc + " rồi!").show();
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

        // tim kiem masv
        TextField txtSearch = new TextField();
        txtSearch.setPromptText("Nhập Mã SV để tra cứu...");
        txtSearch.setPrefWidth(250);

        Button timMa = new Button("Tìm kiếm");
        timMa.setStyle("-fx-background-color: #00BFFF; -fx-text-fill: white;"); // Xanh dương

        Button load = new Button("Tải lại bảng");

        HBox nhap = new HBox(10);
        nhap.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        nhap.setPadding(new Insets(0, 0, 10, 0));
        nhap.getChildren().addAll(new Label("Tra cứu điểm:"), txtSearch, timMa, load);


        timMa.setOnAction(e -> {
            String keyword = txtSearch.getText().trim();
            if (keyword.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Vui lòng nhập Mã sinh viên!").show();
                return;
            }
            try {
                java.util.List<Diem> ketqua = DB.getDiemByMasv(keyword);

                listDiem.clear();
                if (ketqua.isEmpty()) {
                    new Alert(Alert.AlertType.INFORMATION, "Không tìm thấy dữ liệu cho mã: " + keyword).show();
                } else {
                    listDiem.addAll(ketqua);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Lỗi khi tìm kiếm!").show();
            }
        });

        txtSearch.setOnAction(e -> timMa.fire());

        load.setOnAction(e -> {
            txtSearch.clear();
            loadDiemData();
        });


        javafx.scene.layout.VBox centerLayout = new javafx.scene.layout.VBox(10);
        centerLayout.getChildren().addAll(nhap, tableDiem);

        javafx.scene.layout.VBox.setVgrow(tableDiem, javafx.scene.layout.Priority.ALWAYS);

        bp.setPadding(new Insets(10));
        bp.setLeft(grid);
        BorderPane.setMargin(grid, new Insets(0, 20, 0, 0));
        bp.setCenter(centerLayout);

        setContent(bp);
    }
    private void createColumns() {
        TableColumn<Diem, String> colMsv = new TableColumn<>("Mã sinh viên");
        colMsv.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getSv().getMasv()));

        TableColumn<Diem, String> colten = new TableColumn<>("Họ và tên");
        colten.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getSv().getTen()));

        TableColumn<Diem, String> colmon = new TableColumn<>("Môn học");
        colmon.setCellValueFactory(new PropertyValueFactory<>("mon"));

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

        TableColumn<Diem, String> colNam = new TableColumn<>("Năm học");
        colNam.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNamhoc()));

        tableDiem.getColumns().addAll(colMsv, colten, colmon, coldiem, colgpa, colNam);
        tableDiem.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    public ObservableList<Diem> getListDiem(){
        return listDiem;
    }
}