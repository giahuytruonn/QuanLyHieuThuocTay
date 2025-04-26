package qlhtt.Controllers.NhanVien;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import qlhtt.DAO.NhaCungCapDAO;
import qlhtt.Entity.NhaCungCap;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ChinhSuaNhaCungCapController implements Initializable {

    @FXML
    private JFXButton btnHuy;

    @FXML
    private JFXButton btnXacNhan;

    @FXML
    private TableColumn<NhaCungCap, String> colSDT;

    @FXML
    private TableColumn<NhaCungCap, Integer> colSTT; // Cột số thứ tự

    @FXML
    private TableColumn<NhaCungCap, String> colTen;

    @FXML
    private TableView<NhaCungCap> tblNCC;

    @FXML
    private TextField txtDiaChi;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtSDT;

    @FXML
    private TextField txtTen;

    @FXML
    private TextField txtTimKiem;

    private NhaCungCapDAO nhaCungCapDAO = new NhaCungCapDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnXacNhan.setOnAction(event -> updateNhaCungCap());

        btnHuy.setOnAction(event -> {
            hienThiBangNCC(); // Hiển thị lại toàn bộ nhà cung cấp khi hủy
            clearInputFields();
        });

        // Thiết lập cột số thứ tự
        colSTT.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(tblNCC.getItems().indexOf(cellData.getValue()) + 1)
        );
        colSDT.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
        colTen.setCellValueFactory(new PropertyValueFactory<>("tenNhaCungCap"));
        hienThiBangNCC();


        txtTimKiem.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) { // Khi trường tìm kiếm trống, hiển thị toàn bộ nhà cung cấp
                hienThiBangNCC();
            } else if (newValue.matches("\\d{10}")) { // Kiểm tra số điện thoại có đúng 10 chữ số
                performSearch(newValue);
            }
        });
        tblNCC.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                populateFields(newValue); // Điền thông tin vào các trường văn bản với thông tin NhaCungCap đã chọn
            }
        });
    }

    private void updateNhaCungCap() {
        // Lấy nhà cung cấp được chọn
        NhaCungCap selectedNhaCungCap = tblNCC.getSelectionModel().getSelectedItem();
        if (selectedNhaCungCap != null) {
            // Kiểm tra tính hợp lệ của tên nhà cung cấp
            if (!txtTen.getText().matches("^[A-Z][a-zA-Z\\s]*$")) {
                showAlert("Tên nhà cung cấp không hợp lệ. Chữ đầu phải viết hoa và chỉ chứa chữ cái.", Alert.AlertType.ERROR);
                return;
            }

            // Kiểm tra tính hợp lệ của số điện thoại
            if (!txtSDT.getText().matches("^(03|09|08)\\d{8}$")) {
                showAlert("Số điện thoại không hợp lệ. Số điện thoại phải bắt đầu bằng 03, 09, hoặc 08 và có 10 chữ số.", Alert.AlertType.ERROR);
                return;
            }

            // Kiểm tra tính hợp lệ của email
            if (!txtEmail.getText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                showAlert("Email không hợp lệ. Vui lòng nhập đúng định dạng email.", Alert.AlertType.ERROR);
                return;
            }

            // Cập nhật thông tin từ các trường văn bản
            selectedNhaCungCap.setTenNhaCungCap(txtTen.getText());
            selectedNhaCungCap.setDiaChi(txtDiaChi.getText());
            selectedNhaCungCap.setEmail(txtEmail.getText());
            selectedNhaCungCap.setSoDienThoai(txtSDT.getText());

            // Thực hiện cập nhật vào cơ sở dữ liệu
            nhaCungCapDAO.capNhat(selectedNhaCungCap);
            showAlert("Cập nhật thành công!", Alert.AlertType.INFORMATION); // Hiển thị thông báo thành công

            // Cập nhật lại bảng để phản ánh thay đổi
            tblNCC.refresh();
            clearInputFields(); // Xóa các trường văn bản
        } else {
            // Hiển thị thông báo nếu không có nhà cung cấp nào được chọn
            showAlert("Vui lòng chọn một nhà cung cấp để cập nhật.", Alert.AlertType.WARNING);
        }
    }


    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void populateFields(NhaCungCap nhaCungCap) {
        txtTen.setText(nhaCungCap.getTenNhaCungCap());
        txtDiaChi.setText(nhaCungCap.getDiaChi());
        txtEmail.setText(nhaCungCap.getEmail());
        txtSDT.setText(nhaCungCap.getSoDienThoai());
    }

    private void hienThiBangNCC() {
        List<NhaCungCap> danhSachNCC = nhaCungCapDAO.layDanhSachNCC();
        tblNCC.getItems().setAll(danhSachNCC);
    }

    private void clearInputFields() {
        txtTen.clear();
        txtDiaChi.clear();
        txtEmail.clear();
        txtSDT.clear();
    }

    private void performSearch(String keyword) {
        List<NhaCungCap> result = nhaCungCapDAO.timKiem(keyword);
        tblNCC.getItems().setAll(result);

        // Optional: Show an alert if no results are found
        if (result.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Không tìm thấy nhà cung cấp với số điện thoại: " + keyword);
            alert.showAndWait();
        }
    }
}
