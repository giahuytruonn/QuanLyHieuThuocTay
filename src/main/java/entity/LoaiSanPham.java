package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class LoaiSanPham {
    @Id
    @EqualsAndHashCode.Include
    private String maLoaiSp;
    private String tenLoaiSp;

    @OneToMany(mappedBy = "loaiSanPham")
    private Set<SanPham> dsSanPham;
}
