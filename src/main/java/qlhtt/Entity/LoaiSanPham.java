package qlhtt.Entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import qlhtt.DAO.LoaiSanPhamDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LoaiSanPham {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    private String maLoaiSanPham;
    private String tenLoaiSanPham;

    @OneToMany(mappedBy = "loaiSanPham")
    private Set<SanPham> dsSanPham;

    public LoaiSanPham() {
    }

    public LoaiSanPham(String maLoaiSP, String tenLoaiSP) {
        super();
        setMaLoaiSP(maLoaiSP);
        setTenLoaiSP(tenLoaiSP);
    }

    public LoaiSanPham(String maLoaiSP) {
        LoaiSanPhamDAO loaiSanPhamDAO = LoaiSanPhamDAO.getInstance();
        LoaiSanPham loaiSanPham = loaiSanPhamDAO.getLoaiSanPhamBangMaLoaiSanPham(maLoaiSP);
        setMaLoaiSP(loaiSanPham.getMaLoaiSP());
        setTenLoaiSP(loaiSanPham.getTenLoaiSP());
    }

    public LoaiSanPham(ResultSet rs) throws SQLException {
        setMaLoaiSP(rs.getString("maLoaiSanPham"));
        setTenLoaiSP(rs.getString("tenLoaiSanPham"));
    }

    public String getMaLoaiSP() {
        return maLoaiSanPham;
    }

    public void setMaLoaiSP(String maLoaiSP) {
        if(maLoaiSP.matches("LSP[0-9]{3}")) {
            this.maLoaiSanPham = maLoaiSP;
        } else {
            throw new IllegalArgumentException("Loại sản phẩm phải theo định dạng LSPXXX");
        }
    }

    public String getTenLoaiSP() {
        return tenLoaiSanPham;
    }

    public void setTenLoaiSP(String tenLoaiSP) {
//        if(tenLoaiSP.matches("^[\\p{L}\\d\\s]+$")) {
        this.tenLoaiSanPham = tenLoaiSP;
//        } else {
//            throw new IllegalArgumentException("Tên loại sản phẩm không có kí tự đặc biệt và không được rỗng");
//        }
    }

    @Override
    public String toString() {
        return "LoaiSanPham{" +
                "maLoaiSP='" + maLoaiSanPham + '\'' +
                ", tenLoaiSP='" + tenLoaiSanPham + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoaiSanPham loaiSanPham = (LoaiSanPham) o;
        return maLoaiSanPham.equals(loaiSanPham.maLoaiSanPham);
    }

    @Override
    public int hashCode() {
        return maLoaiSanPham.hashCode();
    }
}
