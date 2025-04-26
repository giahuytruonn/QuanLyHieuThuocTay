package qlhtt.Controllers.NguoiQuanLy;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import qlhtt.DAO.NhanVienDAO;
import qlhtt.DAO.TaiKhoanDAO;
import qlhtt.Entity.NhanVien;
import qlhtt.Entity.TaiKhoan;
import qlhtt.Enum.VaiTro;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

public class ThemNhanVienController {

    @FXML
    private JFXButton btnHuy_Them;

    @FXML
    private JFXButton btnTaiAnh_Them;

    @FXML
    private JFXButton btnThem_Them;

    @FXML
    private JFXComboBox<String> cmbGioiTinh_Them;

    @FXML
    private JFXComboBox<VaiTro> cmbVaiTro_Them;

    @FXML
    private DatePicker dpNgaySinh_Them;

    @FXML
    private Pane pnlAnh_Them;

    @FXML
    private TextField txtCCCD_Them;

    @FXML
    private TextField txtEmail_Them;

    @FXML
    private TextField txtSDT_Them;

    @FXML
    private TextField txtTen_Them;

    private Path selectedImagePath;

    public void initialize() {
        ObservableList<String> gioiTinhOptions = FXCollections.observableArrayList("Nam", "Nữ");
        cmbGioiTinh_Them.setItems(gioiTinhOptions);

        cmbVaiTro_Them.setItems(FXCollections.observableArrayList(VaiTro.values()));

        cmbVaiTro_Them.setCellFactory(new Callback<>() {
            @Override
            public ListCell<VaiTro> call(ListView<VaiTro> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(VaiTro item, boolean empty) {
                        super.updateItem(item, empty);
                        setText((empty || item == null) ? null : VaiTroUtils.getDisplayName(item));
                    }
                };
            }
        });

        cmbVaiTro_Them.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(VaiTro item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : VaiTroUtils.getDisplayName(item));
            }
        });

        btnTaiAnh_Them.setOnAction(event -> chooseImage());
        btnThem_Them.setOnAction(event -> handleAddEmployee());
        btnHuy_Them.setOnAction(event -> handleClearFields());
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
                pnlAnh_Them.getChildren().clear();
                pnlAnh_Them.getChildren().add(imageView);

                selectedImagePath = selectedFile.toPath();
                System.out.println(selectedImagePath);
                showAlert("Thành công", "Ảnh đã được chọn.", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Lỗi", "Không thể tải ảnh: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleClearFields() {
        txtCCCD_Them.clear();
        txtEmail_Them.clear();
        txtSDT_Them.clear();
        txtTen_Them.clear();
        cmbGioiTinh_Them.getSelectionModel().clearSelection();
        cmbVaiTro_Them.getSelectionModel().clearSelection();
        dpNgaySinh_Them.setValue(null);
        pnlAnh_Them.getChildren().clear();
    }

    @FXML
    private void handleAddEmployee() {
        try {
            String GT = cmbGioiTinh_Them.getSelectionModel().getSelectedItem();
            Boolean gioiTinh = GT != null && GT.equals("Nữ");
            LocalDate ngaySinh = dpNgaySinh_Them.getValue();
            VaiTro vaiTro = cmbVaiTro_Them.getSelectionModel().getSelectedItem();
            String cccd = txtCCCD_Them.getText();
            String email = txtEmail_Them.getText();
            String soDienThoai = txtSDT_Them.getText();
            String tenNhanVien = txtTen_Them.getText();
            String duongDanAnh = saveImageToDirectory(selectedImagePath);

            // Kiểm tra regex và logic
            if (!isValidName(tenNhanVien)) {
                showAlert("Lỗi", "Họ tên không hợp lệ. Vui lòng viết hoa chữ cái đầu của mỗi từ.", Alert.AlertType.WARNING);
                return;
            }
            if (!isValidCCCD(cccd)) {
                showAlert("Lỗi", "CCCD không hợp lệ. Vui lòng nhập đúng định dạng (12 số).", Alert.AlertType.WARNING);
                return;
            }
            if (!isValidEmail(email)) {
                showAlert("Lỗi", "Email không hợp lệ. Vui lòng nhập đúng định dạng.", Alert.AlertType.WARNING);
                return;
            }
            if (!isValidPhoneNumber(soDienThoai)) {
                showAlert("Lỗi", "Số điện thoại không hợp lệ. Vui lòng nhập đúng định dạng (bắt đầu bằng 09, 08, hoặc 03, và có 10 số).", Alert.AlertType.WARNING);
                return;
            }
            if (!isValidAge(ngaySinh)) {
                showAlert("Lỗi", "Nhân viên phải đủ 18 tuổi hoặc lớn hơn.", Alert.AlertType.WARNING);
                return;
            }
            if (vaiTro == null) {
                showAlert("Lỗi", "Vui lòng chọn vai trò cho nhân viên.", Alert.AlertType.WARNING);
                return;
            }
            if (GT == null) {
                showAlert("Lỗi", "Vui lòng chọn giới tính cho nhân viên.", Alert.AlertType.WARNING);
                return;
            }

            // Tạo đối tượng nhân viên và thêm vào cơ sở dữ liệu
            NhanVien nhanVien = new NhanVien(gioiTinh, ngaySinh, vaiTro, cccd, email, soDienThoai, tenNhanVien, duongDanAnh);
            new NhanVienDAO().addNhanVien(nhanVien);
            handleClearFields();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể thêm nhân viên: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Phương thức kiểm tra họ tên
    private boolean isValidName(String name) {
        return name != null && name.matches("^[AÀẢÃÁẠĂẰẲẴẮẶÂẦẨẪẤẬBCDĐEÈẺẼÉẸÊỀỂỄẾỆFGHIÌỈĨÍỊJKLMNOÒỎÕÓỌÔỒỔỖỐỘƠỜỞỠỚỢPQRSTUÙỦŨÚỤƯỪỬỮỨỰVWXYỲỶỸÝỴZ][aàảãáạăằẳẵắặâầẩẫấậbcdđeèẻẽéẹêềểễếệfghiìỉĩíịjklmnoòỏõóọôồổỗốộơờởỡớợpqrstuùủũúụưừửữứựvwxyỳỷỹýỵz]+(?: [AÀẢÃÁẠĂẰẲẴẮẶÂẦẨẪẤẬBCDĐEÈẺẼÉẸÊỀỂỄẾỆFGHIÌỈĨÍỊJKLMNOÒỎÕÓỌÔỒỔỖỐỘƠỜỞỠỚỢPQRSTUÙỦŨÚỤƯỪỬỮỨỰVWXYỲỶỸÝỴZ][aàảãáạăằẳẵắặâầẩẫấậbcdđeèẻẽéẹêềểễếệfghiìỉĩíịjklmnoòỏõóọôồổỗốộơờởỡớợpqrstuùủũúụưừửữứựvwxyỳỷỹýỵz]*)*$");
    }



    // Phương thức kiểm tra CCCD
    private boolean isValidCCCD(String cccd) {
        return cccd != null && cccd.matches("\\d{12}");
    }

    // Phương thức kiểm tra email
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    // Phương thức kiểm tra số điện thoại
    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("^(09|03|02)\\d{8}$");
    }

    // Phương thức kiểm tra tuổi
    private boolean isValidAge(LocalDate birthDate) {
        if (birthDate == null) {
            return false;
        }
        LocalDate today = LocalDate.now();
        return !birthDate.isAfter(today.minusYears(18)); // Tuổi >= 18
    }




    private String saveImageToDirectory(Path sourceImagePath) {
        if (sourceImagePath == null) {
            return "/Avatar/" + "default.png";
        }

        try {
            String directoryPath = "src/main/resources/Avatar/";
            String fileName = txtSDT_Them.getText() + ".png";
            Path targetPath = Path.of(directoryPath + fileName);

            Files.createDirectories(Path.of(directoryPath));

            Files.copy(sourceImagePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

            return "/Avatar/" + fileName;
        } catch (Exception e) {
            showAlert("Lỗi", "Không thể lưu ảnh: " + e.getMessage(), Alert.AlertType.ERROR);
            return null;
        }
    }


    public static class VaiTroUtils {
        public static String getDisplayName(VaiTro vaiTro) {
            return switch (vaiTro) {
                case NGUOIQUANLY -> "Quản lý";
                case NHANVIEN -> "Nhân viên";
                default -> vaiTro.name();
            };
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
