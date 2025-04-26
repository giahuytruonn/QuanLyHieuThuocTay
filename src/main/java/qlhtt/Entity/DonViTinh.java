package qlhtt.Entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import qlhtt.DAO.DonViTinhDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Set;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DonViTinh {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    private String maDonViTinh;
    private String tenDonViTinh;

    @OneToMany(mappedBy = "donViTinh")
    private Set<SanPham> dsSanPham;

    public DonViTinh() {
    }

    public DonViTinh(String maDonViTinh, String tenDonViTinh) {
        super();
        setMaDonViTinh(maDonViTinh);
        setTenDonViTinh(tenDonViTinh);
    }

    public DonViTinh(String maDonViTinh) {
        DonViTinhDAO donViTinhDAO = DonViTinhDAO.getInstance();
        DonViTinh donViTinh = donViTinhDAO.getDonViTinhBangMaDonViTinh(maDonViTinh);
        setMaDonViTinh(donViTinh.getMaDonViTinh());
        setTenDonViTinh(donViTinh.getTenDonViTinh());
    }

    public DonViTinh(ResultSet rs) throws SQLException {
        setMaDonViTinh(rs.getString("maDonViTinh"));
        setTenDonViTinh(rs.getString("tenDonViTinh"));
    }

    public String getMaDonViTinh() {
        return maDonViTinh;
    }

    public void setMaDonViTinh(String maDonViTinh) {
//        if(maDonViTinh.matches("DVT[0-9]{3}")) {
        this.maDonViTinh = maDonViTinh;
//        } else {
//            throw new IllegalArgumentException("Đơn vị tính phải theo định dạng DVTXXX");
//        }
    }

    public String getTenDonViTinh() {
        return tenDonViTinh;
    }

    public void setTenDonViTinh(String tenDonViTinh) {
//        if(tenDonViTinh.matches("^[\\p{L}\\d\\s]+$")) {
        this.tenDonViTinh = tenDonViTinh;
//        } else {
//            throw new IllegalArgumentException("Tên đơn vị tính không có kí tự đặc biệt và không được rỗng");
//        }
    }

    @Override
    public String toString() {
        return "DonViTinh{" +
                "maDonViTinh='" + maDonViTinh + '\'' +
                ", tenDonViTinh='" + tenDonViTinh + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DonViTinh donViTinh = (DonViTinh) o;
        return Objects.equals(maDonViTinh, donViTinh.maDonViTinh);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maDonViTinh);
    }
}
