package qlhtt.Controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import qlhtt.Entity.TaiKhoan;
import qlhtt.Models.Model;
import qlhtt.Views.AccountType;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public ChoiceBox<AccountType> chonloaitaikhoan_choicebox;
    public Label dangnhap_lbl;
    public TextField tendangnhap_fld;
    public TextField matkhau_fld;
    //public Label quenmatkhau_lbl;
    public Button login_btn;
    public Label err_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chonloaitaikhoan_choicebox.setItems(FXCollections.observableArrayList(AccountType.NGUOIQUANLY,AccountType.NHANVIEN));
        chonloaitaikhoan_choicebox.setValue(Model.getInstance().getViewFactory().getLoginAccountType());
        chonloaitaikhoan_choicebox.valueProperty().addListener(observable -> Model.getInstance().getViewFactory().setLoginAccountType(chonloaitaikhoan_choicebox.getValue()));

        //Hện con trỏ
        Platform.runLater(() -> {
            tendangnhap_fld.requestFocus();
        });

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

        //ArrayList<TaiKhoan> dsTaiKhoan = getDSTaiKHoan();
        List<TaiKhoan> danhSachTaiKhoan = new ArrayList<>();

        danhSachTaiKhoan.add(new TaiKhoan("1", "admin", "admin", true));
        danhSachTaiKhoan.add(new TaiKhoan("2", "nhanvien", "nhanvien", true));
        danhSachTaiKhoan.add(new TaiKhoan("3", "nhanvien2", "nhanvien2", true));
        danhSachTaiKhoan.add(new TaiKhoan("4", "nhanvien3", "nhanvien3", true));

        checkTaiKhoan((ArrayList<TaiKhoan>) danhSachTaiKhoan, tendangnhap_fld.getText(), matkhau_fld.getText());

    }

    private void checkTaiKhoan(ArrayList<TaiKhoan> dsTaiKhoan, String tenDangNhap, String matKhau) {
        Stage stage = (Stage) err_lbl.getScene().getWindow();
        for (TaiKhoan taiKhoan : dsTaiKhoan) {
            if (taiKhoan.getTenDangNhap().equals(tenDangNhap) && taiKhoan.getMatKhau().equals(matKhau)) {
                if (taiKhoan.getTrangTaiTaiKhoan()) {
                    if (Model.getInstance().getViewFactory().getLoginAccountType() == AccountType.NGUOIQUANLY) {
                        Model.getInstance().getViewFactory().closeStage(stage);
                        Model.getInstance().getViewFactory().hienCuaSoNguoiQuanLy();
                    } else {
                        Model.getInstance().getViewFactory().closeStage(stage);
                        Model.getInstance().getViewFactory().hienCuaSoNhanVien();
                    }
                } else {
                    err_lbl.setText("Tài khoản đã bị khóa");
                }
                return;
            }
        }
        err_lbl.setText("Sai tên đăng nhập hoặc mật khẩu");
    }
}
