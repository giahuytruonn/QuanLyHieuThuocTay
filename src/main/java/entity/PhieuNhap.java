package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.N;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhieuNhap {
    @Id
    private String maPhieuNhap;
    //1 -> đã thanh toán, 0 -> chưa thanh toán
    private boolean trangThaiPhieuNhap;
    private LocalDate ngayTao;
    private Double tongTien;
    private String soLo;

    @ManyToOne
    @JoinColumn(name = "maNhaCungCap")
    private NhaCungCap nhaCungCap;

    @ManyToOne
    @JoinColumn(name = "maNhanVien")
    private NhanVien nhanVien;
}
