package com.gm.student_management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class sinhvienDb{
    public static List<Sinhvien> getAll(){
        List<Sinhvien> list = new ArrayList<>();
        String sql = "select * from sinhvien";

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
        } catch (Exception e){
            e.printStackTrace();
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
    public static Sinhvien findByMasv(String masv) throws SQLException {
        Connection conn = DB.getConnect();
        String sql = "SELECT * FROM sinhvien WHERE masv = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, masv);
        ResultSet rs = ps.executeQuery();
        Sinhvien sv = null;
        if (rs.next()) {
            sv = new Sinhvien(
                    rs.getString("masv"),
                    rs.getString("ten"),
                    rs.getString("birth"),
                    rs.getString("gt"),
                    rs.getString("lop")
            );
        }
        rs.close();
        ps.close();
        conn.close();
        return sv;
    }
}