package qlhtt.Test;

import qlhtt.DAO.NhomSanPhamDAO;

public class TestNhomSanPham {
    public static void main(String[] args) {
        NhomSanPhamDAO nhomSanPhamDAO = NhomSanPhamDAO.getInstance();
        System.out.println(nhomSanPhamDAO.getDanhSachNhomSanPham());
        System.out.println(nhomSanPhamDAO.getNhomSanPhamBangMaNhomSanPham("NSP001"));
    }
}
