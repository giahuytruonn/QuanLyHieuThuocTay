package qlhtt.DAO;

import qlhtt.ConnectDB.ConnectDB;
import qlhtt.Entity.DonViTinh;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DonViTinhDAO {
    private static DonViTinhDAO instance = new DonViTinhDAO();
    public static DonViTinhDAO getInstance() {
        return instance;
    }
    /**
     *<B>Note:</B> Lấy danh sách đơn vị tính
     *@return danhSachDVT
     *
     */
    public List<DonViTinh> getDanhSachDonViTinh() {
        List<DonViTinh> danhSachDVT = new ArrayList<>();
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM DonViTinh";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                DonViTinh donViTinh = new DonViTinh(rs);
                danhSachDVT.add(donViTinh);
            }
            ConnectDB.getInstance().disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachDVT;
    }

    /**
     *<B>Note:</B> Lấy đơn vị tính bằng mã đơn vị tính
     *@return donViTinh
     *
     */
    public DonViTinh getDonViTinhBangMaDonViTinh(String maDVT) {
        DonViTinh donViTinh = null;
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM DonViTinh WHERE maDonViTinh = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, maDVT);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                donViTinh = new DonViTinh(rs);
            }
            ConnectDB.getInstance().disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return donViTinh;
    }
}
