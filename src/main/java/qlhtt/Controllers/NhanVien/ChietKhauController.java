package qlhtt.Controllers.NhanVien;

import qlhtt.DAO.ChietKhauDAO;
import qlhtt.Entity.ChietKhau;

public class ChietKhauController {
    private ChietKhauDAO chietKhauDAO = new ChietKhauDAO();

    public ChietKhau getChietKhau(String maChietKhau){
        return chietKhauDAO.getChietKhauBangMaChietKhau(maChietKhau);
    }
    public void capNhatSoLuongChietKhau(ChietKhau chietKhau){
        chietKhauDAO.capNhatSoLuongChietKhau(chietKhau);
    }
}
