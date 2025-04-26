package qlhtt.ThongBaoLoi;

import javafx.scene.control.Alert;

public class ThongBaoLoi {

    public static ThongBaoLoi instance = new ThongBaoLoi();

    public static ThongBaoLoi getInstance() {
        return instance;
    }

    public ThongBaoLoi() {
    }

    public static void thongBao(String thongBao) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(thongBao);
        alert.showAndWait();
    }

    public static void thongBaoThanhCong(String thongBao) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(thongBao);
        alert.showAndWait();
    }
}
