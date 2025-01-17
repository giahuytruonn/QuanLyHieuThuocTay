package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ChiTietPhieuNhap {
    @Id
    @ManyToOne
    @JoinColumn(name = "maSanPham")
    @EqualsAndHashCode.Include
    private SanPham sanPham;

    @Id
    @ManyToOne
    @JoinColumn(name = "maPhieuNhap")
    @EqualsAndHashCode.Include
    private PhieuNhap phieuNhap;

    private int soLuong;
    private double giaNhap;
    private String donViTinh;
    private LocalDate ngaySanXuat;
    private LocalDate hanSuDung;
    private double thanhTien;



}
