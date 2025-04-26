package qlhtt.Entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import qlhtt.DAO.NhomSanPhamDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class NhomSanPham {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    private String maNhomSanPham;
    private String tenNhomSanPham;

    @OneToMany(mappedBy = "nhomSanPham")
    private Set<SanPham> dsSanPham;

    public NhomSanPham() {
    }

    public NhomSanPham(String maNhomSP, String tenNhomSP) {
        super();
        setMaNhomSP(maNhomSP);
        setTenNhomSP(tenNhomSP);
    }

    public NhomSanPham(String maNhomSP) {
        NhomSanPhamDAO nhomSanPhamDAO = NhomSanPhamDAO.getInstance();
        NhomSanPham nhomSanPham = nhomSanPhamDAO.getNhomSanPhamBangMaNhomSanPham(maNhomSP);
        setMaNhomSP(nhomSanPham.getMaNhomSP());
        setTenNhomSP(nhomSanPham.getTenNhomSP());
    }

    public NhomSanPham(ResultSet rs) throws SQLException {
        setMaNhomSP(rs.getString("maNhomSanPham"));
        setTenNhomSP(rs.getString("tenNhomSanPham"));
    }

    public String getMaNhomSP() {
        return maNhomSanPham;
    }

    public void setMaNhomSP(String maNhomSP) {
        this.maNhomSanPham = maNhomSP;
    }

    public String getTenNhomSP() {
        return tenNhomSanPham;
    }

    public void setTenNhomSP(String tenNhomSP) {
        this.tenNhomSanPham = tenNhomSP;
    }

    @Override
    public String toString() {
        return "NhomSanPham{" +
                "maNhomSP='" + maNhomSanPham + '\'' +
                ", tenNhomSP='" + tenNhomSanPham + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NhomSanPham nhomSanPham = (NhomSanPham) o;
        return maNhomSanPham.equals(nhomSanPham.maNhomSanPham);
    }

    @Override
    public int hashCode() {
        return maNhomSanPham.hashCode();
    }
}
