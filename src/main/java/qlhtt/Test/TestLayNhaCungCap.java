package qlhtt.Test;

import qlhtt.DAO.NhaCungCapDAO;
import qlhtt.Entity.NhaCungCap;

public class TestLayNhaCungCap {
    public static void main(String[] args) {
        NhaCungCapDAO nhaCungCapDAO = NhaCungCapDAO.getInstance();
        System.out.println(nhaCungCapDAO.getDanhSachNhaCungCap());

        for (NhaCungCap nhaCungCap : nhaCungCapDAO.getDanhSachNhaCungCap()) {
            System.out.println(nhaCungCap);
        }
    }
}
