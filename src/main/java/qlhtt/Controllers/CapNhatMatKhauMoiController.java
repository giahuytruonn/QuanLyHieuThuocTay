package qlhtt.Controllers;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import qlhtt.DAO.TaiKhoanDAO;
import qlhtt.Models.Model;
import qlhtt.ThongBaoLoi.ThongBaoLoi;

public class CapNhatMatKhauMoiController {
    public PasswordField txt_MatKhau;
    public PasswordField txt_XacThucMatKhau;
    public Button btn_DoiMatKhau;

    public void initialize() {
        btn_DoiMatKhau.setOnAction(event -> {
            xuLyCapNhatMatKhau();
        });
        txt_XacThucMatKhau.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                xuLyCapNhatMatKhau();
            }
        });

        txt_MatKhau.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                xuLyCapNhatMatKhau();
            }
        });
    }

    private void xuLyCapNhatMatKhau() {
        String matKhau = txt_MatKhau.getText();
        String xacThucMatKhau = txt_XacThucMatKhau.getText();
        if (matKhau.equals(xacThucMatKhau)) {
            // Doi mat khau
            String matKhauBCrypt = TaiKhoanController.taoChuoiBCrypt(matKhau);
            TaiKhoanDAO.getInstance().capNhatMatKhauMoi(matKhauBCrypt, Model.getInstance().getTaiKhoanCanDoiMatKhau());
            ThongBaoLoi.getInstance().thongBaoThanhCong("Đổi mật khẩu thành công");
            Model.getInstance().getViewFactory().showLoginWindow();
            Model.getInstance().getViewFactory().closeStage(
                    (javafx.stage.Stage) btn_DoiMatKhau.getScene().getWindow()
            );
        } else {
            ThongBaoLoi.getInstance().thongBao("Mật khẩu không khớp");
        }
    }
}
