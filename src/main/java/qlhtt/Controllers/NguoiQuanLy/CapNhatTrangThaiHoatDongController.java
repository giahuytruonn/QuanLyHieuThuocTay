package qlhtt.Controllers.NguoiQuanLy;

import com.jfoenix.controls.JFXButton;
import com.sun.javafx.charts.Legend;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import qlhtt.Entity.NhanVien;
import qlhtt.Enum.VaiTro;

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
    private TableColumn<NhanVien, VaiTro> vaiTro;
    private TableView<NhanVien> bangNhanVien;

    public void initialize() {
        // Ánh xạ cột "Tên Nhân Viên"
        tenNhanVien.setCellValueFactory(new PropertyValueFactory<>("tenNhanVien"));

        // Ánh xạ cột "Giới Tính" (true = NAM, false = NỮ)
        gioiTinh.setCellValueFactory(cellData -> {
            Boolean gioiTinhValue = cellData.getValue().getGioiTinh();
            return new SimpleStringProperty(gioiTinhValue != null && gioiTinhValue ? "NAM" : "NỮ");
        });

        // Ánh xạ cột "Số Điện Thoại"
        soDienThoai.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));

        // Ánh xạ cột "Vai Trò"
        vaiTro.setCellValueFactory(cellData -> {
            int vaiTroValue = cellData.getValue().getVaiTro();
            String vaiTroStr = (vaiTroValue == 1) ? "Quản lý" : "Nhân viên";
            return new SimpleStringProperty(vaiTroStr);
        });

        // Ánh xạ cột "Trạng Thái"
        trangThai.setCellValueFactory(cellData -> {
            boolean active = cellData.getValue().getVaiTro() == 1;
            return new SimpleStringProperty(active ? "Hoạt động" : "Vô hiệu hóa");
        });

        // Tải dữ liệu mẫu lên bảng
        bangNhanVien.setItems(getNhanVienData());
    }
}
