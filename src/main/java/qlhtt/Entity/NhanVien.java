package qlhtt.Entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import qlhtt.DAO.NhanVienDAO;
import qlhtt.Enum.VaiTro;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class NhanVien {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    private String maNhanVien;
    private String tenNhanVien;
    private String soDienThoai;
    private String email;
    private String cccd;
    @Enumerated
    private VaiTro vaiTro;
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


    public NhanVien(String maNhanVien, String tenNhanVien, String soDienThoai, String email, String cccd, VaiTro vaiTro, LocalDate ngaySinh, Boolean gioiTinh, String duongDanAnh) {
        super();
        setMaNhanVien(maNhanVien);
        setTenNhanVien(tenNhanVien);
        setSoDienThoai(soDienThoai);
        setEmail(email);
        setCccd(cccd);
        setVaiTro(vaiTro);
        setNgaySinh(ngaySinh);
        setGioiTinh(gioiTinh);
        setDuongDanAnh(duongDanAnh);
    }

    public NhanVien(String maNhanVien, String tenNhanVien) {
        super();
        setMaNhanVien(maNhanVien);
        setTenNhanVien(tenNhanVien);
    }

    public NhanVien(String maNhanVien) {
        NhanVienDAO nhanVienDAO = new NhanVienDAO();
        NhanVien nhanVien = nhanVienDAO.getNhanVienBangMaNhanVien(maNhanVien);
        setMaNhanVien(nhanVien.getMaNhanVien());
        setTenNhanVien(nhanVien.getTenNhanVien());
        setSoDienThoai(nhanVien.getSoDienThoai());
        setEmail(nhanVien.getEmail());
        setCccd(nhanVien.getCccd());
        setVaiTro(nhanVien.getVaiTro());
        setNgaySinh(nhanVien.getNgaySinh());
        setGioiTinh(nhanVien.getGioiTinh());
        setDuongDanAnh(nhanVien.getDuongDanAnh());
    }

    public NhanVien(ResultSet rs) throws SQLException {
        setMaNhanVien(rs.getString("maNhanVien"));
        setTenNhanVien(rs.getString("tenNhanVien"));
        setSoDienThoai(rs.getString("soDienThoai"));
        setEmail(rs.getString("email"));
        setCccd(rs.getString("cccd"));
        if(Integer.parseInt(rs.getString("vaiTro")) == 0) {
            setVaiTro(VaiTro.NGUOIQUANLY);
        }
        else {
            setVaiTro(VaiTro.NHANVIEN);
        }
        setNgaySinh(rs.getDate("ngaySinh").toLocalDate());
        setGioiTinh(rs.getBoolean("gioiTinh"));
        setDuongDanAnh(rs.getString("duongDanAnh"));
    }

    public NhanVien(Boolean gioiTinh, LocalDate ngaySinh, VaiTro vaiTro, String cccd, String email, String soDienThoai, String tenNhanVien, String duongDanAnh) {
        setGioiTinh(gioiTinh);
        setCccd(cccd);
        setEmail(email);
        setSoDienThoai(soDienThoai);
        setTenNhanVien(tenNhanVien);
        setDuongDanAnh(duongDanAnh);
        setSoDienThoai(soDienThoai);
        setNgaySinh(ngaySinh);
        setVaiTro(vaiTro);
    }

    public String getMaNhanVien() {
        return maNhanVien==null?"":maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        if(maNhanVien.matches("^NV[0-9]{3}$")){
            this.maNhanVien = maNhanVien;
        }else {
            throw new IllegalArgumentException("Mã nhân viên phải theo định dạng NVXXX");
        }
    }

    public Boolean getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(Boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        Period period = Period.between(ngaySinh, LocalDate.now());
        if(period.getYears() >= 18) {
            this.ngaySinh = ngaySinh;
        }else {
            throw new IllegalArgumentException("Nhân viên phải từ 18 tuổi trở lên");
        }
    }

    public VaiTro getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(VaiTro vaiTro) {
        this.vaiTro = vaiTro;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        if(cccd.matches("[0-9]{12}")) {
            this.cccd = cccd;
        }else {
            throw new IllegalArgumentException("CCCD phải có 12 chữ số");
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if(email.matches("^[a-zA-Z0-9._%+-]+@(gmail.com|gmail.vn)$")) {
            this.email = email;
        }else {
            throw new IllegalArgumentException("Email không hợp lệ, phải theo định dạng tên email@tên miền");
        }
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        if(soDienThoai.matches("^(03|05|07|09)[0-9]{8}$")) {
            this.soDienThoai = soDienThoai;
        }else {
            throw new IllegalArgumentException("Số điện thoại phải bắt đầu bằng số 03, 05, 07, 09 và phải 10 chữ số");
        }
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        //if(tenNhanVien.matches("^(?:\\p{Lu}\\p{Ll}*\\s*)+$")){
            this.tenNhanVien = tenNhanVien;
        //}
        //
    }

    public String getDuongDanAnh() {
        return duongDanAnh;
    }

    public void setDuongDanAnh(String duongDanAnh) {
        this.duongDanAnh = duongDanAnh;
    }

    @Override
    public String toString() {
        return "NhanVien{" +
                "maNhanVien='" + maNhanVien + '\'' +
                ", tenNhanVien='" + tenNhanVien + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", email='" + email + '\'' +
                ", cccd='" + cccd + '\'' +
                ", vaiTro=" + vaiTro +
                ", ngaySinh=" + ngaySinh +
                ", gioiTinh=" + gioiTinh +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NhanVien nhanVien = (NhanVien) o;
        return Objects.equals(maNhanVien, nhanVien.maNhanVien);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maNhanVien);
    }

    public boolean containsKey(String maNhanVien) {
        return false;
    }
}
