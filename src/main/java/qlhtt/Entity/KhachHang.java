package qlhtt.Entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import qlhtt.DAO.KhachHangDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class KhachHang {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    private String maKhachHang;
    private String hoTen;
    //0: Nam, 1: Nữ
    private boolean gioiTinh;
    private String soDienThoai;
    private LocalDate ngaySinh;
    private int diemTichLuy;
    private String email;

    @OneToMany(mappedBy = "khachHang")
    private Set<HoaDon> dsHoaDon;

    public KhachHang() {
    }

    public KhachHang(String maKhachHang, String hoTen, boolean gioiTinh, String soDienThoai, LocalDate ngaySinh, int diemTichLuy, String email) {
        super();
        setMaKhachHang(maKhachHang);
        setHoTen(hoTen);
        setGioiTinh(gioiTinh);
        setSoDienThoai(soDienThoai);
        setNgaySinh(ngaySinh);
        setDiemTichLuy(diemTichLuy);
        setEmail(email);
    }

    public KhachHang( String hoTen, boolean gioiTinh, String soDienThoai, LocalDate ngaySinh, int diemTichLuy, String email) {
        super();
        setHoTen(hoTen);
        setGioiTinh(gioiTinh);
        setSoDienThoai(soDienThoai);
        setNgaySinh(ngaySinh);
        setDiemTichLuy(diemTichLuy);
        setEmail(email);
    }

    public KhachHang(String maKhachHang) {
        KhachHangDAO khachHangDAO = KhachHangDAO.getInstance();
        KhachHang khachHang = khachHangDAO.getKhachHangBangMaKhachHang(maKhachHang);

        if(khachHang != null){
            setMaKhachHang(khachHang.getMaKhachHang());
            setHoTen(khachHang.getHoTen());
            setGioiTinh(khachHang.isGioiTinh());
            setSoDienThoai(khachHang.getSoDienThoai());
            setNgaySinh(khachHang.getNgaySinh());
            setDiemTichLuy(khachHang.getDiemTichLuy());
            setEmail(khachHang.getEmail());
        }else{
            new KhachHang();
        }
    }

    public KhachHang(ResultSet rs) throws SQLException {
        setMaKhachHang(rs.getString("maKhachHang")==null?"":rs.getString("maKhachHang"));
        setHoTen(rs.getString("hoTen"));
        setGioiTinh(rs.getBoolean("gioiTinh"));
        setSoDienThoai(rs.getString("soDienThoai")==null?"":rs.getString("soDienThoai"));
        setNgaySinh(rs.getDate("ngaySinh").toLocalDate());
        setDiemTichLuy(rs.getInt("diemTichLuy"));
        setEmail(rs.getString("email"));
    }

    public String getMaKhachHang() {
        return maKhachHang == null ? null : maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
//        if(maKhachHang.matches("KH[0-9]{4}")) {
        this.maKhachHang = maKhachHang;
//        }else {
//            throw new IllegalArgumentException("Mã khách hàng phải bắt đầu bằng KH và theo sau là 4 chữ số");
//        }
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
//        if(hoTen.matches("^(?:\\p{Lu}\\p{Ll}*\\s*)+$")){
        this.hoTen = hoTen;
//        }
//        else {
//            throw new IllegalArgumentException("Tên Khách hàng phải viết hoa chữ cái đầu tiên của mỗi từ,có khoảng trắng, không có ký tặc biệt hoặc số");
//        }
    }

    public boolean isGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getSoDienThoai() {
        return soDienThoai == null ? "" : soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
//        if(soDienThoai.matches("^(03|05|07|09)[0-9]{8}$")) {
//            this.soDienThoai = soDienThoai;
//        }else {
//            throw new IllegalArgumentException("Số điện thoại phải bắt đầu bằng số 03, 05, 07, 09 và phải 10 chữ số");
//        }

        this.soDienThoai = soDienThoai;
    }

    public int getDiemTichLuy() {
        return diemTichLuy;
    }

    public void setDiemTichLuy(int diemTichLuy) {
//        if(diemTichLuy >= 0) {
        this.diemTichLuy = diemTichLuy;
//        } else {
//            throw new IllegalArgumentException("Điểm tích lũy phải lớn hơn hoặc bằng 0");
//        }
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
//            if(email.matches("^[a-zA-Z0-9._%+-]+@(gmail.com|gmail.vn)$")) {
        this.email = email;
//        }else {
//            throw new IllegalArgumentException("Email không hợp lệ, phải theo định dạng tên email@tên miền");
//        }
    }
    @Override
    public String toString() {
        return "KhachHang{" +
                "maKhachHang='" + maKhachHang + '\'' +
                ", hoTen='" + hoTen + '\'' +
                ", gioiTinh=" + gioiTinh +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", ngaySinh=" + ngaySinh +
                ", diemTichLuy=" + diemTichLuy +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KhachHang khachHang = (KhachHang) o;
        return Objects.equals(maKhachHang, khachHang.maKhachHang);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maKhachHang);
    }
}
