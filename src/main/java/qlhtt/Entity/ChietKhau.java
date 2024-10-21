package qlhtt.Entity;

import qlhtt.DAO.ChiTietHoaDonDAO;
import qlhtt.DAO.ChietKhauDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;

public class ChietKhau {
    private String maChietKhau;
    private int soLuong;
    private Double giaTriChietKhau;
    private LocalDate ngayBatDauApDung;
    private LocalDate ngayKetThucApDung;
    private Boolean trangThaiChietKhau;
    private String moTa;

    public ChietKhau() {
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
        setMaChietKhau(chietKhau.getMaChietKhau());
        setSoLuong(chietKhau.getSoLuong());
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

    public String getMaChietKhau() {
        return maChietKhau;
    }

    public void setMaChietKhau(String maChietKhau) {
        this.maChietKhau = maChietKhau;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        if(soLuong > 0) {
            this.soLuong = soLuong;
        } else {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }
    }

    public Double getGiaTriChietKhau() {
        return giaTriChietKhau;
    }

    public void setGiaTriChietKhau(Double giaTriChietKhau) {
        if(giaTriChietKhau > 0 && giaTriChietKhau <= 1) {
            this.giaTriChietKhau = giaTriChietKhau;
        } else {
            throw new IllegalArgumentException("Giá trị chiết khấu phải lớn hơn 0");
        }
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
        if(ngayKetThucApDung.isAfter(ngayBatDauApDung)) {
            this.ngayKetThucApDung = ngayKetThucApDung;
        } else {
            throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu");
        }
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
        if(moTa.matches("^[\\p{L}\\d\\s]+$")) {
            this.moTa = moTa;
        } else {
            throw new IllegalArgumentException("Mô tả không được để trống và không có kí tự đặc biệt");
        }
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
