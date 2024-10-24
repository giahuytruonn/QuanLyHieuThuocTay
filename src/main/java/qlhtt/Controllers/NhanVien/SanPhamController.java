package qlhtt.Controllers.NhanVien;

import qlhtt.DAO.SanPhamDAO;
import qlhtt.Entity.SanPham;

public class SanPhamController {
    private SanPhamDAO sanPhamDAO;

    public SanPham getSanPhamById(String id){
        SanPham sanPham = sanPhamDAO.getSanPhamBangMaSanPham(id);
        if(sanPham != null){
            return sanPham;
        }else{
            return null;
        }

    }


}
