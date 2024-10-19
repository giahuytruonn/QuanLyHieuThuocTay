package qlhtt.DAO;

import qlhtt.ConnectDB.ConnectDB;
import qlhtt.Entity.NhanVien;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class NhanVienDAO {
    private static NhanVienDAO instance = new NhanVienDAO();

    public static NhanVienDAO getInstance() {
        return instance;
    }

    /**
     *<B>Note:</B> Lấy danh sách nhân viên
     *@return danhSachNV
     *
     */
    public List<NhanVien> getDanhSachNhanVien() {
        List<NhanVien> danhSachNV = new ArrayList<>();
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM NhanVien";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                NhanVien nhanVien = new NhanVien(rs);
                danhSachNV.add(nhanVien);
            }
            ConnectDB.getInstance().disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachNV;
    }
    /**
     *<B>Note:</B> Lấy nhân viên bằng mã nhân viên
     *@return nhanVien
     *
     */

    public NhanVien getNhanVienBangMaNhanVien(String maNV) {
        NhanVien nhanVien = null;
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM NhanVien WHERE maNhanVien = '" + maNV + "'";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) {
                nhanVien = new NhanVien(rs);
            }
            ConnectDB.getInstance().disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
    }
        return nhanVien;
    }
}
