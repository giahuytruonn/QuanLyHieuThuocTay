package qlhtt.Controllers.NguoiQuanLy;


import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import qlhtt.Controllers.LoginController;
import qlhtt.Entity.TaiKhoan;
import qlhtt.Enum.LuaChonNguoiQuanLy;
import qlhtt.Models.Model;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuNguoiQuanLyController implements Initializable {


    public JFXButton btn_TrangTongQuat;
    public JFXButton btn_ThemNhanVien;
    public JFXButton btn_CapNhatTrangThaiHoatDongNhanVien;
    public JFXButton btn_TaoChietKhau;
    public JFXButton btn_CapNhatTrangThaiChietKhau;
    public JFXButton btn_ThongKeSanPHam;
    public JFXButton btn_ThongKeDoanhThu;
    public JFXButton btn_ThongKeHoaDon;
    public JFXButton btn_ThongKePhieuNhap;
    public JFXButton btn_CapNhatThongTinNguoiDung;
    public JFXButton btn_DoiMatKhau;
    public JFXButton btn_DangXuat;

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
        btn_ThemNhanVien.setOnAction(event -> onThemNhanVien());
        btn_CapNhatTrangThaiHoatDongNhanVien.setOnAction(event -> onCapNhatTrangThaiHoatDongNhanVien());
        btn_TaoChietKhau.setOnAction(event -> onTaoChietKhau());
        btn_CapNhatTrangThaiChietKhau.setOnAction(event -> onCapNhatTrangThaiChietKhau());
        btn_ThongKeSanPHam.setOnAction(event -> onThongKeSanPham());
        btn_ThongKeDoanhThu.setOnAction(event -> onThongKeDoanhThu());
        btn_ThongKeHoaDon.setOnAction(event -> onThongKeHoaDon());
        btn_ThongKePhieuNhap.setOnAction(event -> onThongKePhieuNhap());
        btn_CapNhatThongTinNguoiDung.setOnAction(event -> onCapNhatThongTinNguoiDung());
        btn_DoiMatKhau.setOnAction(event -> onDoiMatKhau());
        btn_DangXuat.setOnAction(event -> onDangXuat());
    }

    private void onTrangTongQuat() {
        Model.getInstance().getViewFactory().layLuaChonNguoiQuanLy().set(LuaChonNguoiQuanLy.TRANGTONGQUAT);
    }

    private void onThemNhanVien() {
        Model.getInstance().getViewFactory().layLuaChonNguoiQuanLy().set(LuaChonNguoiQuanLy.THEMNHANVIEN);
    }

    private void onCapNhatTrangThaiHoatDongNhanVien() {
        Model.getInstance().getViewFactory().layLuaChonNguoiQuanLy().set(LuaChonNguoiQuanLy.CAPNHATTRANGTHAIHOATDONG_NHANVIEN);
    }

    private void onTaoChietKhau() {
        Model.getInstance().getViewFactory().layLuaChonNguoiQuanLy().set(LuaChonNguoiQuanLy.TAOCHIETKHAU);
    }

    private void onCapNhatTrangThaiChietKhau() {
        Model.getInstance().getViewFactory().layLuaChonNguoiQuanLy().set(LuaChonNguoiQuanLy.CAPNHATTRANGTHAICHIETKHAU);
    }

    private void onThongKeSanPham() {
        Model.getInstance().getViewFactory().layLuaChonNguoiQuanLy().set(LuaChonNguoiQuanLy.THONGKESANPHAM);
    }

    private void onThongKeDoanhThu() {
        Model.getInstance().getViewFactory().layLuaChonNguoiQuanLy().set(LuaChonNguoiQuanLy.THONGKEDOANHTHU);
    }

    private void onThongKeHoaDon() {
        Model.getInstance().getViewFactory().layLuaChonNguoiQuanLy().set(LuaChonNguoiQuanLy.THONGKEHOADON);
    }

    private void onThongKePhieuNhap() {
        Model.getInstance().getViewFactory().layLuaChonNguoiQuanLy().set(LuaChonNguoiQuanLy.THONGKEPHIEUNHAP);
    }

    private void onCapNhatThongTinNguoiDung() {
        Model.getInstance().getViewFactory().layLuaChonNguoiQuanLy().set(LuaChonNguoiQuanLy.CAPNHATTHONGTINNGUOIDUNG);
    }

    private void onDoiMatKhau() {
        Model.getInstance().getViewFactory().layLuaChonNguoiQuanLy().set(LuaChonNguoiQuanLy.DOI_MAT_KHAU);
    }

    private void onDangXuat() {
        Model.getInstance().getViewFactory().layLuaChonNguoiQuanLy().set(LuaChonNguoiQuanLy.DANG_XUAT);
    }


}
