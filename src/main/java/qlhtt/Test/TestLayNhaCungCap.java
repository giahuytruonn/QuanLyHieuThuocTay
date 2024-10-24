package qlhtt.Test;

import qlhtt.DAO.NhaCungCapDAO;

public class TestLayNhaCungCap {
    public static void main(String[] args) {
        NhaCungCapDAO nhaCungCapDAO = NhaCungCapDAO.getInstance();
        System.out.println(nhaCungCapDAO.getDanhSachNhaCungCap());

    }
}
