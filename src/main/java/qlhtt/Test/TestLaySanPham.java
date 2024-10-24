package qlhtt.Test;

import qlhtt.DAO.SanPhamDAO;

public class TestLaySanPham {
    public static void main(String[] args) {
        SanPhamDAO sanPhamDAO = SanPhamDAO.getInstance();
        System.out.println(sanPhamDAO.getSanPhamBangMaSanPham("8938543840321"));
    }
}
