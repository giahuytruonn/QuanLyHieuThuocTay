package qlhtt.Controllers.NhanVien;

import io.github.cdimascio.dotenv.Dotenv;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import qlhtt.DAO.KhachHangDAO;
import qlhtt.Entity.KhachHang;
import qlhtt.Models.Model;
import qlhtt.Server.HandleClient;
import qlhtt.ThongBao.ThongBao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ThemKhachHangController implements Initializable {
    @FXML
    public TextField txt_HoTen;
    @FXML
    public ChoiceBox<String> chb_GioiTinh;
    @FXML
    public TextField txt_SoDienThoai;
    @FXML
    public DatePicker dpk_NgaySinh;
    @FXML
    public TextField txt_Email;
    public Button btn_Them;
    public Button btn_LamMoi;
    public Button btn_Huy;

    private static final Dotenv dotenv = Dotenv.load();
    private static final String SERVER_HOST = dotenv.get("SERVER_HOST");// Địa chỉ server
    private static final int SERVER_PORT = Integer.parseInt(dotenv.get("SERVER_PORT"));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hienThiGioiTinh();

        btn_Them.setOnAction(event -> {
            themKhachHang();
        });

        btn_LamMoi.setOnAction(event -> lamMoiThongTinKhachHang());
        btn_Huy.setOnAction(event -> {
            txt_HoTen.getScene().getWindow().hide();
        });
    }

    // Display gender options
    public void hienThiGioiTinh() {
        chb_GioiTinh.getItems().add("Nam");
        chb_GioiTinh.getItems().add("Nữ");
        chb_GioiTinh.setValue("Nam");
    }

    // Validate input data
    public Boolean validData() {
        if(txt_HoTen.getText().isEmpty()) {
            ThongBao.getInstance().thongBaoLoi("Tên khách hàng không được để trống");
            txt_HoTen.requestFocus();
            return false;
        }else{
            if(!txt_HoTen.getText().matches("^(?:\\p{Lu}\\p{Ll}*\\s*)+$")){
                ThongBao.getInstance().thongBaoLoi("Tên khách hàng không hợp lệ phải viết hoa chữ cái đầu tiên và có khoảng trắng");
                txt_HoTen.requestFocus();
                return false;
            }
        }

        if (txt_SoDienThoai.getText().isEmpty()) {
            ThongBao.getInstance().thongBaoLoi("Số điện thoại không được để trống");
            txt_SoDienThoai.requestFocus();
            return false;
        }else{
            if(!txt_SoDienThoai.getText().matches("^(03|05|07|09)[0-9]{8}$")) {
                ThongBao.getInstance().thongBaoLoi("Số điện thoại không hợp lệ phải bắt đầu bằng số 03, 05, 07, 09 và phải 10 chữ số");
                txt_SoDienThoai.requestFocus();
                return false;
            }
        }

        if(txt_Email.getText().isEmpty()) {
            ThongBao.getInstance().thongBaoLoi("Email không được để trống");
            txt_Email.requestFocus();
            return false;
        }else{
            if(!txt_Email.getText().matches("^[a-zA-Z0-9._%+-]+@(gmail.com|gmail.vn)$")){
                ThongBao.getInstance().thongBaoLoi("Email không hợp lệ, phải kết thúc bằng @gmail.com hoặc @gmail.vn và không có kí tự đặc biệt");
                txt_Email.requestFocus();
                return false;
            }
        }

        if(chb_GioiTinh.getValue() == null) {
            ThongBao.getInstance().thongBaoLoi("Giới tính không được để trống");
            return false;
        }

        if(dpk_NgaySinh.getValue() == null) {
            ThongBao.getInstance().thongBaoLoi("Ngày sinh không được để trống");
            return false;
        }else{
            if(LocalDate.now().getYear() - dpk_NgaySinh.getValue().getYear() < 18) {
                ThongBao.getInstance().thongBaoLoi("Khách hàng phải trên 18 tuổi");
                return false;
            }
        }
        return true;
    }

    // Reset form fields
    public void lamMoiThongTinKhachHang() {
        txt_HoTen.setText("");
        txt_Email.setText("");
        chb_GioiTinh.setValue("Nam");
        txt_SoDienThoai.setText("");
        dpk_NgaySinh.setValue(null);
    }

    // Add new customer by sending request to the server
    public void themKhachHang() {
        if(validData()) {
            String hoTen = txt_HoTen.getText();
            boolean gioiTinh = chb_GioiTinh.getValue().equals("Nam");
            String soDienThoai = txt_SoDienThoai.getText();
            List<KhachHang> dsKhachHang = KhachHangDAO.getInstance().getDanhSachKhachHang();

            for(KhachHang khachHang : dsKhachHang) {
                if(khachHang.getSoDienThoai().equals(soDienThoai)) {
                    ThongBao.getInstance().thongBaoLoi("Số điện thoại đã tồn tại");
                    return;
                }
            }

            String email = txt_Email.getText();
            LocalDate ngaySinh = dpk_NgaySinh.getValue();
            KhachHang khachHang = new KhachHang(hoTen, gioiTinh, soDienThoai, ngaySinh, 0, email);

            try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                // Send request to add new customer
                String request = String.format("THEM_KHACHHANG %s %b %s %s %s %d", hoTen, gioiTinh, soDienThoai, email, ngaySinh, 0);
                out.println(request);

                // Receive response from server
                String response = in.readLine();
                handleServerResponse(response);

            } catch (IOException e) {
                ThongBao.getInstance().thongBaoLoi("Không thể kết nối tới server");
            }
        }
    }

    // Handle server response for adding customer
    private void handleServerResponse(String response) {
        if ("OK".equals(response)) {
            ThongBao.getInstance().thongBaoThanhCong("Thêm khách hàng thành công");
            lamMoiThongTinKhachHang();
            Model.getInstance().getViewFactory().closeStage((Stage) btn_Them.getScene().getWindow());
        } else if ("FAIL".equals(response)) {
            ThongBao.getInstance().thongBaoLoi("Thêm khách hàng thất bại");
        } else {
            ThongBao.getInstance().thongBaoLoi("Lỗi không xác định");
        }
    }
}
