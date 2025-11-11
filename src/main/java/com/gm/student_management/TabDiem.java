package com.gm.student_management;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class TabDiem extends Tab {
    private ObservableList<Diem> listDiem = FXCollections.observableArrayList();
    private ObservableList<Sinhvien> sinhvien;
    public TabDiem(ObservableList<Sinhvien> sinhvien, Stage stage) {
        setText("Quản lý điểm");

        ComboBox<String> cbMasv = new ComboBox<>();
        for(Sinhvien s: sinhvien){
            cbMasv.getItems().add(s.getMasv());
        }
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
        Button Them = new Button("Thêm điểm");
        Button xoa = new Button("Xóa");
        HBox button = new HBox(10);

        grid.add(new Label("Sinh viên:"), 0, 0); grid.add(cbSV, 1, 0);
        grid.add(new Label("Môn:"), 0, 1); grid.add(cbMon, 1, 1);
        grid.add(new Label("Điểm:"), 0, 2); grid.add(txtDiem, 1, 2);
        button.getChildren().addAll(Them, xoa);
        grid.add(button, 1, 3);

        TableView<Diem> tableDiem = new TableView<>(listDiem);
        TableColumn<Diem, String> colMsv = new TableColumn<>("Mã sinh viên");
        colMsv.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getSv().getMasv()));

        TableColumn<Diem, String> colten = new TableColumn<>("Họ và tên");
        colten.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getSv().getTen()));
        TableColumn<Diem, String> colmon = new TableColumn<>("Môn học");
        colmon.setCellValueFactory(new PropertyValueFactory<>("mon"));
        TableColumn<Diem, String> coldiem = new TableColumn<>("Điểm");
        coldiem.setCellValueFactory(new PropertyValueFactory<>("diem"));

        tableDiem.getColumns().addAll(colMsv, colten, colmon, coldiem);

        Them.setOnAction(e-> {
            Sinhvien sv = cbSV.getValue();
            String mon = cbMon.getValue();
            String diemStr = txtDiem.getText().trim();

            if (sv == null || mon == null || diemStr.isEmpty()) return;
            double val;
            try {
                val = Double.parseDouble(diemStr);
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Điểm phải là số!");
                alert.show();
                return;
            }
            Diem d = new Diem(sv, mon, val);
            listDiem.add(d);

            tableDiem.refresh();
        });
        xoa.setOnAction(e -> {
            Diem sel = tableDiem.getSelectionModel().getSelectedItem();
            if (sel != null) listDiem.remove(sel);
        });
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
