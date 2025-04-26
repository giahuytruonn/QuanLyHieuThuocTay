package qlhtt.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import qlhtt.Controllers.NhanVien.ThongKeDoanhThuController;
import qlhtt.Controllers.NhanVien.ThongKeHoaDonController;
import qlhtt.Entity.TaiKhoan;
import qlhtt.Enum.VaiTro;
import qlhtt.Models.Model;
import qlhtt.Views.AccountType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private static TaiKhoan taiKhoan;

    @FXML
    public ChoiceBox<AccountType> chonloaitaikhoan_choicebox;
    @FXML
    public Label dangnhap_lbl;
    @FXML
    public TextField tendangnhap_fld;
    @FXML
    public TextField matkhau_fld;
    @FXML
    public Button login_btn;
    @FXML
    public Label err_lbl;
    @FXML
    public Label lbl_QuenMatKhau;

    private static final Dotenv dotenv = Dotenv.load();
    private static final String SERVER_HOST = dotenv.get("SERVER_HOST");// Địa chỉ server
    private static final int SERVER_PORT = Integer.parseInt(dotenv.get("SERVER_PORT"));  // Cổng server

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        chonloaitaikhoan_choicebox.setItems(FXCollections.observableArrayList(AccountType.NGUOIQUANLY,AccountType.NHANVIEN));
        chonloaitaikhoan_choicebox.setValue(Model.getInstance().getViewFactory().getLoginAccountType());
        chonloaitaikhoan_choicebox.valueProperty().addListener(observable -> Model.getInstance().getViewFactory().setLoginAccountType(chonloaitaikhoan_choicebox.getValue()));

        //Hện con trỏ
        Platform.runLater(() -> tendangnhap_fld.requestFocus());
        tendangnhap_fld.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onLogin();
            }
        });

        matkhau_fld.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onLogin();
            }
        });

        login_btn.setOnAction(actionEvent -> onLogin());

        lbl_QuenMatKhau.setOnMouseClicked(event -> {
            Model.getInstance().getViewFactory().hienThiTrangDatLaiMatKhau();
            Model.getInstance().getViewFactory().closeStage(
                    (javafx.stage.Stage) lbl_QuenMatKhau.getScene().getWindow()
            );
        });
    }

    // Xử lý socket để gửi yêu cầu đăng nhập tới server
    private void onLogin() {
        String username = tendangnhap_fld.getText();
        String password = matkhau_fld.getText();
        int role = chonloaitaikhoan_choicebox.getValue().ordinal();

        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Gửi yêu cầu đăng nhập tới server
            String request = String.format("LOGIN %s %s %d", username, password, role);
            out.println(request);

            // Nhận phản hồi từ server
            String response = in.readLine();
            handleServerResponse(response);

        } catch (IOException e) {
            err_lbl.setText("Không thể kết nối tới server");
        }
    }

    private void handleServerResponse(String response) {
        System.out.println("Phản hồi từ server: " + response);

        if (response.startsWith("{")) { // Kiểm tra nếu phản hồi là JSON
            try {
                // Tạo ObjectMapper và đăng ký JavaTimeModule
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

                // Deserialize JSON thành đối tượng TaiKhoan
                taiKhoan = objectMapper.readValue(response, TaiKhoan.class);

                if (taiKhoan != null) {
                    System.out.println("Tài khoản được chuyển đổi: " + taiKhoan.getTenDangNhap());
                    // Lưu tài khoản vào Model
                    Model.getInstance().setTaiKhoan(taiKhoan);

                    // Điều hướng giao diện
                    Stage stage = (Stage) err_lbl.getScene().getWindow();
                    if (taiKhoan.getNhanVien().getVaiTro() == VaiTro.NGUOIQUANLY) {
                        Model.getInstance().getViewFactory().closeStage(stage);
                        Model.getInstance().getViewFactory().hienTrangNguoiQuanLy();
                    } else {
                        Model.getInstance().getViewFactory().closeStage(stage);
                        Model.getInstance().getViewFactory().hienTrangNhanVien();
                    }
                } else {
                    err_lbl.setText("Lỗi khi chuyển đổi JSON thành đối tượng TaiKhoan");
                }
            } catch (Exception e) {
                e.printStackTrace();
                err_lbl.setText("Lỗi khi xử lý phản hồi từ server");
            }
        } else if (response.equals("LOCKED")) {
            err_lbl.setText("Tài khoản đã bị khóa");
        } else if (response.equals("FAIL")) {
            err_lbl.setText("Sai tên đăng nhập hoặc mật khẩu");
        } else {
            err_lbl.setText("Lỗi không xác định");
        }
    }

//    private void onLogin() {
//        List<TaiKhoan> danhSachTaiKhoan = TaiKhoanController.layDanhSachTaiKhoan();
//        checkTaiKhoan((ArrayList<TaiKhoan>) danhSachTaiKhoan, tendangnhap_fld.getText(), matkhau_fld.getText(), Model.getInstance().getViewFactory().getLoginAccountType().ordinal());
//    }
//
//    private void checkTaiKhoan(ArrayList<TaiKhoan> dsTaiKhoan, String tenDangNhap, String matKhau, int vaiTro) {
//        Stage stage = (Stage) err_lbl.getScene().getWindow();
//        VaiTro vaiTroTaiKhoan;
//        if(vaiTro == 0) vaiTroTaiKhoan = VaiTro.NGUOIQUANLY;
//        else vaiTroTaiKhoan = VaiTro.NHANVIEN;
//
//        for (TaiKhoan taiKhoan : dsTaiKhoan) {
//            if (taiKhoan.getTenDangNhap().equals(tenDangNhap) && TaiKhoanController.kiemTraMatKhau(matKhau, taiKhoan.getMatKhau())
//                    && taiKhoan.getNhanVien().getVaiTro() == vaiTroTaiKhoan) {
//                if (taiKhoan.getTrangThaiTaiKhoan() ) {
//                    LoginController.taiKhoan = taiKhoan;
//                    Model.getInstance().setTaiKhoan(taiKhoan);
//                    if(vaiTroTaiKhoan == VaiTro.NGUOIQUANLY) {
//                        Model.getInstance().getViewFactory().closeStage(stage);
//                        Model.getInstance().getViewFactory().hienTrangNguoiQuanLy();
//                    } else {
//                        Model.getInstance().getViewFactory().closeStage(stage);
//                        Model.getInstance().getViewFactory().hienTrangNhanVien();
//                    }
//                } else {
//                    err_lbl.setText("Tài khoản đã bị khóa");
//                }
//                return;
//            }
//            else{
//                err_lbl.setText("Sai tên đăng nhập hoặc mật khẩu");
//            }
//        }
//    }

    public String taoMatKhauNgauNhien(){
        int otp = (int) (Math.random() * 1000000);
        return String.valueOf(otp);
    }

    public static TaiKhoan getTaiKhoan() {
        return taiKhoan;
    }
}
