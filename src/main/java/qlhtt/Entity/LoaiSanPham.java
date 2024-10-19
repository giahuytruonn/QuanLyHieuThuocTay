package qlhtt.Entity;

import qlhtt.DAO.LoaiSanPhamDAO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoaiSanPham {
    private String maLoaiSP;
    private String tenLoaiSP;

    public LoaiSanPham() {
    }

    public LoaiSanPham(String maLoaiSP, String tenLoaiSP) {
        super();
        setMaLoaiSP(maLoaiSP);
        setTenLoaiSP(tenLoaiSP);
    }

    public LoaiSanPham(String maLoaiSP) {
        LoaiSanPhamDAO loaiSanPhamDAO = LoaiSanPhamDAO.getInstance();
        LoaiSanPham loaiSanPham = loaiSanPhamDAO.getLoaiSanPhamBangMaLoaiSanPham(maLoaiSP);
        setMaLoaiSP(loaiSanPham.getMaLoaiSP());
        setTenLoaiSP(loaiSanPham.getTenLoaiSP());
    }

    public LoaiSanPham(ResultSet rs) throws SQLException {
        setMaLoaiSP(rs.getString("maLoaiSanPham"));
        setTenLoaiSP(rs.getString("tenLoaiSanPham"));
    }

    public String getMaLoaiSP() {
        return maLoaiSP;
    }

    public void setMaLoaiSP(String maLoaiSP) {
        this.maLoaiSP = maLoaiSP;
    }

    public String getTenLoaiSP() {
        return tenLoaiSP;
    }

    public void setTenLoaiSP(String tenLoaiSP) {
        this.tenLoaiSP = tenLoaiSP;
    }

    @Override
    public String toString() {
        return "LoaiSanPham{" +
                "maLoaiSP='" + maLoaiSP + '\'' +
                ", tenLoaiSP='" + tenLoaiSP + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoaiSanPham loaiSanPham = (LoaiSanPham) o;
        return maLoaiSP.equals(loaiSanPham.maLoaiSP);
    }

    @Override
    public int hashCode() {
        return maLoaiSP.hashCode();
    }
}
