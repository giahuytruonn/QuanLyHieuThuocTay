package qlhtt.Entity;

import qlhtt.DAO.KhachHangDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;

public class KhachHang {
    private String maKhachHang;
    private String hoTen;
    // true: nam, false: nu
    private boolean gioiTinh;
    private String soDienThoai;
    private LocalDate ngaySinh;
    private int diemTichLuy;
    private String email;

    public KhachHang() {
    }

    public KhachHang(String maKhachHang, String hoTen, boolean gioiTinh, String soDienThoai, LocalDate ngaySinh, int diemTichLuy, String email) {
        super();
        setMaKhachHang(maKhachHang);
        setHoTen(hoTen);
        setGioiTinh(gioiTinh);
        setSoDienThoai(soDienThoai);
        setNgaySinh(ngaySinh);
        setDiemTichLuy(diemTichLuy);
        //setEmail(email);
    }

    public KhachHang(String maKhachHang) {
        KhachHangDAO khachHangDAO = KhachHangDAO.getInstance();
        KhachHang khachHang = khachHangDAO.getKhachHangBangMaKhachHang(maKhachHang);
        setMaKhachHang(khachHang.getMaKhachHang());
        setHoTen(khachHang.getHoTen());
        setGioiTinh(khachHang.isGioiTinh());
        setSoDienThoai(khachHang.getSoDienThoai());
        setNgaySinh(khachHang.getNgaySinh());
        setDiemTichLuy(khachHang.getDiemTichLuy());
        //setEmail(khachHang.getEmail());

    }

    public KhachHang(ResultSet rs) throws SQLException {
        setMaKhachHang(rs.getString("maKhachHang"));
        setHoTen(rs.getString("hoTen"));
        setGioiTinh(rs.getBoolean("gioiTinh"));
        setSoDienThoai(rs.getString("soDienThoai"));
        setNgaySinh(rs.getDate("ngaySinh").toLocalDate());
        setDiemTichLuy(rs.getInt("diemTichLuy"));
        //setEmail(rs.getString("email"));
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public boolean isGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public int getDiemTichLuy() {
        return diemTichLuy;
    }

    public void setDiemTichLuy(int diemTichLuy) {
        this.diemTichLuy = diemTichLuy;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Override
    public String toString() {
        return "KhachHang{" +
                "maKhachHang='" + maKhachHang + '\'' +
                ", hoTen='" + hoTen + '\'' +
                ", gioiTinh=" + gioiTinh +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", ngaySinh=" + ngaySinh +
                ", diemTichLuy=" + diemTichLuy +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KhachHang khachHang = (KhachHang) o;
        return Objects.equals(maKhachHang, khachHang.maKhachHang);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maKhachHang);
    }
}
