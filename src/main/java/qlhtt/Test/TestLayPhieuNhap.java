package qlhtt.Test;

import qlhtt.DAO.PhieuNhapDAO;

public class TestLayPhieuNhap {
    public static void main(String[] args) {
        PhieuNhapDAO phieuNhapDAO = PhieuNhapDAO.getInstance();
        System.out.println(phieuNhapDAO.getPhieuNhapBangMaPhieuNhap("PN17102024001"));
    }
}
