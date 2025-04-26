package qlhtt.Controllers.NguoiQuanLy;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import qlhtt.DAO.ChietKhauDAO;
import qlhtt.Entity.ChietKhau;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class TaoChietKhauController implements Initializable {

    @FXML private JFXButton btnXacNhan_Tao;
    @FXML private JFXButton btnHuy_Tao;
    @FXML private DatePicker dpNgayBatDau_Tao;
    @FXML private DatePicker dpNgayKetThuc_Tao;
    @FXML private TextArea txaMoTa_Tao;
    @FXML private TextField txtGiaTri_Tao;
    @FXML private TextField txtSoLuong_Tao;

    ChietKhauDAO chietKhauDAO = new ChietKhauDAO();

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void themChietKhau() {
        String giaTriText = txtGiaTri_Tao.getText().trim();
        String soLuongText = txtSoLuong_Tao.getText().trim();
        LocalDate ngayBatDau = dpNgayBatDau_Tao.getValue();
        LocalDate ngayKetThuc = dpNgayKetThuc_Tao.getValue();
        String moTa = txaMoTa_Tao.getText().trim();

        if (giaTriText.isEmpty() || soLuongText.isEmpty() || ngayBatDau == null || ngayKetThuc == null) {
            showAlert("Thông báo", "Vui lòng điền đầy đủ thông tin.");
            return;
        }

        double giaTriChietKhau;
        int soLuong;

        try {
            giaTriChietKhau = Double.parseDouble(giaTriText);
            soLuong = Integer.parseInt(soLuongText);

            // Kiểm tra giá trị chiết khấu
            if (giaTriChietKhau <= 0 || giaTriChietKhau >= 1) {
                showAlert("Thông báo", "Giá trị chiết khấu phải lớn hơn 0 và nhỏ hơn 1.");
                return;
            }

            // Kiểm tra số lượng
            if (soLuong <= 0 || soLuong > 50) {
                showAlert("Thông báo", "Số lượng phải lớn hơn 0 và không vượt quá 50.");
                return;
            }

            // Kiểm tra ngày bắt đầu
            if (ngayBatDau.isBefore(LocalDate.now())) {
                showAlert("Thông báo", "Ngày bắt đầu phải lớn hơn hoặc bằng ngày hôm nay.");
                return;
            }

            // Kiểm tra ngày kết thúc
            if (!ngayKetThuc.isAfter(ngayBatDau)) {
                showAlert("Thông báo", "Ngày kết thúc phải sau ngày bắt đầu.");
                return;
            }

        } catch (NumberFormatException e) {
            showAlert("Thông báo", "Giá trị chiết khấu và số lượng phải là số hợp lệ.");
            return;
        }

        // Tạo đối tượng Chiết Khấu và thêm vào cơ sở dữ liệu
        ChietKhau chietKhau = new ChietKhau();
        chietKhau.setGiaTriChietKhau(giaTriChietKhau);
        chietKhau.setNgayBatDauApDung(ngayBatDau);
        chietKhau.setNgayKetThucApDung(ngayKetThuc);
        chietKhau.setSoLuong(soLuong);
        chietKhau.setMoTa(moTa);
        chietKhau.setTrangThaiChietKhau(true);

        boolean isSuccess = chietKhauDAO.themChietKhau(chietKhau);
        if (isSuccess) {
            showAlert("Thông báo", "Thêm chiết khấu thành công!");
            huyTao();
        } else {
            showAlert("Thông báo", "Có lỗi xảy ra khi thêm chiết khấu.");
        }
    }


    @FXML
    public void huyTao() {
        // Xóa dữ liệu trong các trường nhập
        txtGiaTri_Tao.clear();
        txtSoLuong_Tao.clear();
        dpNgayBatDau_Tao.setValue(null);
        dpNgayKetThuc_Tao.setValue(null);
        txaMoTa_Tao.clear();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Thiết lập hành động cho các nút
        btnXacNhan_Tao.setOnAction(actionEvent -> themChietKhau());
        btnHuy_Tao.setOnAction(actionEvent -> huyTao());
    }

}
