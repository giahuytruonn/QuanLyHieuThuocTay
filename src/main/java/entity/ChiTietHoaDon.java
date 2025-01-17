package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ChiTietHoaDon {
    @Id
    @ManyToOne
    @JoinColumn(name = "maSanPham")
    private SanPham sanPham;
    @Id
    @ManyToOne
    @JoinColumn(name = "maHoaDon")
    private HoaDon hoaDon;
    @EqualsAndHashCode.Exclude
    private double tongTien;
    @EqualsAndHashCode.Exclude
    private int soLuong;
}
