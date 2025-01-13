package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NhomSanPham {
    @Id
    private String maNhomSp;
    private String tenNhomSp;

    @OneToMany(mappedBy = "nhomSanPham")
    private Set<SanPham> dsSanPham;
}
