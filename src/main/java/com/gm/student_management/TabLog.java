package com.gm.student_management;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;


public class TabLog extends Application {

    private TextField email =  new TextField();
    private PasswordField password =  new PasswordField();
    private Label error = new Label();
    private Button login = new Button("Đăng nhập");

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hệ thống quản lý _ Đăng nhập");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Tiêu đề
        Label title = new Label("ĐĂNG NHẬP HỆ THỐNG");
        title.setStyle("-fx-font-size: 18pt; -fx-font-weight: bold;");
        grid.add(title, 0, 0, 2, 1); // span 2 columns

        // Tên người dùng
        grid.add(new Label("Email:"), 0, 1);
        email.setPromptText("24100317@gmail.com");
        grid.add(email, 1, 1);

        // Mật khẩu
        grid.add(new Label("Mật khẩu:"), 0, 2);
        password.setPromptText("99999");
        grid.add(password, 1, 2);

        // Nút Đăng nhập
        grid.add(login, 1, 3);
        // Label báo lỗi
        error.setStyle("-fx-text-fill: red;");
        grid.add(error, 0, 4, 2, 1);

        // --- Xử lý sự kiện Đăng nhập ---
        login.setOnAction(e -> handleLogin(primaryStage));

        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Xử lý logic kiểm tra đăng nhập
    private void handleLogin(Stage loginStage) {
        String email1 = email.getText();
        String password1 = password.getText();

        if (email1.isEmpty() || password1.isEmpty()) {
            error.setText("Vui lòng nhập đầy đủ email người dùng và mật khẩu.");
            return;
        }

        try {
            if (DB.login(email1, password1)) {
                error.setText("Đăng nhập thành công!");
                loginStage.close();
                openMain();    // Mở ứng dụng chính
            } else {
                error.setText("Tên người dùng hoặc Mật khẩu không đúng.");
                password.clear();
            }
        } catch (SQLException e) {
            error.setText("Lỗi kết nối CSDL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void openMain() {
        Stage mainStage = new Stage();
        mainStage.setTitle("HỆ THỐNG QUẢN LÝ SINH VIÊN (ĐÃ ĐĂNG NHẬP)");

        Mainapp.openMain(mainStage);
    }

    public static void main(String[] args) {
        launch(TabLog.class, args);
    }
}

