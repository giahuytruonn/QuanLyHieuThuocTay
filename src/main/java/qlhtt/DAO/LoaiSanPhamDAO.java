package qlhtt.DAO;

import qlhtt.ConnectDB.ConnectDB;
import qlhtt.Entity.LoaiSanPham;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoaiSanPhamDAO {
    private static LoaiSanPhamDAO instance = new LoaiSanPhamDAO();
    public static LoaiSanPhamDAO getInstance() {
        return instance;
    }
    /**
     *<B>Note:</B> Lấy danh sách loại sản phẩm
     *@return danhSachLSP
     *
     */
    public List<LoaiSanPham> getDanhSachLoaiSanPham() {
        List<LoaiSanPham> danhSachLSP = new ArrayList<>();
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM LoaiSanPham";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                LoaiSanPham loaiSanPham = new LoaiSanPham(rs);
                danhSachLSP.add(loaiSanPham);
            }
            ConnectDB.getInstance().disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachLSP;
    }

    /**
     *<B>Note:</B> Lấy loại sản phẩm bằng mã loại sản phẩm
     *@return loaiSanPham
     *
     */

    public LoaiSanPham getLoaiSanPhamBangMaLoaiSanPham(String maLSP) {
        LoaiSanPham loaiSanPham = null;
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM LoaiSanPham WHERE maLoaiSanPham = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, maLSP);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                loaiSanPham = new LoaiSanPham(rs);
            }
            ConnectDB.getInstance().disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loaiSanPham;
    }
}
