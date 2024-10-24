package qlhtt.Controllers.NhanVien;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ThemKhachHangController {
    @FXML
    public TextField txt_HoTen;
    @FXML
    public Label err_HoTen;
    @FXML
    public ChoiceBox chb_GioiTinh;
    @FXML
    public TextField txt_SoDienThoai;
    @FXML
    public Label err_SoDienThoai;
    @FXML
    public DatePicker dpk_NgaySinh;
    @FXML
    public Label err_NgaySinh;
    @FXML
    public TextField txt_Email;
    @FXML
    public Label err_Email;
}
