package qlhtt.Controllers.NhanVien;

import qlhtt.DAO.ChiTietPhieuNhapDAO;
import qlhtt.Entity.ChiTietPhieuNhap;
import qlhtt.ThongBaoLoi.ThongBaoLoi;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChiTietPhieuNhapController {
    private ChiTietPhieuNhapDAO chiTietPhieuNhapDAO = new ChiTietPhieuNhapDAO();
    private SanPhamController sanPhamController = new SanPhamController();

    public ChiTietPhieuNhap getChiTietPhieuNhapByMaSanPham(String maSanPham) {
        return chiTietPhieuNhapDAO.getChiTietPhieuNhapBangMaSanPham(maSanPham);
    }

    public List<ChiTietPhieuNhap> getDsChiTietPhieuNhapTheoMaSanPham(String maSanPham){
        return chiTietPhieuNhapDAO.getDanhSachChiTietPhieuNhapBangMaSanPham(maSanPham);
    }

    public Map<String, Integer> getDsSoLuongConLaiCuaSanPham(String maSanPham){
        List<ChiTietPhieuNhap> dsChiTietPhieuNhap = getDsChiTietPhieuNhapTheoMaSanPham(maSanPham);
        int soLuongDaBan = dsChiTietPhieuNhap.stream().mapToInt(ChiTietPhieuNhap::getSoLuong).sum() - sanPhamController.getSanPhamById(maSanPham).getSoLuong();
        int soLuongConLai = 0;
        int soLuongLo = 0;
        Map<String, Integer> soLuongConLaiTheoChiTietPhieuNhap = new HashMap<>();

        if(!dsChiTietPhieuNhap.isEmpty()){
            for (ChiTietPhieuNhap chiTiet : dsChiTietPhieuNhap) {
                soLuongLo+= chiTiet.getSoLuong();
                if (soLuongDaBan <= soLuongLo) {
                    soLuongConLai = soLuongLo - soLuongDaBan;
                    if(chiTiet.getHanSuDung().isAfter(LocalDate.now())){
                        soLuongConLaiTheoChiTietPhieuNhap.put(maSanPham, soLuongConLai);
                        sanPhamController.capNhatGiaTienCuaSanPham(chiTiet);
//                        System.out.println(soLuongConLaiTheoChiTietPhieuNhap.get(maSanPham));
                    }else{
                        soLuongDaBan +=soLuongConLai;
                    }
                }
            }
        }else{
            throw new IllegalArgumentException("Không có số lượng");
        }
        return soLuongConLaiTheoChiTietPhieuNhap;
    }

    public ChiTietPhieuNhap getChiTietPhieuNhapHienTaiDangApDung(String maSanPham){
        List<ChiTietPhieuNhap> dsChiTietPhieuNhap = getDsChiTietPhieuNhapTheoMaSanPham(maSanPham);
        int soLuongDaBan = dsChiTietPhieuNhap.stream().mapToInt(ChiTietPhieuNhap::getSoLuong).sum() - sanPhamController.getSanPhamById(maSanPham).getSoLuong();
        int soLuongConLai = 0;
        int soLuongLo = 0;
        Map<String, Integer> soLuongConLaiTheoChiTietPhieuNhap = new HashMap<>();

        if(!dsChiTietPhieuNhap.isEmpty()){
            for (ChiTietPhieuNhap chiTiet : dsChiTietPhieuNhap) {
                soLuongLo+= chiTiet.getSoLuong();
                if (soLuongDaBan < soLuongLo) {
                    return chiTiet;
                }
            }
        }
        return null;
    }

    public List<ChiTietPhieuNhap> getDSSanPhamHetHan() {
        return chiTietPhieuNhapDAO.getDanhSachSapHetHan();
    }


    public void capNhatChiTietPhieuNhap(ChiTietPhieuNhap chiTietPhieuNhap){
        chiTietPhieuNhapDAO.capNhatChiTietPhieuNhap(chiTietPhieuNhap);
    }

    public void taoChiTietPhieuNhap(ChiTietPhieuNhap chiTietPhieuNhap){
        chiTietPhieuNhapDAO.taoChiTietPhieuNhap(chiTietPhieuNhap);
    }

    public List<ChiTietPhieuNhap> getDsChiTietPhieuNhap(){
        return chiTietPhieuNhapDAO.getDanhSachChiTietPhieuNhap();
    }

}
