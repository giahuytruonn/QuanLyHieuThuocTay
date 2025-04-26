package qlhtt.Controllers.NhanVien;

import qlhtt.DAO.NhaCungCapDAO;
import qlhtt.Entity.NhaCungCap;

import java.util.List;

public class NhaCungCapController {

    NhaCungCapDAO nhaCungCapDAO = new NhaCungCapDAO();

    public List<NhaCungCap> getDsNhaCungCap(){
        return nhaCungCapDAO.getDanhSachNhaCungCap();
    }

    public NhaCungCap getNhaCungCapBangTen(String ten){
        return nhaCungCapDAO.getNhaCungCapBangTen(ten);
    }
}
