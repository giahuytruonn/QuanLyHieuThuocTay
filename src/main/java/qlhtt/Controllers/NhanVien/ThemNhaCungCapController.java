package qlhtt.Controllers.NhanVien;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import qlhtt.DAO.NhaCungCapDAO;
import qlhtt.Entity.NhaCungCap;
import qlhtt.Models.Model;

public class ThemNhaCungCapController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton btnHuy_CC;

    @FXML
    private JFXButton btnThem_CC;

    @FXML
    private TextField txtDiaChi_CC;

    @FXML
    private TextField txtEmail_CC;

    @FXML
    private TextField txtSDT_CC;

    @FXML
    private TextField txtTen_CC;

    NhaCungCapDAO nhaCungCapDAO = new NhaCungCapDAO();
    NhaCungCapController nhaCungCapController = new NhaCungCapController();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnThem_CC.setOnAction(event -> themNhaCungCap());
        btnHuy_CC.setOnAction(event -> huyModal());
    }
    private void themNhaCungCap() {
        // Get input values
        String tenNhaCungCap = txtTen_CC.getText();
        String diaChi = txtDiaChi_CC.getText();
        String email = txtEmail_CC.getText();
        String soDienThoai = txtSDT_CC.getText();

        // Validation check
        if (tenNhaCungCap.isEmpty() || diaChi.isEmpty() || email.isEmpty() || soDienThoai.isEmpty()) {
            showAlert("Thông báo !", "Vui lòng nhập đầy đủ thông tin của nhà cung cấp.");
            return;
        }

        // Kiểm tra tên nhà cung cấp (chữ cái đầu viết hoa)
        if (!tenNhaCungCap.matches("^[AÀẢÃÁẠĂẰẲẴẮẶÂẦẨẪẤẬBCDĐEÈẺẼÉẸÊỀỂỄẾỆFGHIÌỈĨÍỊJKLMNOÒỎÕÓỌÔỒỔỖỐỘƠỜỞỠỚỢPQRSTUÙỦŨÚỤƯỪỬỮỨỰVWXYỲỶỸÝỴZ][aàảãáạăằẳẵắặâầẩẫấậbcdđeèẻẽéẹêềểễếệfghiìỉĩíịjklmnoòỏõóọôồổỗốộơờởỡớợpqrstuùủũúụưừửữứựvwxyỳỷỹýỵz]+(?: [AÀẢÃÁẠĂẰẲẴẮẶÂẦẨẪẤẬBCDĐEÈẺẼÉẸÊỀỂỄẾỆFGHIÌỈĨÍỊJKLMNOÒỎÕÓỌÔỒỔỖỐỘƠỜỞỠỚỢPQRSTUÙỦŨÚỤƯỪỬỮỨỰVWXYỲỶỸÝỴZ][aàảãáạăằẳẵắặâầẩẫấậbcdđeèẻẽéẹêềểễếệfghiìỉĩíịjklmnoòỏõóọôồổỗốộơờởỡớợpqrstuùủũúụưừửữứựvwxyỳỷỹýỵz]*)*$")) {
            showAlert("Tên nhà cung cấp không hợp lệ", "Tên nhà cung cấp phải bắt đầu bằng chữ cái viết hoa.");
            return;
        }

        // Kiểm tra số điện thoại (bắt đầu bằng 03, 09, 08 và có 10 số)
        if (!soDienThoai.matches("^(03|09|08)\\d{8}$")) {
            showAlert("Số điện thoại không hợp lệ", "Số điện thoại phải bắt đầu bằng 03, 09, hoặc 08 và có 10 chữ số.");
            return;
        }

        // Kiểm tra email (định dạng chuẩn)
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            showAlert("Email không hợp lệ", "Vui lòng nhập đúng định dạng email.");
            return;
        }

        // Create a new NhaCungCap instance
        NhaCungCap nhaCungCap = new NhaCungCap();
        try {
            // Thiết lập các thuộc tính cho NhaCungCap
            nhaCungCap.setTenNhaCungCap(tenNhaCungCap);
            nhaCungCap.setDiaChi(diaChi);
            nhaCungCap.setEmail(email);
            nhaCungCap.setSoDienThoai(soDienThoai);

            // Gọi phương thức thêm nhà cung cấp trong DAO
            boolean isInserted = nhaCungCapDAO.themNhaCungCap(nhaCungCap);
            if (isInserted) {
                Model.getInstance().getTaoPhieuNhapController().setGiaTriCacComboBox();
                showAlert("Thêm nhà cung cấp thành công!", Alert.AlertType.INFORMATION);
                clearForm();
            } else {
                showAlert("Thêm nhà cung cấp thất bại. Vui lòng thử lại.", Alert.AlertType.ERROR);
            }

        } catch (IllegalArgumentException e) {
            // Hiển thị thông báo lỗi khi có ngoại lệ từ các phương thức set
            showAlert(e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void huyModal() {
        // Close the modal window
        Stage stage = (Stage) btnHuy_CC.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearForm() {
        txtTen_CC.clear();
        txtDiaChi_CC.clear();
        txtEmail_CC.clear();
        txtSDT_CC.clear();
    }


}
