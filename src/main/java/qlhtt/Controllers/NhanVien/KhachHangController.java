package qlhtt.Controllers.NhanVien;

import qlhtt.DAO.KhachHangDAO;
import qlhtt.Entity.KhachHang;

import java.util.List;

public class KhachHangController {
    private KhachHangDAO khachHangDAO = new KhachHangDAO();

    public List<KhachHang> getDsKhachHang(){
        return khachHangDAO.getDanhSachKhachHang();
    }

    public KhachHang getKhachHangBySoDienThoai(String soDienThoai){
        return khachHangDAO.getKhachHangBangSoDienThoai(soDienThoai);
    }

    public void capNhatDiemTichLuy(String maKhachHang, int diemTichLuy){
        khachHangDAO.capNhatDiemTichLuyCuaKhachHang(maKhachHang,diemTichLuy);
    }
}
