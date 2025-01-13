package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaiKhoan {
    @Id
    private String maTaiKhoan;
    private String tenDanNhap;
    private String matKhau;
    //1 -> Đang hoạt động
    //0 -> Ngưng hoạt động
    private boolean trangThaiTaiKhoan;
    @OneToOne
    @JoinColumn(name = "maNhanVien", unique = true)
    private NhanVien nhanVien;
}
