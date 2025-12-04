package com.gm.student_management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class sinhvienDb{
    public static List<Sinhvien> getAll() throws SQLException {
        List<Sinhvien> list = new ArrayList<>();
        String sql = "select * from sinhvien";

        // BỎ KHỐI TRY-CATCH, ĐỂ CONTROLLER XỬ LÝ
        try(Connection conn = DB.getConnect();
            PreparedStatement  ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while(rs.next()) {
                list.add(new Sinhvien(
                        rs.getString("masv"),
                        rs.getString("ten"),
                        rs.getString("birth"),
                        rs.getString("sex"),
                        rs.getString("lop")
                ));
            }
        }

        return list;
    }
    public static void insert(Sinhvien sv){
        String sql = "Insert into sinhvien values(?, ?, ?, ?, ?)";

        try(Connection conn = DB.getConnect();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, sv.getMasv());
            ps.setString(2, sv.getTen());
            ps.setString(3, sv.getBirth());
            ps.setString(4, sv.getSex());
            ps.setString(5, sv.getLop());

            ps.executeUpdate();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public static List<Sinhvien> searchName(String ten) throws SQLException {
        List<Sinhvien> list = new ArrayList<>();
        String sql = "SELECT * FROM sinhvien WHERE ten LIKE ?";

        // tim kiem tuong doi
        try (Connection conn = DB.getConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + ten + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Sinhvien(
                            rs.getString("masv"),
                            rs.getString("ten"),
                            rs.getString("birth"),
                            rs.getString("sex"),
                            rs.getString("lop")
                    ));
                }
            }
        }
        return list;
    }
    public static Sinhvien findByMasv(String masv) throws SQLException {
        String sql = "SELECT * FROM sinhvien WHERE masv = ?";
        Sinhvien sv = null;

        // SỬ DỤNG TRY-WITH-RESOURCES ĐỂ ĐÓNG TỰ ĐỘNG
        try (Connection conn = DB.getConnect();
             PreparedStatement ps = conn.prepareStatement(sql);) {

            ps.setString(1, masv);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    sv = new Sinhvien(
                            rs.getString("masv"),
                            rs.getString("ten"),
                            rs.getString("birth"),
                            rs.getString("sex"),
                            rs.getString("lop")
                    );
                }
            }
        }
        return sv;
    }
    public static void delete(Sinhvien sv) throws SQLException {
        String sql = "DELETE FROM sinhvien WHERE masv = ?";

        try (Connection conn = DB.getConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sv.getMasv());
            ps.executeUpdate(); // Thực thi lệnh xóa

        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi xóa sinh viên: " + e.getMessage());
            throw e;
        }
    }
}