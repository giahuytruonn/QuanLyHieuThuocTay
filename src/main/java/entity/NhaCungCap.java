package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.*;
import org.checkerframework.checker.units.qual.N;

import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class NhaCungCap {
    @Id
    @EqualsAndHashCode.Include
    private String maNhaCungCap;
    private String tenNhaCungCap;
    private String soDienThoai;
    private String diaChi;
    private String email;

    @OneToMany(mappedBy = "nhaCungCap")
    private Set<PhieuNhap> dsPhieuNhap;
}
