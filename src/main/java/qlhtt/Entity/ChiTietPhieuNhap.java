package qlhtt.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import qlhtt.DAO.ChiTietPhieuNhapDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Entity
public class ChiTietPhieuNhap {
    @Id
    @ManyToOne
    @JoinColumn(name = "maSanPham")
    private SanPham sanPham;

    @Id
    @ManyToOne
    @JoinColumn(name = "maPhieuNhap")
    private PhieuNhap phieuNhap;

    private int soLuong;
    private double giaNhap;
    private String donViTinh;
    private LocalDate ngaySanXuat;
    private LocalDate hanSuDung;
    private double thanhTien;

    public ChiTietPhieuNhap(SanPham sanPham, PhieuNhap phieuNhap, LocalDate hanSuDung) {
        setSanPham(sanPham);
        setPhieuNhap(phieuNhap);
        setHanSuDung(hanSuDung);
    }

    public ChiTietPhieuNhap(SanPham sanPham, PhieuNhap phieuNhap,  int soLuong, Double giaNhap, String donViTinh, LocalDate ngaySanXuat, LocalDate hanSuDung, Double thanhTien) {
        super();
        setSanPham(sanPham);
        setPhieuNhap(phieuNhap);
        setSoLuong(soLuong);
        setGiaNhap(giaNhap);
        setDonViTinh(donViTinh);
        setNgaySanXuat(ngaySanXuat);
        setHanSuDung(hanSuDung);
        setThanhTien(thanhTien);
    }

    public ChiTietPhieuNhap(SanPham sanPham, int soLuong, Double giaNhap, String donViTinh, LocalDate hanSuDung, LocalDate ngaySanXuat, double thanhTien) {
        setSanPham(sanPham);
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
        setSoLuong(chiTietPhieuNhap.getSoLuong());
        setGiaNhap(chiTietPhieuNhap.getGiaNhap());
        setDonViTinh(chiTietPhieuNhap.getDonViTinh());
        setNgaySanXuat(chiTietPhieuNhap.getNgaySanXuat());
        setHanSuDung(chiTietPhieuNhap.getHanSuDung());
        setThanhTien(chiTietPhieuNhap.getThanhTien());
    }

    public ChiTietPhieuNhap(){

    }

    public ChiTietPhieuNhap(ResultSet rs) throws SQLException {
        setSanPham(new SanPham(rs.getString("maSanPham")));
        setPhieuNhap(new PhieuNhap(rs.getString("maPhieuNhap")));
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
        this.hanSuDung = hanSuDung;
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
        return sanPham.equals(chiTietPhieuNhap.sanPham) ;
    }

    @Override
    public int hashCode() {
        return sanPham.hashCode();
    }

    public Double thanhTien(int soLuong, Double giaNhap){
        return soLuong * giaNhap;
    }
}