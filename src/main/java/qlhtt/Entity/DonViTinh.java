package qlhtt.Entity;

import qlhtt.DAO.DonViTinhDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class DonViTinh {
    private String maDonViTinh;
    private String tenDonViTinh;

    public DonViTinh() {
    }

    public DonViTinh(String maDonViTinh, String tenDonViTinh) {
        super();
        setMaDonViTinh(maDonViTinh);
        setTenDonViTinh(tenDonViTinh);
    }

    public DonViTinh(String maDonViTinh) {
        DonViTinhDAO donViTinhDAO = DonViTinhDAO.getInstance();
        DonViTinh donViTinh = donViTinhDAO.getDonViTinhBangMaDonViTinh(maDonViTinh);
        setMaDonViTinh(donViTinh.getMaDonViTinh());
        setTenDonViTinh(donViTinh.getTenDonViTinh());
    }

    public DonViTinh(ResultSet rs) throws SQLException {
        setMaDonViTinh(rs.getString("maDonViTinh"));
        setTenDonViTinh(rs.getString("tenDonViTinh"));
    }

    public String getMaDonViTinh() {
        return maDonViTinh;
    }

    public void setMaDonViTinh(String maDonViTinh) {
        this.maDonViTinh = maDonViTinh;
    }

    public String getTenDonViTinh() {
        return tenDonViTinh;
    }

    public void setTenDonViTinh(String tenDonViTinh) {
        this.tenDonViTinh = tenDonViTinh;
    }

    @Override
    public String toString() {
        return "DonViTinh{" +
                "maDonViTinh='" + maDonViTinh + '\'' +
                ", tenDonViTinh='" + tenDonViTinh + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DonViTinh donViTinh = (DonViTinh) o;
        return Objects.equals(maDonViTinh, donViTinh.maDonViTinh);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maDonViTinh);
    }
}
