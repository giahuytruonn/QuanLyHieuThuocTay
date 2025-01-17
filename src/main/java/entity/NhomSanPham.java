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
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class NhomSanPham {
    @Id
    @EqualsAndHashCode.Include
    private String maNhomSp;
    private String tenNhomSp;

    @OneToMany(mappedBy = "nhomSanPham")
    private Set<SanPham> dsSanPham;
}
