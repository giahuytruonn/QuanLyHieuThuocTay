package qlhtt.Entity;

import qlhtt.DAO.NhomSanPhamDAO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NhomSanPham {
    private String maNhomSP;
    private String tenNhomSP;

    public NhomSanPham() {
    }

    public NhomSanPham(String maNhomSP, String tenNhomSP) {
        super();
        setMaNhomSP(maNhomSP);
        setTenNhomSP(tenNhomSP);
    }

    public NhomSanPham(String maNhomSP) {
        NhomSanPhamDAO nhomSanPhamDAO = NhomSanPhamDAO.getInstance();
        NhomSanPham nhomSanPham = nhomSanPhamDAO.getNhomSanPhamBangMaNhomSanPham(maNhomSP);
        setMaNhomSP(nhomSanPham.getMaNhomSP());
        setTenNhomSP(nhomSanPham.getTenNhomSP());
    }

    public NhomSanPham(ResultSet rs) throws SQLException {
        setMaNhomSP(rs.getString("maNhomSanPham"));
        setTenNhomSP(rs.getString("tenNhomSanPham"));
    }

    public String getMaNhomSP() {
        return maNhomSP;
    }

    public void setMaNhomSP(String maNhomSP) {
        this.maNhomSP = maNhomSP;
    }

    public String getTenNhomSP() {
        return tenNhomSP;
    }

    public void setTenNhomSP(String tenNhomSP) {
        this.tenNhomSP = tenNhomSP;
    }

    @Override
    public String toString() {
        return "NhomSanPham{" +
                "maNhomSP='" + maNhomSP + '\'' +
                ", tenNhomSP='" + tenNhomSP + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NhomSanPham nhomSanPham = (NhomSanPham) o;
        return maNhomSP.equals(nhomSanPham.maNhomSP);
    }

    @Override
    public int hashCode() {
        return maNhomSP.hashCode();
    }
}
