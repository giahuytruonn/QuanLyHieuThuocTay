package qlhtt.Entity;

import qlhtt.DAO.NhaCungCapDAO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NhaCungCap {
    private String maNhaCungCap;
    private String tenNhaCungCap;
    private String soDienThoai;
    private String diaChi;
    private String email;

    public NhaCungCap() {
    }

    public NhaCungCap(String maNhaCungCap, String tenNhaCungCap, String soDienThoai, String diaChi, String email) {
        super();
        setMaNhaCungCap(maNhaCungCap);
        setTenNhaCungCap(tenNhaCungCap);
        setSoDienThoai(soDienThoai);
        setDiaChi(diaChi);
        setEmail(email);
    }

    public NhaCungCap(String maNhaCungCap) {
        NhaCungCapDAO nhaCungCapDAO = NhaCungCapDAO.getInstance();
        NhaCungCap nhaCungCap = nhaCungCapDAO.getNhaCungCapBangMaNhaCungCap(maNhaCungCap);
        setMaNhaCungCap(nhaCungCap.getMaNhaCungCap());
        setTenNhaCungCap(nhaCungCap.getTenNhaCungCap());
        setSoDienThoai(nhaCungCap.getSoDienThoai());
        setDiaChi(nhaCungCap.getDiaChi());
        setEmail(nhaCungCap.getEmail());
    }

    public NhaCungCap(ResultSet rs) throws SQLException {
        setMaNhaCungCap(rs.getString("maNhaCungCap"));
        setTenNhaCungCap(rs.getString("tenNhaCungCap"));
        setSoDienThoai(rs.getString("soDienThoai"));
        setDiaChi(rs.getString("diaChi"));
        setEmail(rs.getString("email"));
    }

    public String getMaNhaCungCap() {
        return maNhaCungCap;
    }

    public void setMaNhaCungCap(String maNhaCungCap) {
        this.maNhaCungCap = maNhaCungCap;
    }

    public String getTenNhaCungCap() {
        return tenNhaCungCap;
    }

    public void setTenNhaCungCap(String tenNhaCungCap) {
        this.tenNhaCungCap = tenNhaCungCap;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "NhaCungCap{" +
                "maNhaCungCap='" + maNhaCungCap + '\'' +
                ", tenNhaCungCap='" + tenNhaCungCap + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", diaChi='" + diaChi + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NhaCungCap nhaCungCap = (NhaCungCap) o;
        return maNhaCungCap.equals(nhaCungCap.maNhaCungCap);
    }

    @Override
    public int hashCode() {
        return maNhaCungCap.hashCode();
    }
}
