package qlhtt.Entity;

import qlhtt.DAO.ChiTietPhieuNhapDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

public class ChiTietPhieuNhap {
    private SanPham sanPham;
    private PhieuNhap phieuNhap;
    private String soLo;
    private int soLuong;
    private Double giaNhap;
    private String donViTinh;
    private LocalDate ngaySanXuat;
    private LocalDate hanSuDung;
    private Double thanhTien;

    public ChiTietPhieuNhap() {
    }

    public ChiTietPhieuNhap(SanPham sanPham, PhieuNhap phieuNhap, String soLo, int soLuong, Double giaNhap, String donViTinh, LocalDate ngaySanXuat, LocalDate hanSuDung, Double thanhTien) {
        super();
        setSanPham(sanPham);
        setPhieuNhap(phieuNhap);
        setSoLo(soLo);
        setSoLuong(soLuong);
        setGiaNhap(giaNhap);
        setDonViTinh(donViTinh);
        setNgaySanXuat(ngaySanXuat);
        setHanSuDung(hanSuDung);
        setThanhTien(thanhTien);
    }

    public ChiTietPhieuNhap(String maPhieuNhap) {
        ChiTietPhieuNhapDAO chiTietPhieuNhapDAO = ChiTietPhieuNhapDAO.getInstance();
        ChiTietPhieuNhap chiTietPhieuNhap = chiTietPhieuNhapDAO.getChiTietPhieuNhapBangMaPhieuNhap(maPhieuNhap);
        setSanPham(chiTietPhieuNhap.getSanPham());
        setPhieuNhap(chiTietPhieuNhap.getPhieuNhap());
        setSoLo(chiTietPhieuNhap.getSoLo());
        setSoLuong(chiTietPhieuNhap.getSoLuong());
        setGiaNhap(chiTietPhieuNhap.getGiaNhap());
        setDonViTinh(chiTietPhieuNhap.getDonViTinh());
        setNgaySanXuat(chiTietPhieuNhap.getNgaySanXuat());
        setHanSuDung(chiTietPhieuNhap.getHanSuDung());
        setThanhTien(chiTietPhieuNhap.getThanhTien());
    }

    public ChiTietPhieuNhap(ResultSet rs) throws SQLException {
        setSanPham(new SanPham(rs.getString("maSanPham")));
        setPhieuNhap(new PhieuNhap(rs.getString("maPhieuNhap")));
        setSoLo(rs.getString("soLo"));
        setSoLuong(rs.getInt("soLuong"));
        setGiaNhap(rs.getDouble("giaNhap"));
        setDonViTinh(rs.getString("donViTinh"));
        setNgaySanXuat(rs.getDate("ngaySanXuat").toLocalDate());
        setHanSuDung(rs.getDate("hanSuDung").toLocalDate());
        setThanhTien(rs.getDouble("thanhTien"));
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }

    public PhieuNhap getPhieuNhap() {
        return phieuNhap;
    }

    public void setPhieuNhap(PhieuNhap phieuNhap) {
        this.phieuNhap = phieuNhap;
    }

    public String getSoLo() {
        return soLo;
    }

    public void setSoLo(String soLo) {
        this.soLo = soLo;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        if(soLuong > 0){
            this.soLuong = soLuong;
        }
        else {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }
    }

    public Double getGiaNhap() {
        return giaNhap;
    }

    public void setGiaNhap(Double giaNhap) {
        if(giaNhap > 0){
            this.giaNhap = giaNhap;
        }
        else {
            throw new IllegalArgumentException("Giá nhập phải lớn hơn 0");
        }
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public LocalDate getNgaySanXuat() {
        return ngaySanXuat;
    }

    public void setNgaySanXuat(LocalDate ngaySanXuat) {
        this.ngaySanXuat = ngaySanXuat;
    }

    public LocalDate getHanSuDung() {
        return hanSuDung;
    }

    public void setHanSuDung(LocalDate hanSuDung) {
        if(hanSuDung.isAfter(ngaySanXuat)) {
            Period period = Period.between(ngaySanXuat, hanSuDung);

            if (period.getYears() >= 1) {
                this.hanSuDung = hanSuDung;
            } else {
                throw new IllegalArgumentException("Hạn sử dụng phải ít nhất 1 năm trở lên.");
            }
        } else {
            throw new IllegalArgumentException("Hạn sử dụng phải sau ngày sản xuất.");
        }
    }

    public Double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(Double thanhTien) {
        if(thanhTien > 0){
            this.thanhTien = thanhTien;
        }
        else {
            throw new IllegalArgumentException("Thành tiền phải lớn hơn 0");
        }
    }

    @Override
    public String toString() {
        return "ChiTietPhieuNhap{" +
                "sanPham=" + sanPham +
                ", phieuNhap=" + phieuNhap +
                ", soLo='" + soLo + '\'' +
                ", soLuong=" + soLuong +
                ", giaNhap=" + giaNhap +
                ", donViTinh='" + donViTinh + '\'' +
                ", ngaySanXuat=" + ngaySanXuat +
                ", hanSuDung=" + hanSuDung +
                ", thanhTien=" + thanhTien +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChiTietPhieuNhap chiTietPhieuNhap = (ChiTietPhieuNhap) o;
        return sanPham.equals(chiTietPhieuNhap.sanPham) &&
                phieuNhap.equals(chiTietPhieuNhap.phieuNhap);
    }

    @Override
    public int hashCode() {
        return sanPham.hashCode() + phieuNhap.hashCode();
    }

    public Double thanhTien(int soLuong, Double giaNhap){
        return soLuong * giaNhap;
    }
}
