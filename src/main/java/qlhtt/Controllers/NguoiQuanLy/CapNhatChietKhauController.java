package qlhtt.Controllers.NguoiQuanLy;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import qlhtt.Entity.ChietKhau;
import qlhtt.DAO.ChietKhauDAO;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class CapNhatChietKhauController implements Initializable {

    @FXML private TableView<ChietKhau> tblChietKhau_CapNhat;
    @FXML private TableColumn<ChietKhau, String> colMaChietKhau_CapNhat;
    @FXML private TableColumn<ChietKhau, Double> colGiaTri_CapNhat;
    @FXML private TableColumn<ChietKhau, LocalDate> colNgayBatDau_CapNhat;
    @FXML private TableColumn<ChietKhau, LocalDate> colNgayKetThuc_CapNhat;
    @FXML private TableColumn<ChietKhau, Integer> colSoLuong_CapNhat;
    @FXML private TableColumn<ChietKhau, Boolean> colTrangThai_CapNhat;

    private final ChietKhauDAO chietKhauDAO = new ChietKhauDAO();

    private void createTableView() {
        // Thiết lập các cột cho TableView
        colMaChietKhau_CapNhat.setCellValueFactory(new PropertyValueFactory<>("moTa"));
        colGiaTri_CapNhat.setCellValueFactory(new PropertyValueFactory<>("giaTriChietKhau"));
        colNgayBatDau_CapNhat.setCellValueFactory(new PropertyValueFactory<>("ngayBatDauApDung"));
        colNgayKetThuc_CapNhat.setCellValueFactory(new PropertyValueFactory<>("ngayKetThucApDung"));
        colSoLuong_CapNhat.setCellValueFactory(new PropertyValueFactory<>("soLuong"));

        // Thiết lập trạng thái cho ComboBox trong cột trạng thái
        colTrangThai_CapNhat.setCellValueFactory(data -> new SimpleBooleanProperty(data.getValue().getTrangThaiChietKhau()));
        colTrangThai_CapNhat.setCellFactory(column -> createTrangThaiCell());

        // Hiển thị tất cả chiết khấu khi khởi động
        hienThiTatCaChietKhau();
    }

    private TableCell<ChietKhau, Boolean> createTrangThaiCell() {
        return new TableCell<>() {
            private final ComboBox<String> comboBox = new ComboBox<>();

            {
                comboBox.getItems().addAll("Đang hoạt động", "Vô hiệu hóa");
                comboBox.setConverter(new StringConverter<>() {
                    @Override public String toString(String object) { return object; }
                    @Override public String fromString(String string) { return string; }
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    comboBox.setValue(item ? "Đang hoạt động" : "Vô hiệu hóa");
                    setGraphic(comboBox);
                    comboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
                        ChietKhau chietKhau = getTableRow().getItem();
                        boolean newStatus = newValue.equals("Đang hoạt động");
                        chietKhau.setTrangThaiChietKhau(newStatus);
                        chietKhauDAO.updateTrangThaiChietKhau(chietKhau.getMoTa(), newStatus);
                    });
                }
            }
        };
    }

    private void hienThiTatCaChietKhau() {
        List<ChietKhau> danhSachChietKhau = chietKhauDAO.layTatCaChietKhau();
        tblChietKhau_CapNhat.getItems().setAll(danhSachChietKhau);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createTableView();
    }
}
