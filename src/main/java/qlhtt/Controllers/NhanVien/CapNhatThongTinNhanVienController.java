package qlhtt.Controllers.NhanVien;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import qlhtt.Controllers.LoginController;
import qlhtt.Controllers.NguoiQuanLy.MenuNguoiQuanLyController;
import qlhtt.DAO.NhanVienDAO;
import qlhtt.Entity.NhanVien;
import qlhtt.Entity.TaiKhoan;
import qlhtt.Enum.VaiTro;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class CapNhatThongTinNhanVienController {

    @FXML
    private JFXButton btnHuy;

    @FXML
    private JFXButton btnThem;

    @FXML
    private JFXButton btnTaiAnh;

    @FXML
    private JFXComboBox<String> cmbGioiTinh;

    @FXML
    private JFXComboBox<VaiTro> cmbVaiTro;

    @FXML
    private DatePicker dpNgaySinh;

    @FXML
    private Pane pnlAnh;

    @FXML
    private TextField txtCCCD;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtSDT;

    @FXML
    private TextField txtTen;

    private Path selectedImagePath;

    TaiKhoan taiKhoan = LoginController.getTaiKhoan();

    private NhanVien originalNhanVien; // Biến lưu trữ dữ liệu gốc

    public void initialize() {
        // Thiết lập các ComboBox
        ObservableList<String> gioiTinhOptions = FXCollections.observableArrayList("Nam", "Nữ");
        cmbGioiTinh.setItems(gioiTinhOptions);

        // Cài đặt giá trị ComboBox cho giới tính
        if (taiKhoan != null && taiKhoan.getNhanVien() != null) {
            NhanVien nhanVien = taiKhoan.getNhanVien();
            String gioiTinh = nhanVien.getGioiTinh() ? "Nam" : "Nữ";
            cmbGioiTinh.setValue(gioiTinh);

            // Lưu dữ liệu gốc
            originalNhanVien = new NhanVien(
                    nhanVien.getGioiTinh(),
                    nhanVien.getNgaySinh(),
                    nhanVien.getVaiTro(),
                    nhanVien.getCccd(),
                    nhanVien.getEmail(),
                    nhanVien.getSoDienThoai(),
                    nhanVien.getTenNhanVien(),
                    nhanVien.getDuongDanAnh()
            );

            // Thiết lập các giá trị ban đầu cho các trường
            cmbVaiTro.setItems(FXCollections.observableArrayList(VaiTro.values()));
            cmbVaiTro.setValue(nhanVien.getVaiTro());
            txtTen.setText(nhanVien.getTenNhanVien());
            txtSDT.setText(nhanVien.getSoDienThoai());
            txtEmail.setText(nhanVien.getEmail());
            dpNgaySinh.setValue(nhanVien.getNgaySinh());
            txtCCCD.setText(nhanVien.getCccd());

            Image avatarImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(taiKhoan.getNhanVien().getDuongDanAnh())));
            if (avatarImage.isError()) {
                System.out.println("Error loading image: " + avatarImage.getException());
            }
                ImageView imageView = new ImageView();
                imageView.setImage(avatarImage);
                imageView.setFitWidth(295);
                imageView.setFitHeight(295);
                imageView.setPreserveRatio(true);
                imageView.setClip(new Circle(147.5, 147.5, 147.5));
                pnlAnh.getChildren().clear();
                pnlAnh.getChildren().add(imageView);

        }


        // Thiết lập sự kiện cho các nút bấm
        btnThem.setOnAction(event -> handleUpdateEmployee());
        btnHuy.setOnAction(event -> handleClearFields());
        btnTaiAnh.setOnAction(event -> chooseImage());
    }


    private void chooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                Image image = new Image(selectedFile.toURI().toString());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(295);
                imageView.setFitHeight(295);
                imageView.setPreserveRatio(true);
                imageView.setClip(new Circle(147.5, 147.5, 147.5));
                pnlAnh.getChildren().clear();
                pnlAnh.getChildren().add(imageView);

                selectedImagePath = selectedFile.toPath();
                showAlert("Thành công", "Ảnh đã được chọn.", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Lỗi", "Không thể tải ảnh: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleClearFields() {
        if (originalNhanVien != null) {
            // Khôi phục lại các trường với dữ liệu gốc
            txtTen.setText(originalNhanVien.getTenNhanVien());
            txtSDT.setText(originalNhanVien.getSoDienThoai());
            txtEmail.setText(originalNhanVien.getEmail());
            txtCCCD.setText(originalNhanVien.getCccd());
            dpNgaySinh.setValue(originalNhanVien.getNgaySinh());
            cmbGioiTinh.setValue(originalNhanVien.getGioiTinh() ? "Nam" : "Nữ");
            cmbVaiTro.setValue(originalNhanVien.getVaiTro());

            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(taiKhoan.getNhanVien().getDuongDanAnh())));

            ImageView imageView = new ImageView(image);
                imageView.setFitWidth(295);
                imageView.setFitHeight(295);
                imageView.setPreserveRatio(true);
                imageView.setClip(new Circle(147.5, 147.5, 147.5));
                pnlAnh.getChildren().clear();
                pnlAnh.getChildren().add(imageView);

        }
    }


    private boolean isValidName(String name) {
        String regex = "^[A-ZÀ-Ỹ][a-zà-ỹ]*(?: [A-ZÀ-Ỹ][a-zà-ỹ]*)*$";
        return name != null && name.matches(regex);
    }

    private boolean isValidCCCD(String cccd) {
        String regex = "^\\d{12}$";
        return cccd != null && cccd.matches(regex);
    }

    private boolean isValidEmail(String email) {
        String regex = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
        return email != null && email.matches(regex);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^(09|08|02)\\d{8}$";
        return phoneNumber != null && phoneNumber.matches(regex);
    }

    private boolean isValidAge(LocalDate birthDate) {
        if (birthDate == null) return false;
        LocalDate today = LocalDate.now();
        return today.minusYears(18).isAfter(birthDate) || today.minusYears(18).isEqual(birthDate);
    }

    @FXML
    private void handleUpdateEmployee() {
        try {
            String tenNhanVien = txtTen.getText();
            String cccd = txtCCCD.getText();
            String email = txtEmail.getText();
            String soDienThoai = txtSDT.getText();
            LocalDate ngaySinh = dpNgaySinh.getValue();

            // Kiểm tra hợp lệ
            if (!isValidName(tenNhanVien)) {
                showAlert("Lỗi", "Tên nhân viên không hợp lệ. Hãy nhập tên đúng định dạng (viết hoa chữ cái đầu).", Alert.AlertType.WARNING);
                return;
            }
            if (!isValidCCCD(cccd)) {
                showAlert("Lỗi", "CCCD không hợp lệ. Phải gồm 12 chữ số.", Alert.AlertType.WARNING);
                return;
            }
            if (!isValidEmail(email)) {
                showAlert("Lỗi", "Email không hợp lệ. Hãy nhập email đúng định dạng.", Alert.AlertType.WARNING);
                return;
            }
            if (!isValidPhoneNumber(soDienThoai)) {
                showAlert("Lỗi", "Số điện thoại không hợp lệ. Bắt đầu bằng 09, 08, hoặc 02 và có 10 số.", Alert.AlertType.WARNING);
                return;
            }
            if (!isValidAge(ngaySinh)) {
                showAlert("Lỗi", "Nhân viên phải trên 18 tuổi.", Alert.AlertType.WARNING);
                return;
            }

            String GT = cmbGioiTinh.getSelectionModel().getSelectedItem();
            Boolean gioiTinh = GT != null && GT.equals("Nữ");
            VaiTro vaiTro = cmbVaiTro.getSelectionModel().getSelectedItem();
            String duongDanAnh = (selectedImagePath != null)
                    ? saveImageToDirectory(selectedImagePath)
                    : taiKhoan.getNhanVien().getDuongDanAnh();
            String maNhanVien = taiKhoan.getNhanVien().getMaNhanVien();

            NhanVien nhanVien = new NhanVien(maNhanVien, tenNhanVien, soDienThoai, email, cccd, vaiTro, ngaySinh, gioiTinh, duongDanAnh);
            new NhanVienDAO().updateNhanVien(nhanVien);
            showAlert("Thành công", "Cập nhật nhân viên thành công.", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể cập nhật nhân viên: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }




    private String saveImageToDirectory(Path sourceImagePath) {
        try {
            String directoryPath = "src/main/resources/Avatar/";
            String fileName = txtSDT.getText() + ".png";
            Path targetPath = Path.of(directoryPath + fileName);

            Files.createDirectories(Path.of(directoryPath));

            if (sourceImagePath != null) {
                Files.copy(sourceImagePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }


            return "/Avatar/" + fileName;
        } catch (Exception e) {
            showAlert("Lỗi", "Không thể lưu ảnh: " + e.getMessage(), Alert.AlertType.ERROR);
            return null;
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
