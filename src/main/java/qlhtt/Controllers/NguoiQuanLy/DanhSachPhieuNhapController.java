package qlhtt.Controllers.NguoiQuanLy;

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
import net.sf.jasperreports.engine.*;
import qlhtt.ConnectDB.ConnectDB;
import qlhtt.DAO.PhieuNhapDAO;
import qlhtt.Entity.*;
import qlhtt.Models.Model;
import qlhtt.ThongBao.ThongBao;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class DanhSachPhieuNhapController implements Initializable {

    @FXML
    private Button btn_Back;

    @FXML
    private Button btn_LamMoi;

    @FXML
    private Button btn_Next;

    @FXML
    private Button btn_TimPhieuNhap;

    @FXML
    private Button btn_XuatPhieuNhap;

    @FXML
    private ChoiceBox<String> chb_Trang;

    @FXML
    private Label lbl_HienThi;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private TableColumn<PhieuNhap, String> tbc_MaPhieuNhap;

    @FXML
    private TableColumn<PhieuNhap, LocalDate> tbc_NgayNhap;

    @FXML
    private TableColumn<Object,String> tbc_STT;

    @FXML
    private TableColumn<PhieuNhap, String> tbc_TenNhaCungCap;

    @FXML
    private TableColumn<PhieuNhap, String> tbc_TenNhanVien;

    @FXML
    private TableColumn<PhieuNhap , Double> tbc_TongTien;

    @FXML
    private TableView<PhieuNhap> tbv_DanhSachPhieuNhap;

    @FXML
    private TextField txt_TimPhieuNhap;

    private int tongSoTrang;
    private int trangHienTai;
    private List<PhieuNhap> danhSachPhieuNhap;

    private PhieuNhap phieuNhap;

    private Boolean isSearching = false;

    public void setTongSoTrang(int tongSoTrang) {
        this.tongSoTrang = tongSoTrang;
    }

    public void setTrangHienTai(int trangHienTai) {
        this.trangHienTai = trangHienTai;
    }

    public void setDanhSachPhieuNhap(List<PhieuNhap> danhSachPhieuNhap) {
        this.danhSachPhieuNhap = danhSachPhieuNhap;
    }

    public int getTongSoTrang() {
        return tongSoTrang;
    }

    public int getTrangHienTai() {
        return trangHienTai;
    }

    public List<PhieuNhap> getDanhSachPhieuNhap() {
        return danhSachPhieuNhap;
    }

    public static DanhSachPhieuNhapController instance = new DanhSachPhieuNhapController();

    public static DanhSachPhieuNhapController getInstance() {
        return instance;
    }

    public DanhSachPhieuNhapController() {
    }

    @FXML
    public void hienThiDuLieuLenTableView(List<PhieuNhap> DanhSachPhieuNhap) {
        // Cột số thứ tự (index)
        tbc_STT.setCellValueFactory(param -> {
            int index = param.getTableView().getItems().indexOf(param.getValue());
            return new SimpleStringProperty(String.valueOf(10*(getTrangHienTai()-1) + index + 1));
        });

        tbc_MaPhieuNhap.setCellValueFactory(new PropertyValueFactory<>("maPhieuNhap"));
        tbc_TenNhaCungCap.setCellValueFactory(param -> {
            NhaCungCap nhaCungCap = param.getValue().getNhaCungCap();
            return new SimpleStringProperty(nhaCungCap != null ? nhaCungCap.getTenNhaCungCap(): "");
        });
        tbc_TenNhanVien.setCellValueFactory(param -> {
            NhanVien nhanVien = param.getValue().getNhanVien();
            return new SimpleStringProperty(nhanVien != null ? nhanVien.getTenNhanVien(): "");
        });

        // Cột đơn giá
        tbc_TongTien.setCellValueFactory(new PropertyValueFactory<>("tongTien"));

        // Cột số lượng
        tbc_NgayNhap.setCellValueFactory(new PropertyValueFactory<>("ngayTao"));

        ObservableList<PhieuNhap> phieuNhapList = FXCollections.observableArrayList(DanhSachPhieuNhap);
        tbv_DanhSachPhieuNhap.setItems(phieuNhapList);
        lbl_HienThi.setText("Hiển thị tổng cộng " + PhieuNhapDAO.getInstance().layTongSoPhieuNhap() + " phiếu nhập");
    }

    //Thêm số trang vào choicebox
    public void themSoTrangVaoChoiceBox(int tongSoTrang){
        ObservableList<String> list = FXCollections.observableArrayList();
        for(int i = 1; i <= tongSoTrang; i++){
            list.add("Trang " + i);
        }
        chb_Trang.setValue("Trang " + getTrangHienTai());
        chb_Trang.setItems(list);
    }

    //Thay đổi bảng khi sử dụng sự kiện chọn trang
    public void thayDoiBangKhiChonTrang(){
        List<PhieuNhap> danhSachPhieuNhap = PhieuNhapDAO.getInstance().layPhieuNhapTheoSoTrang(getTrangHienTai());
        hienThiDuLieuLenTableView(danhSachPhieuNhap);
    }

    //Thay đổi bảng khi chọn trang tiếp theo
    public void thayDoiBangKhiChonTrangTiepTheo(){
        isSearching = true;
        if(getTrangHienTai() < getTongSoTrang()){
            setTrangHienTai(getTrangHienTai() + 1);
            List<PhieuNhap> danhSachPhieuNhap = PhieuNhapDAO.getInstance().layPhieuNhapTheoSoTrang(getTrangHienTai());
            hienThiDuLieuLenTableView(danhSachPhieuNhap);
            chb_Trang.setValue("Trang " + getTrangHienTai());
        }
        isSearching = false;
    }

    //Thay đổi bảng khi chọn trang trước
    public void thayDoiBangKhiChonTrangTruoc(){
        isSearching = true;
        if(getTrangHienTai() > 1){
            setTrangHienTai(getTrangHienTai() - 1);
            List<PhieuNhap> danhSachPhieuNhap = PhieuNhapDAO.getInstance().layPhieuNhapTheoSoTrang(getTrangHienTai());
            hienThiDuLieuLenTableView(danhSachPhieuNhap);
            chb_Trang.setValue("Trang " + getTrangHienTai());
        }
        isSearching = false;
    }

    //Hàm thực thi tìm kiếm
    @FXML
    public void thucHienTimKiem(){
        isSearching = true;
        tbv_DanhSachPhieuNhap.getItems().clear();
        String maPhieuNhap = txt_TimPhieuNhap.getText().trim();

        ThongBao thongBaoLoi = ThongBao.getInstance();
        // Kiểm tra maSanPham
        if (maPhieuNhap.isEmpty()) {
            thongBaoLoi.thongBaoLoi("Vui lòng nhập mã phiếu nhập");
        }else{
            PhieuNhap phieuNhap = PhieuNhapDAO.getInstance().getPhieuNhapBangMaPhieuNhap(maPhieuNhap);
            List<PhieuNhap> dsPhieuNhap = new ArrayList<>();
            if (phieuNhap != null) {
                dsPhieuNhap.add(phieuNhap);
                hienThiDuLieuLenTableView(dsPhieuNhap);
                // Tính toán số trang
                int tongSoTrang = (int) Math.ceil((double) dsPhieuNhap.size() / 10);
                setTongSoTrang(tongSoTrang);
                // Thêm số trang vào choicebox
                themSoTrangVaoChoiceBox(tongSoTrang);
                btn_Next.setDisable(true);
                btn_Back.setDisable(true);
                lbl_HienThi.setText("Hiển thị tổng cộng " + dsPhieuNhap.size() + " phiếu nhập tìm thấy");
            }else{
                thongBaoLoi.thongBaoLoi("Không tìm thấy phiếu nhập");
            }
            isSearching = false;
        }
    }

    @FXML
    public void timKiemBangNhap(){
        btn_TimPhieuNhap.setOnAction(actionEvent -> {
            thucHienTimKiem();
        });

        // Thực hiện tìm kiếm khi nhấn phím Enter
        txt_TimPhieuNhap.setOnAction(actionEvent -> {
            thucHienTimKiem();
        });
    }

    //Lam mới bảng
    @FXML
    public void lamMoiBang(){
        txt_TimPhieuNhap.clear();
        setTrangHienTai(1);
        setTongSoTrang((int)Math.ceil((double)PhieuNhapDAO.getInstance().layTongSoPhieuNhap() / 10));
        List<PhieuNhap> danhSachPhieuNhap = PhieuNhapDAO.getInstance().layPhieuNhapTheoSoTrang(getTrangHienTai());
        hienThiDuLieuLenTableView(danhSachPhieuNhap);
        themSoTrangVaoChoiceBox(getTongSoTrang());
        chb_Trang.setValue("Trang " + getTrangHienTai());
        lbl_HienThi.setText("Hiển thị tổng cộng " + PhieuNhapDAO.getInstance().layTongSoPhieuNhap() + " phiếu nhập");
        btn_Next.setDisable(false);
        btn_Back.setDisable(false);
        chb_Trang.setDisable(false);
    }
    public void suKienXuatPhieuNhap(PhieuNhap phieuNhap){
        if(phieuNhap != null){
            try {
                String maPN = phieuNhap.getMaPhieuNhap();
                ConnectDB connectDB = ConnectDB.getInstance();
                connectDB.connect();
                Connection connection = connectDB.getConnection();

                JasperReport jasperReport = JasperCompileManager.compileReport("src/main/resources/Fxml/MauPhieuNhap.jrxml");

                Map<String, Object> parameters = new HashMap<>();
                parameters.put("ReportTitle", "My Report Title");
                parameters.put("maPhieuNhap1", maPN);
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
        } else {ThongBao.thongBaoLoi("Chọn phiếu nhập trước khi xuất !!");}
    }
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle){
        setTrangHienTai(1);
        setTongSoTrang((int)Math.ceil((double)PhieuNhapDAO.getInstance().layTongSoPhieuNhap() / 10));
        List<PhieuNhap> danhSachPhieuNhap = PhieuNhapDAO.getInstance().layPhieuNhapTheoSoTrang(getTrangHienTai());
        Model.getInstance().setDanhSachPhieuNhapController(this);
        // Hiển thị dữ liệu lên TableView
        hienThiDuLieuLenTableView(danhSachPhieuNhap);
        //Thêm số trang vào choicebox
        themSoTrangVaoChoiceBox(getTongSoTrang());
        //Sự kiện chọn trang
        chb_Trang.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null && !isSearching){
                setTrangHienTai(Integer.parseInt(newValue.substring(6)));
                thayDoiBangKhiChonTrang();
            }else if(isSearching){
                chb_Trang.setValue("Trang " + getTrangHienTai());
            }
        });
        btn_Next.setOnAction(actionEvent -> {
            thayDoiBangKhiChonTrangTiepTheo();
        });
        btn_Back.setOnAction(actionEvent -> {
            thayDoiBangKhiChonTrangTruoc();
        });
        timKiemBangNhap();
        suKienKhiChonDongPhieuNhap();
        btn_LamMoi.setOnAction(actionEvent -> {
            lamMoiBang();
        });
        btn_XuatPhieuNhap.setOnAction(actionEvent -> {
            suKienXuatPhieuNhap(phieuNhap);
        });
    }
    public void suKienKhiChonDongPhieuNhap(){
        tbv_DanhSachPhieuNhap.getSelectionModel().selectedItemProperty().addListener((observableValue, sanPhamCu, sanPhamMoi) -> {
            if(sanPhamMoi != null){
                phieuNhap = PhieuNhapDAO.getInstance().getPhieuNhapBangMaPhieuNhap(sanPhamMoi.getMaPhieuNhap());
            }
        });
    }
}
