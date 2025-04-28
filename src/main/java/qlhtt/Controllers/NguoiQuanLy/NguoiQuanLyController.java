package qlhtt.Controllers.NguoiQuanLy;




import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.Base64;
import java.util.Optional;
import java.util.ResourceBundle;

import io.github.cdimascio.dotenv.Dotenv;

public class NguoiQuanLyController implements Initializable {
    public BorderPane nguoiQuanLy;

    private static final Dotenv dotenv = Dotenv.load();
    private static final String SERVER_HOST = dotenv.get("SERVER_HOST");
    private static final int SERVER_PORT = Integer.parseInt(dotenv.get("SERVER_PORT"));
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
                    javafx.stage.Window window = nguoiQuanLy.getScene().getWindow();

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Xác nhận đăng xuất");
                    alert.setHeaderText(null);
                    alert.setContentText("Bạn có chắc chắn muốn đăng xuất không?");

                    // Hiển thị hộp thoại và chờ kết quả
                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        // Gửi request đến server để thông báo đăng xuất
                        String maNhanVien = Model.getInstance().getTaiKhoan().getNhanVien().getMaNhanVien();
                        String response = sendRequestToServer("LOGOUT " + maNhanVien);
                        if (!response.equals("SUCCESS")) {
                            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                            errorAlert.setTitle("Lỗi");
                            errorAlert.setHeaderText(null);
                            errorAlert.setContentText("Lỗi khi đăng xuất trên server");
                            errorAlert.showAndWait();
                        }

                        // Đóng cửa sổ và chuyển về màn hình đăng nhập
                        Model.getInstance().getViewFactory().closeStage((javafx.stage.Stage) window);
                        Model.getInstance().getViewFactory().showLoginWindow();
                        Model.getInstance().getViewFactory().lamMoiCacGiaoDien();
                    }
                }

            }
        });
    }

    private String sendRequestToServer(String request) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            out.println(request);
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }
}
