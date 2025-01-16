package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ChietKhau {
    @Id
    @EqualsAndHashCode.Include
    private String maChietKhau;
    private int soLuong;
    private double giaTriChietKhau;
    private LocalDate ngayBatDauApDung;
    private LocalDate ngayKetThucApDung;
    //0-> không hoạt động, 1-> hoạt động
    private Boolean trangThaiChietKhau;
    private String moTa;

    @OneToMany(mappedBy = "chietKhau")
    private Set<HoaDon> dsHoaDon;


}
