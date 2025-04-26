package qlhtt.Test;

import qlhtt.Controllers.NhanVien.HoaDonController;
import qlhtt.Entity.HoaDon;

public class TestHoaDon {
    public static void main(String[] args) {
        HoaDonController hoaDonController = new HoaDonController();
        System.out.println(hoaDonController.getDsHoaDonTheoTrangThaiVaThanhVien(true, true));
        int roundedAmount = ((200000 + 49999) / 50000) * 50000;
        System.out.println(roundedAmount);
    }
}
