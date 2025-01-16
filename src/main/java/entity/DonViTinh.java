package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DonViTinh {
    @Id
    @EqualsAndHashCode.Include
    private String maDonViTinh;
    private String tenDonViTinh;

    @OneToMany(mappedBy = "donViTinh")
    private Set<SanPham> dsSanPham;
}
