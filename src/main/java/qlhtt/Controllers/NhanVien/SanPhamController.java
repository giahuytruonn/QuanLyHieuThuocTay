package qlhtt.Controllers.NhanVien;

import qlhtt.DAO.SanPhamDAO;
import qlhtt.Entity.ChiTietPhieuNhap;
import qlhtt.Entity.SanPham;

import java.util.List;

public class SanPhamController {
    private SanPhamDAO sanPhamDAO = new SanPhamDAO();

    public SanPham getSanPhamById(String id){
        SanPham sanPham = sanPhamDAO.getSanPhamBangMaSanPham(id);
        if(sanPham != null){
            return sanPham;
        }else{
            return null;
        }

    }

    public List<SanPham> getDsSanPham(){
        return sanPhamDAO.getDanhSachSanPham();
    }

    public boolean capNhatGiaTienCuaSanPham(ChiTietPhieuNhap chiTietPhieuNhap){
        double donGia = tinhDonGia(chiTietPhieuNhap.getGiaNhap());
        return sanPhamDAO.capNhatGiaTienCuaSanPham(chiTietPhieuNhap.getSanPham().getMaSanPham(),donGia);
    }

    public double tinhDonGia(double giaNhap){
        float mucThang = 0;
        if(giaNhap <=1000){
            mucThang = 0.15f;
        }else{
            if(giaNhap > 1000 && giaNhap <=5000){
                mucThang = 0.1f;
            }else{
                if(giaNhap > 5000 && giaNhap <=10000){
                    mucThang = 0.07f;
                }else{
                    if(giaNhap > 100000 && giaNhap <=1000000){
                        mucThang = 0.05f;
                    }else mucThang = 0.02f;
                }
            }
        }

        double giaMoi = giaNhap * (1+ mucThang);

        giaMoi= Math.ceil(giaMoi);
        return giaMoi;
    }

    public void capNhatSoLuongSanPham(String maSanPham, int soLuong){
        sanPhamDAO.capNhatSoLuongSanPham(maSanPham, soLuong);
    }

}