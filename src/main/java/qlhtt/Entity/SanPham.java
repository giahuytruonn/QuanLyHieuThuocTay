package qlhtt.Entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import qlhtt.DAO.SanPhamDAO;
import qlhtt.Enum.ChiDinhSuDung;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SanPham {
    @Id
    @EqualsAndHashCode.Include
    //@GeneratedValue(strategy = GenerationType.UUID)
    private String maSanPham;
    private String tenSanPham;
    private String quyCachDongGoi;
    private String thanhPhan;
    private String dangBaoChe;
    @Enumerated
    private ChiDinhSuDung chiDinhSuDung;
    private String nhaSanXuat;
    private String quocGiaSanXuat;
    private String doiTuongSuDung;
    private String moTaSanPham;
    private int soLuong;
    private double donGia;
    private String duongDung;
    private String hamLuong;
    @ManyToOne
    @JoinColumn(name = "maLoaiSanPham")
    private LoaiSanPham loaiSanPham;

    @ManyToOne
    @JoinColumn(name = "maNhomSanPham")
    private NhomSanPham nhomSanPham;

    @ManyToOne
    @JoinColumn(name = "maDonViTinh")
    private DonViTinh donViTinh;

    @OneToMany(mappedBy = "sanPham")
    private List<ChiTietHoaDon> dsChiTietHoaDon;

    @OneToMany(mappedBy = "sanPham")
    private Set<ChiTietPhieuNhap> dsChiTietPhieuNhap;

    //    public SanPham(String maSanPham) {
//        setMaSanPham(maSanPham);
//    }
    public SanPham(String maSanPham,String tenSanPham, int soLuong, String nhomSanPham, String tenNhomSanPham) {
        setMaSanPham(maSanPham);
        setSoLuong(soLuong);
        setTenSanPham(tenSanPham);
        setNhomSanPham(new NhomSanPham(nhomSanPham,tenNhomSanPham));
    }

    public SanPham() {

    }

    public SanPham(String maSanPham,String tenSanPham , String nhomSanPham, String tenNhomSanPham) {
        setMaSanPham(maSanPham);
        setTenSanPham(tenSanPham);
        setNhomSanPham(new NhomSanPham(nhomSanPham,tenNhomSanPham));
    }



    public SanPham(String maSanPham, String tenSanPham, String quyCachDongGoi, String thanhPhan, String dangBaoChe, ChiDinhSuDung chiDinhSuDung, String nhaSanXuat, String quocGiaSanXuat, String doiTuongSuDung ,String moTaSanPham, int soLuong, Double donGia, String duongDung, String hamLuong, DonViTinh donViTinh, NhomSanPham nhomSanPham, LoaiSanPham loaiSanPham) {
        super();
        setMaSanPham(maSanPham);
        setTenSanPham(tenSanPham);
        setQuyCachDongGoi(quyCachDongGoi);
        setThanhPhan(thanhPhan);
        setDangBaoChe(dangBaoChe);
        setChiDinhSuDung(chiDinhSuDung);
        setNhaSanXuat(nhaSanXuat);
        setQuocGiaSanXuat(quocGiaSanXuat);
        setDoiTuongSuDung(doiTuongSuDung);
        setMoTaSanPham(moTaSanPham);
        setSoLuong(soLuong);
        setDonGia(donGia);
        setDuongDung(duongDung);
        setHamLuong(hamLuong);
        setDonViTinh(donViTinh);
        setNhomSanPham(nhomSanPham);
        setLoaiSanPham(loaiSanPham);
    }




    public SanPham(String maSanPham) {
        SanPhamDAO sanPhamDAO = SanPhamDAO.getInstance();
        SanPham sanPham = sanPhamDAO.getSanPhamBangMaSanPham(maSanPham);
        setMaSanPham(sanPham.getMaSanPham());
        setTenSanPham(sanPham.getTenSanPham());
        setQuyCachDongGoi(sanPham.getQuyCachDongGoi());
        setThanhPhan(sanPham.getThanhPhan());
        setDangBaoChe(sanPham.getDangBaoChe());
        setChiDinhSuDung(sanPham.getChiDinhSuDung());
        setNhaSanXuat(sanPham.getNhaSanXuat());
        setQuocGiaSanXuat(sanPham.getQuocGiaSanXuat());
        setDoiTuongSuDung(sanPham.getDoiTuongSuDung());
        setMoTaSanPham(sanPham.getMoTaSanPham());
        setSoLuong(sanPham.getSoLuong());
        setDonGia(sanPham.getDonGia());
        setDuongDung(sanPham.getDuongDung());
        setHamLuong(sanPham.getHamLuong());
        setDonViTinh(sanPham.getDonViTinh());
        setNhomSanPham(sanPham.getNhomSanPham());
        setLoaiSanPham(sanPham.getLoaiSanPham());
    }

    public SanPham(ResultSet rs) throws SQLException{
        setMaSanPham(rs.getString("maSanPham"));
        setTenSanPham(rs.getString("tenSanPham"));
        setQuyCachDongGoi(rs.getString("quyCachDongGoi"));
        setThanhPhan(rs.getString("thanhPhan"));
        setDangBaoChe(rs.getString("dangBaoChe"));
        if(Integer.parseInt(rs.getString("chiDinhSuDung")) == 0) {
            setChiDinhSuDung(ChiDinhSuDung.KE_TOA);
        }
        else {
            setChiDinhSuDung(ChiDinhSuDung.KHONG_KE_TOA);
        }
        setNhaSanXuat(rs.getString("nhaSanXuat"));
        setQuocGiaSanXuat(rs.getString("quocGiaSanXuat"));
        setDoiTuongSuDung(rs.getString("doiTuongSuDung"));
        setMoTaSanPham(rs.getString("moTaSanPham"));
        setSoLuong(rs.getInt("soLuong"));
        setDonGia(rs.getDouble("donGia"));
        setDuongDung(rs.getString("duongDung"));
        setHamLuong(rs.getString("hamLuong"));
        setDonViTinh(new DonViTinh(rs.getString("maDonViTinh")));
        setNhomSanPham(new NhomSanPham(rs.getString("maNhomSanPham")));
        setLoaiSanPham(new LoaiSanPham(rs.getString("maLoaiSanPham")));
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
//        if(maSanPham.matches("[0-9/w]{13}")) {
//            this.maSanPham = maSanPham;
//        } else {
//            throw new IllegalArgumentException("Mã sản phẩm phải có 13 chữ số");
//        }
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        if(tenSanPham.isEmpty()){
            throw new IllegalArgumentException("Tên sản phẩm không được rỗng");
        }else {
            this.tenSanPham = tenSanPham;
        }
    }

    public String getQuyCachDongGoi() {
        return quyCachDongGoi;
    }

    public void setQuyCachDongGoi(String quyCachDongGoi) {
        this.quyCachDongGoi = quyCachDongGoi;
    }

    public String getThanhPhan() {
        return thanhPhan;
    }

    public void setThanhPhan(String thanhPhan) {
        this.thanhPhan = thanhPhan;
    }

    public String getDangBaoChe() {
        return dangBaoChe;
    }

    public void setDangBaoChe(String dangBaoChe) {
        this.dangBaoChe = dangBaoChe;
    }

    public ChiDinhSuDung getChiDinhSuDung() {
        return chiDinhSuDung;
    }

    public void setChiDinhSuDung(ChiDinhSuDung chiDinhSuDung) {
        this.chiDinhSuDung = chiDinhSuDung;
    }

    public String getNhaSanXuat() {
        return nhaSanXuat;
    }

    public void setNhaSanXuat(String nhaSanXuat) {
        if(nhaSanXuat.isEmpty()){
            throw new IllegalArgumentException("Nhà sản xuất không được rỗng");
        }else {
            this.nhaSanXuat = nhaSanXuat;
        }
    }

    public String getQuocGiaSanXuat() {
        return quocGiaSanXuat;
    }

    public void setQuocGiaSanXuat(String quocGiaSanXuat) {
        this.quocGiaSanXuat = quocGiaSanXuat;
    }

    public String getMoTaSanPham() {
        return moTaSanPham;
    }

    public void setMoTaSanPham(String moTaSanPham) {
        this.moTaSanPham = moTaSanPham;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        if(soLuong >= 0) {
            this.soLuong = soLuong;
        } else {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }
    }

    public Double getDonGia() {
        return donGia;
    }

    public void setDonGia(Double donGia) {
        this.donGia = donGia;
    }

    public String getDuongDung() {
        return duongDung;
    }

    public void setDuongDung(String duongDung) {
        this.duongDung = duongDung;
    }

    public String getHamLuong() {
        return hamLuong;
    }

    public void setHamLuong(String hamLuong) {
        this.hamLuong = hamLuong;
    }

    public DonViTinh getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(DonViTinh donViTinh) {
        this.donViTinh = donViTinh;
    }

    public NhomSanPham getNhomSanPham() {
        return nhomSanPham;
    }

    public void setNhomSanPham(NhomSanPham nhomSanPham) {
        this.nhomSanPham = nhomSanPham;
    }

    public LoaiSanPham getLoaiSanPham() {
        return loaiSanPham;
    }

    public void setLoaiSanPham(LoaiSanPham loaiSanPham) {
        this.loaiSanPham = loaiSanPham;
    }

    public String getDoiTuongSuDung() {
        return doiTuongSuDung;
    }

    public void setDoiTuongSuDung(String doiTuongSuDung) {
        this.doiTuongSuDung = doiTuongSuDung;
    }

    @Override
    public String toString() {
        return "SanPham{" +
                "maSanPham='" + maSanPham + '\'' +
                ", tenSanPham='" + tenSanPham + '\'' +
                ", quyCachDongGoi='" + quyCachDongGoi + '\'' +
                ", thanhPhan='" + thanhPhan + '\'' +
                ", dangBaoChe='" + dangBaoChe + '\'' +
                ", chiDinhSuDung=" + chiDinhSuDung +
                ", nhaSanXuat='" + nhaSanXuat + '\'' +
                ", quocGiaSanXuat='" + quocGiaSanXuat + '\'' +
                ", moTaSanPham='" + moTaSanPham + '\'' +
                ", soLuong=" + soLuong +
                ", donGia=" + donGia +
                ", duongDung='" + duongDung + '\'' +
                ", hamLuong='" + hamLuong + '\'' +
                ", donViTinh=" + donViTinh +
                ", nhomSanPham=" + nhomSanPham +
                ", loaiSanPham=" + loaiSanPham +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SanPham sanPham = (SanPham) o;
        return maSanPham.equals(sanPham.maSanPham);
    }

    @Override
    public int hashCode() {
        return maSanPham.hashCode();
    }
}
