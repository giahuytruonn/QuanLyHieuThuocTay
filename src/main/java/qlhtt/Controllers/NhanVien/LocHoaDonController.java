package qlhtt.Controllers.NhanVien;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import qlhtt.DAO.HoaDonDAO;
import qlhtt.DAO.SanPhamDAO;
import qlhtt.Entity.HoaDon;
import qlhtt.Entity.SanPham;
import qlhtt.Models.Model;
import qlhtt.ThongBao.ThongBao;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LocHoaDonController implements Initializable {
    public TextField txt_TimKiem;
    public ListView listView_TenThuoc;
    public Button btn_LocHoaDon;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<SanPham> sanPhamList = SanPhamDAO.getInstance().getDanhSachSanPham();
        txt_TimKiem.setOnKeyTyped(event -> {
            timKiemCacTenThuocCoTrongHoaDon(sanPhamList,txt_TimKiem.getText());
        });

        btn_LocHoaDon.setOnAction(event -> {
            if(listView_TenThuoc.getSelectionModel().getSelectedItem() == null) {
                ThongBao.getInstance().thongBaoLoi("Vui lòng chọn tên thuốc cần lọc");
            }else{
                List<HoaDon> hoaDonList = locHoaDonTheoTenThuoc(listView_TenThuoc.getSelectionModel().getSelectedItem().toString());
                if(hoaDonList.size() == 0) {
                    ThongBao.getInstance().thongBaoLoi("Không tìm thấy hóa đơn nào");
                }else{
                    Model.getInstance().getViewFactory().closeStage((Stage) btn_LocHoaDon.getScene().getWindow());
                    Model.getInstance().hienHoaDonSauKhiLoc(hoaDonList);

                }
            }
        });
    }

    private void timKiemCacTenThuocCoTrongHoaDon(List<SanPham> sanPhamList, String text) {
        listView_TenThuoc.getItems().clear();
//        List<SanPham> sanPhamList = SanPhamDAO.getInstance().getDanhSachSanPhamTheoTenSanPham(text);
        for (SanPham sanPham : sanPhamList) {
            if(sanPham.getTenSanPham().contains(text)) {
                listView_TenThuoc.getItems().add(sanPham.getTenSanPham());
            }
        }
    }

    public List<HoaDon> locHoaDonTheoTenThuoc(String tenThuoc) {
        return HoaDonDAO.getInstance().getHoaDonTheoTenThuoc(tenThuoc);
    }
}

