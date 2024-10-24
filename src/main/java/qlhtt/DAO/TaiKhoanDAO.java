package qlhtt.DAO;

import qlhtt.ConnectDB.ConnectDB;
import qlhtt.Entity.TaiKhoan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoanDAO {
    private static TaiKhoanDAO instance = new TaiKhoanDAO();

    public static TaiKhoanDAO getInstance() {
        return instance;
    }

    public List<TaiKhoan> getDanhSachTaiKhoan() {
        List<TaiKhoan> dsTaiKhoan = new ArrayList<>();
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM TaiKhoan";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                TaiKhoan taiKhoan = new TaiKhoan(rs);
                dsTaiKhoan.add(taiKhoan);
            }
            ConnectDB.getInstance().disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsTaiKhoan;
    }

    public TaiKhoan getTaiKhoanBangMaTaiKhoan(String maTK) {
        TaiKhoan taiKhoan = null;
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM TaiKhoan WHERE maTaiKhoan = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, maTK);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                taiKhoan = new TaiKhoan(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taiKhoan;
    }
}
