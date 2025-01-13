package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SanPham {
    @Id
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

}
