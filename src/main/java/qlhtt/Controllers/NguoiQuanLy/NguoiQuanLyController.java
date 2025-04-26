package qlhtt.Controllers.NguoiQuanLy;




import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import qlhtt.Models.Model;
import qlhtt.Service.ApiRequest;
import qlhtt.Service.ApiResponse;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

import io.github.cdimascio.dotenv.Dotenv;

public class NguoiQuanLyController implements Initializable {
    public BorderPane nguoiQuanLy;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().layLuaChonNguoiQuanLy().addListener((observableValue,oldVal,newVal)->{
            switch ((newVal)){
                case TRANGTONGQUAT -> nguoiQuanLy.setCenter(Model.getInstance().getViewFactory().hienTrangTongQuatCuaNguoiQuanLy());
                case THEMNHANVIEN -> nguoiQuanLy.setCenter(Model.getInstance().getViewFactory().hienTrangThemNhanVien());
                case CAPNHATTRANGTHAIHOATDONG_NHANVIEN -> nguoiQuanLy.setCenter(Model.getInstance().getViewFactory().hienTrangCapNhatTrangThaiHoatDongNhanVien());
                case TAOCHIETKHAU -> nguoiQuanLy.setCenter(Model.getInstance().getViewFactory().hienTrangTaoChietKhau());
                case CAPNHATTRANGTHAICHIETKHAU -> nguoiQuanLy.setCenter(Model.getInstance().getViewFactory().hienTrangCapNhatTrangThaiChietKhau());
                case THONGKESANPHAM -> nguoiQuanLy.setCenter(Model.getInstance().getViewFactory().hienTrangThongKeSanPham());
                case THONGKEDOANHTHU -> nguoiQuanLy.setCenter(Model.getInstance().getViewFactory().hienTrangThongKeDoanhThu());
                case THONGKEHOADON -> nguoiQuanLy.setCenter(Model.getInstance().getViewFactory().hienTrangThongKeHoaDon());
                case THONGKEPHIEUNHAP -> nguoiQuanLy.setCenter(Model.getInstance().getViewFactory().hienTrangThongKePhieuNhap());
                case CAPNHATTHONGTINNGUOIDUNG -> nguoiQuanLy.setCenter(Model.getInstance().getViewFactory().hienTrangCapNhatThongTinNguoiQuanLy());
                case DOI_MAT_KHAU -> nguoiQuanLy.setCenter(Model.getInstance().getViewFactory().hienTrangDoiMatKhauNguoiQuanLy());
                default -> {
                    Model.getInstance().getViewFactory().closeStage(
                            (Stage) nguoiQuanLy.getScene().getWindow()
                    );
                    Model.getInstance().getViewFactory().lamMoiCacGiaoDien();
                    Model.getInstance().getViewFactory().showLoginWindow();
                }

            }
        });
    }
}
