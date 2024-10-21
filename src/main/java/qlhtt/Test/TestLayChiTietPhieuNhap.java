package qlhtt.Test;

import qlhtt.DAO.ChiTietPhieuNhapDAO;

public class TestLayChiTietPhieuNhap {
    public static void main(String[] args) {
        ChiTietPhieuNhapDAO chiTietPhieuNhapDAO = ChiTietPhieuNhapDAO.getInstance();
        System.out.println(chiTietPhieuNhapDAO.getChiTietPhieuNhapBangMaPhieuNhap("PN17102024001"));
    }
}
