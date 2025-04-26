package qlhtt.Controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import qlhtt.DAO.TaiKhoanDAO;
import qlhtt.Models.Model;
import qlhtt.ThongBaoLoi.ThongBaoLoi;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.ResourceBundle;

public class DatLaiMatKhauController implements Initializable {

    public Button btn_TiepTheo;
    public TextField txt_Email;
    public Label lbl_Back;
    public Label lbl_Loi;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_TiepTheo.setDisable(true);

        lbl_Back.setOnMouseClicked(event -> {
            Model.getInstance().getViewFactory().showLoginWindow();
            Model.getInstance().getViewFactory().closeStage(
                    (javafx.stage.Stage) lbl_Back.getScene().getWindow()
            );
        });

        txt_Email.setOnKeyReleased(event -> {
            String email = txt_Email.getText();
            if (!checkEmail(email)) {
                lbl_Loi.setText("Email không hợp lệ");
                btn_TiepTheo.setDisable(true);
            }else{
                lbl_Loi.setText("");
                btn_TiepTheo.setDisable(false);
            }
        });

        btn_TiepTheo.setOnAction(event -> {
            xuLyDatLaiMatKhau();
        });

        txt_Email.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                xuLyDatLaiMatKhau();
            }
        });
    }

    private void xuLyDatLaiMatKhau() {
        String email = txt_Email.getText();
        Model.getInstance().setEmail(email);
        if(TaiKhoanDAO.getInstance().timTaiKhoanBangEmailNhanVien(email) != null){
            String OTP = Model.getInstance().taoOTP();
            try {
                Model.getInstance().guiOTP(email, OTP);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Model.getInstance().setOTP(OTP);
            Model.getInstance().setTaiKhoanCanDoiMatKhau(TaiKhoanDAO.getInstance().timTaiKhoanBangEmailNhanVien(email));
            Model.getInstance().getViewFactory().hienThiTrangXacNhanOTP();
            Model.getInstance().getViewFactory().closeStage(
                    (javafx.stage.Stage) btn_TiepTheo.getScene().getWindow()
            );
        }else{
            ThongBaoLoi.getInstance().thongBao("Email không tồn tại");
        }
    }


    public Boolean checkEmail(String email) {
        if(!email.matches("^[a-zA-Z0-9._%+-]+@(gmail.com|gmail.vn)$")) {
            //Model.getInstance().getViewFactory().showAlert("Vui lòng nhập email");
            return false;
        }
        return true;
    }

}
