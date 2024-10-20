package qlhtt.DAO;

import qlhtt.ConnectDB.ConnectDB;
import qlhtt.Entity.KhachHang;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO {
    private static KhachHangDAO instance = new KhachHangDAO();

    public static KhachHangDAO getInstance() {
        return instance;
    }

    /**
     *
     * @return dsKhachHang
     */
    public List<KhachHang> getDanhSachKhachHang() {
        List<KhachHang> dsKhachHang = new ArrayList<>();
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM KhachHang";
            Statement statement = con.createStatement();
            statement.executeQuery(sql);
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                KhachHang khachHang = new KhachHang(rs);
                dsKhachHang.add(khachHang);
            }
            ConnectDB.getInstance().disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsKhachHang;
    }

    /**
     *
     * @return khachHang
     */

    public KhachHang getKhachHangBangMaKhachHang(String maKH) {
        KhachHang khachHang = null;
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM KhachHang WHERE maKhachHang = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, maKH);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                khachHang = new KhachHang(rs);
            }
            ConnectDB.getInstance().disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return khachHang;
    }
}

