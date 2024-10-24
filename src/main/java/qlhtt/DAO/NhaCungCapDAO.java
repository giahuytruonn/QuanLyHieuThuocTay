package qlhtt.DAO;

import qlhtt.ConnectDB.ConnectDB;
import qlhtt.Entity.NhaCungCap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhaCungCapDAO {
    private static NhaCungCapDAO instance = new NhaCungCapDAO();

    public static NhaCungCapDAO getInstance() {
        return instance;
    }

    /**
     *
     * @return dsNhaCungCap
     */
    public List<NhaCungCap> getDanhSachNhaCungCap() {
        List<NhaCungCap> dsNhaCungCap = new ArrayList<>();
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM NhaCungCap";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                NhaCungCap nhaCungCap = new NhaCungCap(rs);
                dsNhaCungCap.add(nhaCungCap);
            }
            ConnectDB.getInstance().disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsNhaCungCap;
    }

    /**
     *
     * @return nhaCungCap
     */

    public NhaCungCap getNhaCungCapBangMaNhaCungCap(String maNCC) {
        NhaCungCap nhaCungCap = null;
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM NhaCungCap WHERE maNhaCungCap = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, maNCC);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                nhaCungCap = new NhaCungCap(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nhaCungCap;
    }
}
