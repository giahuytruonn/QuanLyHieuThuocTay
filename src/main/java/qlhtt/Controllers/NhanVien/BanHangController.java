package qlhtt.Controllers.NhanVien;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import qlhtt.Entity.SanPham;
import qlhtt.exception.ThongBaoLoi;

public class BanHangController {
    @FXML
    private TextField maSanPham_tf;

    @FXML
    private TableView sanPham_tableView;

    private SanPhamController sanPhamController;

    private ThongBaoLoi thongBaoLoi;

    long prevousTime;

    long thresholdTime = 200;

    public void themSanPhamVaoTable(){

        maSanPham_tf.setOnKeyReleased(keyEvent -> {
            long currentTime = System.currentTimeMillis();
            long timeDiff = currentTime - prevousTime;
            prevousTime = currentTime;

            String maSanPham = maSanPham_tf.getText();

            if(timeDiff < thresholdTime){
                SanPham sanPham = sanPhamController.getSanPhamById(maSanPham);
                if(sanPham != null){
                    sanPham_tableView.getItems().add(sanPham);
                }else {
                    thongBaoLoi.thongBao("Không tìm thấy sản phẩm");
                }
            }else{
                if(keyEvent.getCode().toString().equals("ENTER")){
                    SanPham sanPham = sanPhamController.getSanPhamById(maSanPham);

                    if(sanPham != null){
                        sanPham_tableView.getItems().add(sanPham);
                    }else{
                        thongBaoLoi.thongBao("Không tìm thấy sản phẩm");
                    }
                }
            }
        });
    }
}
