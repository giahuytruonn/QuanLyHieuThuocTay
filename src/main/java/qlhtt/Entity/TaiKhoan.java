package qlhtt.Entity;

import java.util.Objects;

public class TaiKhoan {
    private String maTaiKhoan;
    private String tenDangNhap;
    private String matKhau;
    // 1 -> Đang họat động
    // 0 -> Ngưng hoạt động
    private Boolean trangTaiTaiKhoan;
    //private NhanVien nhanVien;

    public TaiKhoan(String maTaiKhoan, String tenDangNhap, String matKhau, Boolean trangTaiTaiKhoan) {
        this.maTaiKhoan = maTaiKhoan;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.trangTaiTaiKhoan = trangTaiTaiKhoan;
    }

    public TaiKhoan(String tenDangNhap, String matKhau) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
    }

    public TaiKhoan() {

    }

    public String getMaTaiKhoan() {
        return maTaiKhoan;
    }

    public void setMaTaiKhoan(String maTaiKhoan) {
        this.maTaiKhoan = maTaiKhoan;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public Boolean getTrangTaiTaiKhoan() {
        return trangTaiTaiKhoan;
    }

    public void setTrangTaiTaiKhoan(Boolean trangTaiTaiKhoan) {
        this.trangTaiTaiKhoan = trangTaiTaiKhoan;
    }

    @Override
    public String toString() {
        return "TaiKhoan{" +
                "maTaiKhoan='" + maTaiKhoan + '\'' +
                ", tenDangNhap='" + tenDangNhap + '\'' +
                ", matKhau='" + matKhau + '\'' +
                ", trangTaiTaiKhoan=" + trangTaiTaiKhoan +
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
