package qlhtt.Controllers.NhanVien;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import qlhtt.Controllers.LoginController;
import qlhtt.Controllers.ThanhToanQRControlller;
import qlhtt.Entity.TaiKhoan;
import qlhtt.Enum.LuaChonNhanVien;
import qlhtt.Models.Model;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuNhanVienController implements Initializable {
    public JFXButton btn_TrangTongQuat;
    public JFXButton btn_BanHang;
    public JFXButton btn_DanhSachHoaDon;
    public JFXButton btn_CapNhatThongTinKhachHang;
    public JFXButton btn_ThongKeDoanhThu;
    public JFXButton btn_ThongKeHoaDon;
    public JFXButton btn_ThongKePhieuNhap;
    public JFXButton btn_CapNhatThongTin;
    public JFXButton btn_DoiMatKhau;
    public JFXButton btn_DangXuat;
    public JFXButton btn_CapNhatThongTinNhaCungCap;
    public JFXButton btn_QuanLySanPham;
    public JFXButton btn_NhapHang;
    public JFXButton btn_DanhSachPhieuNhap;


    @FXML
    public Label tenMenu;
    @FXML
    public Label mailMenu;
    @FXML
    public ImageView avatarMenu;

    TaiKhoan taiKhoan = LoginController.getTaiKhoan();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tenMenu.setText("Hi, " + taiKhoan.getNhanVien().getTenNhanVien());
        mailMenu.setText(taiKhoan.getNhanVien().getEmail());
        Image avatarImage = new Image(getClass().getResourceAsStream(taiKhoan.getNhanVien().getDuongDanAnh()));
        if (avatarImage.isError()) {
            System.out.println("Error loading image: " + avatarImage.getException());
        }
        avatarMenu.setImage(avatarImage);
        addListeners();
    }

    private void addListeners() {
        btn_TrangTongQuat.setOnAction(event -> onTrangTongQuat());
        btn_BanHang.setOnAction(event -> onBanHang());
        btn_DanhSachHoaDon.setOnAction(event -> onDanhSachHoaDon());
        btn_CapNhatThongTinKhachHang.setOnAction(event -> onCapNhatThongTinKhachHang());
        btn_CapNhatThongTinNhaCungCap.setOnAction(event -> onCapNhatThongTinNhaCungCap());
        btn_ThongKeDoanhThu.setOnAction(event -> onThongKeDoanhThu());
        btn_ThongKeHoaDon.setOnAction(event -> onThongKeHoaDon());
        btn_ThongKePhieuNhap.setOnAction(event -> onThongKePhieuNhap());
        btn_CapNhatThongTin.setOnAction(event -> onCapNhatThongTin());
        btn_DoiMatKhau.setOnAction(event -> onDoiMatKhau());
        btn_DangXuat.setOnAction(event -> onDangXuat());
        btn_QuanLySanPham.setOnAction(event -> onQuanLySanPham());
        btn_NhapHang.setOnAction(event -> onNhapHang());
        btn_DanhSachPhieuNhap.setOnAction(event -> onDanhSachPhieuNhap());
    }

    private void onTrangTongQuat() {
        Model.getInstance().getViewFactory().layLuaChonNhanVien().set(LuaChonNhanVien.TRANGTONGQUAT);
    }

    private void onBanHang() {
        Model.getInstance().getViewFactory().layLuaChonNhanVien().set(LuaChonNhanVien.BANHANG);
    }

    private void onDanhSachHoaDon() {
        Model.getInstance().getViewFactory().layLuaChonNhanVien().set(LuaChonNhanVien.DANHSACHHOADON);
    }

    private void onCapNhatThongTinKhachHang() {
        Model.getInstance().getViewFactory().layLuaChonNhanVien().set(LuaChonNhanVien.CAPNHATTHONGTINKHACHHANG);
    }

    private void onCapNhatThongTinNhaCungCap() {
        Model.getInstance().getViewFactory().layLuaChonNhanVien().set(LuaChonNhanVien.CAPNHATTHONGTINNHACUNGCAP);
    }

    private void onThongKeDoanhThu() {
        Model.getInstance().getViewFactory().layLuaChonNhanVien().set(LuaChonNhanVien.THONGKEDOANHTHU);
    }

    private void onThongKeHoaDon() {
        Model.getInstance().getViewFactory().layLuaChonNhanVien().set(LuaChonNhanVien.THONGKEHOADON);
    }

    private void onThongKePhieuNhap() {
        Model.getInstance().getViewFactory().layLuaChonNhanVien().set(LuaChonNhanVien.THONGKEPHIEUNHAP);
    }

    private void onCapNhatThongTin() {
        Model.getInstance().getViewFactory().layLuaChonNhanVien().set(LuaChonNhanVien.CAPNHATTHONGTINNGUOIDUNG);
    }

    private void onDoiMatKhau() {
        Model.getInstance().getViewFactory().layLuaChonNhanVien().set(LuaChonNhanVien.DOI_MAT_KHAU);
    }

    private void onQuanLySanPham() {
        Model.getInstance().getViewFactory().layLuaChonNhanVien().set(LuaChonNhanVien.QUANLYSANPHAM);
    }

    private void onNhapHang() {
        Model.getInstance().getViewFactory().layLuaChonNhanVien().set(LuaChonNhanVien.NHAPHANG);
    }

    private void onDanhSachPhieuNhap() {
        Model.getInstance().getViewFactory().layLuaChonNhanVien().set(LuaChonNhanVien.DANHSACHPHIEUNHAP);
    }

    private void onDangXuat() {
        Model.getInstance().getViewFactory().layLuaChonNhanVien().set(LuaChonNhanVien.DANG_XUAT);
    }


}
