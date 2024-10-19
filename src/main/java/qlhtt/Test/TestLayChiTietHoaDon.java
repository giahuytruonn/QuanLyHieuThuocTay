package qlhtt.Test;

import qlhtt.DAO.ChiTietHoaDonDAO;

public class TestLayChiTietHoaDon {
    public static void main(String[] args) {
        ChiTietHoaDonDAO chiTietHoaDonDAO = ChiTietHoaDonDAO.getInstance();
        System.out.println(chiTietHoaDonDAO.getDanhSachChiTietHoaDon());
    }
}
