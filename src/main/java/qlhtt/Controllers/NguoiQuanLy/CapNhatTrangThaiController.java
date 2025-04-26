package qlhtt.Controllers.NguoiQuanLy;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import qlhtt.DAO.NhanVienDAO;
import qlhtt.DAO.TaiKhoanDAO;

import java.util.*;

public class CapNhatTrangThaiController {

    @FXML
    private JFXButton btnTim_CapNhat;

    @FXML
    private TableColumn<Map<String, Object>, String> colTen_CapNhat;

    @FXML
    private TableColumn<Map<String, Object>, String> colGioiTinh_CapNhat;

    @FXML
    private TableColumn<Map<String, Object>, String> colSDT_CapNhat;

    @FXML
    private TableColumn<Map<String, Object>, String> colVaiTro_CapNhat;

    @FXML
    private TableColumn<Map<String, Object>, Boolean> colTrangThai_CapNhat;

    @FXML
    private TableView<Map<String, Object>> tblNhanVien_CapNhat;

    @FXML
    private TextField txtSDT_CapNhat;

    private NhanVienDAO nhanVienDAO = new NhanVienDAO();

    @FXML
    public void initialize() {


        // Thiết lập các cột cho TableView
        colTen_CapNhat.setCellValueFactory(data -> new SimpleStringProperty((String) data.getValue().get("tenNhanVien")));
        colGioiTinh_CapNhat.setCellValueFactory(data -> new SimpleStringProperty((Boolean) data.getValue().get("gioiTinh") ? "Nam" : "Nữ"));
        colSDT_CapNhat.setCellValueFactory(data -> new SimpleStringProperty((String) data.getValue().get("soDienThoai")));
        colVaiTro_CapNhat.setCellValueFactory(data -> {
            // Lấy giá trị vai trò từ Map
            String vaiTro = (String) data.getValue().get("vaiTro");
            // Kiểm tra và trả về giá trị tương ứng
            return new SimpleStringProperty(vaiTro != null && vaiTro.equals("0") ? "Người quản lý" : "Nhân viên");
        });
        colTrangThai_CapNhat.setCellValueFactory(data -> new SimpleBooleanProperty((Boolean) data.getValue().get("trangThaiTaiKhoan"))); // Thay yourComboBoxKey bằng khóa của ComboBox trong Map

// Thiết lập CellFactory
        colTrangThai_CapNhat.setCellFactory(column -> new TableCell<Map<String, Object>, Boolean>() {
            private final ComboBox<String> comboBox = new ComboBox<>();

            {
                comboBox.getItems().addAll("Hoạt động", "Vô hiệu hóa");
                comboBox.setConverter(new StringConverter<>() {
                    @Override
                    public String toString(String object) {
                        return object;
                    }

                    @Override
                    public String fromString(String string) {
                        return string;
                    }
                });

                // Lắng nghe thay đổi giá trị của ComboBox
                comboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
                    if (getTableRow() != null && getTableRow().getItem() != null) {
                        Map<String, Object> rowData = (Map<String, Object>) getTableRow().getItem();
                        boolean newStatus = "Hoạt động".equals(newValue);
                        rowData.put("trangThaiTaiKhoan", newStatus);

                        // Gọi DAO để cập nhật cơ sở dữ liệu
                        TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();
                        taiKhoanDAO.updateDatabaseStatus((String) rowData.get("maNhanVien"), newStatus);
                    }
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    // Thiết lập giá trị ComboBox theo dữ liệu nguồn
                    comboBox.setValue(item ? "Hoạt động" : "Vô hiệu hóa");
                    setGraphic(comboBox);
                }
            }
        });



        hienThiTatCaNhanVien();
    }

    public class BooleanStringConverter extends StringConverter<Boolean> {
        @Override
        public String toString(Boolean object) {
            return object != null && object ? "Đang hoạt động" : "Vô hiệu hóa";
        }

        @Override
        public Boolean fromString(String string) {
            return "Đang hoạt động".equals(string);
        }
    }

    @FXML
    private void btnTim_CapNhat_Click(ActionEvent event) {
        String soDienThoai = txtSDT_CapNhat.getText();
        if (soDienThoai.isEmpty()) {
            hienThiTatCaNhanVien();
        } else {
            timKiemNhanVienTheoSDT(soDienThoai);
        }
    }

    private void hienThiTatCaNhanVien() {
        List<Map<String, Object>> danhSachNhanVien = nhanVienDAO.layTatCaNhanVien();
        tblNhanVien_CapNhat.getItems().setAll(danhSachNhanVien);
    }

    private void timKiemNhanVienTheoSDT(String soDienThoai) {
        List<Map<String, Object>> danhSachNhanVien = nhanVienDAO.timKiemTheoSDT(soDienThoai);
        tblNhanVien_CapNhat.getItems().setAll(danhSachNhanVien);
    }

}
