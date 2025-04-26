package qlhtt.Test;

import qlhtt.Controllers.NhanVien.ChiTietHoaDonController;
import qlhtt.DAO.ChiTietHoaDonDAO;
import qlhtt.Entity.ChiTietHoaDon;

import java.util.List;

public class TestLayChiTietHoaDon {
    public static void main(String[] args) {
        ChiTietHoaDonDAO chiTietHoaDonDAO = ChiTietHoaDonDAO.getInstance();
        ChiTietHoaDonController chiTietHoaDonController = new ChiTietHoaDonController();

        List<ChiTietHoaDon> dsChiTietHoaDon = chiTietHoaDonController.getDsChiTietHoaDonTheoMaHoaDon("HD20102024001");
        System.out.println(dsChiTietHoaDon);
    }
}
