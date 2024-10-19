package qlhtt.DAO;

import qlhtt.ConnectDB.ConnectDB;
import qlhtt.Entity.SanPham;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SanPhamDAO {
    private static SanPhamDAO instance = new SanPhamDAO();

    public static SanPhamDAO getInstance() {
        return instance;
    }

    public List<SanPham> getDanhSachSanPham() {
        List<SanPham> dsSanPham = new ArrayList<>();
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM SanPham";
            Statement statement = con.createStatement();
            statement.executeQuery(sql);
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                SanPham sanPham = new SanPham(rs);
                dsSanPham.add(sanPham);
            }
            ConnectDB.getInstance().disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsSanPham;
    }

    public SanPham getSanPhamBangMaSanPham(String maSP) {
        SanPham sanPham = null;
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM SanPham WHERE maSanPham = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, maSP);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                sanPham = new SanPham(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sanPham;
    }
}
