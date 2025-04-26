package qlhtt.Views;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import qlhtt.Controllers.NguoiQuanLy.NguoiQuanLyController;
import qlhtt.Controllers.NhanVien.NhanVienController;
import qlhtt.Controllers.ThanhToanQRControlller;
import qlhtt.Enum.LuaChonNguoiQuanLy;
import qlhtt.Enum.LuaChonNhanVien;

import java.io.IOException;

import qlhtt.Models.Model;

public class ViewFactory {
    private AccountType loginAccountType;

    // NguoiQuanLy Views
    private final ObjectProperty<LuaChonNguoiQuanLy> luaChonNguoiQuanLy;
    private AnchorPane trangTongQuat;
    private AnchorPane themNhanVien;
    private AnchorPane capNhatTrangThaiHoatDongNhanVien;
    private AnchorPane taoChietKhau;
    private AnchorPane capNhatTrangThaiChietKhau;
    private AnchorPane thongKeSanPham;
    private AnchorPane thongKeDoanhThu;
    private AnchorPane thongKeHoaDon;
    private AnchorPane thongKePhieuNhap;
    private AnchorPane capNhatThongTinNguoiQuanLy;
    private AnchorPane doiMatKhauNguoiQuanLy;
    private AnchorPane dangXuat;

    // NhanVien Views
    private final ObjectProperty<LuaChonNhanVien> luaChonNhanVien;
    private AnchorPane trangTongQuatNhanVien;
    private AnchorPane banHang;
    private AnchorPane danhSachHoaDon;
    private AnchorPane capNhatThongTinKhachHang;
    private AnchorPane capNhatThongTinNhaCungCap;
    private AnchorPane quanLySanPham;
    private AnchorPane nhapHang;
    private AnchorPane danhSachPhieuNhap;
    private AnchorPane thongKeDoanhThu_NhanVien;
    private AnchorPane thongKeHoaDon_NhanVien;
    private AnchorPane thongKePhieuNhap_NhanVien;
    private AnchorPane capNhatThongTinNhanVien;
    private AnchorPane doiMatKhauNhanVien;
    private AnchorPane dangXuatNhanVien;

    public ViewFactory(){
        this.loginAccountType = AccountType.NGUOIQUANLY;
        this.luaChonNguoiQuanLy = new SimpleObjectProperty<>();
        this.luaChonNhanVien = new SimpleObjectProperty<>();
    }

    public AccountType getLoginAccountType() {
        return loginAccountType;
    }

    public void setLoginAccountType(AccountType loginAccountType) {
        this.loginAccountType = loginAccountType;
    }

    public void closeStage(Stage stage) {
        stage.close();
    }

    public void showLoginWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
        createStage(loader);
    }

    private Scene createScene(FXMLLoader loader) {
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scene;
    }

    private void createStageNoResizable(FXMLLoader loader) {
        Scene scene = createScene(loader);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Quản lý tiệm thuốc tây");
        stage.setResizable(false);
        stage.show();
    }

    private void createNewStage(FXMLLoader loader, Stage primaryStage) {
        Scene scene = createScene(loader);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Quản lý tiệm thuốc tây");
        stage.setResizable(false);
        // Căn giữa cửa sổ trên màn hình
        stage.initOwner(primaryStage); // primaryStage là cửa sổ chính của ứng dụng
        stage.setOnShown(e -> stage.centerOnScreen());
        stage.show();
    }

    // NguoiQuanLy
    public ObjectProperty<LuaChonNguoiQuanLy> layLuaChonNguoiQuanLy() {
        return luaChonNguoiQuanLy;
    }

    private void createStage(FXMLLoader loader) {
        Scene scene = createScene(loader);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Quản lý tiệm thuốc tây");
        //stage.setResizable(false);
        stage.show();
    }

    public AnchorPane hienTrangTongQuatCuaNguoiQuanLy() {
        if(trangTongQuat == null){
            try{
                trangTongQuat = new FXMLLoader(getClass().getResource("/Fxml/NguoiQuanLy/TrangTongQuat.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return trangTongQuat;
    }

    public AnchorPane hienTrangThemNhanVien(){
        if(themNhanVien == null){
            try{
                themNhanVien = new FXMLLoader(getClass().getResource("/Fxml/NguoiQuanLy/ThemNhanVien.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return themNhanVien;
    }

    public AnchorPane hienTrangCapNhatTrangThaiHoatDongNhanVien(){
        if(capNhatTrangThaiHoatDongNhanVien == null){
            try{
                capNhatTrangThaiHoatDongNhanVien = new FXMLLoader(getClass().getResource("/Fxml/NguoiQuanLy/CapNhatTrangThaiHoatDong.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return capNhatTrangThaiHoatDongNhanVien;
    }

    public AnchorPane hienTrangTaoChietKhau(){
        if(taoChietKhau == null){
            try{
                taoChietKhau = new FXMLLoader(getClass().getResource("/Fxml/NguoiQuanLy/TaoChietKhau.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return taoChietKhau;
    }

    public AnchorPane hienTrangCapNhatTrangThaiChietKhau(){
        if(capNhatTrangThaiChietKhau == null){
            try{
                capNhatTrangThaiChietKhau = new FXMLLoader(getClass().getResource("/Fxml/NguoiQuanLy/CapNhatTrangThaiChietKhau.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return capNhatTrangThaiChietKhau;
    }

    public AnchorPane hienTrangThongKeSanPham(){
        if(thongKeSanPham == null){
            try{
                thongKeSanPham = new FXMLLoader(getClass().getResource("/Fxml/NguoiQuanLy/ThongKeSanPham.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return thongKeSanPham;
    }

    public AnchorPane hienTrangThongKeDoanhThu(){
        if(thongKeDoanhThu == null){
            try{
                thongKeDoanhThu = new FXMLLoader(getClass().getResource("/Fxml/NguoiQuanLy/ThongKeDoanhThu.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return thongKeDoanhThu;
    }

    public AnchorPane hienTrangThongKeHoaDon(){
        if(thongKeHoaDon == null){
            try{
                thongKeHoaDon = new FXMLLoader(getClass().getResource("/Fxml/NguoiQuanLy/ThongKeHoaDon.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return thongKeHoaDon;
    }

    public AnchorPane hienTrangThongKePhieuNhap(){
        if(thongKePhieuNhap == null){
            try{
                thongKePhieuNhap = new FXMLLoader(getClass().getResource("/Fxml/NguoiQuanLy/ThongKePhieuNhap.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return thongKePhieuNhap;
    }

    public AnchorPane hienTrangCapNhatThongTinNguoiQuanLy(){
        if(capNhatThongTinNguoiQuanLy == null){
            try{
                capNhatThongTinNguoiQuanLy = new FXMLLoader(getClass().getResource("/Fxml/NhanVien/ChinhSuaThongTinCaNhan.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return capNhatThongTinNguoiQuanLy;
    }

    public AnchorPane hienTrangDoiMatKhauNguoiQuanLy(){
        if(doiMatKhauNguoiQuanLy == null){
            try{
                doiMatKhauNguoiQuanLy = new FXMLLoader(getClass().getResource("/Fxml/NhanVien/DoiMatKhau.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return doiMatKhauNguoiQuanLy;
    }

//    public AnchorPane hienTrangDangXuat(){
//        if(dangXuat == null){
//            try{
//                dangXuat = new FXMLLoader(getClass().getResource("/Fxml/NguoiQuanLy/DangXuat.fxml")).load();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return dangXuat;
//    }

    public void hienTrangNguoiQuanLy() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/NguoiQuanLy/NguoiQuanLy.fxml"));
        NguoiQuanLyController nguoiQuanLyController = new NguoiQuanLyController();
        loader.setController(nguoiQuanLyController);
        createStage(loader);
    }

    // NhanVien
    public ObjectProperty<LuaChonNhanVien> layLuaChonNhanVien() {
        return luaChonNhanVien;
    }

    public AnchorPane hienTrangTongQuatCuaNhanVien() {
        if(trangTongQuatNhanVien == null){
            try{
                trangTongQuatNhanVien = new FXMLLoader(getClass().getResource("/Fxml/NhanVien/TrangTongQuat.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return trangTongQuatNhanVien;
    }

    public AnchorPane hienTrangBanHang(){
        if(banHang == null){
            try{
                banHang = new FXMLLoader(getClass().getResource("/Fxml/NhanVien/BanHang.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return banHang;
    }

    public AnchorPane hienTrangDanhSachHoaDon(){
        if(danhSachHoaDon == null){
            try{
                danhSachHoaDon = new FXMLLoader(getClass().getResource("/Fxml/NhanVien/XemHoaDon.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return danhSachHoaDon;
    }

    public AnchorPane hienTrangCapNhatThongTinKhachHang(){
        if(capNhatThongTinKhachHang == null){
            try{
                capNhatThongTinKhachHang = new FXMLLoader(getClass().getResource("/Fxml/NhanVien/CapNhatThongTinKhachHang.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return capNhatThongTinKhachHang;
    }

    public AnchorPane hienTrangCapNhatThongTinNhaCungCap(){
        if(capNhatThongTinNhaCungCap == null){
            try{
                capNhatThongTinNhaCungCap = new FXMLLoader(getClass().getResource("/Fxml/NhanVien/ChinhSuaNhaCungCap.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return capNhatThongTinNhaCungCap;
    }

    public AnchorPane hienTrangQuanLySanPham(){
        if(quanLySanPham == null){
            try{
                quanLySanPham = new FXMLLoader(getClass().getResource("/Fxml/NhanVien/DanhSachSanPham.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return quanLySanPham;
    }

    public AnchorPane hienTrangNhapHang(){
        if(nhapHang == null){
            try{
                nhapHang = new FXMLLoader(getClass().getResource("/Fxml/NhanVien/TaoPhieuNhap.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return nhapHang;
    }

    public AnchorPane hienTrangDanhSachPhieuNhap(){
        if(danhSachPhieuNhap == null){
            try{
                danhSachPhieuNhap = new FXMLLoader(getClass().getResource("/Fxml/NguoiQuanLy/XemDanhSachPhieuNhap.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return danhSachPhieuNhap;
    }

    public AnchorPane hienTrangThongKeDoanhThu_NhanVien(){
        if(thongKeDoanhThu_NhanVien == null){
            try{
                thongKeDoanhThu_NhanVien = new FXMLLoader(getClass().getResource("/Fxml/NhanVien/ThongKeDoanhThu.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return thongKeDoanhThu_NhanVien;
    }

    public AnchorPane hienTrangThongKeHoaDon_NhanVien(){
        if(thongKeHoaDon_NhanVien == null){
            try{
                thongKeHoaDon_NhanVien = new FXMLLoader(getClass().getResource("/Fxml/NhanVien/ThongKeHoaDon.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return thongKeHoaDon_NhanVien;
    }

    public AnchorPane hienTrangThongKePhieuNhap_NhanVien(){
        if(thongKePhieuNhap_NhanVien == null){
            try{
                thongKePhieuNhap_NhanVien = new FXMLLoader(getClass().getResource("/Fxml/NhanVien/ThongKePhieuNhap.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return thongKePhieuNhap_NhanVien;
    }

    public AnchorPane hienTrangCapNhatThongTinNhanVien(){
        if(capNhatThongTinNhanVien == null){
            try{
                capNhatThongTinNhanVien = new FXMLLoader(getClass().getResource("/Fxml/NhanVien/ChinhSuaThongTinCaNhan.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return capNhatThongTinNhanVien;
    }

    public AnchorPane hienTrangDoiMatKhauNhanVien(){
        if(doiMatKhauNhanVien == null){
            try{
                doiMatKhauNhanVien = new FXMLLoader(getClass().getResource("/Fxml/NhanVien/DoiMatKhau.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return doiMatKhauNhanVien;
    }

    public void hienTrangNhanVien() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/NhanVien/NhanVien.fxml"));
        NhanVienController nhanVienController = new NhanVienController();
        loader.setController(nhanVienController);
        createStage(loader);
    }

    public void hienTrangThemNhanVien(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/NguoiQuanLy/ThemNhanVien.fxml"));
        createNewStage(loader, primaryStage);
    }

    public void hienTrangThemSanPham(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/NhanVien/ThemSanPham.fxml"));
        createNewStage(loader, primaryStage);
    }


    public void hienTrangTraCuuSanPham(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/NhanVien/TraCuuSanPham.fxml"));
        createNewStage(loader, primaryStage);
    }
    public void hienTrangCapNhatTrangThaiHoatDong(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/NguoiQuanLy/CapNhatTrangThaiHoatDong.fxml"));
        createNewStage(loader, primaryStage);
    }

    public void showStage(String dsSanPham, Parent root) {
        Stage stage = new Stage();
        stage.setTitle(dsSanPham);
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void hienMaQR(String giaTriThanhToan){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/ThanhToanQR.fxml"));
            Parent root = loader.load();

            // Lấy controller và truyền dữ liệu sản phẩm vào
            ThanhToanQRControlller controller = loader.getController();
            controller.hienThiQR(giaTriThanhToan);

            // Hiển thị trang thông tin sản phẩm
            Stage stage = new Stage();
            stage.setTitle("QR Thanh toán");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void hienThiTrangHoaDon(String gmail){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/HoaDon.fxml"));
            Parent root = loader.load();

            // Lấy controller và truyền dữ liệu sản phẩm vào
            // HoaDonController controller = loader.getController();
//            XacNhanHoaDonController controller = loader.getController();
//            controller.guiHoaDon(gmail);
            // controller.guiHoaDon(email);

            // Hiển thị trang thông tin sản phẩm
            Stage stage = new Stage();
            stage.setTitle("Hóa đơn");
            stage.setScene(new Scene(root));
            stage.setOnCloseRequest(event->{
                Model.getInstance().getBanHangController().datLai();
                Model.getInstance().setHoaDon(null);
                Model.getInstance().getBanHangController().loadData(Model.getInstance().getHoaDon());
            });
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void hienTrangBanHangTuDanhSachHoaDon() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/NhanVien/BanHang.fxml"));
            Parent root = loader.load();

            // Lấy controller và truyền dữ liệu sản phẩm vào
            //NhanVienController controller = loader.getController();
            //controller.hienThiHoaDon(hoaDon);
            NhanVienController controller = loader.getController();
            BorderPane borderPane = controller.getNhanVienBorderPane();
            borderPane.setCenter(Model.getInstance().getViewFactory().hienTrangBanHang());

            // Hiển thị trang thông tin sản phẩm
            Stage stage = new Stage();
            stage.setTitle("Bán hàng");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void hienThiTrangDatLaiMatKhau(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/DatLaiMatKhau.fxml"));
            Parent root = loader.load();

            // Hiển thị trang thông tin sản phẩm
            Stage stage = new Stage();
            stage.setTitle("Đặt lại mật khẩu");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void hienThiTrangXacNhanOTP() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/XacNhanOTP.fxml"));
            Parent root = loader.load();

            // Hiển thị trang thông tin sản phẩm
            Stage stage = new Stage();
            stage.setTitle("Xác nhận OTP");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void hienThiTrangCapNhatMatKhau() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/CapNhatMatKhauMoi.fxml"));
            Parent root = loader.load();

            // Hiển thị trang thông tin sản phẩm
            Stage stage = new Stage();
            stage.setTitle("Đổi mật khẩu");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void lamMoiCacGiaoDien() {
        if(trangTongQuat != null) {
            trangTongQuat = null;
        }
        if(themNhanVien != null) {
            themNhanVien = null;
        }
        if(capNhatTrangThaiHoatDongNhanVien != null) {
            capNhatTrangThaiHoatDongNhanVien = null;
        }
        if(taoChietKhau != null) {
            taoChietKhau = null;
        }
        if(capNhatTrangThaiChietKhau != null) {
            capNhatTrangThaiChietKhau = null;
        }
        if(thongKeSanPham != null) {
            thongKeSanPham = null;
        }
        if(thongKeDoanhThu != null) {
            thongKeDoanhThu = null;
        }
        if(thongKeHoaDon != null) {
            thongKeHoaDon = null;
        }
        if(thongKePhieuNhap != null) {
            thongKePhieuNhap = null;
        }
        if(capNhatThongTinNguoiQuanLy != null) {
            capNhatThongTinNguoiQuanLy = null;
        }
        if(doiMatKhauNguoiQuanLy != null) {
            doiMatKhauNguoiQuanLy = null;
        }
        if(trangTongQuatNhanVien != null) {
            trangTongQuatNhanVien = null;
        }
        if(banHang != null) {
            banHang = null;
        }
        if(danhSachHoaDon != null) {
            danhSachHoaDon = null;
        }
        if(capNhatThongTinKhachHang != null) {
            capNhatThongTinKhachHang = null;
        }
        if(capNhatThongTinNhaCungCap != null) {
            capNhatThongTinNhaCungCap = null;
        }
        if(quanLySanPham != null) {
            quanLySanPham = null;
        }
        if(nhapHang != null) {
            nhapHang = null;
        }
        if(danhSachPhieuNhap
                != null) {
            danhSachPhieuNhap = null;
        }
        if(thongKeDoanhThu_NhanVien != null) {
            thongKeDoanhThu_NhanVien = null;
        }
        if(thongKeHoaDon_NhanVien != null) {
            thongKeHoaDon_NhanVien = null;
        }
        if(thongKePhieuNhap_NhanVien != null) {
            thongKePhieuNhap_NhanVien = null;
        }
        if(capNhatThongTinNhanVien != null) {
            capNhatThongTinNhanVien = null;
        }
        if(doiMatKhauNhanVien != null) {
            doiMatKhauNhanVien = null;
        }
    }
    public void hienThiTrangThemKhachHang(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/NhanVien/ThemKhachHang.fxml"));
        createNewStage(loader, primaryStage);
    }

    public void hienThiTrangLocHoaDonTheoSanPham(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/NhanVien/LocHoaDon.fxml"));
        createNewStage(loader, primaryStage);
    }
    public Stage hienThiThemNhaCungCap(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/NhanVien/ThemNhaCungCap.fxml"));
        createNewStage(loader, primaryStage);
        return primaryStage;
    }
}