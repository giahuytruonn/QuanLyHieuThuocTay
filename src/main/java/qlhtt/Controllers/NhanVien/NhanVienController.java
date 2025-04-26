package qlhtt.Controllers.NhanVien;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import qlhtt.Controllers.ThanhToanQRControlller;
import qlhtt.DAO.NhanVienDAO;
import qlhtt.Entity.NhanVien;
import qlhtt.Enum.LuaChonNhanVien;
import qlhtt.Models.Model;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NhanVienController implements Initializable {
    NhanVienDAO nhanVienDAO = new NhanVienDAO();
    public BorderPane nhanVien;

    @FXML
    public BorderPane getNhanVienBorderPane() {
        return nhanVien;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().setNhanVienController(this);
        Model.getInstance().getViewFactory().layLuaChonNhanVien().addListener((observableValue,oldVal,newVal)->{
            switch ((newVal)){
                case TRANGTONGQUAT -> nhanVien.setCenter(Model.getInstance().getViewFactory().hienTrangTongQuatCuaNhanVien());
                case BANHANG -> nhanVien.setCenter(Model.getInstance().getViewFactory().hienTrangBanHang());
                case DANHSACHHOADON -> nhanVien.setCenter(Model.getInstance().getViewFactory().hienTrangDanhSachHoaDon());
                case CAPNHATTHONGTINKHACHHANG -> nhanVien.setCenter(Model.getInstance().getViewFactory().hienTrangCapNhatThongTinKhachHang());
                case CAPNHATTHONGTINNHACUNGCAP -> nhanVien.setCenter(Model.getInstance().getViewFactory().hienTrangCapNhatThongTinNhaCungCap());
                case THONGKEDOANHTHU -> nhanVien.setCenter(Model.getInstance().getViewFactory().hienTrangThongKeDoanhThu_NhanVien());
                case THONGKEHOADON -> nhanVien.setCenter(Model.getInstance().getViewFactory().hienTrangThongKeHoaDon_NhanVien());
                case THONGKEPHIEUNHAP -> nhanVien.setCenter(Model.getInstance().getViewFactory().hienTrangThongKePhieuNhap_NhanVien());
                case CAPNHATTHONGTINNGUOIDUNG -> nhanVien.setCenter(Model.getInstance().getViewFactory().hienTrangCapNhatThongTinNhanVien());
                case DOI_MAT_KHAU -> nhanVien.setCenter(Model.getInstance().getViewFactory().hienTrangDoiMatKhauNhanVien());
                case QUANLYSANPHAM -> nhanVien.setCenter(Model.getInstance().getViewFactory().hienTrangQuanLySanPham());
                case NHAPHANG -> nhanVien.setCenter(Model.getInstance().getViewFactory().hienTrangNhapHang());
                case DANHSACHPHIEUNHAP -> nhanVien.setCenter(Model.getInstance().getViewFactory().hienTrangDanhSachPhieuNhap());
                default -> {
                    Model.getInstance().getViewFactory().closeStage(
                            (javafx.stage.Stage) nhanVien.getScene().getWindow()
                    );
                    Model.getInstance().getViewFactory().lamMoiCacGiaoDien();
                    Model.getInstance().getViewFactory().showLoginWindow();
                    Model.getInstance().getViewFactory().lamMoiCacGiaoDien();
//                    Model.getInstance().getViewFactory().hienMaQR("20000");
                }

            }
        });
    }

    public NhanVien getNhanVienBangMa(String maNhanVien){
        return nhanVienDAO.getNhanVienBangMaNhanVien(maNhanVien);
    }


}