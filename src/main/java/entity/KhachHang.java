package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.checkerframework.checker.units.qual.A;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class KhachHang {
    @Id
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
