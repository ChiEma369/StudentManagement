package com.gm.student_management;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.gm.student_management.Sinhvien;
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
    public static boolean login(String email, String password) throws SQLException {
        String sql = "select password from user where email = ?";

        try (Connection conn = getConnect();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);

            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String pass = rs.getString("password");
                    return password.equals(pass);
                }
            }
        }
        return false; //sai nguoi dung, mkhau
    }
    public static void updateDiem(Diem d) throws SQLException {
        String sql = "UPDATE diem SET diem=? WHERE masv=? AND mon=? AND namhoc=?";

        try (Connection conn = getConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Gán tham số cho câu lệnh SQL:
            ps.setDouble(1, d.getDiem());
            ps.setString(2, d.getSv().getMasv());
            ps.setString(3, d.getMon());
            ps.setString(4, d.getNam());

            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi cập nhật điểm: " + e.getMessage());
            throw e;
        }
    }
    public static void insertDiem(Diem d) throws SQLException {
        String sql = "INSERT INTO diem (masv, mon, namhoc, diem) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, d.getSv().getMasv());
            ps.setString(2, d.getMon());
            ps.setString(3, d.getNam());
            ps.setDouble(4, d.getDiem());
            ps.executeUpdate();
        }
    }

    public static void deleteDiem(Diem d) throws SQLException {
        String sql = "DELETE FROM diem WHERE masv=? AND mon=? AND namhoc=?";
        try (Connection conn = getConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, d.getSv().getMasv());
            ps.setString(2, d.getMon());
            ps.setString(3, d.getNam());
            ps.executeUpdate();
        }
    }

    // 3. Load tất cả điểm từ DB
    public static ObservableList<Diem> loadAllDiem(ObservableList<Sinhvien> sinhvien) throws SQLException {
        ObservableList<Diem> list = FXCollections.observableArrayList();
        String sql = "SELECT masv, mon, namhoc, diem FROM diem";
        try (Connection conn = getConnect(); 
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String masv = rs.getString("masv");
                String mon = rs.getString("mon");
                String namhoc = rs.getString("namhoc");
                double diemVal = rs.getDouble("diem");

                Sinhvien sv = sinhvien.stream()
                        .filter(s -> s.getMasv().equals(masv))
                        .findFirst().orElse(null);

                if (sv != null) {
                    list.add(new Diem(sv, mon, namhoc, diemVal));
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
                list.add(new Diem(sv, rs.getString("mon"), rs.getString("namhoc"), rs.getDouble("diem")));
            }
        }
        rs.close(); st.close(); conn.close();
        return list;
    }
    // thong ke diem
    public static int[] ThongKe(String tenMon, String gioiTinh, String namhoc) throws SQLException {
        int[] ketqua = {0, 0, 0, 0, 0};

        String sql = "SELECT d.diem FROM diem d " + "JOIN sinhvien s ON d.masv = s.masv " + "WHERE d.mon = ? ? AND d.namhoc = ?";

        if (!gioiTinh.equals("Tất cả")) {
            sql += " AND s.sex = ?";
        }

        try (Connection conn = getConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tenMon);
            ps.setString(2, namhoc);
            if (!gioiTinh.equals("Tất cả")) {
                ps.setString(3, gioiTinh);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    double diem = rs.getDouble("diem");

                    if (diem < 4.0) ketqua[0]++;
                    else if (diem < 5.0) ketqua[1]++;
                    else if (diem < 7.0) ketqua[2]++;
                    else if (diem < 8.5) ketqua[3]++;   // Khá
                    else ketqua[4]++;                   // Giỏi
                }
            }
        }
        return ketqua;
    }

    public static List<String> getDsMon() throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT mon FROM diem";
        try (Connection conn = getConnect();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(rs.getString("mon"));
        }
        return list;
    }
    public static List<String> getDsNamhoc() throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT namhoc FROM diem ORDER BY namhoc DESC";
        try (Connection conn = getConnect();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(rs.getString("namhoc"));
        }
        return list;
    }
}
