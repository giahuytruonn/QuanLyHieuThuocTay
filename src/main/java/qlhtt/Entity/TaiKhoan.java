package qlhtt.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import qlhtt.DAO.TaiKhoanDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;


@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class TaiKhoan {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    private String maTaiKhoan;
    private String tenDangNhap;
    private String matKhau;
    //1 -> Đang hoạt động
    //0 -> Ngưng hoạt động
    private boolean trangThaiTaiKhoan;
    @OneToOne
    @JoinColumn(name = "maNhanVien", unique = true)
    private NhanVien nhanVien;

    public TaiKhoan() {
    }

    public TaiKhoan(String maTaiKhoan, String tenDangNhap, String matKhau, Boolean trangThaiTaiKhoan, NhanVien nhanVien) {
        super();
        setMaTaiKhoan(maTaiKhoan);
        setTenDangNhap(tenDangNhap);
        setMatKhau(matKhau);
        setTrangThaiTaiKhoan(trangThaiTaiKhoan);
        setNhanVien(nhanVien);
    }

    public TaiKhoan(String maTaiKhoan) {
        TaiKhoanDAO taiKhoanDAO = TaiKhoanDAO.getInstance();
        TaiKhoan taiKhoan = taiKhoanDAO.getTaiKhoanBangMaTaiKhoan(maTaiKhoan);
        setMaTaiKhoan(taiKhoan.getMaTaiKhoan());
        setTenDangNhap(taiKhoan.getTenDangNhap());
        setMatKhau(taiKhoan.getMatKhau());
        setTrangThaiTaiKhoan(taiKhoan.getTrangThaiTaiKhoan());
        setNhanVien(taiKhoan.getNhanVien());
    }

    public TaiKhoan(ResultSet rs) throws SQLException{
        setMaTaiKhoan(rs.getString("maTaiKhoan"));
        setTenDangNhap(rs.getString("tenDangNhap"));
        setMatKhau(rs.getString("matKhau"));
        setTrangThaiTaiKhoan(rs.getBoolean("trangThaiTaiKhoan"));
        setNhanVien(new NhanVien(rs.getString("maNhanVien")));
    }

    public TaiKhoan(String matKhau, String tenDangNhap) {
        setMatKhau(matKhau);
        setTenDangNhap(tenDangNhap);
    }

    public String getMaTaiKhoan() {
        return maTaiKhoan;
    }

    public void setMaTaiKhoan(String maTaiKhoan) {
        if(maTaiKhoan.matches("TK[0-9]{3}")) {
            this.maTaiKhoan = maTaiKhoan;
        } else {
            throw new IllegalArgumentException("Tài khoản phải theo định dạng TKXXX");
        }
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public Boolean getTrangThaiTaiKhoan() {
        return trangThaiTaiKhoan;
    }

    public void setTrangThaiTaiKhoan(Boolean trangThaiTaiKhoan) {
        this.trangThaiTaiKhoan = trangThaiTaiKhoan;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        if(matKhau.isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu không được để trống");
        } else {
            this.matKhau = matKhau;
        }
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    @Override
    public String toString() {
        return "TaiKhoan{" +
                "maTaiKhoan='" + maTaiKhoan + '\'' +
                ", tenDangNhap='" + tenDangNhap + '\'' +
                ", matKhau='" + matKhau + '\'' +
                ", trangThaiTaiKhoan=" + trangThaiTaiKhoan +
                ", nhanVien=" + nhanVien +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaiKhoan taiKhoan = (TaiKhoan) o;
        return Objects.equals(maTaiKhoan, taiKhoan.maTaiKhoan);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maTaiKhoan);
    }
}
