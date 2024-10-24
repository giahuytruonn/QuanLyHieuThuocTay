package qlhtt.DAO;

import qlhtt.ConnectDB.ConnectDB;
import qlhtt.Entity.HoaDon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class HoaDonDAO {
    private static HoaDonDAO instance = new HoaDonDAO();

    public static HoaDonDAO getInstance() {
        return instance;
    }

    public List<HoaDon> getDanhSachHoaDon() {
        List<HoaDon> dsHoaDon = new ArrayList<>();
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM HoaDon";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                HoaDon hoaDon = new HoaDon(rs);
                dsHoaDon.add(hoaDon);
            }
            ConnectDB.getInstance().disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsHoaDon;
    }

    public HoaDon getHoaDonBangMaHoaDon(String maHD) {
        HoaDon hoaDon = null;
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM HoaDon WHERE maHoaDon = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, maHD);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                hoaDon = new HoaDon(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hoaDon;
    }
}
