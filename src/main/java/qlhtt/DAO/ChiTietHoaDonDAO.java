package qlhtt.DAO;

import qlhtt.ConnectDB.ConnectDB;
import qlhtt.Entity.ChiTietHoaDon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietHoaDonDAO {
    private static ChiTietHoaDonDAO instance = new ChiTietHoaDonDAO();

    public static ChiTietHoaDonDAO getInstance() {
        return instance;
    }

    public List<ChiTietHoaDon> getDanhSachChiTietHoaDon() {
        List<ChiTietHoaDon> dsChiTietHoaDon = new ArrayList<>();
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM ChiTietHoaDon";
            Statement statement = con.createStatement();
            statement.executeQuery(sql);
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon(rs);
                dsChiTietHoaDon.add(chiTietHoaDon);
            }
            ConnectDB.getInstance().disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsChiTietHoaDon;
    }

    public ChiTietHoaDon getChiTietHoaDonBangMaHoaDon(String maHoaDon) {
        ChiTietHoaDon chiTietHoaDon = null;
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM ChiTietHoaDon WHERE maHoaDon = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, maHoaDon);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                chiTietHoaDon = new ChiTietHoaDon(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chiTietHoaDon;
    }
}
