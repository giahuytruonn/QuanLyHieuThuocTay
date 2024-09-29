package qlhtt.Controllers;

import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import qlhtt.Models.Model;
import qlhtt.Views.AccountType;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public ChoiceBox<AccountType> chonloaitaikhoan_choicebox;
    public Label dangnhap_lbl;
    public TextField tendangnhap_fld;
    public TextField matkhau_fld;
    public Label quenmatkhau_lbl;
    public Button login_btn;
    public Label err_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chonloaitaikhoan_choicebox.setItems(FXCollections.observableArrayList(AccountType.NGUOIQUANLY,AccountType.NHANVIEN));
        chonloaitaikhoan_choicebox.setValue(Model.getInstance().getViewFactory().getLoginAccountType());
        chonloaitaikhoan_choicebox.valueProperty().addListener(observable -> Model.getInstance().getViewFactory().setLoginAccountType(chonloaitaikhoan_choicebox.getValue()));
        login_btn.setOnAction(event -> onLogin());
    }

    private void onLogin() {
        Stage stage = (Stage) err_lbl.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        if(Model.getInstance().getViewFactory().getLoginAccountType() == AccountType.NGUOIQUANLY){
            Model.getInstance().getViewFactory().hienCuaSoNguoiQuanLy();
        }
        else{
            Model.getInstance().getViewFactory().hienCuaSoNhanVien();
        }
    }
}
