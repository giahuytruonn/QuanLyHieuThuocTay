package qlhtt.Test;

import qlhtt.DAO.NhaCungCapDAO;
import qlhtt.DAO.NhanVienDAO;
import qlhtt.DAO.PhieuNhapDAO;
import qlhtt.Entity.NhaCungCap;
import qlhtt.Entity.PhieuNhap;

public class TestLayPhieuNhap {
    public static void main(String[] args) {
        System.out.println(PhieuNhapDAO.getInstance().getDanhSachPhieuNhapTheoThang());

    }
}
