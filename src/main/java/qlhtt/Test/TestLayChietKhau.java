package qlhtt.Test;

import qlhtt.DAO.ChietKhauDAO;

public class TestLayChietKhau {
    public static void main(String[] args) {
        ChietKhauDAO chietKhauDAO = ChietKhauDAO.getInstance();
        System.out.println(chietKhauDAO.getChietKhauBangMaChietKhau("2010202405001"));
    }
}
