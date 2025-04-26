package qlhtt.Entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import qlhtt.DAO.ChiTietHoaDonDAO;
import qlhtt.Enum.PhuongThucThanhToan;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Entity
public class ChiTietHoaDon {

    @EmbeddedId
    private ChiTietHoaDonId chiTietHoaDonId;

    public void setChiTietHoaDonId(ChiTietHoaDonId chiTietHoaDonId) {
        this.chiTietHoaDonId = chiTietHoaDonId;
    }

    public ChiTietHoaDonId getChiTietHoaDonId() {
        return chiTietHoaDonId;
    }

    @ManyToOne
    @JoinColumn(name = "maSanPham", insertable = false, updatable = false)
    private SanPham sanPham;

    @ManyToOne
    @JoinColumn(name = "maHoaDon",insertable = false,updatable = false)
    private HoaDon hoaDon;
    @EqualsAndHashCode.Exclude
    private double tongTien;
    @EqualsAndHashCode.Exclude
    private int soLuong;

    public ChiTietHoaDon() {
    }


    public ChiTietHoaDon(String maHoaDon) {
        ChiTietHoaDonDAO chiTietHoaDonDAO = ChiTietHoaDonDAO.getInstance();
        ChiTietHoaDon chiTietHoaDon = chiTietHoaDonDAO.getChiTietHoaDonBangMaHoaDon(maHoaDon);
        setSanPham(chiTietHoaDon.getSanPham());
        setHoaDon(chiTietHoaDon.getHoaDon());
        setTongTien(chiTietHoaDon.getTongTien());
        setSoLuong(chiTietHoaDon.getSoLuong());
    }

    public ChiTietHoaDon(ResultSet rs) throws SQLException{
        setSanPham(new SanPham(rs.getString("maSanPham")));
        setHoaDon(new HoaDon(rs.getString("maHoaDon")));
        setTongTien(rs.getDouble("tongTien"));
        setSoLuong(rs.getInt("soLuong"));
    }

    public ChiTietHoaDon(SanPham sanPham, HoaDon hoaDon, double tongTien, int soLuong) {
        setSanPham(sanPham);
        setHoaDon(hoaDon);
        setTongTien(tongTien);
        setSoLuong(soLuong);
    }

    public ChiTietHoaDon(LocalDate ngayTao, HoaDon hoaDon, SanPham sanPham, int soLuong, PhuongThucThanhToan pttt, double tongTien, boolean tthd) {
        setSanPham(new SanPham(String.valueOf(sanPham)));
    }



    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }

    public Double getTongTien() {
        return tongTien;
    }

    public void setTongTien(Double tongTien) {
        if(tongTien > 0){
            this.tongTien = tongTien;
        } else {
            throw new IllegalArgumentException("Tổng tiền phải lớn hơn 0");
        }
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        if(soLuong > 0){
            this.soLuong = soLuong;
        } else {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }
    }

    @Override
    public String toString() {
        return "ChiTietHoaDon{" +
                "sanPham=" + sanPham +
                ", hoaDon=" + hoaDon +
                ", tongTien=" + tongTien +
                ", soLuong=" + soLuong +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChiTietHoaDon chiTietHoaDon = (ChiTietHoaDon) o;
        return sanPham.equals(chiTietHoaDon.sanPham) && hoaDon.equals(chiTietHoaDon.hoaDon);
    }

    @Override
    public int hashCode() {
        return sanPham.hashCode() + hoaDon.hashCode();
    }

    public Double tongTienSanPham(int soLuong, Double donGia){
        return soLuong * donGia;
    }
}
