package qlhtt.Models;

import qlhtt.Controllers.NguoiQuanLy.DanhSachPhieuNhapController;
import qlhtt.Controllers.NhanVien.*;
import qlhtt.Entity.*;
import qlhtt.Entity.SanPham;
import qlhtt.Entity.TaiKhoan;
import qlhtt.Views.ViewFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Model {
    private static Model model;
    private final ViewFactory viewFactory;
    private DanhSachSanPhamController danhSachSanPhamController;
    private DanhSachPhieuNhapController danhSachPhieuNhapController;
    private TaoPhieuNhapController taoPhieuNhapController;

    private ThongKeDoanhThuController thongKeDoanhThuController;
    private ThongKeHoaDonController thongKeHoaDonController;

    private TaiKhoan taiKhoan;
    private HoaDon hoaDon;
    private KhachHang khachHang;
    private List<ChiTietHoaDon> chiTietHoaDonList = new ArrayList<>();
    //private HoaDonController hoaDonController;
    private NhanVienController nhanVienController;
    private boolean trangThaiThanhToan = false;
    private BanHangController banHangController;
    private String OTP;
    private String email;
    private TaiKhoan taiKhoanCanDoiMatKhau;
    private XemHoaDonController xemHoaDonController;
    private int diemTichLuy;

    public ThongKeDoanhThuController getThongKeDoanhThuController() {
        return thongKeDoanhThuController;
    }

    public void setThongKeDoanhThuController(ThongKeDoanhThuController thongKeDoanhThuController) {
        this.thongKeDoanhThuController = thongKeDoanhThuController;
    }

    public ThongKeHoaDonController getThongKeHoaDonController() {
        return thongKeHoaDonController;
    }

    public void setThongKeHoaDonController(ThongKeHoaDonController thongKeHoaDonController) {
        this.thongKeHoaDonController = thongKeHoaDonController;
    }

    public BanHangController getBanHangController() {
        return banHangController;
    }

    public void setBanHangController(BanHangController banHangController) {
        this.banHangController = banHangController;
    }

    public List<ChiTietHoaDon> getChiTietHoaDonList() {
        return chiTietHoaDonList;
    }

    public void setChiTietHoaDonList(List<ChiTietHoaDon> chiTietHoaDonList) {
        this.chiTietHoaDonList = chiTietHoaDonList;
    }

    public void setTrangThaiThanhToan(boolean trangThaiThanhToan) {
        this.trangThaiThanhToan = trangThaiThanhToan;
    }

    public void setTaiKhoan(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    public TaiKhoan getTaiKhoan() {
        return taiKhoan;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }
    public KhachHang getKhachHang() {
        return khachHang==null?new KhachHang():khachHang;
    }
    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }
    public HoaDon getHoaDon() {
        return hoaDon;
    }
    private Model(){
        this.viewFactory = new ViewFactory();
    }

    public static synchronized Model getInstance(){
        if(model == null){
            model = new Model();
        }
        return model;
    }

    public qlhtt.Views.ViewFactory getViewFactory(){
        return viewFactory;
    }

    public void setDanhSachSanPhamController(DanhSachSanPhamController controller){
        this.danhSachSanPhamController = controller;
    }

    public void setDanhSachPhieuNhapController(DanhSachPhieuNhapController controller){
        this.danhSachPhieuNhapController = controller;
    }

    public void setNhanVienController(NhanVienController controller){
        this.nhanVienController = controller;
    }

    public void lamMoiDanhSachSanPham(){
        danhSachSanPhamController.lamMoiBang();
    }

    public void hienThiDuLieuSauKhiLoc(List<SanPham> dsSanPham){
        danhSachSanPhamController.hienThiDuLieuSauKhiLoc(dsSanPham);
    }

    public void hienHoaDonSauKhiLoc(List<HoaDon> dsHoaDon){
        xemHoaDonController.hienHoaDonSauKhiLoc(dsHoaDon);
    }

    public void setXemHoaDonController(XemHoaDonController controller){
        this.xemHoaDonController = controller;
    }

    public void hienThiDanhSachHoaDonSangBanHang(HoaDon hoaDon){
        nhanVienController.getNhanVienBorderPane().setCenter(viewFactory.hienTrangBanHang());
        banHangController.loadData(hoaDon);
    }


    public void setOTP(String otp) {
        this.OTP = otp;
    }

    public String getOTP() {
        return OTP;
    }

    public static Message prepareMessage(Session session, String myEmail, String recipientEmail, String subject, String content) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            message.setSubject(subject);
            message.setText(content);
            return message;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String taoOTP(){
        int otp = (int) (Math.random() * 1000000);
        return String.valueOf(otp);
    }

    public void guiOTP(String gmail, String OTP) throws IOException {
        if(gmail != null){
            Properties properties = new Properties();

            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");

            String myEmail = "duythvo2004@gmail.com";
            String recipientEmail = gmail;
            String password = "wdij irrl uetm nrbg";

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(myEmail, password);
                }
            });
            Message message = prepareMessage(session, myEmail, recipientEmail, "Thông báo đặt lại mật khẩu", "Mã OTP của bạn là: " + OTP);

            try {
                Transport.send(message);
                System.out.println("Message sent successfully");

            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setTaiKhoanCanDoiMatKhau(TaiKhoan taiKhoan){
        this.taiKhoanCanDoiMatKhau = taiKhoan;
    }

    public TaiKhoan getTaiKhoanCanDoiMatKhau(){
        return taiKhoanCanDoiMatKhau;
    }

    public void setTaoPhieuNhapController(TaoPhieuNhapController taoPhieuNhapController) {
        this.taoPhieuNhapController = taoPhieuNhapController;
    }

    public TaoPhieuNhapController getTaoPhieuNhapController() {
        return taoPhieuNhapController;
    }

    public int getDiemTichLuy() {
        return diemTichLuy;
    }

    public void setDiemTichLuy(int diemTichLuy) {
        this.diemTichLuy = diemTichLuy;
    }
}
