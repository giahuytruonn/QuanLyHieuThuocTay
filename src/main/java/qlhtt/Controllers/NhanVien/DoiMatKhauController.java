package qlhtt.Controllers.NhanVien;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.springframework.security.crypto.bcrypt.BCrypt;
import qlhtt.Controllers.LoginController;
import qlhtt.DAO.TaiKhoanDAO;
import qlhtt.Entity.TaiKhoan;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DoiMatKhauController implements Initializable {
    @FXML
    private JFXButton btnHuy;

    @FXML
    private JFXButton btnXacNhan;

    @FXML
    private TextField txtMKCu;

    @FXML
    private TextField txtMKMoi;

    @FXML
    private TextField txtNhapLaiMK;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TaiKhoan taiKhoan = LoginController.getTaiKhoan();
        btnHuy.setOnAction(event -> handleHuy());
        btnXacNhan.setOnAction(event -> {
            try {
                handleXacNhan();
            } catch (SQLException e) {
                showAlert("Thất bại!" , e.getMessage());
            }
        });
    }
    @FXML
    private void handleXacNhan() throws SQLException {
        String matKhauCu = txtMKCu.getText();
        String matKhauMoi = txtMKMoi.getText();
        String nhapLaiMK = txtNhapLaiMK.getText();

        if (matKhauCu.isEmpty() || matKhauMoi.isEmpty() || nhapLaiMK.isEmpty()) {
            showAlert("Lỗi!", "Vui lòng nhập đầy đủ thông tin.");
            return;
        }

        TaiKhoan taiKhoan = LoginController.getTaiKhoan();
        String maTaiKhoan = taiKhoan.getMaTaiKhoan();

        if (!BCrypt.checkpw(matKhauCu, taiKhoan.getMatKhau())) {
            showAlert("Mật khẩu cũ không đúng!", "Vui lòng kiểm tra lại mật khẩu cũ.");
            return;
        }

        if (matKhauMoi.equals(matKhauCu)) {
            showAlert("Mật khẩu mới không được giống mật khẩu cũ!", "Vui lòng nhập mật khẩu mới khác.");
            return;
        }

        if (!matKhauMoi.equals(nhapLaiMK)) {
            showAlert("Mật khẩu không khớp!", "Vui lòng nhập lại mật khẩu mới.");
            return;
        }

        String hashedNewPassword = BCrypt.hashpw(matKhauMoi, BCrypt.gensalt());
        TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();
        boolean isUpdated = taiKhoanDAO.updateMatKhau(maTaiKhoan, hashedNewPassword);

        if (isUpdated) {
            showAlert("Thành công!", "Mật khẩu đã được cập nhật. Hãy đăng nhập lại để xác nhận.");
            txtMKCu.clear();
            txtMKMoi.clear();
            txtNhapLaiMK.clear();
        } else {
            showAlert("Lỗi!", "Không thể cập nhật mật khẩu. Vui lòng thử lại.");
        }
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleHuy() {
        txtMKCu.clear();
        txtMKMoi.clear();
        txtNhapLaiMK.clear();
    }

}
