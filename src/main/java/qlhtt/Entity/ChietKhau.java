package qlhtt.Entity;

import jakarta.persistence.*;
import qlhtt.DAO.ChietKhauDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
public class ChietKhau {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String maChietKhau;
    private int soLuong;
    private double giaTriChietKhau;
    private LocalDate ngayBatDauApDung;
    private LocalDate ngayKetThucApDung;
    //0-> không hoạt động, 1-> hoạt động
    private Boolean trangThaiChietKhau;
    private String moTa;

    @OneToMany(mappedBy = "chietKhau")
    private Set<HoaDon> dsHoaDon;

    public ChietKhau() {
        setMaChietKhau(null);
    }

    public ChietKhau(String maChietKhau, int soLuong, Double giaTriChietKhau, LocalDate ngayBatDauApDung, LocalDate ngayKetThucApDung, Boolean trangThaiChietKhau, String moTa) {
        super();
        setMaChietKhau(maChietKhau);
        setSoLuong(soLuong);
        setGiaTriChietKhau(giaTriChietKhau);
        setNgayBatDauApDung(ngayBatDauApDung);
        setNgayKetThucApDung(ngayKetThucApDung);
        setTrangThaiChietKhau(trangThaiChietKhau);
        setMoTa(moTa);
    }

    public ChietKhau(String maChietKhau) {
        ChietKhauDAO chietKhauDAO = ChietKhauDAO.getInstance();
        ChietKhau chietKhau = chietKhauDAO.getChietKhauBangMaChietKhau(maChietKhau);
        if(chietKhau!= null){
            setMaChietKhau(chietKhau.getMaChietKhau());
            setSoLuong(chietKhau.getSoLuong());
        }else{
            new ChietKhau();
        }
    }

    public ChietKhau(ResultSet rs) throws SQLException{
        setMaChietKhau(rs.getString("maChietKhau"));
        setSoLuong(rs.getInt("soLuong"));
        setGiaTriChietKhau(rs.getDouble("giaTriChietKhau"));
        setNgayBatDauApDung(rs.getDate("ngayBatDauApDung").toLocalDate());
        setNgayKetThucApDung(rs.getDate("ngayKetThucApDung").toLocalDate());
        setTrangThaiChietKhau(rs.getBoolean("trangThaiChietKhau"));
        setMoTa(rs.getString("moTa"));
    }


    public ChietKhau(double giaTri, LocalDate ngayBatDau, LocalDate ngayKetThuc, int soLuong, String moTa) {
        setSoLuong(soLuong);
        setGiaTriChietKhau(giaTri);
        setNgayBatDauApDung(ngayBatDau);
        setNgayKetThucApDung(ngayKetThuc);
        setMoTa(moTa);
    }

    public String getMaChietKhau() {
        return maChietKhau== null ? "" : maChietKhau;
    }

    public void setMaChietKhau(String maChietKhau) {
        this.maChietKhau = maChietKhau;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public Double getGiaTriChietKhau() {
        return giaTriChietKhau;
    }

    public void setGiaTriChietKhau(Double giaTriChietKhau) {
//        if(giaTriChietKhau > 0 && giaTriChietKhau <= 1) {
        this.giaTriChietKhau = giaTriChietKhau;
//        } else {
//            throw new IllegalArgumentException("Giá trị chiết khấu phải lớn hơn 0 và bé hơn 1");
//        }
    }

    public LocalDate getNgayBatDauApDung() {
        return ngayBatDauApDung;
    }

    public void setNgayBatDauApDung(LocalDate ngayBatDauApDung) {
        this.ngayBatDauApDung = ngayBatDauApDung;
    }

    public LocalDate getNgayKetThucApDung() {
        return ngayKetThucApDung;
    }

    public void setNgayKetThucApDung(LocalDate ngayKetThucApDung) {
//        if(ngayKetThucApDung.isAfter(ngayBatDauApDung)) {
        this.ngayKetThucApDung = ngayKetThucApDung;
//        } else {
//            throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu");
//        }
    }

    public Boolean getTrangThaiChietKhau() {
        return trangThaiChietKhau;
    }

    public void setTrangThaiChietKhau(Boolean trangThaiChietKhau) {
        this.trangThaiChietKhau = trangThaiChietKhau;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
//        if(!moTa.isEmpty()) {
        this.moTa = moTa;
//        } else {
//            throw new IllegalArgumentException("Mô tả không được để trống");
//        }
    }

    @Override
    public String toString() {
        return "ChietKhau{" +
                "maChietKhau='" + maChietKhau + '\'' +
                ", soLuong=" + soLuong +
                ", giaTriChietKhau=" + giaTriChietKhau +
                ", ngayBatDauApDung=" + ngayBatDauApDung +
                ", ngayKetThucApDung=" + ngayKetThucApDung +
                ", trangThaiChietKhau=" + trangThaiChietKhau +
                ", moTa='" + moTa + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChietKhau chietKhau = (ChietKhau) o;
        return Objects.equals(maChietKhau, chietKhau.maChietKhau);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maChietKhau);
    }
}
