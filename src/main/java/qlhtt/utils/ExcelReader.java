package qlhtt.utils;

import org.apache.poi.ss.usermodel.*;
import qlhtt.Controllers.NhanVien.NhaCungCapController;
import qlhtt.Entity.ChiTietPhieuNhap;
import qlhtt.Entity.PhieuNhap;
import qlhtt.Entity.SanPham;
import qlhtt.ThongBao.ThongBao;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {

    private NhaCungCapController nhaCungCapController = new NhaCungCapController();

    public static ExcelReader instance = new ExcelReader();

    public static ExcelReader getInstance() {
        return instance;
    }

    public List<ChiTietPhieuNhap> getDsChiTietPhieuNhap(File file) {
        List<ChiTietPhieuNhap> dsChiTietPhieuNhap = new ArrayList<>();

        try{
            InputStream inputStream = new FileInputStream(file);
            if(inputStream == null) {
                System.out.println("Khong ton tai file");
            }
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // Lấy sheet đầu tiên

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Bỏ qua tiêu đề nếu có
                }

                try {
                    ChiTietPhieuNhap chiTietPhieuNhap = new ChiTietPhieuNhap();
                    PhieuNhap phieuNhap1 = new PhieuNhap(); // Tạo đối tượng PhieuNhap
                    phieuNhap1.setNhaCungCap(nhaCungCapController.getNhaCungCapBangTen(row.getCell(24).getStringCellValue()));
                    phieuNhap1.setSoLo(String.valueOf((int)row.getCell(23).getNumericCellValue()));
                    chiTietPhieuNhap.setPhieuNhap(phieuNhap1);

                    // Đọc dữ liệu từng cell
                    SanPham sanPham = new SanPham(row.getCell(0).getStringCellValue());
                    chiTietPhieuNhap.setSanPham(sanPham);

                    chiTietPhieuNhap.setGiaNhap(row.getCell(17).getNumericCellValue());
                    chiTietPhieuNhap.setHanSuDung(row.getCell(18).getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    chiTietPhieuNhap.setNgaySanXuat(row.getCell(19).getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    chiTietPhieuNhap.setSoLuong((int) row.getCell(20).getNumericCellValue());
                    chiTietPhieuNhap.setThanhTien(row.getCell(21).getNumericCellValue());
                    chiTietPhieuNhap.setDonViTinh(row.getCell(22).getStringCellValue());

                    // Thêm vào danh sách
                    dsChiTietPhieuNhap.add(chiTietPhieuNhap);
                }catch (Exception e) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsChiTietPhieuNhap;
    }

}