package qlhtt.Entity;

import qlhtt.DAO.NhanVienDAO;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;

public class NhanVien {
    private String maNhanVien;
    private String tenNhanVien;
    private String soDienThoai;
    private String email;
    private String cccd;
    private int vaiTro;
    private LocalDate ngaySinh;
    private Boolean gioiTinh;


    public NhanVien(String maNhanVien, String tenNhanVien, String soDienThoai, String email, String cccd, int vaiTro, LocalDate ngaySinh, Boolean gioiTinh) {
        this.maNhanVien = maNhanVien;
        this.tenNhanVien = tenNhanVien;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.cccd = cccd;
        this.vaiTro = vaiTro;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
    }

    public NhanVien() {

    }

    public NhanVien(String maNhanVien) {
        NhanVienDAO nhanVienDAO = new NhanVienDAO();
        NhanVien nhanVien = nhanVienDAO.getNhanVienBangMaNhanVien(maNhanVien);
        setMaNhanVien(nhanVien.getMaNhanVien());
        setTenNhanVien(nhanVien.getTenNhanVien());
        setSoDienThoai(nhanVien.getSoDienThoai());
        setEmail(nhanVien.getEmail());
        setCccd(nhanVien.getCccd());
        setVaiTro(nhanVien.getVaiTro());
        setNgaySinh(nhanVien.getNgaySinh());
        setGioiTinh(nhanVien.getGioiTinh());
    }

    public NhanVien(ResultSet rs) throws SQLException{
        this.maNhanVien = rs.getString("maNhanVien");
        this.tenNhanVien = rs.getString("tenNhanVien");
        this.soDienThoai = rs.getString("soDienThoai");
        this.email = rs.getString("email");
        this.cccd = rs.getString("cccd");
        this.vaiTro = rs.getInt("vaiTro");
        this.ngaySinh = rs.getDate("ngaySinh").toLocalDate();
        this.gioiTinh = rs.getBoolean("gioiTinh");
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public Boolean getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(Boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public int getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(int vaiTro) {
        this.vaiTro = vaiTro;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
    }

    @Override
    public String toString() {
        return "NhanVien{" +
                "maNhanVien='" + maNhanVien + '\'' +
                ", tenNhanVien='" + tenNhanVien + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", email='" + email + '\'' +
                ", cccd='" + cccd + '\'' +
                ", vaiTro=" + vaiTro +
                ", ngaySinh=" + ngaySinh +
                ", gioiTinh=" + gioiTinh +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NhanVien nhanVien = (NhanVien) o;
        return Objects.equals(maNhanVien, nhanVien.maNhanVien);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maNhanVien);
    }
}
