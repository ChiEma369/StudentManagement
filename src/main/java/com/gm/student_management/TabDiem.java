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

    public void loadDiemData() throws SQLException {
        try {
            listDiem.clear();
            listDiem.addAll(DB.loadAllDiem(this.sinhvien));

        } catch (SQLException ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Không thể load điểm từ database. Vui lòng kiểm tra Server MySQL. Chi tiết: " + ex.getMessage()).show();
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
        cbMon.getItems().addAll("Nhập môn công nghệ thông tin",
                "Lập trình C",
                "OOP",
                "Cơ sở dữ liệu",
                "Hệ điều hành",
                "Mạng máy tính",
                "Trí tuệ nhân tạo",
                "Lập trình Web",
                "Lập trình Python",
                "Lập trình Java",
                "Lập trình C++",
                "Thiết kế phần mềm",
                "Đại số tuyến tính",
                "Giải tích",
                "Toán rời rạc",
                "Lý thuyết xác suất thống kê",
                "Triết học Mác_Lênin"
        );


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
        TextField txtNamhoc = new TextField();
        Button Them = new Button("Thêm điểm");
        Button xoa = new Button("Xóa");
        HBox button = new HBox(10);

        grid.add(new Label("Sinh viên:"), 0, 0); grid.add(cbSV, 1, 0);
        grid.add(new Label("Môn:"), 0, 1); grid.add(cbMon, 1, 1);
        grid.add(new Label("Điểm:"), 0, 2); grid.add(txtDiem, 1, 2);
        Label lbml = new Label("Mã lớp");
        grid.add(lbml,  0, 3);    grid.add(txtNamhoc, 1, 3);
        button.getChildren().addAll(Them, xoa);
        grid.add(button, 1, 4);

        TableView<Diem> tableDiem = new TableView<>(listDiem);
        TableColumn<Diem, String> colMsv = new TableColumn<>("Mã sinh viên");
        colMsv.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getSv().getMasv()));

        TableColumn<Diem, String> colten = new TableColumn<>("Họ và tên");
        colten.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getSv().getTen()));
        TableColumn<Diem, String> colmon = new TableColumn<>("Môn học");
        colmon.setCellValueFactory(new PropertyValueFactory<>("mon"));

        TableColumn<Diem, String> colNam = new TableColumn<>("Năm học");
        colNam.setCellValueFactory(new PropertyValueFactory<>("namhoc"));        //trung ten getter

        TableColumn<Diem, Double> coldiem = new TableColumn<>("Điểm");
        coldiem.setCellValueFactory(new PropertyValueFactory<>("diem"));
        coldiem.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Double>() {
            @Override
            public String toString(Double object) {
                // Chuyển Double thành String để hiển thị
                return object == null ? "" : object.toString();
            }

            @Override
            public Double fromString(String string) {
                // Chuyển String người dùng nhập thành Double
                try {
                    return Double.parseDouble(string);
                } catch (NumberFormatException e) {
                    new Alert(Alert.AlertType.ERROR, "Điểm phải là số thực!").show();
                    return null; // Trả về null để hủy bỏ việc cập nhật
                }
            }
        }));

        coldiem.setOnEditCommit(e -> {
            Double newValue = e.getNewValue();
            Diem diemToUpdate = e.getRowValue();

            if (newValue != null) {
                try {
                    diemToUpdate.setDiem(newValue);
                    DB.updateDiem(diemToUpdate); // Gọi hàm cập nhật CSDL
                } catch (Exception ex) {
                    new Alert(Alert.AlertType.ERROR, "Lỗi cập nhật điểm vào CSDL: " + ex.getMessage()).show();
                    // Tải lại bảng để quay về giá trị cũ
                    e.getTableView().refresh();
                }
            }
        });
        TableColumn<Diem, String> colgpa = new TableColumn<>("GPA");
        colgpa.setCellValueFactory(c -> new SimpleStringProperty(String.format("%.2f", c.getValue().getGPA())));

        tableDiem.getColumns().addAll(colMsv, colten, colmon, colNam, coldiem, colgpa);
        tableDiem.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // load du lieu
        try {
            loadDiemData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Them.setOnAction(e -> {
            Sinhvien sv = cbSV.getValue();
            String mon = cbMon.getValue();
            String namhoc = txtNamhoc.getText().trim();
            String diemStr = txtDiem.getText().trim();

            if (sv == null || mon == null || diemStr.isEmpty() || namhoc.isEmpty()) return;
            double val;
            try {
                val = Double.parseDouble(diemStr);
            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR, "Điểm phải là số!").show();
                return;
            }

            Diem d = new Diem(sv, mon, namhoc, val);
            // 1. Thêm vào DB
            try {
                DB.insertDiem(d); // viết 1 method trong class DB để insert vào table diem
            } catch (Exception ex) {
                ex.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Lỗi lưu database!").show();
                return;
            }

            listDiem.add(d);
            tableDiem.refresh();
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
            }
        });

        // load diem data
        try {
            loadDiemData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        bp.setLeft(grid);
        bp.setCenter(tableDiem);
        setContent(bp);

        }
        public ObservableList<Diem> getListDiem(){
        return listDiem;
    }
    private void clearF(TextField... fields) {
        for (TextField f : fields) f.clear();
    }
}
