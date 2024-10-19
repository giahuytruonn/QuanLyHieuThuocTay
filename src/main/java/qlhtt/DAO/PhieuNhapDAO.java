package qlhtt.DAO;

import qlhtt.ConnectDB.ConnectDB;
import qlhtt.Entity.PhieuNhap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhieuNhapDAO {
    private static PhieuNhapDAO instance = new PhieuNhapDAO();

    public static PhieuNhapDAO getInstance() {
        return instance;
    }

    public List<PhieuNhap> getDanhSachPhieuNhap() {
        List<PhieuNhap> dsPhieuNhap = new ArrayList<>();
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM PhieuNhap";
            Statement statement = con.createStatement();
            statement.executeQuery(sql);
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                PhieuNhap phieuNhap = new PhieuNhap(rs);
                dsPhieuNhap.add(phieuNhap);
            }
            ConnectDB.getInstance().disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsPhieuNhap;
    }

    public PhieuNhap getPhieuNhapBangMaPhieuNhap(String maPhieuNhap) {
        PhieuNhap phieuNhap = null;
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM PhieuNhap WHERE maPhieuNhap = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, maPhieuNhap);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                phieuNhap = new PhieuNhap(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return phieuNhap;
    }
}
