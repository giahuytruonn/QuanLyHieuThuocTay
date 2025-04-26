package qlhtt.Entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import qlhtt.DAO.NhaCungCapDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class NhaCungCap {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    private String maNhaCungCap;
    private String tenNhaCungCap;
    private String soDienThoai;
    private String diaChi;
    private String email;

    @OneToMany(mappedBy = "nhaCungCap")
    private Set<PhieuNhap> dsPhieuNhap;

    public NhaCungCap() {
    }

    public NhaCungCap(String maNhaCungCap, String tenNhaCungCap, String soDienThoai, String diaChi, String email) {
        super();
        setMaNhaCungCap(maNhaCungCap);
        setTenNhaCungCap(tenNhaCungCap);
        setSoDienThoai(soDienThoai);
        setDiaChi(diaChi);
        setEmail(email);
    }

    public NhaCungCap(String maNhaCungCap) {
        NhaCungCapDAO nhaCungCapDAO = NhaCungCapDAO.getInstance();
        NhaCungCap nhaCungCap = nhaCungCapDAO.getNhaCungCapBangMaNhaCungCap(maNhaCungCap);
        setMaNhaCungCap(nhaCungCap.getMaNhaCungCap());
        setTenNhaCungCap(nhaCungCap.getTenNhaCungCap());
        setSoDienThoai(nhaCungCap.getSoDienThoai());
        setDiaChi(nhaCungCap.getDiaChi());
        setEmail(nhaCungCap.getEmail());
    }

    public NhaCungCap(ResultSet rs) throws SQLException {
        setMaNhaCungCap(rs.getString("maNhaCungCap"));
        setTenNhaCungCap(rs.getString("tenNhaCungCap"));
        setSoDienThoai(rs.getString("soDienThoai"));
        setDiaChi(rs.getString("diaChi"));
        setEmail(rs.getString("email"));
    }

    public NhaCungCap(String tenNhaCungCap, String diaChi, String email, String soDienThoai) {
        setDiaChi(diaChi);
        setEmail(email);
        setSoDienThoai(soDienThoai);
        setTenNhaCungCap(tenNhaCungCap);
    }

    public String getMaNhaCungCap() {
        return maNhaCungCap;
    }

    public void setMaNhaCungCap(String maNhaCungCap) {
        if(maNhaCungCap.matches("NCC[0-9]{3}")){
            this.maNhaCungCap = maNhaCungCap;
        } else {
            throw new IllegalArgumentException("Mã nhà cung cấp phải theo định dạng NCCXXX");
        }
    }

    public String getTenNhaCungCap() {
        return tenNhaCungCap;
    }

    public void setTenNhaCungCap(String tenNhaCungCap) {
//        if(tenNhaCungCap.matches("^[\\p{L}\\d\\s]+$")) {
//            this.tenNhaCungCap = tenNhaCungCap;
//        } else {
//            throw new IllegalArgumentException("Tên nhà cung cấp không có kí tự đặc biệt và không được rỗng");
//        }

        this.tenNhaCungCap = tenNhaCungCap;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
//        if(soDienThoai.matches("^(03|05|07|09)[0-9]{8}$")) {
//            this.soDienThoai = soDienThoai;
//        }else {
//            throw new IllegalArgumentException("Số điện thoại phải bắt đầu bằng số 03, 05, 07, 09 và phải 10 chữ số");
//        }
        this.soDienThoai = soDienThoai;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
//        if(diaChi.matches("^[\\p{L}\\d\\s]+$")) {
//            this.diaChi = diaChi;
//        } else {
//            throw new IllegalArgumentException("Địa chỉ không có kí tự đặc biệt và không được rỗng");
//        }
        this.diaChi = diaChi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
//        if(email.matches("^[a-zA-Z0-9._%+-]+@(gmail.com|gmail.vn)$")) {
//            this.email = email;
//        }else {
//            throw new IllegalArgumentException("Email không hợp lệ, phải theo định dạng tên email@tên miền");
//        }
        this.email = email;
    }

    @Override
    public String toString() {
        return "NhaCungCap{" +
                "maNhaCungCap='" + maNhaCungCap + '\'' +
                ", tenNhaCungCap='" + tenNhaCungCap + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", diaChi='" + diaChi + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NhaCungCap nhaCungCap = (NhaCungCap) o;
        return maNhaCungCap.equals(nhaCungCap.maNhaCungCap);
    }

    @Override
    public int hashCode() {
        return maNhaCungCap.hashCode();
    }
}
