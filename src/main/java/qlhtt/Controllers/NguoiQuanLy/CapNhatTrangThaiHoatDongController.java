package qlhtt.Controllers.NguoiQuanLy;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import qlhtt.Entity.NhanVien;

public class CapNhatTrangThaiHoatDongController {
    @FXML
    private TableColumn<NhanVien, Boolean> gioiTinh;

    @FXML
    private TextField sdtTimKiem;

    @FXML
    private TableColumn<NhanVien, String> soDienThoai;

    @FXML
    private TableColumn<NhanVien, String> tenNhanVien;

    @FXML
    private JFXButton timBtn;

    @FXML
    private TableColumn<NhanVien, String> trangThai;

    @FXML
    private TableColumn<NhanVien, String> vaiTro;

    public void initialize() {

    }
}
