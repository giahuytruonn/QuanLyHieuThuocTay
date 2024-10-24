package qlhtt.Entity;

import qlhtt.DAO.SanPhamDAO;
import qlhtt.Enum.ChiDinhSuDung;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class SanPham {
    private String maSanPham;
    private String tenSanPham;
    private String quyCachDongGoi;
    private String thanhPhan;
    private String dangBaoChe;
    private ChiDinhSuDung chiDinhSuDung;
    private String nhaSanXuat;
    private String quocGiaSanXuat;
    private String doiTuongSuDung;
    private String moTaSanPham;
    private int soLuong;
    private Double donGia;
    private String duongDung;
    private String hamLuong;
    private DonViTinh donViTinh;
    private NhomSanPham nhomSanPham;
    private LoaiSanPham loaiSanPham;

    public SanPham() {
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
        if(maSanPham.matches("[0-9]{13}")) {
            this.maSanPham = maSanPham;
        } else {
            throw new IllegalArgumentException("Mã sản phẩm phải có 13 chữ số");
        }
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        if(tenSanPham.matches("^[\\p{L}\\d\\s]+$")) {
            this.tenSanPham = tenSanPham;
        } else {
            throw new IllegalArgumentException("Tên sản phẩm không có kí tự đặc biệt và không được rỗng");
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
        if(nhaSanXuat.matches("^[\\p{L}\\d\\s]+$")) {
            this.nhaSanXuat = nhaSanXuat;
        } else {
            throw new IllegalArgumentException("Nhà sản xuất không có kí tự đặc biệt và không được rỗng");
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
        if(donGia > 0) {
            this.donGia = donGia;
        } else {
            throw new IllegalArgumentException("Đơn giá phải lớn hơn 0");
        }
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
