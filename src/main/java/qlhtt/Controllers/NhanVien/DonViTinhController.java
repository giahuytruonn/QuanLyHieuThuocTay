package qlhtt.Controllers.NhanVien;

import qlhtt.DAO.DonViTinhDAO;
import qlhtt.Entity.DonViTinh;

import java.util.List;

public class DonViTinhController {
    DonViTinhDAO donViTinhDAO = new DonViTinhDAO();

    public List<DonViTinh> getDsDonViTinh(){
        return donViTinhDAO.getDanhSachDonViTinh();
    }
}
