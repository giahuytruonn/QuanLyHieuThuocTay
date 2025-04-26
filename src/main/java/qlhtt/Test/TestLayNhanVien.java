package qlhtt.Test;

import qlhtt.ConnectDB.ConnectDB;
import qlhtt.DAO.NhanVienDAO;
import qlhtt.Entity.NhanVien;
import qlhtt.Enum.VaiTro;

import java.time.LocalDateTime;
import java.util.List;

public class TestLayNhanVien {
    public static void main(String[] args) {
        List<NhanVien> danhSachNV = NhanVienDAO.getInstance().getDanhSachNhanVienTheoTrangThai();
        for (NhanVien nhanVien : danhSachNV) {
            System.out.println(nhanVien.getMaNhanVien());
            System.out.println(nhanVien.getTenNhanVien());
//            System.out.println(nhanVien.getSoDienThoai());
//            System.out.println(nhanVien.getEmail());
//            System.out.println(nhanVien.getCccd());
//            System.out.println(nhanVien.getVaiTro());
//            System.out.println(nhanVien.getNgaySinh());
//            System.out.println(nhanVien.getGioiTinh());
        }

        System.out.println(System.currentTimeMillis());

        System.out.println(LocalDateTime.now());

    }
}
