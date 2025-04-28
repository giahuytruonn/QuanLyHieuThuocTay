package qlhtt.Controllers.NhanVien;

import org.springframework.security.core.parameters.P;
import qlhtt.DAO.PhieuNhapDAO;
import qlhtt.Entity.PhieuNhap;

import java.time.LocalDate;
import java.util.List;

public class PhieuNhapController {
    PhieuNhapDAO phieuNhapDAO = new PhieuNhapDAO();

    public PhieuNhap getPhieuNhapBangMaPhieuNhap (String maPhieuNhap){
        return phieuNhapDAO.getPhieuNhapBangMaPhieuNhap(maPhieuNhap) != null ? phieuNhapDAO.getPhieuNhapBangMaPhieuNhap(maPhieuNhap):null;
    }

    public PhieuNhap getPhieuNhapVuaTao(){
        return phieuNhapDAO.getPhieuNhapVuaTao();
    }

    public List<PhieuNhap> getDSPhieuNhapYeuCau(LocalDate startDate, LocalDate endDate){
        return phieuNhapDAO.getDanhSachPhieuNhapTheoYeuCau(startDate, endDate);
    }

    public void taoPhieuNhap(PhieuNhap phieuNhap){
        phieuNhapDAO.taoPhieuNhap(phieuNhap);
    }

    public void capNhatTrangThaiPhieuNhap(String maPhieuNhap, boolean trangThaiPhieuNhap){
        phieuNhapDAO.capNhatTrangThaiPhieuNhap(maPhieuNhap, trangThaiPhieuNhap);
    }

}
