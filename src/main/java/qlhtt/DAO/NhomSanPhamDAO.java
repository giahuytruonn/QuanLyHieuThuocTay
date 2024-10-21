package qlhtt.DAO;

import qlhtt.ConnectDB.ConnectDB;
import qlhtt.Entity.NhanVien;
import qlhtt.Entity.NhomSanPham;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhomSanPhamDAO {
    private static NhomSanPhamDAO instance = new NhomSanPhamDAO();
    public static NhomSanPhamDAO getInstance() {
        return instance;
    }
    /**
     *<B>Note:</B> Lấy danh sách nhóm sản phẩm
     *@return danhSachNSP
     *
     */
    public List<NhomSanPham> getDanhSachNhomSanPham() {
        List<NhomSanPham> danhSachNSP = new ArrayList<>();
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM NhomSanPham";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                NhomSanPham nhomSanPham = new NhomSanPham(rs);
                danhSachNSP.add(nhomSanPham);
            }
            ConnectDB.getInstance().disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachNSP;
    }

    /**
     *<B>Note:</B> Lấy nhóm sản phẩm bằng mã nhóm sản phẩm
     *@return nhomSanPham
     *
     */

    public NhomSanPham getNhomSanPhamBangMaNhomSanPham(String maNSP) {
        NhomSanPham nhomSanPham = null;
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM NhomSanPham WHERE maNhomSanPham = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, maNSP);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                nhomSanPham = new NhomSanPham(rs);
            }
            ConnectDB.getInstance().disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nhomSanPham;
    }
}
