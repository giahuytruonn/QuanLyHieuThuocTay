package qlhtt.Controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import qlhtt.Models.Model;

import java.io.IOException;

public class XacNhanOTPController {
    public Button btn_TiepTheo;
    public TextField txt_OTP;
    public Label lbl_Back;
    public Label lbl_Loi;
    public Label lbl_ThoiGian;
    public Button btn_GuiLai;

    public void initialize() {
      //  btn_TiepTheo.setDisable(true);
        btn_GuiLai.setDisable(true);
        lbl_ThoiGian.setText("60s");
        Timeline timeline = new Timeline();
        timeline.setCycleCount(60);
        final int[] time ={60};

        KeyFrame keyFrame = new KeyFrame(javafx.util.Duration.seconds(1), event -> {
            time[0]--;
            lbl_ThoiGian.setText(+ time[0] + "s");
            if(time[0] == 0){
                timeline.stop();
                lbl_ThoiGian.setText("");
                btn_GuiLai.setDisable(false);
                btn_TiepTheo.setDisable(true);
            }
        });

        timeline.getKeyFrames().add(keyFrame);

        timeline.play();

        btn_GuiLai.setOnAction(event -> {
            try {
                String OTP = Model.getInstance().taoOTP();
                Model.getInstance().setOTP(OTP);
                Model.getInstance().guiOTP(Model.getInstance().getEmail(), OTP);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            time[0] = 60;
            timeline.play();
            btn_GuiLai.setDisable(true);
            btn_TiepTheo.setDisable(false);
        });

        lbl_Back.setOnMouseClicked(event -> {
            Model.getInstance().getViewFactory().hienThiTrangDatLaiMatKhau();
            Model.getInstance().getViewFactory().closeStage(
                    (javafx.stage.Stage) lbl_Back.getScene().getWindow()
            );
        });

        btn_TiepTheo.setOnAction(event -> {
           xuLyXacNhanOTP();
        });

        txt_OTP.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER:
                    xuLyXacNhanOTP();
                    break;
                default:
                    break;
            }
        });
    }

    private void xuLyXacNhanOTP() {
        if (txt_OTP.getText().equals(Model.getInstance().getOTP())) {
            Model.getInstance().getViewFactory().hienThiTrangCapNhatMatKhau();
            System.out.println("Xác nhận thành công");
            Model.getInstance().getViewFactory().closeStage(
                    (javafx.stage.Stage) btn_TiepTheo.getScene().getWindow()
            );
        } else {
            lbl_Loi.setText("Mã OTP không chính xác");
        }
    }
}
