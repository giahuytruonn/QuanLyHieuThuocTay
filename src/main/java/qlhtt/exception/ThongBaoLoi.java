package qlhtt.exception;

import javafx.scene.control.Alert;

public class ThongBaoLoi {
    public void thongBao(String noiDung) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thong Bao Loi");
        alert.setHeaderText(noiDung);
        alert.showAndWait();
    }
}
