package qlhtt.Test;

import qlhtt.DAO.KhachHangDAO;

public class TestLayKhachHang {
    public static void main(String[] args) {
        KhachHangDAO khachHangDAO = KhachHangDAO.getInstance();
        System.out.println(khachHangDAO.getKhachHangBangMaKhachHang("KH0001"));
    }
}
