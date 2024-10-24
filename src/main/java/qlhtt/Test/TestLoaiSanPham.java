package qlhtt.Test;

import qlhtt.DAO.LoaiSanPhamDAO;

public class TestLoaiSanPham {
    public static void main(String[] args) {
        LoaiSanPhamDAO loaiSanPhamDAO = LoaiSanPhamDAO.getInstance();
        System.out.println(loaiSanPhamDAO.getDanhSachLoaiSanPham());
        System.out.println(loaiSanPhamDAO.getLoaiSanPhamBangMaLoaiSanPham("LSP001"));
    }
}
