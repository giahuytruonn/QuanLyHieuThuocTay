package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HoaDon {
    @Id
    private String maHoaDon;
    //true -> hoàn thành, false -> chưa hoàn thành
    private Boolean trangThaiHoaDon;
    private LocalDate ngayTao;
    private Double tongGiaTriHoaDon;
    private Double tienDaGiam;
    private Double tongTienThanhToan;
    private Double tongTienKhachTra;
    @Enumerated
    private PhuongThucThanhToan phuongThucThanhToan;
    private Double tongTienTraLai;
    private static final double VAT = 0.08;

    @ManyToOne
    @JoinColumn(name = "maKhachHang")
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "maNhanVien")
    private NhanVien nhanVien;

    @ManyToOne
    @JoinColumn(name = "maChietKhau")
    private ChietKhau chietKhau;

    @OneToMany(mappedBy = "hoaDon")
    private Set<ChiTietHoaDon> dsChiTietHoaDon;

}
