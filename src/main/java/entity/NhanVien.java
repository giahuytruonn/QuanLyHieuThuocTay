package entity;

import dao.NhanVienDao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.checkerframework.checker.signature.qual.Identifier;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class NhanVien {
    @Id
    private String maNhanVien;
    private String tenNhanVien;
    private String soDienThoai;
    private String email;
    private String cccd;
    @Enumerated
    private  VaiTro vaiTro;
    private LocalDate ngaySinh;
    //true: nam, false: nu
    private Boolean gioiTinh;
    private String duongDanAnh;
    @OneToOne(mappedBy = "nhanVien")
    private TaiKhoan taiKhoan;

    @OneToMany(mappedBy = "nhanVien")
    private Set<HoaDon> dsHoaDon;

    @OneToMany(mappedBy = "nhanVien")
    private Set<PhieuNhap> dsPhieuNhap;

    public NhanVien(String maNhanVien, String tenNhanVien, String sdt){
        this.maNhanVien = maNhanVien;
        this.tenNhanVien = tenNhanVien;
        this.soDienThoai = sdt;
    }

}
