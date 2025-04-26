
package qlhtt.Controllers.NhanVien;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import qlhtt.ConnectDB.ConnectDB;
import qlhtt.DAO.PhieuNhapDAO;
import qlhtt.Entity.*;
import qlhtt.Models.Model;
import qlhtt.ThongBao.ThongBao;
import qlhtt.ThongBaoLoi.ThongBaoLoi;
import qlhtt.utils.ExcelReader;
import net.sf.jasperreports.engine.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class TaoPhieuNhapController implements Initializable {
    @FXML
    public Button xoa_btn;

    @FXML
    public TextField soLo_tf;

    @FXML
    private TableView chiTietPhieuNhap_tableView;

    @FXML
    private TableColumn<ChiTietPhieuNhap, String> stt_tblColumn;

    @FXML
    private TableColumn<ChiTietPhieuNhap, String> maSP_tblColumn;

    @FXML
    private TableColumn<ChiTietPhieuNhap, LocalDate> hanSuDung_tblColumn;

    @FXML
    private TableColumn<ChiTietPhieuNhap, LocalDate> ngaySanXuat_tblColumn;
    @FXML
    private TableColumn<ChiTietPhieuNhap, String> donViTinh_tblColumn;

    @FXML
    private TableColumn<ChiTietPhieuNhap, Integer> soLuong_tblColumn;

    @FXML
    private TableColumn<ChiTietPhieuNhap, Double> thanhTien_tblColumn;

    @FXML
    private TableColumn<ChiTietPhieuNhap, Double> giaNhap_tblColumn;

    @FXML
    public TextField maSanPham_tf;

    @FXML
    public ComboBox<String> donViTinh_cb;

    @FXML
    public DatePicker ngaySanXuat_dp;

    @FXML
    public DatePicker hanSuDung_dp;

    @FXML
    public TextField giaNhap_tf;

    @FXML
    public TextField soLuong_tf;

    @FXML
    public Button them_btn;

    @FXML
    public ComboBox<String> nhaCungCap_cb;

    @FXML
    public Button themPhieuNhap_btn;

    @FXML
    public Button reset_btn;

    @FXML
    public Button themNhaCungCap_btn;

    @FXML
    public Button import_btn;

    @FXML
    public Label valueSoLuong_lbl;

    @FXML
    public Label valueThanhTien_lbl;

    ConnectDB connectDB = ConnectDB.getInstance();
    private ChiTietPhieuNhapController chiTietPhieuNhapController = new ChiTietPhieuNhapController();
    private SanPhamController sanPhamController = new SanPhamController();
    private NhanVienController nhanVienController = new NhanVienController();
    private DonViTinhController donViTinhController = new DonViTinhController();
    private Map<String, Double> thanhTienMap = new HashMap<>();
    private PhieuNhapController phieuNhapController = new PhieuNhapController();
    private NhaCungCapController nhaCungCapController = new NhaCungCapController();

    private PhieuNhap phieuNhap = new PhieuNhap();
    private TaiKhoan taiKhoan = Model.getInstance().getTaiKhoan();

    ObservableList<ChiTietPhieuNhap> chiTietPhieuNhapList = FXCollections.observableArrayList();
    ObservableList<String> donViTinhList = FXCollections.observableArrayList();
    ObservableList<String> nhaCungCapList = FXCollections.observableArrayList();

    int count = 0;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setGiaTriCacComboBox();
        suKien();
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            // Giới hạn chiều dài văn bản
            if (change.getControlNewText().length() > 4) {
                return null;
            }

            if(!change.getControlNewText().matches("[0-9]*")){
                return null;
            }

            return change;
        });
        soLuong_tf.setTextFormatter(formatter); // Áp dụng cho TextField

        TextFormatter<String> formatter1 = new TextFormatter<>(change -> {
            if (change.getControlNewText().length() > 9) {
                return null;
            }

            if(!change.getControlNewText().matches("[0-9]*")){
                return null;
            }
            return change;
        });
        giaNhap_tf.setTextFormatter(formatter1); // Áp dụng cho TextField
    }

    public void suKien(){
        xoaHangDangChon();
        suKienReset();
        docFileExcel();
        suKienThemChiTietPhieuNhap();
        suKienClickTrenTable();
        suKienThemPhieuNhap();
        suKienThemNhaCungCap();
    }

    public void themDataVaoBang(ChiTietPhieuNhap chiTietPhieuNhap) {
        maSP_tblColumn.setCellValueFactory(cellData -> {
            String code = cellData.getValue().getSanPham().getMaSanPham();
            return new SimpleObjectProperty<>(code);
        });

        hanSuDung_tblColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getHanSuDung()));
        hanSuDung_tblColumn.setCellFactory(column -> new TableCell<ChiTietPhieuNhap, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                if(empty || item == null) {
                    setText(null);
                }else{
                    setText(item.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                }
            }
        });

        ngaySanXuat_tblColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNgaySanXuat()));
        ngaySanXuat_tblColumn.setCellFactory(column -> new TableCell<ChiTietPhieuNhap, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                if(empty || item == null) {
                    setText(null);
                }else{
                    setText(item.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                }
            }
        });

        donViTinh_tblColumn.setCellValueFactory(cellData -> {
            ChiTietPhieuNhap chiTiet = cellData.getValue();
            return new SimpleStringProperty(chiTiet.getDonViTinh());
        });


        soLuong_tblColumn.setCellValueFactory(new PropertyValueFactory<>("soLuong"));

        giaNhap_tblColumn.setCellValueFactory(new PropertyValueFactory<>("giaNhap"));

        thanhTienMap.put(chiTietPhieuNhap.getSanPham().getMaSanPham(), chiTietPhieuNhap.getSoLuong()* chiTietPhieuNhap.getGiaNhap());
        thanhTien_tblColumn.setCellValueFactory(cellData -> {
            String code = cellData.getValue().getSanPham().getMaSanPham();
            Double thanhTien = thanhTienMap.get(code);
            return new SimpleObjectProperty<>(thanhTien);
        });

        chiTietPhieuNhapList.add(chiTietPhieuNhap);

        chiTietPhieuNhap_tableView.setItems(chiTietPhieuNhapList);
        count++;
        capNhatValueTong();
    }

    public void xoaHangDangChon() {
        xoa_btn.setOnAction(event -> {
            ChiTietPhieuNhap chiTietPhieuNhap = (ChiTietPhieuNhap) chiTietPhieuNhap_tableView.getSelectionModel().getSelectedItem();
            if (chiTietPhieuNhap != null) {
                chiTietPhieuNhapList.remove(chiTietPhieuNhap);
                thanhTienMap.remove(chiTietPhieuNhap.getSanPham().getMaSanPham());
                chiTietPhieuNhap_tableView.refresh();
                count--;
                capNhatValueTong();
            }
        });
    }

    public void suKienReset() {
        reset_btn.setOnAction(event -> {
            resetAll();
            count = 0;
            capNhatValueTong();
        });
    }

    public void suKienThemChiTietPhieuNhap(){
        them_btn.setOnAction(event->{
            ChiTietPhieuNhap chiTietPhieuNhap = checkValidAndGetChiTietPhieuNhap(maSanPham_tf.getText());
            if(chiTietPhieuNhap!=null){
                if(!capNhatChiTietPhieuNhap(chiTietPhieuNhap.getSanPham().getMaSanPham())){
                    themDataVaoBang(chiTietPhieuNhap);
                    chiTietPhieuNhap_tableView.refresh();
                }else{
                    chiTietPhieuNhap_tableView.refresh();
                    capNhatValueTong();
                }
                reset_Field();
            }
        });
    }

    public boolean capNhatChiTietPhieuNhap(String maSanPham){
        ChiTietPhieuNhap chiTietPhieuNhap = checkValidAndGetChiTietPhieuNhap(maSanPham);
        if(chiTietPhieuNhap!=null){
            for (ChiTietPhieuNhap existingChiTiet : chiTietPhieuNhapList) {
                if(existingChiTiet.getSanPham().getMaSanPham().equals(chiTietPhieuNhap.getSanPham().getMaSanPham())){
                    existingChiTiet.setHanSuDung(chiTietPhieuNhap.getHanSuDung());
                    existingChiTiet.setNgaySanXuat(chiTietPhieuNhap.getNgaySanXuat());
                    existingChiTiet.setSoLuong(chiTietPhieuNhap.getSoLuong());
                    existingChiTiet.setGiaNhap(chiTietPhieuNhap.getGiaNhap());

                    thanhTienMap.put(maSanPham, existingChiTiet.getGiaNhap() * existingChiTiet.getSoLuong());
                    return true;
                }
            }
        }
        return false;
    }

    public ChiTietPhieuNhap checkValidAndGetChiTietPhieuNhap(String maSanPham){
        if(maSanPham.isEmpty()){
            ThongBao.thongBaoLoi("Mã sản phẩm không được để trống");
        }else{
            if(sanPhamController.getSanPhamById(maSanPham)==null){
                ThongBao.thongBaoLoi("Sản phẩm không tồn tại");
            }
        }
        String donViTinh = donViTinh_cb.getSelectionModel().getSelectedItem().toString();
        String tenDonViTinh = sanPhamController.getSanPhamById(maSanPham).getDonViTinh().getTenDonViTinh();
        if(!tenDonViTinh.equals(donViTinh)){
            ThongBao.thongBaoLoi("Đơn vị tính phải là " + tenDonViTinh);
            return null;
        }
        LocalDate dateHanSuDung = hanSuDung_dp.getValue();
        LocalDate dateSanXuat = ngaySanXuat_dp.getValue();

        if(dateSanXuat.isAfter(dateHanSuDung)){
            ThongBao.thongBaoLoi("Ngày sản xuất phải trước hạn sử dụng");
            return null;
        }else{
            Period period = Period.between(dateSanXuat, dateHanSuDung);
            if(period.getMonths() < 3 && period.getYears() == 0 || (period.getMonths() == 3 && period.getDays() < 0 && period.getYears()==0) ){
                ThongBaoLoi.thongBao("Hạn sử dụng phải hơn 3 tháng");
                return null;
            }else{
                if(dateHanSuDung == null || dateSanXuat == null){
                    ThongBao.thongBaoLoi("Hạn sử dụng hoặc ngày sản xuất không được để trống");
                    return null;
                }
            }

        }
        String strSoLuong = soLuong_tf.getText().trim();
        int soLuong=0;
        if(strSoLuong.isEmpty()){
            ThongBao.thongBaoLoi("Số lượng không được để trống");
            return null;
        }else{
            if(!strSoLuong.matches("\\d+")){
                ThongBao.thongBaoLoi("Giá trị số lượng không hợp lệ");
                return null;
            }else{
                soLuong = Integer.parseInt(strSoLuong);
            }
        }
        String strGiaNhap = giaNhap_tf.getText().trim();
        double giaNhap = 0.0;
        if(strGiaNhap.isEmpty()){
            ThongBao.thongBaoLoi("Không được để trống giá nhập");
            return null;
        }else{
            if(!strGiaNhap.matches("\\d+(\\.\\d+)?")){
                ThongBao.thongBaoLoi("Giá nhập phải là một số");
                return null;
            }else{
                giaNhap = Double.parseDouble(strGiaNhap);
            }
        }

        thanhTienMap.put(maSanPham, soLuong * giaNhap);

        return new ChiTietPhieuNhap(sanPhamController.getSanPhamById(maSanPham), soLuong,giaNhap,donViTinh,dateHanSuDung, dateSanXuat, thanhTienMap.get(maSanPham));
    }

    public void setGiaTriCacComboBox(){
        donViTinhList.addAll(donViTinhController.getDsDonViTinh().stream().map(DonViTinh::getTenDonViTinh).toList());
        donViTinh_cb.setItems(donViTinhList);
        donViTinh_cb.setValue(donViTinhList.get(0));

        nhaCungCapList.addAll(nhaCungCapController.getDsNhaCungCap().stream().map(NhaCungCap::getTenNhaCungCap).toList());
        nhaCungCap_cb.setItems(nhaCungCapList);
        nhaCungCap_cb.setValue(nhaCungCapList.get(0));
    }

    public void capNhatValueTong(){
        valueSoLuong_lbl.setText(count + " sản phẩm");
        valueThanhTien_lbl.setText(thanhTienMap.values().stream().mapToDouble(Double::doubleValue).sum() + " VNĐ");
    }

    public void taoPhieuNhap(){

        String nhaCungCap = nhaCungCap_cb.getSelectionModel().getSelectedItem();
//        phieuNhap = new PhieuNhap(thanhTienMap.values().stream().mapToDouble(Double::doubleValue).sum(),
//                nhaCungCapController.getNhaCungCapBangTen(nhaCungCap),
//                nhanVienController.getNhanVienBangMa(taiKhoan.getNhanVien().getMaNhanVien()) , true);

        phieuNhap = new PhieuNhap( nhanVienController.getNhanVienBangMa(taiKhoan.getNhanVien().getMaNhanVien()), nhaCungCapController.getNhaCungCapBangTen(nhaCungCap),true,LocalDate.now(),thanhTienMap.values().stream().mapToDouble(Double::doubleValue).sum(), soLo_tf.getText());
        System.out.println(phieuNhap);
        phieuNhapController.taoPhieuNhap(phieuNhap);

        phieuNhap = phieuNhapController.getPhieuNhapVuaTao();

        addChiTietVaoPhieuNhap();
    }

    public void addChiTietVaoPhieuNhap(){
        chiTietPhieuNhapList.stream().forEach(i->{
            i.setPhieuNhap(phieuNhap);
            chiTietPhieuNhapController.taoChiTietPhieuNhap(i);
        });

    }

    public void reset_Field(){
        maSanPham_tf.setText("");
        soLuong_tf.setText("");
        giaNhap_tf.setText("");
        hanSuDung_dp.setValue(null);
        ngaySanXuat_dp.setValue(null);
        donViTinh_cb.setValue(donViTinhList.stream().findFirst().orElse(null));
    }

    public void resetAll(){
        chiTietPhieuNhapList.clear();
        thanhTienMap.clear();
        chiTietPhieuNhap_tableView.refresh();
        capNhatValueTong();
        soLo_tf.setText("");
        nhaCungCap_cb.setValue(nhaCungCapList.stream().findFirst().orElse(null));
        reset_Field();
    }

    public void suKienThemPhieuNhap(){
        themPhieuNhap_btn.setOnAction(event->{
            if(!checkEmptyField()){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Xác nhận");
                alert.setHeaderText("Bạn có muốn xuất phiếu nhập sau khi thêm không ?");

                // Hiển thị thông báo và chờ người dùng phản hồi
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    taoPhieuNhap();
                    phieuNhapController.capNhatTrangThaiPhieuNhap(phieuNhap.getMaPhieuNhap(), true);
                    chiTietPhieuNhapList.forEach(i->{
                        sanPhamController.capNhatSoLuongSanPham(i.getSanPham().getMaSanPham(), i.getSoLuong());
                    });
                    count=0;
                    resetAll();
                    ThongBao.thongBaoThanhCong("Thêm phiếu nhập thành công! Thực hiện xuất Phiếu Nhập! ");
                    suKienXuatPhieuNhap();

                } else {
                    taoPhieuNhap();
                    phieuNhapController.capNhatTrangThaiPhieuNhap(phieuNhap.getMaPhieuNhap(), true);
                    chiTietPhieuNhapList.forEach(i->{
                        sanPhamController.capNhatSoLuongSanPham(i.getSanPham().getMaSanPham(), i.getSoLuong());
                    });
                    count=0;
                    resetAll();
                    ThongBao.thongBaoThanhCong("Thêm phiếu nhập thành công!");
                }
            }
        });
    }

    public boolean checkEmptyField(){
        if(soLo_tf.getText().isEmpty()){
            ThongBao.thongBaoLoi("Số lô không được để trống");
            return true;
        }
        if(chiTietPhieuNhap_tableView.getItems().isEmpty()){
            ThongBao.thongBaoLoi("Không tồn tại sản phẩm cần nhập nào trong danh sách.");
            return true;
        }
        return false;
    }

    public void docFileExcel(){
        import_btn.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Chọn file Excel");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Excel File", "*.xls", "*.xlsx")
            );

            File file = fileChooser.showOpenDialog(new Stage());

            if(file!=null){
                List<ChiTietPhieuNhap> dsChiTietPhieuNhap = ExcelReader.getInstance().getDsChiTietPhieuNhap(file);
                if(!dsChiTietPhieuNhap.isEmpty()){
                    dsChiTietPhieuNhap.forEach(i->{
                        themDataVaoBang(i);
                    });
                    capNhatValueTong();
                    ChiTietPhieuNhap chiTietPhieuNhap = dsChiTietPhieuNhap.stream().findFirst().orElse(null);

                    nhaCungCap_cb.setValue(chiTietPhieuNhap.getPhieuNhap().getNhaCungCap().getTenNhaCungCap());
                    soLo_tf.setText(chiTietPhieuNhap.getPhieuNhap().getSoLo());
                }
            }else{
                ThongBao.thongBaoLoi("Vui lòng chọn đúng file.xlsx");
            }
        });
    }

    public void suKienClickTrenTable(){
        chiTietPhieuNhap_tableView.setOnMouseClicked(event -> {
            ChiTietPhieuNhap chiTietPhieuNhap = (ChiTietPhieuNhap) chiTietPhieuNhap_tableView.getSelectionModel().getSelectedItem();
            maSanPham_tf.setText(chiTietPhieuNhap.getSanPham().getMaSanPham());
            soLuong_tf.setText(chiTietPhieuNhap.getSoLuong()+"");
            String giaNhap = chiTietPhieuNhap.getGiaNhap()/10+"";
            giaNhap = giaNhap.replace(".","");
            System.out.println(giaNhap);
            giaNhap_tf.setText(giaNhap);
            hanSuDung_dp.setValue(chiTietPhieuNhap.getHanSuDung());
            ngaySanXuat_dp.setValue(chiTietPhieuNhap.getNgaySanXuat());
            donViTinh_cb.setValue(chiTietPhieuNhap.getDonViTinh());
        });
    }
    public void suKienXuatPhieuNhap(){
        String maPN = phieuNhap.getMaPhieuNhap();
            try {
                connectDB.connect();
                Connection connection = connectDB.getConnection();

                JasperReport jasperReport = JasperCompileManager.compileReport("src/main/resources/Fxml/MauPhieuNhap.jrxml");

                Map<String, Object> parameters = new HashMap<>();
                parameters.put("ReportTitle", "My Report Title");
                parameters.put("maPhieuNhap1", maPN);  // Truyền maHoaDon vào báo cáo

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

                JasperExportManager.exportReportToPdfFile(jasperPrint, "src/main/resources/PhieuNhap/"+maPN+".pdf");

                System.out.println("Báo cáo đã được xuất thành công!");

                File pdfFile = new File("src/main/resources/PhieuNhap/"+maPN+".pdf");
                if (pdfFile.exists()) {
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(pdfFile);
                    } else {
                        System.out.println("Mở file không được hỗ trợ trên hệ thống này.");
                    }
                } else {
                    System.out.println("File PDF không tồn tại: " + "src/main/resources/PhieuNhap/"+maPN+".pdf");
                }
            } catch (JRException | SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    public void suKienThemNhaCungCap() {
        themNhaCungCap_btn.setOnAction(actionEvent -> {
            Model.getInstance().setTaoPhieuNhapController(this);
            Model.getInstance().getViewFactory().hienThiThemNhaCungCap((Stage) themNhaCungCap_btn.getScene().getWindow());
        });
    }
    public void suKienCapNhatNhaCungCap(){
        ObservableList<String> nhaCungCapListNew = FXCollections.observableArrayList();
        nhaCungCapListNew.addAll(nhaCungCapController.getDsNhaCungCap().stream().map(NhaCungCap::getTenNhaCungCap).toList());
        nhaCungCap_cb.setItems(nhaCungCapListNew);
        if (!nhaCungCapListNew.isEmpty()) {
            nhaCungCap_cb.setValue(nhaCungCapListNew.get(0));
        }
    }

//    public String taoSoLo(ChiTietPhieuNhap chiTietPhieuNhap){
//        String nhaCungCap = nhaCungCap_cb.getSelectionModel().getSelectedItem();
//        StringBuilder soLo = new StringBuilder();
//        for (char kyTu : nhaCungCap.toCharArray()) {
//            if(Character.isUpperCase(kyTu)){
//                soLo.append(kyTu);
//            }
//        }
//        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
//        Date ngaySanXuat = Date.valueOf(chiTietPhieuNhap.getNgaySanXuat());
//        soLo.append(sdf.format(ngaySanXuat));
//        return soLo.toString();
//    }
    
}
