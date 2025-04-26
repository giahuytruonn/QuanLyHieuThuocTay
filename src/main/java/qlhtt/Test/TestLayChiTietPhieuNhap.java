package qlhtt.Test;

import qlhtt.Controllers.NhanVien.ChiTietPhieuNhapController;
import qlhtt.DAO.ChiTietPhieuNhapDAO;
import qlhtt.Entity.ChiTietPhieuNhap;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class TestLayChiTietPhieuNhap {
    public static void main(String[] args) {
        ChiTietPhieuNhapDAO chiTietPhieuNhapDAO = ChiTietPhieuNhapDAO.getInstance();
        ChiTietPhieuNhapController chiTietPhieuNhapController = new ChiTietPhieuNhapController();

        System.out.println(ChiTietPhieuNhapDAO.getInstance().getDanhSachChiTietPhieuNhap());
    }
}
