package qlhtt.Server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import net.sf.jasperreports.engine.*;
import qlhtt.ConnectDB.ConnectDB;
import qlhtt.Controllers.LoginController;
import qlhtt.Controllers.NhanVien.*;
import qlhtt.Controllers.TaiKhoanController;
import qlhtt.DAO.*;
import qlhtt.Entity.*;
import qlhtt.Enum.VaiTro;
import qlhtt.Models.Model;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandleClient implements Runnable {
    private final Socket clientSocket;

    public HandleClient(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Nhận từ client: " + message);
                String response = handleRequest(message);
                out.println(response);
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi xử lý client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Lỗi khi đóng kết nối client: " + e.getMessage());
            }
        }
    }

    private String handleRequest(String request) {
        String[] parts = request.split(" ", 2);
        String command = parts[0];

        switch (command) {
            case "LOGIN":
                return handleLogin(parts[1].split(" "));
            case "THONG_KE_SAN_PHAM":
                return handleThongKeSanPham(parts[1].split(" "));
            case "SEARCH":
                return handleSearchSanPham(parts[1].split(" "));
            case "PAGE":
                return handlePageSanPham(parts[1].split(" "));
            case "CAPNHAT_SANPHAM":
                return handleCapNhatSanPham(parts[1]);
            case "THEM_SANPHAM":
                return handleThemSanPham(parts[1]);
            case "CAPNHAT_KHACHHANG":
                return handleCapNhatKhachHang(parts[1]);
            case "TIM_KHACHHANG":
                return handleTimKhachHang(parts[1]);
            case "GET_SANPHAM":
                return handleGetSanPham(parts[1].split(" "));
            case "GET_CTPNNOW":
                return handleGetChiTietPhieuNhapHienTai(parts[1].split(" "));
            case "UPDATE_PRICE":
                return handleUpdatePrice(parts[1].split(" "));
            case "GET_KHACHHANGBYSODIENTHOAI":
                return handleGetKhachHangBySoDienThoai(parts[1].split(" "));
            case "GET_LISTCTHD":
                return handleGetListChiTietHoaDon(parts[1].split(" "));
            case "GET_HOADONMOINHAT":
                return handleGetHoaDonMoiNhat(parts[1].split(" "));
            case "CREATE_HOADON":
                return handleCreateHoaDon(parts[1].split(" "));
            case "DELETE_CTHD":
                return handleDeleteCTHD(parts[1].split(" "));
            case "CREATE_CTHD":
                return handleCreateCTHD(parts[1].split(" "));
            case "UPDATE_HD":
                return handleUpdateHD(parts[1].split(" "));
            case "UPDATE_DTL":
                return handleUpdateDTL(parts[1].split(" "));
            case "UPDATE_SOLUONG_CK":
                return handleUpdateSoLuongCK(parts[1].split(" "));
            case "GET_CK":
                return handleGetCK(parts[1].split(" "));
            case "CREATE_HOADONPDF":
                return handleTaoHoaDonPDF(parts[1].split(" "));
            case "GET_LIST_CTHD":
                return handleGetListCTHD();
            case "GET_LIST_HD_YEU_CAU":
                return handleGetListHD(parts[1].split(" "));
            case "GET_LIST_HD_7_NGAY":
                return handleGetListHD7Ngay();
            case "GET_LIST_PHIEU_NHAP_YEU_CAU":
                return handleGetPhieuNhap(parts[1].split(" "));
            case "GET_LIST_SAN_PHAM":
                return handleGetListSanPham();
            case "GET_LIST_SAN_PHAM_HET_HAN":
                return handleGetListSanPhamHetHan();
            case "THEM_NHACUNGCAP":
                return handleThemNhaCungCap(parts[1]);
            case "GET_DONVITINH":
                return handleGetDonViTinh();
            case "GET_NHACUNGCAP":
                return handleGetNhaCungCap();
            case "GET_NHANVIEN":
                return handleGetNhanVien(parts[1]);
            case "GET_NHACUNGCAP_BY_TEN":
                return handleGetNhaCungCapByTen(parts[1]);
            case "CREATE_PHIEUNHAP":
                return handleCreatePhieuNhap(parts[1]);
            case "GET_PHIEUNHAP_MOINHAT":
                return handleGetPhieuNhapMoiNhat();
            case "CREATE_CHITIETPHIEUNHAP":
                return handleCreateChiTietPhieuNhap(parts[1]);
            case "UPDATE_PHIEUNHAP_STATUS":
                return handleUpdatePhieuNhapStatus(parts[1].split(" "));
            case "UPDATE_SANPHAM_SOLUONG":
                return handleUpdateSanPhamSoLuong(parts[1].split(" "));
            case "CREATE_PHIEUNHAP_PDF":
                return handleTaoPhieuNhapPDF(parts[1].split(" "));
            case "LOGOUT":
                if (parts.length < 2) {
                    return "ERROR";
                }
                String maNhanVien = parts[1];
                return handleLogout(maNhanVien);
            default:
                return "UNKNOWN_COMMAND";
        }
    }

    private String handleLogout(String maNhanVien) {
        try {
                System.out.println("Nhân viên " + maNhanVien + " đã đăng xuất");
                return "SUCCESS";
        } catch (Exception e) {
            System.err.println("Lỗi khi xử lý đăng xuất: " + e.getMessage());
            e.printStackTrace();
            return "ERROR";
        }
    }



    private String handleGetDonViTinh() {
        try {
            DonViTinhController donViTinhController = new DonViTinhController();
            List<DonViTinh> dsDonViTinh = donViTinhController.getDsDonViTinh();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(dsDonViTinh);
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String handleGetNhaCungCap() {
        try {
            NhaCungCapController nhaCungCapController = new NhaCungCapController();
            List<NhaCungCap> dsNhaCungCap = nhaCungCapController.getDsNhaCungCap();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(dsNhaCungCap);
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String handleGetNhanVien(String maNhanVien) {
        try {
            NhanVienController nhanVienController = new NhanVienController();
            NhanVien nhanVien = nhanVienController.getNhanVienBangMa(maNhanVien);
            if (nhanVien == null) {
                return "NOT_FOUND";
            }
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(nhanVien);
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String handleGetNhaCungCapByTen(String tenNhaCungCap) {
        try {
            NhaCungCapController nhaCungCapController = new NhaCungCapController();
            NhaCungCap nhaCungCap = nhaCungCapController.getNhaCungCapBangTen(tenNhaCungCap);
            if (nhaCungCap == null) {
                return "NOT_FOUND";
            }
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(nhaCungCap);
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String handleCreatePhieuNhap(String phieuNhapJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            PhieuNhap phieuNhap = objectMapper.readValue(phieuNhapJson, PhieuNhap.class);
            PhieuNhapController phieuNhapController = new PhieuNhapController();
            phieuNhapController.taoPhieuNhap(phieuNhap);
            return "SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String handleGetPhieuNhapMoiNhat() {
        try {
            PhieuNhapController phieuNhapController = new PhieuNhapController();
            PhieuNhap phieuNhap = phieuNhapController.getPhieuNhapVuaTao();
            if (phieuNhap == null) {
                return "NOT_FOUND";
            }
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(phieuNhap);
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String handleCreateChiTietPhieuNhap(String chiTietJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            ChiTietPhieuNhap chiTietPhieuNhap = objectMapper.readValue(chiTietJson, ChiTietPhieuNhap.class);
            ChiTietPhieuNhapController chiTietPhieuNhapController = new ChiTietPhieuNhapController();
            chiTietPhieuNhapController.taoChiTietPhieuNhap(chiTietPhieuNhap);
            return "SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String handleUpdatePhieuNhapStatus(String[] parts) {
        try {
            String maPhieuNhap = parts[0];
            boolean trangThai = Boolean.parseBoolean(parts[1]);
            PhieuNhapController phieuNhapController = new PhieuNhapController();
            phieuNhapController.capNhatTrangThaiPhieuNhap(maPhieuNhap, trangThai);
            return "SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String handleUpdateSanPhamSoLuong(String[] parts) {
        try {
            String maSanPham = parts[0];
            int soLuong = Integer.parseInt(parts[1]);
            SanPhamController sanPhamController = new SanPhamController();
            sanPhamController.capNhatSoLuongSanPham(maSanPham, soLuong);
            return "SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String handleTaoPhieuNhapPDF(String[] parts) {
        if (parts.length < 1) {
            return "ERROR Thiếu mã phiếu nhập";
        }

        String maPN = parts[0];
        String pdfPath = "src/main/resources/PhieuNhap/" + maPN + ".pdf";

        try {
            ConnectDB connectDB = ConnectDB.getInstance();
            connectDB.connect();
            Connection connection = connectDB.getConnection();

            JasperReport jasperReport = JasperCompileManager.compileReport("src/main/resources/Fxml/MauPhieuNhap.jrxml");

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("ReportTitle", "Phiếu Nhập");
            parameters.put("maPhieuNhap1", maPN);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfPath);

            System.out.println("Đã tạo phiếu nhập PDF: " + pdfPath);

            return "SUCCESS " + pdfPath;

        } catch (JRException e) {
            e.printStackTrace();
            return "ERROR JasperReports: " + e.getMessage();
        } catch (SQLException e) {
            e.printStackTrace();
            return "ERROR Database: " + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR Unexpected: " + e.getMessage();
        }
    }

    private String handleGetListSanPhamHetHan() {
        try {
            ChiTietPhieuNhapController chiTietPhieuNhapController = new ChiTietPhieuNhapController();
            List<ChiTietPhieuNhap> dsCTPN = chiTietPhieuNhapController.getDSSanPhamHetHan();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(dsCTPN);
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String handleGetListSanPham() {
        try {
            SanPhamController sanPhamController = new SanPhamController();
            List<SanPham> dsSP = sanPhamController.getDsSanPham();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(dsSP);
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String handleGetPhieuNhap(String[] parts) {
        LocalDate startDate = LocalDate.parse(parts[0]);
        LocalDate endDate = LocalDate.parse(parts[1]);

        try {
            PhieuNhapController phieuNhapController = new PhieuNhapController();
            List<PhieuNhap> dsPhieuNhap = phieuNhapController.getDSPhieuNhapYeuCau(startDate, endDate);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(dsPhieuNhap);
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String handleGetListHD7Ngay() {
        try {
            HoaDonController hoaDonController = new HoaDonController();
            List<HoaDon> dsHoaDonYeuCau = hoaDonController.getDSHoaDon7Ngay();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(dsHoaDonYeuCau);
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String handleGetListHD(String[] parts) {
        LocalDate startDate = LocalDate.parse(parts[0]);
        LocalDate endDate = LocalDate.parse(parts[1]);

        try {
            HoaDonController hoaDonController = new HoaDonController();
            List<HoaDon> dsHoaDonYeuCau = hoaDonController.getDsHoaDonTheoYeuCau(startDate, endDate);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(dsHoaDonYeuCau);
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String handleGetListCTHD() {
        try {
            ChiTietHoaDonController chiTietHoaDonController = new ChiTietHoaDonController();
            List<ChiTietHoaDon> dsChiTietHoaDon = chiTietHoaDonController.getDsChiTietHoaDon();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(dsChiTietHoaDon);
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String handleLogin(String[] parts) {
        String tenDangNhap = parts[0];
        String matKhau = parts[1];
        int vaiTro = Integer.parseInt(parts[2]);

        List<TaiKhoan> dsTaiKhoan = TaiKhoanController.layDanhSachTaiKhoan();
        VaiTro vaiTroTaiKhoan = vaiTro == 0 ? VaiTro.NGUOIQUANLY : VaiTro.NHANVIEN;

        for (TaiKhoan taiKhoan : dsTaiKhoan) {
            if (taiKhoan.getTenDangNhap().equals(tenDangNhap)
                    && TaiKhoanController.kiemTraMatKhau(matKhau, taiKhoan.getMatKhau())
                    && taiKhoan.getNhanVien().getVaiTro() == vaiTroTaiKhoan) {
                if (taiKhoan.getTrangThaiTaiKhoan()) {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.registerModule(new JavaTimeModule());
                        return objectMapper.writeValueAsString(taiKhoan);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "ERROR";
                    }
                } else {
                    return "LOCKED";
                }
            }
        }
        return "FAIL";
    }

    private String handleThongKeSanPham(String[] parts) {
        try {
            LocalDate startDate = LocalDate.parse(parts[0]);
            LocalDate endDate = LocalDate.parse(parts[1]);
            List<HoaDon> dsHoaDon = HoaDonDAO.getInstance().getDanhSachHoaDonTheoYeuCau(startDate, endDate);
            HashMap<LocalDate, Integer> dsSPTheoNgay = loadDataThongKeSPTheoNgay(dsHoaDon);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(dsSPTheoNgay);
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private HashMap<LocalDate, Integer> loadDataThongKeSPTheoNgay(List<HoaDon> dsHD) {
        HashMap<LocalDate, Integer> dsSPTheoNgay = new HashMap<>();
        List<ChiTietHoaDon> dsCTHD = ChiTietHoaDonDAO.getInstance().getDanhSachChiTietHoaDon();

        for (HoaDon hoaDon : dsHD) {
            int tongSoLuong = 0;
            for (ChiTietHoaDon chiTietHoaDon : dsCTHD) {
                if (chiTietHoaDon.getHoaDon().getMaHoaDon().equals(hoaDon.getMaHoaDon())) {
                    tongSoLuong += chiTietHoaDon.getSoLuong();
                }
            }
            dsSPTheoNgay.put(hoaDon.getNgayTao(), tongSoLuong);
        }
        return dsSPTheoNgay;
    }

    private String handleSearchSanPham(String[] parts) {
        if (parts.length < 1) return "INVALID_SEARCH";
        String maSanPham = parts[0];
        List<SanPham> result = SanPhamDAO.getInstance().timKiemSanPhamTheoMa(maSanPham);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(result);
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String handlePageSanPham(String[] parts) {
        if (parts.length < 1) return "INVALID_PAGE";
        int trang = Integer.parseInt(parts[0]);
        List<SanPham> result = SanPhamDAO.getInstance().laySanPhamTheoSoTrang(trang);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(result);
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String handleCapNhatSanPham(String sanPhamJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            SanPham sanPham = objectMapper.readValue(sanPhamJson, SanPham.class);
            boolean result = SanPhamDAO.getInstance().capNhatSanPham(sanPham);
            return result ? "OK" : "FAIL";
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String handleThemSanPham(String sanPhamJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            SanPham sanPham = objectMapper.readValue(sanPhamJson, SanPham.class);
            boolean result = SanPhamDAO.getInstance().themSanPham(sanPham);
            return result ? "OK" : "FAIL";
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String handleCapNhatKhachHang(String khachHangJson) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            KhachHang kh = mapper.readValue(khachHangJson, KhachHang.class);
            boolean result = KhachHangDAO.getInstance().capNhatKhachHang(kh);
            return result ? "OK" : "FAIL";
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String handleTimKhachHang(String soDienThoai) {
        try {
            KhachHang kh = KhachHangDAO.getInstance().getKhachHangBangSoDienThoai(soDienThoai);
            if (kh == null) return "NOT_FOUND";
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.writeValueAsString(kh);
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String handleGetCK(String[] parts) {
        String maChietKhau = parts.length > 0 ? parts[0] : "";
        ChietKhauController chietKhauController = new ChietKhauController();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            ChietKhau chietKhau = chietKhauController.getChietKhau(maChietKhau);
            if (chietKhau != null) {
                return objectMapper.writeValueAsString(chietKhau);
            } else {
                return "NOT_FOUND";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String handleUpdateSoLuongCK(String[] parts) {
        String payload = String.join(" ", Arrays.copyOfRange(parts, 0, parts.length));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            ChietKhau chietKhau = objectMapper.readValue(payload, ChietKhau.class);
            ChietKhauController chietKhauController = new ChietKhauController();
            chietKhauController.capNhatSoLuongChietKhau(chietKhau);
            return "SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String handleUpdateDTL(String[] parts) {
        if (parts.length > 1) {
            String maKhachHang = parts[0];
            int diemTichLuy = Integer.parseInt(parts[1]);
            KhachHangController khachHangController = new KhachHangController();
            khachHangController.capNhatDiemTichLuy(maKhachHang, diemTichLuy);
            return "SUCCESS";
        }
        return "FAIL";
    }

    private String handleUpdateHD(String[] parts) {
        String payload = String.join(" ", Arrays.copyOfRange(parts, 0, parts.length));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            HoaDon hoaDon = objectMapper.readValue(payload, HoaDon.class);
            HoaDonController hoaDonController = new HoaDonController();
            if (hoaDon.getChietKhau().getMaChietKhau().equals("")) {
                hoaDon.setChietKhau(null);
            }
            if (hoaDon.getKhachHang().getMaKhachHang() == null) {
                hoaDon.setKhachHang(null);
            }
            hoaDonController.capNhatHoaDon(hoaDon);
            return "SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String handleCreateCTHD(String[] parts) {
        String payload = String.join(" ", Arrays.copyOfRange(parts, 0, parts.length));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            ChiTietHoaDon chiTietHoaDon = objectMapper.readValue(payload, ChiTietHoaDon.class);
            ChiTietHoaDonController chiTietHoaDonController = new ChiTietHoaDonController();
            chiTietHoaDonController.taoChiTietHoaDon(chiTietHoaDon);
            return "SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String handleDeleteCTHD(String[] parts) {
        String maHoaDon = parts[0];
        ChiTietHoaDonController chiTietHoaDonController = new ChiTietHoaDonController();
        chiTietHoaDonController.xoaChiTietHoaDon(maHoaDon);
        return "SUCCESS";
    }

    private String handleCreateHoaDon(String[] parts) {
        String payload = String.join(" ", Arrays.copyOfRange(parts, 0, parts.length));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            HoaDon hoaDon = objectMapper.readValue(payload, HoaDon.class);
            HoaDonController hoaDonController = new HoaDonController();
            if (hoaDon.getChietKhau().getMaChietKhau().equals("")) {
                hoaDon.setChietKhau(null);
            }
            if (hoaDon.getKhachHang().getMaKhachHang() == null) {
                hoaDon.setKhachHang(null);
            }
            hoaDonController.taoHoaDon(hoaDon);
            return "SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String handleGetHoaDonMoiNhat(String[] parts) {
        try {
            HoaDonController hoaDonController = new HoaDonController();
            HoaDon hoaDon = hoaDonController.getHoaDonMoiNhat();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(hoaDon);
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String handleGetListChiTietHoaDon(String[] parts) {
        String maHoaDon = parts[0];
        ChiTietHoaDonController chiTietHoaDonController = new ChiTietHoaDonController();
        List<ChiTietHoaDon> chiTietHoaDons = chiTietHoaDonController.getDsChiTietHoaDonTheoMaHoaDon(maHoaDon);
        if (chiTietHoaDons != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                return objectMapper.writeValueAsString(chiTietHoaDons);
            } catch (Exception e) {
                e.printStackTrace();
                return "ERROR";
            }
        } else {
            return "NOT_FOUND";
        }
    }

    private String handleGetKhachHangBySoDienThoai(String[] parts) {
        String soDienThoai = parts.length > 0 ? parts[0] : "";
        KhachHangController khachHangController = new KhachHangController();
        KhachHang khachHang = khachHangController.getKhachHangBySoDienThoai(soDienThoai);
        if (khachHang != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                return objectMapper.writeValueAsString(khachHang);
            } catch (Exception e) {
                e.printStackTrace();
                return "ERROR";
            }
        } else {
            return "NOT_FOUND";
        }
    }

    private String handleUpdatePrice(String[] parts) {
        try {
            String payload = String.join(" ", Arrays.copyOfRange(parts, 0, parts.length));
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            ChiTietPhieuNhap chiTietPhieuNhap = objectMapper.readValue(payload, ChiTietPhieuNhap.class);
            SanPhamController sanPhamController = new SanPhamController();
            if (sanPhamController.capNhatGiaTienCuaSanPham(chiTietPhieuNhap)) {
                return "SUCCESS";
            } else {
                return "FAIL";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String handleGetChiTietPhieuNhapHienTai(String[] parts) {
        String maSanPham = parts[0];
        ChiTietPhieuNhapController chiTietPhieuNhapController = new ChiTietPhieuNhapController();
        ChiTietPhieuNhap chiTietPhieuNhap = chiTietPhieuNhapController.getChiTietPhieuNhapHienTaiDangApDung(maSanPham);
        if (chiTietPhieuNhap != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                return objectMapper.writeValueAsString(chiTietPhieuNhap);
            } catch (Exception e) {
                e.printStackTrace();
                return "ERROR";
            }
        } else {
            return "NOT_FOUND";
        }
    }

    private String handleGetSanPham(String[] parts) {
        String maSanPham = parts[0];
        SanPhamController sanPhamController = new SanPhamController();
        SanPham sanPham = sanPhamController.getSanPhamById(maSanPham);
        if (sanPham != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                return objectMapper.writeValueAsString(sanPham);
            } catch (Exception e) {
                e.printStackTrace();
                return "ERROR";
            }
        } else {
            return "PRODUCT_NOT_FOUND";
        }
    }

    private String handleTaoHoaDonPDF(String[] parts) {
        if (parts.length < 1) {
            return "ERROR Thiếu mã hóa đơn";
        }
        String maHD = parts[0];
        String pdfPath = "src/main/resources/HoaDon/" + maHD + ".pdf";
        try {
            ConnectDB connectDB = ConnectDB.getInstance();
            connectDB.connect();
            Connection connection = connectDB.getConnection();
            JasperReport jasperReport = JasperCompileManager.compileReport("src/main/resources/Fxml/MauHoaDon.jrxml");
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("ReportTitle", "Hóa Đơn");
            parameters.put("maHoaDonParam", maHD);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfPath);
            System.out.println("Đã tạo hóa đơn PDF: " + pdfPath);
            return "SUCCESS " + pdfPath;
        } catch (JRException e) {
            e.printStackTrace();
            return "ERROR JasperReports: " + e.getMessage();
        } catch (SQLException e) {
            e.printStackTrace();
            return "ERROR Database: " + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR Unexpected: " + e.getMessage();
        }
    }

    private String handleThemNhaCungCap(String nhaCungCapJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            NhaCungCap nhaCungCap = objectMapper.readValue(nhaCungCapJson, NhaCungCap.class);
            boolean result = NhaCungCapDAO.getInstance().themNhaCungCap(nhaCungCap);
            return result ? "OK" : "FAIL";
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }
}