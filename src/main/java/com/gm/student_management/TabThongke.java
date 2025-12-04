package com.gm.student_management;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.sql.SQLException;
import java.util.List;

public class TabThongke extends Tab {

    private BarChart<String, Number> chart;
    private ComboBox<String> cbMon;
    private ComboBox<String> cbSex;
    private ComboBox<String> cbNamhoc;

    public TabThongke() {
        setText("Thống kê & Báo cáo");

        // 1. Tạo thanh công cụ (Toolbar) phía trên
        HBox toolbar = new HBox(15);
        toolbar.setPadding(new Insets(15));
        toolbar.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #ccc; -fx-border-width: 0 0 1 0;");

        Label Mon = new Label("Chọn Môn học:");
        cbMon = new ComboBox<>();
        cbMon.setPrefWidth(200);

        Label lbNam = new Label("Năm học:");
        cbNamhoc = new ComboBox<>();
        cbNamhoc.setPrefWidth(120);

        Label Sex = new Label("Giới tính:");
        cbSex = new ComboBox<>();
        cbSex.getItems().addAll("Tất cả", "Nam", "Nữ");
        cbSex.setValue("Tất cả");

        Button Xem = new Button("Biểu Đồ");
        // Thêm icon

        toolbar.getChildren().addAll(Mon, cbMon, lbNam, cbNamhoc, Sex, cbSex, Xem);

        // 2. Tạo Biểu đồ
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Phân loại học lực");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Số lượng sinh viên");

        chart = new BarChart<>(xAxis, yAxis);
        chart.setCategoryGap(100);
        chart.setBarGap(20);

        chart.setTitle("PHỔ ĐIỂM CHI TIẾT");
        chart.setLegendSide(Side.BOTTOM);
        chart.setAnimated(false); // Hiệu ứng chuyển động

        // 3. Bố cục chính
        BorderPane root = new BorderPane();
        root.setTop(toolbar);
        root.setCenter(chart);

        setContent(root);

        // 4. Load danh sách môn
        loadCombo();
        Xem.setOnAction(e -> hienThi());
    }

    private void loadCombo() {
        try {
            cbMon.setItems(FXCollections.observableArrayList(DB.getDsMon()));
            if (!cbMon.getItems().isEmpty()) { cbMon.getSelectionModel().selectFirst(); }

            List<String> listNam = DB.getDsNamhoc();
            listNam.add(0, "Tất cả");
            cbNamhoc.setItems(FXCollections.observableArrayList(listNam));
            cbNamhoc.getSelectionModel().selectFirst();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void hienThi() {
        String mon = cbMon.getValue();
        String nam = cbNamhoc.getValue();
        String gioiTinh = cbSex.getValue();

        if (mon == null || nam == null) {
            new Alert(Alert.AlertType.WARNING, "Vui lòng chọn Môn và Năm học!").show();
            return;
        }

        try {
            chart.getData().clear();
            int[] data = DB.ThongKe(mon, gioiTinh, nam);

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Môn: " + mon + " (" + gioiTinh + ")");

            series.getData().add(new XYChart.Data<>("Kém (<4)", data[0]));
            series.getData().add(new XYChart.Data<>("Yếu (4-5)", data[1]));
            series.getData().add(new XYChart.Data<>("Trung bình (5-7)", data[2]));
            series.getData().add(new XYChart.Data<>("Khá (7-8.5)", data[3]));
            series.getData().add(new XYChart.Data<>("Giỏi (>8.5)", data[4]));

            chart.getData().add(series);

            // chinh mau sac
            String mauSac = "";
            switch (gioiTinh) {
                case "Nam":
                    mauSac = "#2196F3";
                    break;
                case "Nữ":
                    mauSac = "#FF69B4";
                    break;
                default: // "Tất cả"
                    mauSac = "#20B2AA";
                    break;
            }

            for (XYChart.Data<String, Number> entry : series.getData()) {
                if (entry.getNode() != null) {
                    entry.getNode().setStyle("-fx-bar-fill: " + mauSac + ";");
                }
            }

            chart.setTitle("BIỂU ĐỒ PHỔ ĐIỂM " + mon.toUpperCase());

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Lỗi khi tải dữ liệu thống kê!").show();
            e.printStackTrace();
        }
    }
}