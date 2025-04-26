package qlhtt.Controllers.NhanVien;

import org.springframework.security.core.parameters.P;
import qlhtt.DAO.PhieuNhapDAO;
import qlhtt.Entity.PhieuNhap;

public class PhieuNhapController {
    PhieuNhapDAO phieuNhapDAO = new PhieuNhapDAO();

    public PhieuNhap getPhieuNhapBangMaPhieuNhap (String maPhieuNhap){
        return phieuNhapDAO.getPhieuNhapBangMaPhieuNhap(maPhieuNhap) != null ? phieuNhapDAO.getPhieuNhapBangMaPhieuNhap(maPhieuNhap):null;
    }

    public PhieuNhap getPhieuNhapVuaTao(){
        return phieuNhapDAO.getPhieuNhapVuaTao();
    }

    public void taoPhieuNhap(PhieuNhap phieuNhap){
        phieuNhapDAO.taoPhieuNhap(phieuNhap);
    }

    public void capNhatTrangThaiPhieuNhap(String maPhieuNhap, boolean trangThaiPhieuNhap){
        phieuNhapDAO.capNhatTrangThaiPhieuNhap(maPhieuNhap, trangThaiPhieuNhap);
    }

}
