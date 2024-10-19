package qlhtt.Entity;

import qlhtt.DAO.NhanVienDAO;
import qlhtt.Enum.VaiTro;

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
    private VaiTro vaiTro;
    private LocalDate ngaySinh;
    private Boolean gioiTinh;


    public NhanVien(String maNhanVien, String tenNhanVien, String soDienThoai, String email, String cccd, VaiTro vaiTro, LocalDate ngaySinh, Boolean gioiTinh) {
        super();
        setMaNhanVien(maNhanVien);
        setTenNhanVien(tenNhanVien);
        setSoDienThoai(soDienThoai);
        setEmail(email);
        setCccd(cccd);
        setVaiTro(vaiTro);
        setNgaySinh(ngaySinh);
        setGioiTinh(gioiTinh);
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

    public NhanVien(ResultSet rs) throws SQLException {
        setMaNhanVien(rs.getString("maNhanVien"));
        setTenNhanVien(rs.getString("tenNhanVien"));
        setSoDienThoai(rs.getString("soDienThoai"));
        setEmail(rs.getString("email"));
        setCccd(rs.getString("cccd"));
        if(Integer.parseInt(rs.getString("vaiTro")) == 0) {
            setVaiTro(VaiTro.NGUOIQUANLY);
        }
        else {
            setVaiTro(VaiTro.NHANVIEN);
        }
        setNgaySinh(rs.getDate("ngaySinh").toLocalDate());
        setGioiTinh(rs.getBoolean("gioiTinh"));
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

    public VaiTro getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(VaiTro vaiTro) {
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
