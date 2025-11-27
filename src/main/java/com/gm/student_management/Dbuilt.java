package com.gm.student_management;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Dbuilt {
    private static final String URL = "jdbc:mysql://localhost:3306/sinhvien_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";          // đổi theo password của bạn
    private static final String PASS = "121127Chi";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
