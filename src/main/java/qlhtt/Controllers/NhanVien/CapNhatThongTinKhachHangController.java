package qlhtt.Controllers.NhanVien;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import qlhtt.Entity.KhachHang;
import qlhtt.ThongBao.ThongBao;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class CapNhatThongTinKhachHangController implements Initializable {

    private static final Dotenv dotenv = Dotenv.load();
    private static final String SERVER_HOST = dotenv.get("SERVER_HOST");// Địa chỉ server
    private static final int SERVER_PORT = Integer.parseInt(dotenv.get("SERVER_PORT"));

    @FXML public TextField txt_TenKhachHang;
    @FXML public TextField txt_Email;
    @FXML public ChoiceBox<String> chb_GioiTinh;
    @FXML public DatePicker dp_NgaySinh;
    @FXML public TextField txt_SoDienThoai;
    @FXML public Button btn_LamMoi;
    @FXML public Button btn_CapNhat;
    @FXML public Button btn_tim;

    public Boolean validData() {
        if (txt_TenKhachHang.getText().isEmpty()) {
            ThongBao.thongBaoLoi("Tên khách hàng không được để trống");
            txt_TenKhachHang.requestFocus();
            return false;
        } else if (!txt_TenKhachHang.getText().matches("^(?:\\p{Lu}\\p{Ll}*\\s*)+$")) {
            ThongBao.thongBaoLoi("Tên khách hàng không hợp lệ phải viết hoa chữ cái đầu tiên và có khoảng trắng");
            txt_TenKhachHang.requestFocus();
            return false;
        }

        if (txt_SoDienThoai.getText().isEmpty()) {
            ThongBao.thongBaoLoi("Số điện thoại không được để trống");
            txt_SoDienThoai.requestFocus();
            return false;
        } else if (!txt_SoDienThoai.getText().matches("^(03|05|07|09)[0-9]{8}$")) {
            ThongBao.thongBaoLoi("Số điện thoại không hợp lệ phải bắt đầu bằng số 03, 05, 07, 09 và phải 10 chữ số");
            txt_SoDienThoai.requestFocus();
            return false;
        }

        if (txt_Email.getText().isEmpty()) {
            ThongBao.thongBaoLoi("Email không được để trống");
            txt_Email.requestFocus();
            return false;
        } else if (!txt_Email.getText().matches("^[a-zA-Z0-9._%+-]+@(gmail.com|gmail.vn)$")) {
            ThongBao.getInstance().thongBaoLoi("Email không hợp lệ, phải kết thúc bằng @gmail.com hoặc @gmail.vn và không có kí tự đặc biệt");
            txt_Email.requestFocus();
            return false;
        }

        if (chb_GioiTinh.getValue() == null) {
            ThongBao.thongBaoLoi("Giới tính không được để trống");
            return false;
        }

        if (dp_NgaySinh.getValue() == null) {
            ThongBao.thongBaoLoi("Ngày sinh không được để trống");
            return false;
        }

        return true;
    }

    public void lamMoiThongTinKhachHang() {
        txt_TenKhachHang.setText("");
        txt_Email.setText("");
        chb_GioiTinh.setValue("Nam");
        txt_SoDienThoai.setText("");
        dp_NgaySinh.setValue(null);
    }

    public void capNhatThongTinKhachHang() {
        if (validData()) {
            try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                KhachHang kh = new KhachHang();
                kh.setHoTen(txt_TenKhachHang.getText());
                kh.setEmail(txt_Email.getText());
                kh.setGioiTinh(chb_GioiTinh.getValue().equals("Nam"));
                kh.setSoDienThoai(txt_SoDienThoai.getText());
                kh.setNgaySinh(dp_NgaySinh.getValue());

                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());

                String json = mapper.writeValueAsString(kh);
                out.println("CAPNHAT_KHACHHANG " + json);

                String response = in.readLine();
                if ("OK".equals(response)) {
                    ThongBao.thongBaoThanhCong("Cập nhật thông tin khách hàng thành công");
                    lamMoiThongTinKhachHang();
                } else {
                    ThongBao.thongBaoLoi("Cập nhật thông tin khách hàng thất bại");
                }

            } catch (IOException e) {
                ThongBao.thongBaoLoi("Không thể kết nối đến server");
            }
        }
    }

    public void timKhachHang() {
        if (txt_SoDienThoai.getText().isEmpty()) {
            ThongBao.thongBaoLoi("Số điện thoại không được để trống");
            txt_SoDienThoai.requestFocus();
            return;
        } else if (!txt_SoDienThoai.getText().matches("^(03|05|07|09)[0-9]{8}$")) {
            ThongBao.thongBaoLoi("Số điện thoại không hợp lệ");
            txt_SoDienThoai.requestFocus();
            return;
        }

        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("TIM_KHACHHANG " + txt_SoDienThoai.getText());
            String json = in.readLine();

            if ("NOT_FOUND".equals(json)) {
                ThongBao.thongBaoLoi("Không tìm thấy khách hàng");
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            KhachHang kh = mapper.readValue(json, KhachHang.class);

            txt_TenKhachHang.setText(kh.getHoTen());
            txt_Email.setText(kh.getEmail());
            chb_GioiTinh.setValue(kh.isGioiTinh() ? "Nam" : "Nữ");
            dp_NgaySinh.setValue(kh.getNgaySinh());

        } catch (IOException e) {
            ThongBao.thongBaoLoi("Lỗi kết nối server");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chb_GioiTinh.getItems().addAll("Nam", "Nữ");
        chb_GioiTinh.setValue("Nam");
        btn_LamMoi.setOnAction(e -> lamMoiThongTinKhachHang());
        btn_CapNhat.setOnAction(e -> capNhatThongTinKhachHang());
        btn_tim.setOnAction(e -> timKhachHang());
    }
}
