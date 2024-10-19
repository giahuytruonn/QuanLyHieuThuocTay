package qlhtt.Entity;

import qlhtt.DAO.PhieuNhapDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PhieuNhap {
    private String maPhieuNhap;
    private NhanVien nhanVien;
    private NhaCungCap nhaCungCap;
    //0: chua thanh toan, 1: da thanh toan
    private boolean trangThaiPhieuNhap;
    private LocalDate ngayTao;
    private double tongTien;

    public PhieuNhap() {
    }

    public PhieuNhap(String maPhieuNhap, NhanVien nhanVien, NhaCungCap nhaCungCap, boolean trangThaiPhieuNhap, LocalDate ngayTao, double tongTien) {
        super();
        setMaPhieuNhap(maPhieuNhap);
        setNhanVien(nhanVien);
        setNhaCungCap(nhaCungCap);
        setTrangThaiPhieuNhap(trangThaiPhieuNhap);
        setNgayTao(ngayTao);
        setTongTien(tongTien);
    }

    public PhieuNhap(String maPhieuNhap) {
        PhieuNhapDAO phieuNhapDAO = PhieuNhapDAO.getInstance();
        PhieuNhap phieuNhap = phieuNhapDAO.getPhieuNhapBangMaPhieuNhap(maPhieuNhap);
        setMaPhieuNhap(phieuNhap.getMaPhieuNhap());
        setNhanVien(phieuNhap.getNhanVien());
        setNhaCungCap(phieuNhap.getNhaCungCap());
        setTrangThaiPhieuNhap(phieuNhap.isTrangThaiPhieuNhap());
        setNgayTao(phieuNhap.getNgayTao());
        setTongTien(phieuNhap.getTongTien());
    }

    public PhieuNhap (ResultSet rs) throws SQLException {
        setMaPhieuNhap(rs.getString("maPhieuNhap"));
        setNhanVien(new NhanVien(rs.getString("maNhanVien")));
        setNhaCungCap(new NhaCungCap(rs.getString("maNhaCungCap")));
        setTrangThaiPhieuNhap(rs.getBoolean("trangThaiPhieuNhap"));
        setNgayTao(rs.getDate("ngayTao").toLocalDate());
        setTongTien(rs.getDouble("tongTien"));
    }

    public String getMaPhieuNhap() {
        return maPhieuNhap;
    }

    public void setMaPhieuNhap(String maPhieuNhap) {
        this.maPhieuNhap = maPhieuNhap;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public NhaCungCap getNhaCungCap() {
        return nhaCungCap;
    }

    public void setNhaCungCap(NhaCungCap nhaCungCap) {
        this.nhaCungCap = nhaCungCap;
    }

    public boolean isTrangThaiPhieuNhap() {
        return trangThaiPhieuNhap;
    }

    public void setTrangThaiPhieuNhap(boolean trangThaiPhieuNhap) {
        this.trangThaiPhieuNhap = trangThaiPhieuNhap;
    }

    public LocalDate getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDate ngayTao) {
        this.ngayTao = ngayTao;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    @Override
    public String toString() {
        return "PhieuNhap{" +
                "maPhieuNhap='" + maPhieuNhap + '\'' +
                ", nhanVien=" + nhanVien +
                ", nhaCungCap=" + nhaCungCap +
                ", trangThaiPhieuNhap=" + trangThaiPhieuNhap +
                ", ngayTao=" + ngayTao +
                ", tongTien=" + tongTien +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhieuNhap phieuNhap = (PhieuNhap) o;
        return maPhieuNhap.equals(phieuNhap.maPhieuNhap);
    }

    @Override
    public int hashCode() {
        return maPhieuNhap.hashCode();
    }

    public Double tongTienPhieuNhap(List<ChiTietPhieuNhap> dsChiTietPhieuNhap){
        Double tongTienPhieuNhap = 0.0;
        for (ChiTietPhieuNhap chiTietPhieuNhap : dsChiTietPhieuNhap){
            tongTienPhieuNhap += chiTietPhieuNhap.getThanhTien();
        }
        return tongTienPhieuNhap;
    }
}
