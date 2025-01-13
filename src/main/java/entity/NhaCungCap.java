package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.checkerframework.checker.units.qual.N;

import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class NhaCungCap {
    @Id
    private String maNhaCungCap;
    private String tenNhaCungCap;
    private String soDienThoai;
    private String diaChi;
    private String email;

    @OneToMany(mappedBy = "nhaCungCap")
    private Set<PhieuNhap> dsPhieuNhap;
}
