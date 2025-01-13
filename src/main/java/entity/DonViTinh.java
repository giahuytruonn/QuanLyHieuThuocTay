package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonViTinh {
    @Id
    private String maDonViTinh;
    private String tenDonViTinh;

    @OneToMany(mappedBy = "donViTinh")
    private Set<SanPham> dsSanPham;
}
