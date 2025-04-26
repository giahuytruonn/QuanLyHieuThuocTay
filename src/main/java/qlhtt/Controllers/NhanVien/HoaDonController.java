package qlhtt.Controllers.NhanVien;

import qlhtt.DAO.HoaDonDAO;
import qlhtt.Entity.HoaDon;

import java.util.List;

public class HoaDonController {
    HoaDonDAO hoaDonDAO = new HoaDonDAO();

    public List<HoaDon> getDsHoaDon() {
        return hoaDonDAO.getDanhSachHoaDon();
    }

    public List<HoaDon> getDsHoaDonTheoTrangThai(boolean trangThaiThanhToan){
        return hoaDonDAO.getDsHoaDonBangTrangThai(trangThaiThanhToan);
    }

    public HoaDon getHoaDonBangMaHoaDon(String maHoaDon){
        return hoaDonDAO.getHoaDonBangMaHoaDon(maHoaDon);
    }

    public void taoHoaDon(HoaDon hoaDon){
        hoaDonDAO.taoHoaDon(hoaDon);
    }

    public HoaDon getHoaDonMoiNhat(){
        return hoaDonDAO.getHoaDonMoiNhat();
    }
    public void capNhatTrangThaiHoaDon(HoaDon hoaDon){
        hoaDonDAO.capNhatTrangThaiHoaDon(hoaDon);
    }

    public void xoaHoaDonGanNhat(){
        hoaDonDAO.xoaHoaDonGanNhat();
    }

    public void capNhatHoaDon(HoaDon hoaDon){
        hoaDonDAO.capNhatHoaDon(hoaDon);
    }

    public List<HoaDon> getDsHoaDonTheoTrangThaiVaThanhVien(boolean trangThaiThanhToan, boolean isThanhVien){
        if(isThanhVien){
            return hoaDonDAO.getDsHoaDonBangTrangThaiVaCoThanhVien(trangThaiThanhToan);
        }else{
            return hoaDonDAO.getDsHoaDonBangTrangThaiVaKhongThanhVien(trangThaiThanhToan);
        }
    }


}
