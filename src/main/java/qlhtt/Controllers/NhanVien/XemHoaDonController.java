package qlhtt.Controllers.NhanVien;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import qlhtt.DAO.HoaDonDAO;
import qlhtt.Entity.ChiTietHoaDon;
import qlhtt.Entity.HoaDon;
import qlhtt.Enum.PhuongThucThanhToan;
import qlhtt.Models.Model;
import qlhtt.ThongBao.ThongBao;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

@SuppressWarnings("ALL")
public class XemHoaDonController implements Initializable {

    @FXML
    public Tab khac_tab;
    @FXML
    public AnchorPane loadingPane;
    @FXML
    public ProgressIndicator progressIndicator;
    @FXML
    public TableColumn<HoaDon, String> sttKhac_tblColumn;
    @FXML
    public TableColumn<HoaDon, String> maHoaDonKhac_tblColumn;
    @FXML
    public TableColumn<HoaDon, LocalDate> ngayTaoKhac_tblColumn;
    @FXML
    public TableColumn<HoaDon, String> soDienThoaiKhac_tblColumn;
    @FXML
    public TableColumn<HoaDon, PhuongThucThanhToan> phuongThucKhac_tblColumn;
    @FXML
    public TableColumn<HoaDon, Double> thanhTienKhac_tblColumn;
    @FXML
    public TableColumn<HoaDon, String> trangThaiKhac_tblColumn;
    @FXML
    public Button btn_Timkiem;
    @FXML
    private TableView hoaDon_tableView;
    @FXML
    private TableView hoaDonTam_tableView;
    @FXML
    private TableView chiTietHoaDon_tableView;
    @FXML
    private TableView khac_tableView;
    @FXML
    private TableColumn<HoaDon, String> stt_tblColumn;
    @FXML
    private TableColumn<HoaDon, String> maHoaDon_tblColumn;
    @FXML
    private TableColumn<HoaDon, LocalDate> ngayTao_tblColumn;
    @FXML
    private TableColumn<HoaDon, String> soDienThoai_tblColumn;
    @FXML
    private TableColumn<HoaDon, PhuongThucThanhToan> phuongThuc_tblColumn;
    @FXML
    private TableColumn<HoaDon, Double> thanhTien_tblColumn;
    @FXML
    private TableColumn<HoaDon, String> sttTam_tblColumn;
    @FXML
    private TableColumn<HoaDon, String> maHoaDonTam_tblColumn;
    @FXML
    private TableColumn<HoaDon, LocalDate> ngayTaoTam_tblColumn;
    @FXML
    private TableColumn<HoaDon, String> soDienThoaiTam_tblColumn;
    @FXML
    private TableColumn<HoaDon, PhuongThucThanhToan> phuongThucTam_tblColumn;
    @FXML
    private TableColumn<HoaDon, Double> thanhTienTam_tblColumn;
    @FXML
    private TableColumn<ChiTietHoaDon, String> sttChiTiet_tblColumn;
    @FXML
    private TableColumn<ChiTietHoaDon, String> maSanPham_tblColumn;
    @FXML
    private TableColumn<ChiTietHoaDon, String> tenSanPham_tblColumn;
    @FXML
    private TableColumn<ChiTietHoaDon, Integer> soLuong_tblColumn;
    @FXML
    private TableColumn<ChiTietHoaDon, Double> thanhTienChiTiet_tblColumn;
    @FXML
    private Tab hoaDon_tab;
    @FXML
    private Tab hoaDonTam_tab;
    @FXML
    private ToggleButton thanhVien_toggle;
    @FXML
    private ToggleButton thanhVienTam_toggle;
    @FXML
    private Label thanhVien_lbl;
    @FXML
    private Label thanhVienTam_lbl;
    @FXML
    private Label tongTien_lbl;
    @FXML
    private Label giamGia_lbl;
    @FXML
    private Label thanhTien_lbl;
    @FXML
    private Button thanhToan_btn;
    @FXML
    private Button xuatHoaDon_btn;
    @FXML
    private TextField soDienThoai_tf;
    @FXML
    private DatePicker ngayTao_dp;
    @FXML
    private TabPane tabPane;
    @FXML
    private Button loc_btn;
    @FXML
    private Button lamMoi_btn;
    @FXML
    private AnchorPane banHang_Page;

    private HoaDonController hoaDonController = new HoaDonController();
    private ChiTietHoaDonController chiTietHoaDonController = new ChiTietHoaDonController();
    private SanPhamController sanPhamController = new SanPhamController();

    ObservableList<HoaDon> dsHoaDonThanhVien = FXCollections.observableArrayList();
    ObservableList<HoaDon> dsHoaDonKhongThanhVien = FXCollections.observableArrayList();
    ObservableList<HoaDon> dsHoaDonTamThanhVien = FXCollections.observableArrayList();
    ObservableList<HoaDon> dsHoaDonTamKhongThanhVien = FXCollections.observableArrayList();
    ObservableList<HoaDon> dsHoaDonSauKhiLoc = FXCollections.observableArrayList();
    ObservableList<HoaDon> currentList = FXCollections.observableArrayList();
    ObservableList<ChiTietHoaDon> chiTietHoaDonList = FXCollections.observableArrayList();

    private HoaDon hoaDon;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().setXemHoaDonController(this);
        showLoading();
        suKien();
        thanhToan();
        xuatHoaDon();
        thanhVien_lbl.setFont(new Font("Arial",14));
    }

    public void suKien(){
        hoaDon_tab.setOnSelectionChanged(event -> {
            if(hoaDon_tab.isSelected()){
                xemDanhSachHoaDon(true);
            }
        });

        hoaDonTam_tab.setOnSelectionChanged(event -> {
            if(hoaDonTam_tab.isSelected()){
                xemDanhSachHoaDon(false);
            }
        });

        loc_btn.setOnAction(event->{
            Model.getInstance().getViewFactory().hienThiTrangLocHoaDonTheoSanPham((Stage) loc_btn.getScene().getWindow());
            khac_tab.getTabPane().getSelectionModel().select(khac_tab);
        });

        suKienToggle();
        getDsChiTietHoaDon();
//        suKienLoc();
        suKienReset();
        //thanhToan();
        suKienTimKiem();
    }

    private void suKienTimKiem() {
        btn_Timkiem.setOnAction(event->{
            khac_tab.getTabPane().getSelectionModel().select(khac_tab);
            String soDienThoai = soDienThoai_tf.getText();
            if(soDienThoai.matches("^(03|05|07|09)[0-9]{8}$")) {
                List<HoaDon> dsHoaDon = HoaDonDAO.getInstance().getDsHoaDonTheoSoDienThoai(soDienThoai);
                if (dsHoaDon.size() > 0) {
                    hienHoaDonSauKhiLoc(dsHoaDon);
                    soDienThoai_tf.setText("");
                } else {
                    ThongBao.thongBaoLoi("Không tìm thấy hóa đơn");
                    soDienThoai_tf.requestFocus();
                }
            }else{
                ThongBao.thongBaoLoi("Số điện thoại không hợp lệ");
                soDienThoai_tf.requestFocus();
            }
        });
    }


    public void themDataVaoBangHoaDon(HoaDon hoaDon) {
        stt_tblColumn.setCellValueFactory(cellData -> {
            int index = cellData.getTableView().getItems().indexOf(cellData.getValue());
            return new SimpleStringProperty(String.valueOf(index + 1));
        });

        maHoaDon_tblColumn.setCellValueFactory(cellData -> {
            String code = cellData.getValue().getMaHoaDon();
            return new SimpleObjectProperty<>(code);
        });

        ngayTao_tblColumn.setCellValueFactory(new PropertyValueFactory<>("ngayTao"));

        soDienThoai_tblColumn.setCellValueFactory(cellData->{
            HoaDon hoaDon1 = cellData.getValue();
            if(hoaDon1.getKhachHang()!=null){
                return new SimpleStringProperty(hoaDon1.getKhachHang().getSoDienThoai());
            }else{
                return new SimpleStringProperty("");
            }
        });

        phuongThuc_tblColumn.setCellValueFactory(cellData ->{
            HoaDon hoaDon1 = cellData.getValue();
            return new SimpleObjectProperty<>(hoaDon1.getPhuongThucThanhToan());
        });

        thanhTien_tblColumn.setCellValueFactory(new PropertyValueFactory<>("tongGiaTriHoaDon"));

        currentList.add(hoaDon);
        hoaDon_tableView.setItems(currentList);

    }

    public void themDataVaoBangHoaDonTam(HoaDon hoaDon) {
        sttTam_tblColumn.setCellValueFactory(cellData -> {
            int index = cellData.getTableView().getItems().indexOf(cellData.getValue());
            return new SimpleStringProperty(String.valueOf(index + 1));
        });

        maHoaDonTam_tblColumn.setCellValueFactory(cellData -> {
            String code = cellData.getValue().getMaHoaDon();
            return new SimpleObjectProperty<>(code);
        });

        ngayTaoTam_tblColumn.setCellValueFactory(new PropertyValueFactory<>("ngayTao"));

        soDienThoaiTam_tblColumn.setCellValueFactory(cellData->{
            HoaDon hoaDon1 = cellData.getValue();
            if(hoaDon1.getKhachHang()!=null){
                return new SimpleStringProperty(hoaDon1.getKhachHang().getSoDienThoai());
            }else{
                return new SimpleStringProperty("");
            }
        });

        phuongThucTam_tblColumn.setCellValueFactory(cellData ->{
            HoaDon hoaDon1 = cellData.getValue();
            return new SimpleObjectProperty<>(hoaDon1.getPhuongThucThanhToan());
        });

        thanhTienTam_tblColumn.setCellValueFactory(new PropertyValueFactory<>("tongGiaTriHoaDon"));

        currentList.add(hoaDon);
        hoaDonTam_tableView.setItems(currentList);
    }

    public void themDataVaoBangKhac(HoaDon hoaDon) {
        sttKhac_tblColumn.setCellValueFactory(cellData -> {
            int index = cellData.getTableView().getItems().indexOf(cellData.getValue());
            return new SimpleStringProperty(String.valueOf(index + 1));
        });

        maHoaDonKhac_tblColumn.setCellValueFactory(cellData -> {
            String code = cellData.getValue().getMaHoaDon();
            return new SimpleObjectProperty<>(code);
        });

        ngayTaoKhac_tblColumn.setCellValueFactory(new PropertyValueFactory<>("ngayTao"));

        soDienThoaiKhac_tblColumn.setCellValueFactory(cellData->{
            HoaDon hoaDon1 = cellData.getValue();
            if(hoaDon1.getKhachHang()!=null){
                return new SimpleStringProperty(hoaDon1.getKhachHang().getSoDienThoai());
            }else{
                return new SimpleStringProperty("");
            }
        });

        phuongThucKhac_tblColumn.setCellValueFactory(cellData ->{
            HoaDon hoaDon1 = cellData.getValue();
            return new SimpleObjectProperty<>(hoaDon1.getPhuongThucThanhToan());
        });

        thanhTienKhac_tblColumn.setCellValueFactory(new PropertyValueFactory<>("tongGiaTriHoaDon"));

        trangThaiKhac_tblColumn.setCellValueFactory(cellData ->{
            HoaDon hoaDon1 = cellData.getValue();
            if(hoaDon1.getTrangThaiHoaDon()){
                return new SimpleStringProperty("Đã Thanh Toán");
            }else{
                return new SimpleStringProperty("Chưa Thanh Toán");
            }
        });

        currentList.add(hoaDon);
        khac_tableView.setItems(currentList);
    }

    public void themDataVaoBangChiTietHoaDon(ChiTietHoaDon chiTietHoaDon){
        sttChiTiet_tblColumn.setCellValueFactory(cellData -> {
            int index = cellData.getTableView().getItems().indexOf(cellData.getValue());
            return new SimpleStringProperty(String.valueOf(index + 1));
        });

        maSanPham_tblColumn.setCellValueFactory(cellData ->{
            ChiTietHoaDon chiTietHoaDon1 = cellData.getValue();
            String code = chiTietHoaDon1.getSanPham().getMaSanPham();
            return new SimpleStringProperty(code);
        });

        tenSanPham_tblColumn.setCellValueFactory(cellData ->{
            String ten = cellData.getValue().getSanPham().getTenSanPham();
            return new SimpleStringProperty(ten);
        });

        soLuong_tblColumn.setCellValueFactory(new PropertyValueFactory<>("soLuong"));

        thanhTienChiTiet_tblColumn.setCellValueFactory(new PropertyValueFactory<>("tongTien"));

        chiTietHoaDonList.add(chiTietHoaDon);

        chiTietHoaDon_tableView.setItems(chiTietHoaDonList);
    }

    public void xemDanhSachHoaDon(boolean trangThai){
        currentList.clear();
        hoaDon_tableView.refresh();
        hoaDonTam_tableView.refresh();
        if(trangThai){
            if(thanhVien_toggle.isSelected()){
                dsHoaDonThanhVien.forEach(this::themDataVaoBangHoaDon);
            }else{
                dsHoaDonKhongThanhVien.forEach(this::themDataVaoBangHoaDon);
            }
        }else{
            if(thanhVienTam_toggle.isSelected()){
                dsHoaDonTamThanhVien.forEach(this::themDataVaoBangHoaDonTam);
            }else{
                dsHoaDonTamKhongThanhVien.forEach(this::themDataVaoBangHoaDonTam);
            }
        }

    }


    public void showLoading() {
        loadingPane.setVisible(true);

        // Tạo Task để tải dữ liệu
        Task<Void> loadingTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    // Bước 1: Tải dữ liệu HoaDonThanhVien
                    updateProgress(0, 4);  // 0 phần công việc đã hoàn thành, tổng công việc là 4
                    dsHoaDonThanhVien.addAll(hoaDonController.getDsHoaDonTheoTrangThaiVaThanhVien(true, true));

                    // Bước 2: Tải dữ liệu HoaDonKhongThanhVien
                    updateProgress(1, 4);  // 1 bước đã hoàn thành, tổng công việc là 4
                    dsHoaDonKhongThanhVien.addAll(hoaDonController.getDsHoaDonTheoTrangThaiVaThanhVien(true, false));

                    // Bước 3: Tải dữ liệu HoaDonTamKhongThanhVien
                    updateProgress(2, 4);  // 2 bước đã hoàn thành, tổng công việc là 4
                    dsHoaDonTamKhongThanhVien.addAll(hoaDonController.getDsHoaDonTheoTrangThaiVaThanhVien(false, false));

                    // Bước 4: Tải dữ liệu HoaDonTamThanhVien
                    updateProgress(3, 4);  // 3 bước đã hoàn thành, tổng công việc là 4
                    dsHoaDonTamThanhVien.addAll(hoaDonController.getDsHoaDonTheoTrangThaiVaThanhVien(false, true));

                    // Hoàn thành tất cả công việc
                    updateProgress(4, 4);  // 4 bước đã hoàn thành, tổng công việc là 4

                } catch (Exception e) {
                    // Nếu có lỗi, huỷ Task và hiển thị lỗi
                    updateProgress(0, 4);
                }

                // Cập nhật giao diện sau khi load xong
                Platform.runLater(() -> {
                    loadingPane.setVisible(false);
                    xemDanhSachHoaDon(true);
                });

                return null;
            }
        };

        // Liên kết ProgressIndicator với Task
        progressIndicator.progressProperty().bind(loadingTask.progressProperty());

        // Khi tải xong
        loadingTask.setOnSucceeded(event -> {
            // Đảm bảo các cập nhật UI đều được thực hiện trên UI thread
            Platform.runLater(() -> {
                loadingPane.setVisible(false);
            });
        });

        // Khi xảy ra lỗi
        loadingTask.setOnFailed(event -> {
            Platform.runLater(() -> {
                loadingPane.setVisible(false);
            });
        });

        // Chạy Task trên thread nền
        new Thread(loadingTask).start();
    }


    public void suKienToggle(){
        thanhVien_toggle.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(hoaDon_tab.isSelected()){
                xemDanhSachHoaDon(true);
            }

            if(thanhVien_toggle.isSelected()){
                thanhVien_lbl.setText("Thành Viên");
            }else{
                thanhVien_lbl.setText("Không Thành Viên");
            }
        });

        thanhVienTam_toggle.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(hoaDonTam_tab.isSelected()){
                xemDanhSachHoaDon(false);
            }

            if(thanhVienTam_toggle.isSelected()){
                thanhVienTam_lbl.setText("Thành Viên");
            }else{
                thanhVienTam_lbl.setText("Không Thành Viên");
            }
        });
    }

    public void getDsChiTietHoaDon(){
        hoaDon_tableView.setOnMouseClicked(event->{
            HoaDon selectedHoaDon = (HoaDon) hoaDon_tableView.getSelectionModel().getSelectedItem();
            hoaDon = selectedHoaDon;
            if(selectedHoaDon!=null){
                showChiTietHoaDon(selectedHoaDon.getMaHoaDon());
            }else{
                ThongBao.thongBaoLoi("Không tồn tại hóa đơn");
            }
        });

        hoaDonTam_tableView.setOnMouseClicked(event->{
            HoaDon selectedHoaDon = (HoaDon) hoaDonTam_tableView.getSelectionModel().getSelectedItem();
            hoaDon = selectedHoaDon;
            if(selectedHoaDon!=null){
                showChiTietHoaDon(selectedHoaDon.getMaHoaDon());
            }else{
                ThongBao.thongBaoLoi("Không tồn tại hóa đơn");
            }
        });

        khac_tableView.setOnMouseClicked(event->{
            HoaDon selectedHoaDon = (HoaDon) khac_tableView.getSelectionModel().getSelectedItem();
            hoaDon = selectedHoaDon;
            if(selectedHoaDon!=null){
                showChiTietHoaDon(selectedHoaDon.getMaHoaDon());
            }else{
                ThongBao.thongBaoLoi("Không tồn tại hóa đơn");
            }
        });
    }

    public void showChiTietHoaDon(String maHoaDon){
        List<ChiTietHoaDon> dsChiTietHoaDon = chiTietHoaDonController.getDsChiTietHoaDonTheoMaHoaDon(maHoaDon.trim());
        chiTietHoaDonList.clear();
        chiTietHoaDon_tableView.refresh();
        hoaDon = hoaDonController.getHoaDonBangMaHoaDon(maHoaDon);
        if(dsChiTietHoaDon.size() >0){
            dsChiTietHoaDon.forEach(this::themDataVaoBangChiTietHoaDon);
            NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
            thanhTien_lbl.setText(nf.format(hoaDonController.getHoaDonBangMaHoaDon(maHoaDon).getTongTienThanhToan())+" VNĐ");
            tongTien_lbl.setText(nf.format(dsChiTietHoaDon.stream().mapToDouble(ChiTietHoaDon::getTongTien).sum())+" VNĐ");
            giamGia_lbl.setText(nf.format(hoaDonController.getHoaDonBangMaHoaDon(maHoaDon).getTienDaGiam())+" VNĐ");
        }else{
            ThongBao.thongBaoLoi("Hóa đơn không có sản phẩm");
        }
    }



    public void suKienReset(){
        lamMoi_btn.setOnAction(event->{
            dsHoaDonTamKhongThanhVien.clear();
            dsHoaDonThanhVien.clear();
            dsHoaDonTamThanhVien.clear();
            dsHoaDonKhongThanhVien.clear();
            dsHoaDonSauKhiLoc.clear();
            chiTietHoaDonList.clear();
            tongTien_lbl.setText("");
            thanhTien_lbl.setText("");
            giamGia_lbl.setText("");
            tabPane.getSelectionModel().select(hoaDon_tab);
            showLoading();
            khac_tableView.setItems(dsHoaDonSauKhiLoc);
            khac_tableView.refresh();
        });
    }


    public void thanhToan(){
        thanhToan_btn.setOnAction(event->{
            if(!hoaDon.getTrangThaiHoaDon()){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Xác nhận");
                alert.setHeaderText("Bạn có chắc chắn muốn thanh toán hóa đơn này không?");
                // Hiển thị thông báo và chờ người dùng phản hồi
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    Model.getInstance().setHoaDon(hoaDon);
                    if(Model.getInstance().getBanHangController()!=null){
                        Model.getInstance().getBanHangController().datLai();
                    }
                    Model.getInstance().hienThiDanhSachHoaDonSangBanHang(hoaDon);
                }
            }else{
                ThongBao.thongBaoLoi("Hóa đơn đã được thanh toán");
            }
        });
    }

    public void xuatHoaDon(){
        xuatHoaDon_btn.setOnAction(event->{
            new BanHangController().suKienTaoHoaDon(hoaDon);
            if(hoaDon.getTrangThaiHoaDon()){
                String maHD = hoaDon.getMaHoaDon();
                File pdfFile = new File("src/main/resources/HoaDon/"+maHD+".pdf");
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().open(pdfFile);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.out.println("Mở file không được hỗ trợ trên hệ thống này.");
                }
            }else{
                ThongBao.thongBaoLoi("Hóa đơn chưa được thanh toán");
            }
        });
    }

    public void hienHoaDonSauKhiLoc(List<HoaDon> dsHoaDon) {
        currentList.clear();
        hoaDon_tableView.refresh();
        hoaDonTam_tableView.refresh();
        dsHoaDonSauKhiLoc.addAll(dsHoaDon);
        dsHoaDonSauKhiLoc.forEach(this::themDataVaoBangKhac);
        khac_tableView.refresh();
    }


}
