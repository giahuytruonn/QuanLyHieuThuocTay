package qlhtt.Controllers;

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
import qlhtt.Entity.TaiKhoan;
import qlhtt.Enum.VaiTro;
import qlhtt.Models.Model;
import qlhtt.Views.AccountType;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
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
    }

    private void onLogin() {
        List<TaiKhoan> danhSachTaiKhoan = TaiKhoanController.layDanhSachTaiKhoan();
        checkTaiKhoan((ArrayList<TaiKhoan>) danhSachTaiKhoan, tendangnhap_fld.getText(), matkhau_fld.getText(), Model.getInstance().getViewFactory().getLoginAccountType().ordinal());
    }

    private void checkTaiKhoan(ArrayList<TaiKhoan> dsTaiKhoan, String tenDangNhap, String matKhau, int vaiTro) {
        Stage stage = (Stage) err_lbl.getScene().getWindow();
        VaiTro vaiTroTaiKhoan;
        if(vaiTro == 0) vaiTroTaiKhoan = VaiTro.NGUOIQUANLY;
        else vaiTroTaiKhoan = VaiTro.NHANVIEN;

        for (TaiKhoan taiKhoan : dsTaiKhoan) {
            if (taiKhoan.getTenDangNhap().equals(tenDangNhap) && TaiKhoanController.kiemTraMatKhau(matKhau, taiKhoan.getMatKhau())
                    && taiKhoan.getNhanVien().getVaiTro() == vaiTroTaiKhoan) {
                if (taiKhoan.getTrangThaiTaiKhoan() ) {
                    if(vaiTroTaiKhoan == VaiTro.NGUOIQUANLY) {
                        Model.getInstance().getViewFactory().closeStage(stage);
                        Model.getInstance().getViewFactory().hienTrangTongQuatCuaNguoiQuanLy();
                    } else {
                        Model.getInstance().getViewFactory().closeStage(stage);
                        Model.getInstance().getViewFactory().hienTrangTongQuatCuaNhanVien();
                    }
                } else {
                    err_lbl.setText("Tài khoản đã bị khóa");
                }
                return;
            }
            else{
                err_lbl.setText("Sai tên đăng nhập hoặc mật khẩu");
            }
        }
    }
}
