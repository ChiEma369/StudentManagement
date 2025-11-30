package com.gm.student_management;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DB {
    private static final String URL = "jdbc:mysql://localhost:3306/sinhvien_db";
    private static final String USER = "root";          // đổi theo password của bạn
    private static final String PASS = "121127Chi";

    public static Connection getConnect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
    public static void insertDiem(Diem d) throws SQLException {
        String sql = "INSERT INTO diem (masv, mon, lop, diem) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, d.getSv().getMasv());
            ps.setString(2, d.getMon());
            ps.setString(3, d.getLop());
            ps.setDouble(4, d.getDiem());
            ps.executeUpdate();
        }
    }

    // 2. Xóa điểm
    public static void deleteDiem(Diem d) throws SQLException {
        String sql = "DELETE FROM diem WHERE masv=? AND mon=? AND lop=?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, d.getSv().getMasv());
            ps.setString(2, d.getMon());
            ps.setString(3, d.getLop());
            ps.executeUpdate();
        }
    }

    // 3. Load tất cả điểm từ DB
    public static ObservableList<Diem> loadAllDiem() throws SQLException {
        ObservableList<Diem> list = FXCollections.observableArrayList();
        String sql = "SELECT masv, mon, lop, diem FROM diem";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String masv = rs.getString("masv");
                String mon = rs.getString("mon");
                String lop = rs.getString("lop");
                double diemVal = rs.getDouble("diem");

                Sinhvien sv = Mainapp.getSinhvien().stream()
                        .filter(s -> s.getMasv().equals(masv))
                        .findFirst().orElse(null);

                if (sv != null) {
                    list.add(new Diem(sv, mon, lop, diemVal));
                }
            }
        }
        return list;
    }

    public static List<Diem> getAllDiem() throws SQLException {
        List<Diem> list = new ArrayList<>();
        Connection conn = getConnect();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM diem");
        while (rs.next()) {
            Sinhvien sv = sinhvienDb.findByMasv(rs.getString("masv"));

            if (sv != null) {
                list.add(new Diem(sv, rs.getString("mon"), rs.getString("lop"), rs.getDouble("diem")));
            }
        }
        rs.close(); st.close(); conn.close();
        return list;
    }
}
