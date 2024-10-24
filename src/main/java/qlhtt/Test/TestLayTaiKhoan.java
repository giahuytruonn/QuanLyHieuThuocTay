package qlhtt.Test;

import qlhtt.DAO.TaiKhoanDAO;

public class TestLayTaiKhoan {
    public static void main(String[] args) {
        TaiKhoanDAO taiKhoanDAO = TaiKhoanDAO.getInstance();
        System.out.println(taiKhoanDAO.getTaiKhoanBangMaTaiKhoan("TK001"));
    }
}
