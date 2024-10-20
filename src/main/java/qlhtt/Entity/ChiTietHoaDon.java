package qlhtt.Entity;

import qlhtt.DAO.ChiTietHoaDonDAO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChiTietHoaDon {
    private SanPham sanPham;
    private HoaDon hoaDon;
    private Double tongTien;
    private int soLuong;

    public ChiTietHoaDon() {
    }

    public ChiTietHoaDon(SanPham sanPham, HoaDon hoaDon, Double tongTien, int soLuong) {
        super();
        setSanPham(sanPham);
        setHoaDon(hoaDon);
        setTongTien(tongTien);
        setSoLuong(soLuong);
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
