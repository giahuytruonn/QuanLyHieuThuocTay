package qlhtt.Entity;

import qlhtt.DAO.HoaDonDAO;
import qlhtt.Enum.PhuongThucThanhToan;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class HoaDon {
    private String maHoaDon;
    //0 -> Chưa thanh toán
    //1 -> Đã thanh toán
    private Boolean trangThaiHoaDon;
    private LocalDate ngayTao;
    private Double tongGiaTriHoaDon;
    private Double tienDaGiam;
    private Double tongTienThanhToan;
    private Double tongTienKhachTra;
    private PhuongThucThanhToan phuongThucThanhToan;
    private Double tongTienTraLai;
    private static final Double VAT = 0.08;
    private NhanVien nhanVien;
    private KhachHang khachHang;
    private ChietKhau chietKhau;

    public HoaDon() {
    }

    public HoaDon(String maHoaDon, Boolean trangThaiHoaDon, LocalDate ngayTao, Double tongGiaTriHoaDon, Double tienDaGiam, Double tongTienThanhToan, Double tongTienKhachTra, PhuongThucThanhToan phuongThucThanhToan, Double tongTienTraLai, NhanVien nhanVien, KhachHang khachHang, ChietKhau chietKhau) {
        super();
        setMaHoaDon(maHoaDon);
        setTrangThaiHoaDon(trangThaiHoaDon);
        setNgayTao(ngayTao);
        setTongGiaTriHoaDon(tongGiaTriHoaDon);
        setTienDaGiam(tienDaGiam);
        setTongTienThanhToan(tongTienThanhToan);
        setTongTienKhachTra(tongTienKhachTra);
        setPhuongThucThanhToan(phuongThucThanhToan);
        setTongTienTraLai(tongTienTraLai);
        setNhanVien(nhanVien);
        setKhachHang(khachHang);
        setChietKhau(chietKhau);
    }

    public HoaDon(String maHoaDon) {
        HoaDonDAO hoaDonDAO = HoaDonDAO.getInstance();
        HoaDon hoaDon = hoaDonDAO.getHoaDonBangMaHoaDon(maHoaDon);
        setMaHoaDon(hoaDon.getMaHoaDon());
        setTrangThaiHoaDon(hoaDon.getTrangThaiHoaDon());
        setNgayTao(hoaDon.getNgayTao());
        setTongGiaTriHoaDon(hoaDon.getTongGiaTriHoaDon());
        setTienDaGiam(hoaDon.getTienDaGiam());
        setTongTienThanhToan(hoaDon.getTongTienThanhToan());
        setTongTienKhachTra(hoaDon.getTongTienKhachTra());
        setPhuongThucThanhToan(hoaDon.getPhuongThucThanhToan());
        setTongTienTraLai(hoaDon.getTongTienTraLai());
        setNhanVien(hoaDon.getNhanVien());
        setKhachHang(hoaDon.getKhachHang());
        setChietKhau(hoaDon.getChietKhau());
    }

    public HoaDon(ResultSet rs) throws SQLException{
        setMaHoaDon(rs.getString("maHoaDon"));
        setTrangThaiHoaDon(rs.getBoolean("trangThaiHoaDon"));
        setNgayTao(rs.getDate("ngayTao").toLocalDate());
        setTongGiaTriHoaDon(rs.getDouble("tongGiaTriHoaDon"));
        setTienDaGiam(rs.getDouble("tienDaGiam"));
        setTongTienThanhToan(rs.getDouble("tongTienThanhToan"));
        setTongTienKhachTra(rs.getDouble("tongTienKhachTra"));
        if(Integer.parseInt(rs.getString("phuongThucThanhToan")) == 0) {
            setPhuongThucThanhToan(PhuongThucThanhToan.TIENMAT);
        } else {
            setPhuongThucThanhToan(PhuongThucThanhToan.QRCODE);
        }
        setTongTienTraLai(rs.getDouble("tongTienTraLai"));
        setNhanVien(new NhanVien(rs.getString("maNhanVien")));
        setKhachHang(new KhachHang(rs.getString("maKhachHang")));
        setChietKhau(rs.getString("maChietKhau") == null ? null : new ChietKhau(rs.getString("maChietKhau")));
    }

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public Boolean getTrangThaiHoaDon() {
        return trangThaiHoaDon;
    }

    public void setTrangThaiHoaDon(Boolean trangThaiHoaDon) {
        this.trangThaiHoaDon = trangThaiHoaDon;
    }

    public LocalDate getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDate ngayTao) {
        this.ngayTao = ngayTao;
    }

    public Double getTongGiaTriHoaDon() {
        return tongGiaTriHoaDon;
    }

    public void setTongGiaTriHoaDon(Double tongGiaTriHoaDon) {
        if(tongGiaTriHoaDon > 0){
            this.tongGiaTriHoaDon = tongGiaTriHoaDon;
        } else {
            throw new IllegalArgumentException("Tổng giá trị hóa đơn phải lớn hơn 0");
        }
    }

    public Double getTienDaGiam() {
        return tienDaGiam;
    }

    public void setTienDaGiam(Double tienDaGiam) {
        if(tienDaGiam >= 0){
            this.tienDaGiam = tienDaGiam;
        } else {
            throw new IllegalArgumentException("Tiền đã giảm phải lớn hơn hoặc bằng 0");
        }
    }

    public Double getTongTienThanhToan() {
        return tongTienThanhToan;
    }

    public void setTongTienThanhToan(Double tongTienThanhToan) {
        if(tongTienThanhToan > 0){
            this.tongTienThanhToan = tongTienThanhToan;
        } else {
            throw new IllegalArgumentException("Tổng tiền thanh toán phải lớn hơn 0");
        }
    }

    public Double getTongTienKhachTra() {
        return tongTienKhachTra;
    }

    public void setTongTienKhachTra(Double tongTienKhachTra) {
        if(tongTienKhachTra > 0){
            this.tongTienKhachTra = tongTienKhachTra;
        } else {
            throw new IllegalArgumentException("Tổng tiền khách trả phải lớn hơn 0");
        }
    }

    public PhuongThucThanhToan getPhuongThucThanhToan() {
        return phuongThucThanhToan;
    }

    public void setPhuongThucThanhToan(PhuongThucThanhToan phuongThucThanhToan) {
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    public Double getVAT() {
        return VAT;
    }

    public Double getTongTienTraLai() {
        return tongTienTraLai;
    }

    public void setTongTienTraLai(Double tongTienTraLai) {
        if(tongTienTraLai >= 0){
            this.tongTienTraLai = tongTienTraLai;
        } else {
            throw new IllegalArgumentException("Tổng tiền trả lại phải lớn hơn bằng 0");
        }
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public ChietKhau getChietKhau() {
        return chietKhau;
    }

    public void setChietKhau(ChietKhau chietKhau) {
        this.chietKhau = chietKhau;
    }

    @Override
    public String toString() {
        return "HoaDon{" +
                "maHoaDon='" + maHoaDon + '\'' +
                ", trangThaiHoaDon=" + trangThaiHoaDon +
                ", ngayTao=" + ngayTao +
                ", tongGiaTriHoaDon=" + tongGiaTriHoaDon +
                ", tienDaGiam=" + tienDaGiam +
                ", tongTienThanhToan=" + tongTienThanhToan +
                ", tongTienKhachTra=" + tongTienKhachTra +
                ", phuongThucThanhToan=" + phuongThucThanhToan +
                ", tongTienTraLai=" + tongTienTraLai +
                ", VAT=" + VAT +
                ", nhanVien=" + nhanVien +
                ", khachHang=" + khachHang +
                ", chietKhau=" + chietKhau +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HoaDon hoaDon = (HoaDon) o;
        return Objects.equals(maHoaDon, hoaDon.maHoaDon);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maHoaDon);
    }

    public Double tinhGiaTriHoaDon(List<ChiTietHoaDon> dsChiTietHoaDon){
        Double tongGiaTriHoadon = 0.0;
        for (ChiTietHoaDon chiTietHoaDon : dsChiTietHoaDon){
            tongGiaTriHoadon += chiTietHoaDon.getTongTien();
        }
        return tongGiaTriHoadon;
    }

    public Double tinhTienGiamGia(int diemTichLuy, Double giaTriChietKhau, Double tongGiaTriHoaDon){
        Double tienDaGiam = 0.0;
        tienDaGiam = tongGiaTriHoaDon - (tongGiaTriHoaDon * giaTriChietKhau) - diemTichLuy;
        return tienDaGiam;
    }

    public Double tinhTongTienThanhToan(Double tongGiaTriHoaDon, Double tienDaGiam){
        Double tongTienThanhToan = 0.0;
        tongTienThanhToan = tongGiaTriHoaDon*(1+VAT) - tienDaGiam;
        return tongTienThanhToan;
    }

    public Double tinhTienTraLai(Double tienKhachTra, Double tongTienThanhToan){
        Double tienTraLai = 0.0;
        tienTraLai = tienKhachTra - tongTienThanhToan;
        return tienTraLai;
    }
}
