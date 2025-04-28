package qlhtt.Server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import net.sf.jasperreports.engine.*;
import qlhtt.ConnectDB.ConnectDB;
import qlhtt.Controllers.LoginController;
import qlhtt.Controllers.NhanVien.*;
import qlhtt.Controllers.TaiKhoanController;
import qlhtt.DAO.ChiTietHoaDonDAO;
import qlhtt.DAO.HoaDonDAO;
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

import qlhtt.DAO.SanPhamDAO;

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

                // Xử lý yêu cầu từ client
                String response = handleRequest(message);
                out.println(response); // Gửi phản hồi tới client
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
        // Phân tích yêu cầu từ client
        String[] parts = request.split(" ");
        String command = parts[0];

        switch (command) {
            case "LOGIN":
                return handleLogin(parts);
//            case "THONG_KE_DOANH_THU":
//                return handleThongKeDoanhThu(parts);
            case "THONG_KE_SAN_PHAM":
                return handleThongKeSanPham(parts);
            case "SEARCH":
                return handleSearchSanPham(parts);
            case "PAGE":
                return handlePageSanPham(parts);
            case "CAPNHAT_SANPHAM":
                return handleCapNhatSanPham(request.substring("CAPNHAT_SANPHAM ".length()));
            case "THEM_SANPHAM":
                return handleThemSanPham(request.substring("THEM_SANPHAM ".length()));
            case "CAPNHAT_KHACHHANG":
                return handleCapNhatKhachHang(request.substring("CAPNHAT_KHACHHANG ".length()));
            case "TIM_KHACHHANG":
                return handleTimKhachHang(parts[1]);
            case "GET_SANPHAM":
                return handleGetSanPham(parts);
            case "GET_CTPNNOW":
                return handleGetChiTietPhieuNhapHienTai(parts);
            case "UPDATE_PRICE":
                return handleUpdatePrice(parts);
            case "GET_KHACHHANGBYSODIENTHOAI":
                return handleGetKhachHangBySoDienThoai(parts);
            case "GET_LISTCTHD":
                return handleGetListChiTietHoaDon(parts);
            case "GET_HOADONMOINHAT":
                return handleGetHoaDonMoiNhat(parts);
            case "CREATE_HOADON":
                return handleCreateHoaDon(parts);
            case "DELETE_CTHD":
                return handleDeleteCTHD(parts);
            case "CREATE_CTHD":
                return handleCreateCTHD(parts);
            case "UPDATE_HD":
                return handleUpdateHD(parts);
            case "UPDATE_DTL":
                return handleUpdateDTL(parts);
            case "UPDATE_SOLUONG_CK":
                return handleUpdateSoLuongCK(parts);
            case "GET_CK":
                return handleGetCK(parts);
            case "CREATE_HOADONPDF":
                return handleTaoHoaDonPDF(parts);
            case "GET_LIST_CTHD":
                return handleGetListCTHD();
            case "GET_LIST_HD_YEU_CAU":
                return handleGetListHD(parts);
            case "GET_LIST_HD_7_NGAY":
                return handleGetListHD7Ngay();
            default:
                return "UNKNOWN_COMMAND";
        }
    }

    private String handleGetListHD7Ngay() {
        try{
            HoaDonController hoaDonController = new HoaDonController();
            List<HoaDon> dsHoaDonYeuCau = hoaDonController.getDSHoaDon7Ngay();

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(dsHoaDonYeuCau);

        }catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String handleGetListHD(String[] parts) {
        LocalDate startDate = LocalDate.parse(parts[1]);
        LocalDate endDate = LocalDate.parse(parts[2]);

        try{
            HoaDonController hoaDonController = new HoaDonController();
            List<HoaDon> dsHoaDonYeuCau = hoaDonController.getDsHoaDonTheoYeuCau(startDate,endDate);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(dsHoaDonYeuCau);

        }catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }

    }

    private String handleGetListCTHD() {
        try {
            ChiTietHoaDonController chiTietHoaDonController = new ChiTietHoaDonController();
            List<ChiTietHoaDon> dsChiTietHoaDon = chiTietHoaDonController.getDsChiTietHoaDon();

            // Chuyển dữ liệu thành JSON
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(dsChiTietHoaDon);
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String handleLogin(String[] parts) {
        String tenDangNhap = parts[1];
        String matKhau = parts[2];
        int vaiTro = Integer.parseInt(parts[3]);

        List<TaiKhoan> dsTaiKhoan = TaiKhoanController.layDanhSachTaiKhoan();

        VaiTro vaiTroTaiKhoan;
        if(vaiTro == 0) vaiTroTaiKhoan = VaiTro.NGUOIQUANLY;
        else vaiTroTaiKhoan = VaiTro.NHANVIEN;

        for (TaiKhoan taiKhoan : dsTaiKhoan) {
            if (taiKhoan.getTenDangNhap().equals(tenDangNhap)
                    && TaiKhoanController.kiemTraMatKhau(matKhau, taiKhoan.getMatKhau())
                    && taiKhoan.getNhanVien().getVaiTro() == vaiTroTaiKhoan) {
                if (taiKhoan.getTrangThaiTaiKhoan() ) {
                    try {
                        // Tạo ObjectMapper và đăng ký JavaTimeModule
                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.registerModule(new JavaTimeModule());

                        // Chuyển đổi đối tượng TaiKhoan thành JSON
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

//    private String handleThongKeDoanhThu(String[] parts) {
//        try {
//            LocalDate startDate = LocalDate.parse(parts[1]);
//            LocalDate endDate = LocalDate.parse(parts[2]);
//
//            // Lấy dữ liệu thống kê
//            List<HoaDon> dsHoaDon = HoaDonDAO.getInstance().getDanhSachHoaDonTheoYeuCau(startDate, endDate);
//            List<ChiTietHoaDon> dsCTHD = ChiTietHoaDonDAO.getInstance().getDanhSachChiTietHoaDon();
//            HashMap<LocalDate, Double> dsDoanhThu = loadDataThongKeDoanhThu(dsHoaDon, dsCTHD);
//
//            // Chuyển dữ liệu thành JSON
//            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.registerModule(new JavaTimeModule());
//            return objectMapper.writeValueAsString(dsDoanhThu);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "ERROR";
//        }
//    }

    private HashMap<LocalDate, Double> loadDataThongKeDoanhThu(List<HoaDon> dsHD, List<ChiTietHoaDon> dsCTHD) {
        HashMap<LocalDate, Double> dsDoanhThu = new HashMap<>();
        for (HoaDon hoaDon : dsHD) {
            LocalDate ngayTao = hoaDon.getNgayTao();
            double tongTien = 0.0;
            for (ChiTietHoaDon chiTietHoaDon : dsCTHD) {
                if (chiTietHoaDon.getHoaDon().getMaHoaDon().equals(hoaDon.getMaHoaDon())) {
                    tongTien += chiTietHoaDon.getTongTien();
                }
            }
            dsDoanhThu.put(ngayTao, dsDoanhThu.getOrDefault(ngayTao, 0.0) + tongTien);
        }
        return dsDoanhThu;
    }

    private String handleThongKeSanPham(String[] parts) {
        try {
            LocalDate startDate = LocalDate.parse(parts[1]);
            LocalDate endDate = LocalDate.parse(parts[2]);

            // Lấy dữ liệu thống kê sản phẩm
            List<HoaDon> dsHoaDon = HoaDonDAO.getInstance().getDanhSachHoaDonTheoYeuCau(startDate, endDate);
            HashMap<LocalDate, Integer> dsSPTheoNgay = loadDataThongKeSPTheoNgay(dsHoaDon);

            // Chuyển dữ liệu thành JSON
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
        if (parts.length < 2) return "INVALID_SEARCH";
        String maSanPham = parts[1];

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
        if (parts.length < 2) return "INVALID_PAGE";
        int trang = Integer.parseInt(parts[1]);

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
            boolean result = qlhtt.DAO.KhachHangDAO.getInstance().capNhatKhachHang(kh);
            return result ? "OK" : "FAIL";
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    private String handleTimKhachHang(String soDienThoai) {
        try {
            KhachHang kh = qlhtt.DAO.KhachHangDAO.getInstance().getKhachHangBangSoDienThoai(soDienThoai);
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
        String maChietKhau = "";
        if(parts.length > 1) {
            maChietKhau = parts[1];
        }
        ChietKhauController chietKhauController = new ChietKhauController();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            ChietKhau chietKhau = chietKhauController.getChietKhau(maChietKhau);
            if (chietKhau != null) {
                // Chuyển đổi đối tượng ChietKhau thành JSON
                return objectMapper.writeValueAsString(chietKhau);
            } else {
                return "NOT_FOUND";
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String handleUpdateSoLuongCK(String[] parts) {
        String payload = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        // Chuyển đổi JSON thành đối tượng ChiTietPhieuNhap
        // parts[1] chứa JSON của ChiTietPhieuNhap
        ChietKhau chietKhau;
        try {
            chietKhau = objectMapper.readValue(payload, ChietKhau.class);
            ChietKhauController chietKhauController = new ChietKhauController();
            chietKhauController.capNhatSoLuongChietKhau(chietKhau);
            return "SUCCESS";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String handleUpdateDTL(String[] parts) {
        if(parts.length > 2){
            String maKhachHang = parts[1];
            //System.out.println(parts[2]);
            int diemTichLuy = Integer.parseInt(parts[2]);
            KhachHangController khachHangController = new KhachHangController();
            khachHangController.capNhatDiemTichLuy(maKhachHang, diemTichLuy);
        }
        else{
            return "FAIL";
        }
        return "SUCCESS";
    }

    private String handleUpdateHD(String[] parts) {
        String payload = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        // Chuyển đổi JSON thành đối tượng ChiTietPhieuNhap
        // parts[1] chứa JSON của ChiTietPhieuNhap
        HoaDon hoaDon;
        try {
            hoaDon = objectMapper.readValue(payload, HoaDon.class);
            HoaDonController hoaDonController = new HoaDonController();
            if(hoaDon.getChietKhau().getMaChietKhau().equals("")){
                hoaDon.setChietKhau(null);
            }

            if(hoaDon.getKhachHang().getMaKhachHang() == null){
                hoaDon.setKhachHang(null);
            }
            hoaDonController.capNhatHoaDon(hoaDon);
            return "SUCCESS";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String handleCreateCTHD(String[] parts) {
        String payload = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        // Chuyển đổi JSON thành đối tượng ChiTietPhieuNhap
        // parts[1] chứa JSON của ChiTietPhieuNhap
        ChiTietHoaDon chiTietHoaDon;
        try {
            chiTietHoaDon = objectMapper.readValue(payload, ChiTietHoaDon.class);
            ChiTietHoaDonController chiTietHoaDonController = new ChiTietHoaDonController();
            chiTietHoaDonController.taoChiTietHoaDon(chiTietHoaDon);
            return "SUCCESS";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String handleDeleteCTHD(String[] parts) {
        String maHoaDon = parts[1];
        ChiTietHoaDonController chiTietHoaDonController = new ChiTietHoaDonController();
        chiTietHoaDonController.xoaChiTietHoaDon(maHoaDon);
        return "SUCCESS";
    }

    private String handleCreateHoaDon(String[] parts) {
        String payload = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        // Chuyển đổi JSON thành đối tượng ChiTietPhieuNhap
        // parts[1] chứa JSON của ChiTietPhieuNhap
        HoaDon hoaDon;
        try {
            hoaDon = objectMapper.readValue(payload, HoaDon.class);
            HoaDonController hoaDonController = new HoaDonController();
            if(hoaDon.getChietKhau().getMaChietKhau().equals("")) {
                hoaDon.setChietKhau(null);
            }

            if(hoaDon.getKhachHang().getMaKhachHang() == null) {
                hoaDon.setKhachHang(null);
            }
            hoaDonController.taoHoaDon(hoaDon);
            return "SUCCESS";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String handleGetHoaDonMoiNhat(String[] parts) {
        HoaDonController hoaDonController = new HoaDonController();
        HoaDon hoaDon = hoaDonController.getHoaDonMoiNhat();
        try {

            // Tạo ObjectMapper và đăng ký JavaTimeModule
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            // Chuyển đổi đối tượng ChiTietPhieuNhap thành JSON
            return objectMapper.writeValueAsString(hoaDon);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String handleGetListChiTietHoaDon(String[] parts) {
        String maHoaDon = parts[1];
        ChiTietHoaDonController chiTietHoaDonController = new ChiTietHoaDonController();
        List<ChiTietHoaDon> chiTietHoaDons = chiTietHoaDonController.getDsChiTietHoaDonTheoMaHoaDon(maHoaDon);
        if (chiTietHoaDons != null) {
            try {
                // Tạo ObjectMapper và đăng ký JavaTimeModule
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                // Chuyển đổi đối tượng ChiTietPhieuNhap thành JSON
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
        String soDienThoai = "";
        if(parts.length > 1) {
            soDienThoai = parts[1];
        }
        KhachHangController khachHangController = new KhachHangController();
        // Lấy số lượng sản phẩm từ controller
        KhachHang khachHang = khachHangController.getKhachHangBySoDienThoai(soDienThoai);
        if (khachHang != null) {
            try {
                // Tạo ObjectMapper và đăng ký JavaTimeModule
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                // Chuyển đổi đối tượng ChiTietPhieuNhap thành JSON
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
            String payload = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            // Chuyển đổi JSON thành đối tượng ChiTietPhieuNhap
            // parts[1] chứa JSON của ChiTietPhieuNhap
            ChiTietPhieuNhap chiTietPhieuNhap = objectMapper.readValue(payload, ChiTietPhieuNhap.class);
            System.out.println(chiTietPhieuNhap);
            SanPhamController sanPhamController = new SanPhamController();
            if(sanPhamController.capNhatGiaTienCuaSanPham(chiTietPhieuNhap)){
                return "SUCCESS";
            } else {
                return "FAIL";
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String handleGetChiTietPhieuNhapHienTai(String[] parts) {
        String maSanPham = parts[1];
        ChiTietPhieuNhapController chiTietPhieuNhapController = new ChiTietPhieuNhapController();
        // Lấy số lượng sản phẩm từ controller
        ChiTietPhieuNhap chiTietPhieuNhap = chiTietPhieuNhapController.getChiTietPhieuNhapHienTaiDangApDung(maSanPham);
        if (chiTietPhieuNhap != null) {
            try {
                // Tạo ObjectMapper và đăng ký JavaTimeModule
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());

                // Chuyển đổi đối tượng ChiTietPhieuNhap thành JSON
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
        String maSanPham = parts[1];

        SanPhamController sanPhamController = new SanPhamController();
        // Lấy sản phẩm từ controller
        SanPham sanPham = sanPhamController.getSanPhamById(maSanPham);

        if (sanPham != null) {
            try {
                // Tạo ObjectMapper và đăng ký JavaTimeModule
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());

                // Chuyển đổi đối tượng SanPham thành JSON
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
        if (parts.length < 2) {
            return "ERROR Thiếu mã hóa đơn";
        }

        String maHD = parts[1];
        String pdfPath = "src/main/resources/HoaDon/" + maHD + ".pdf";

        try {
            // Kết nối đến cơ sở dữ liệu

            ConnectDB connectDB = ConnectDB.getInstance();
            connectDB.connect();
            Connection connection = connectDB.getConnection();

            // Biên dịch báo cáo Jasper
            JasperReport jasperReport = JasperCompileManager.compileReport("src/main/resources/Fxml/MauHoaDon.jrxml");

            // Đặt tham số cho báo cáo
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("ReportTitle", "Hóa Đơn");
            parameters.put("maHoaDonParam", maHD);  // Truyền mã hóa đơn vào báo cáo

            // Điền dữ liệu vào báo cáo
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

            // Xuất báo cáo ra file PDF
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
}