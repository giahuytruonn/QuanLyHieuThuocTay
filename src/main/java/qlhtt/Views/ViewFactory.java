package qlhtt.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import qlhtt.Controllers.NguoiQuanLy.CapNhatTrangThaiHoatDongController;
import qlhtt.Controllers.NguoiQuanLy.TrangTongQuatController;
import qlhtt.Controllers.NhanVien.NhanVienController;

public class ViewFactory {
    private AccountType loginAccountType;

    public ViewFactory(){
        this.loginAccountType = AccountType.NGUOIQUANLY;
    }

    public AccountType getLoginAccountType() {
        return loginAccountType;
    }

    public void setLoginAccountType(AccountType loginAccountType) {
        this.loginAccountType = loginAccountType;
    }

    public void closeStage(Stage stage) {
        stage.close();
    }

    public void showLoginWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
        createStage(loader);
    }

    private Scene createScene(FXMLLoader loader) {
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scene;
    }

    private void createStageNoResizable(FXMLLoader loader) {
        Scene scene = createScene(loader);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Quản lý tiệm thuốc tây");
        stage.setResizable(false);
        stage.show();
    }

    private void createStage(FXMLLoader loader) {
        Scene scene = createScene(loader);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Quản lý tiệm thuốc tây");
        //stage.setResizable(false);
        stage.show();
    }

    public void hienTrangTongQuatCuaNguoiQuanLy() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/NguoiQuanLy/TrangTongQuat.fxml"));
        TrangTongQuatController trangTongQuatController = new TrangTongQuatController();
        loader.setController(trangTongQuatController);
        createStageNoResizable(loader);
    }

    public void hienCuaSoNhanVien() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/NhanVien/NhanVien.fxml"));
        NhanVienController nhanVienController = new NhanVienController();
        loader.setController(nhanVienController);
        createStage(loader);
    }

    public void capNhatTrangThaiHoatDong(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/NguoiQuanLy/CapNhatTrangThaiHoatDong.fxml"));
        CapNhatTrangThaiHoatDongController capNhatTrangThaiHoatDongController = new CapNhatTrangThaiHoatDongController();
        loader.setController(capNhatTrangThaiHoatDongController);
        createStage(loader);
    }
}
