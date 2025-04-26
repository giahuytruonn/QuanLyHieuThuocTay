package qlhtt.ThongBao;

import javafx.scene.control.Alert;

public class ThongBao {

    public static ThongBao instance = new ThongBao();

    public static ThongBao getInstance() {
        return instance;
    }

    public ThongBao() {
    }

    public static void thongBaoLoi(String thongBao) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(thongBao);
        alert.showAndWait();
    }

    public static void thongBaoThanhCong(String thongBao){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thành Công");
        alert.setHeaderText(null);
        alert.setContentText(thongBao);
        alert.showAndWait();
    }
}
