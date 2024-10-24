package qlhtt.DAO;

import qlhtt.ConnectDB.ConnectDB;
import qlhtt.Entity.ChietKhau;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChietKhauDAO {
    private static ChietKhauDAO instance = new ChietKhauDAO();

    public static ChietKhauDAO getInstance() {
        return instance;
    }

    /**
     * <B>Note:<B/> Lấy danh sách chiết khấu
     * @return danhSachCK
     */

    public List<ChietKhau> getDanhSachChietKhau() {
        List<ChietKhau> danhSachCK = new ArrayList<>();
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM ChietKhau";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                ChietKhau chietKhau = new ChietKhau(rs);
                danhSachCK.add(chietKhau);
            }
            ConnectDB.getInstance().disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachCK;
    }

    /**
     * <B>Note:<B/> Lấy chiết khấu bằng mã chiết khấu
     * @return chietKhau
     */

    public ChietKhau getChietKhauBangMaChietKhau(String maCK) {
        ChietKhau chietKhau = null;
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM ChietKhau WHERE maChietKhau = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, maCK);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                chietKhau = new ChietKhau(rs);
            }
            ConnectDB.getInstance().disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chietKhau;
    }
}
