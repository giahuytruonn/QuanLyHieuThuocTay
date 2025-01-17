package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.checkerframework.checker.units.qual.A;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class KhachHang {
    @Id
    @EqualsAndHashCode.Include
    private String maKhachHang;
    private String hoTen;
    //0: Nam, 1: Nữ
    private boolean gioiTinh;
    private String soDienThoai;
    private LocalDate ngaySinh;
    private int diemTichLuy;
    private String email;

    @OneToMany(mappedBy = "khachHang")
    private Set<HoaDon> dsHoaDon;
}
