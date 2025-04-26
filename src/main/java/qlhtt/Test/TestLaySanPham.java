package qlhtt.Test;

import qlhtt.DAO.SanPhamDAO;
import qlhtt.Entity.SanPham;

import java.util.HashMap;

public class TestLaySanPham {
    public static void main(String[] args) {
        SanPhamDAO sanPhamDAO = SanPhamDAO.getInstance();
        SanPham sanPham = sanPhamDAO.getSanPhamBangMaSanPham("8936014580530");
        int phanDu = 651 % 5;
        System.out.println(phanDu);


    }
}
