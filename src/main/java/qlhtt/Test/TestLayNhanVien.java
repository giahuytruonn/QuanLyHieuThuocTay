package qlhtt.Test;

import qlhtt.ConnectDB.ConnectDB;
import qlhtt.DAO.NhanVienDAO;
import qlhtt.Entity.NhanVien;

import java.util.List;

public class TestLayNhanVien {
    public static void main(String[] args) {
        ConnectDB connectDB = ConnectDB.getInstance();
        try {
            connectDB.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        List<NhanVien> danhSachNV = NhanVienDAO.getInstance().getDanhSachNhanVien();
//        for (NhanVien nhanVien : danhSachNV) {
//            System.out.println(nhanVien.getMaNhanVien());
////            System.out.println(nhanVien.getTenNhanVien());
////            System.out.println(nhanVien.getSoDienThoai());
////            System.out.println(nhanVien.getEmail());
////            System.out.println(nhanVien.getCccd());
////            System.out.println(nhanVien.getVaiTro());
////            System.out.println(nhanVien.getNgaySinh());
////            System.out.println(nhanVien.getGioiTinh());
//        }
        NhanVien nhanVien = NhanVienDAO.getInstance().getNhanVienBangMaNhanVien("NV001");
        System.out.println(nhanVien.getMaNhanVien());
        System.out.println(nhanVien.getTenNhanVien());

    }
}
