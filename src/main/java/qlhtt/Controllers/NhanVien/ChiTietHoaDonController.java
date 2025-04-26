package qlhtt.Controllers.NhanVien;

import qlhtt.DAO.ChiTietHoaDonDAO;
import qlhtt.Entity.ChiTietHoaDon;

import java.util.List;

public class ChiTietHoaDonController {
    ChiTietHoaDonDAO chiTietHoaDonDAO = new ChiTietHoaDonDAO();

    public List<ChiTietHoaDon> getDsChiTietHoaDonTheoMaHoaDon(String maHoaDon){
        return chiTietHoaDonDAO.getDsChiTietHoaDonBangMaHoaDon(maHoaDon);
    }

    public void taoChiTietHoaDon(ChiTietHoaDon chiTietHoaDon){
        chiTietHoaDonDAO.taoChiTietHoaDon(chiTietHoaDon);
    }

    public void xoaChiTietHoaDon(String maHoaDon){
        chiTietHoaDonDAO.xoaChiTietHoaDonBangMaHoaDon(maHoaDon);
    }
}
