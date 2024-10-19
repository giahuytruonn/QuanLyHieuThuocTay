package qlhtt.DAO;

import qlhtt.ConnectDB.ConnectDB;
import qlhtt.Entity.ChiTietPhieuNhap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietPhieuNhapDAO {
    private static ChiTietPhieuNhapDAO instance = new ChiTietPhieuNhapDAO();

    public static ChiTietPhieuNhapDAO getInstance() {
        return instance;
    }

    public List<ChiTietPhieuNhap> getDanhSachChiTietPhieuNhap() {
        List<ChiTietPhieuNhap> dsChiTietPhieuNhap = new ArrayList<>();
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM ChiTietPhieuNhap";
            Statement statement = con.createStatement();
            statement.executeQuery(sql);
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                ChiTietPhieuNhap chiTietPhieuNhap = new ChiTietPhieuNhap(rs);
                dsChiTietPhieuNhap.add(chiTietPhieuNhap);
            }
            ConnectDB.getInstance().disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsChiTietPhieuNhap;
    }

    public ChiTietPhieuNhap getChiTietPhieuNhapBangMaPhieuNhap(String maPhieuNhap) {
        ChiTietPhieuNhap chiTietPhieuNhap = null;
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM ChiTietPhieuNhap WHERE maPhieuNhap = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, maPhieuNhap);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                chiTietPhieuNhap = new ChiTietPhieuNhap(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chiTietPhieuNhap;
    }
}
