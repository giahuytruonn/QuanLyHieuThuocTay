package qlhtt.Test;

import qlhtt.DAO.HoaDonDAO;
import qlhtt.Entity.HoaDon;

import java.util.List;

public class TestLayHoaDon {
    public static void main(String[] args) {
        HoaDonDAO hoaDonDAO = HoaDonDAO.getInstance();
        System.out.println(hoaDonDAO.getHoaDonBangMaHoaDon("HD20102024001"));
    }
}
