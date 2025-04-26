package qlhtt.Controllers.NhanVien;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.controlsfx.control.RangeSlider;
import qlhtt.DAO.DonViTinhDAO;
import qlhtt.DAO.LoaiSanPhamDAO;
import qlhtt.DAO.NhomSanPhamDAO;
import qlhtt.DAO.SanPhamDAO;
import qlhtt.Entity.DonViTinh;
import qlhtt.Entity.LoaiSanPham;
import qlhtt.Entity.NhomSanPham;
import qlhtt.Entity.SanPham;
import qlhtt.Models.Model;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

public class TraCuuSanPhamController implements Initializable{
    @FXML
    public ToggleButton btn_10;
    @FXML
    public ToggleButton btn_10_50;
    @FXML
    public ToggleButton btn_50_100;
    @FXML
    public ToggleButton btn_100_200;
    @FXML
    public ToggleButton btn_500;
    @FXML
    public ToggleButton btn_200_500;
    @FXML
    public RangeSlider range_slider;
    @FXML
    public ChoiceBox<String> chb_LoaiSanPham;
    @FXML
    public ChoiceBox<String>  chb_NhomSanPham;
    @FXML
    public ChoiceBox<String> chb_DonViTinh;
    @FXML
    public ChoiceBox<String>  chb_QuocGiaSanXuat;
    @FXML
    public Button btn_Loc;
    @FXML
    public Button btn_LamMoi;
    @FXML
    public Button btn_Huy;
    @FXML
    public Label lbl_ChonGia;
    @FXML
    public TextField txt_Min;
    @FXML
    public TextField txt_Max;

    public double giaMin;
    public double giaMax;

    ToggleGroup priceGroup = new ToggleGroup();

    public void thoatKhoiTrang(){
        Model.getInstance().getViewFactory().closeStage((Stage) btn_Huy.getScene().getWindow());
    }

    public void lamMoi(){
        chb_LoaiSanPham.setValue(null);
        chb_NhomSanPham.setValue(null);
        chb_QuocGiaSanXuat.setValue(null);
        chb_DonViTinh.setValue(null);
        txt_Min.setDisable(true);
        txt_Max.setDisable(true);
        range_slider.setDisable(true);
        priceGroup.selectToggle(null);
        range_slider.setLowValue(0);
        range_slider.setHighValue(5000000);
        range_slider.setMin(0);
        range_slider.setMax(5000000);
        txt_Min.setText(dinhDangGiaTheoVND(0.0));
        txt_Max.setText(dinhDangGiaTheoVND(5000000.0));
    }

    public void hienThiQuocGiaSanXuat(){
        List<String> dsQuocGiaSanXuat = new ArrayList<>();

        // Lấy danh sách tên quốc gia từ Locale và thêm vào dsQuocGiaSanXuat
        for (String countryCode : Locale.getISOCountries()) {
            Locale locale = new Locale("", countryCode);
            dsQuocGiaSanXuat.add(locale.getDisplayCountry());
        }

        // Thêm danh sách quốc gia vào ChoiceBox (giả sử tên là chb_QuocGiaSanXuat)
        for (String quocGia : dsQuocGiaSanXuat) {
            chb_QuocGiaSanXuat.getItems().add(quocGia);
        }

    }

    public void hienThiLoaiSanPham(){
        List<LoaiSanPham> dsLoaiSanPham = LoaiSanPhamDAO.getInstance().getDanhSachLoaiSanPham();
        for(LoaiSanPham loaiSanPham : dsLoaiSanPham){
            chb_LoaiSanPham.getItems().add(loaiSanPham.getTenLoaiSP());
        }
    }

    public void hienThiNhomSanPham(){
        List<NhomSanPham> dsNhomSanPham = NhomSanPhamDAO.getInstance().getDanhSachNhomSanPham();
        for(NhomSanPham nhomSanPham : dsNhomSanPham){
            chb_NhomSanPham.getItems().add(nhomSanPham.getTenNhomSP());
        }
    }

    public void hienThiDonViTinh(){
        List<DonViTinh> dsDonViTinh = DonViTinhDAO.getInstance().getDanhSachDonViTinh();
        for(DonViTinh donViTinh : dsDonViTinh){
            chb_DonViTinh.getItems().add(donViTinh.getTenDonViTinh());
        }
    }

    public String dinhDangGiaTheoVND(Double gia){
        DecimalFormat df = new DecimalFormat("#,### VNĐ");
        return df.format(gia);
    }

    private void suKienChonKhoangGia(){
        range_slider.setMin(0);
        range_slider.setMax(5000000);
        range_slider.setLowValue(0);
        range_slider.setHighValue(5000000);
        txt_Min.setText(dinhDangGiaTheoVND(0.0));
        txt_Max.setText(dinhDangGiaTheoVND(5000000.0));
        //Su kien khi chon khoang gia
        lbl_ChonGia.setOnMouseClicked(event -> {
            txt_Min.setDisable(false);
            txt_Max.setDisable(false);
            range_slider.setDisable(false);
            priceGroup.selectToggle(null);

            range_slider.lowValueProperty().addListener((observable, oldValue, newValue) -> {
                txt_Min.setText(dinhDangGiaTheoVND(newValue.doubleValue()));
                System.out.println("Min: " + newValue.doubleValue());
            });

            range_slider.highValueProperty().addListener((observable, oldValue, newValue) -> {
                txt_Max.setText(dinhDangGiaTheoVND(newValue.doubleValue()));
                System.out.println("Max: " + newValue.doubleValue());
            });
        });
    }

    //suKienChonKhoanGia
    private void suKienChonGia(){
        btn_10.setToggleGroup(priceGroup);
        btn_10_50.setToggleGroup(priceGroup);
        btn_50_100.setToggleGroup(priceGroup);
        btn_100_200.setToggleGroup(priceGroup);
        btn_200_500.setToggleGroup(priceGroup);
        btn_500.setToggleGroup(priceGroup);
        //Su kien khi chon gia
        priceGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (newToggle != null) {
                ToggleButton selectedButton = (ToggleButton) newToggle;
               if(selectedButton == btn_10) {
                   txt_Min.setText(dinhDangGiaTheoVND(0.0));
                   txt_Max.setText(dinhDangGiaTheoVND(10000.0));
                   range_slider.setLowValue(0);
                   range_slider.setHighValue(10000);
               } else if(selectedButton == btn_10_50) {
                   txt_Min.setText(dinhDangGiaTheoVND(10000.0));
                   txt_Max.setText(dinhDangGiaTheoVND(50000.0));
                   range_slider.setLowValue(10000);
                   range_slider.setHighValue(50000);
                } else if(selectedButton == btn_50_100) {
                    txt_Min.setText(dinhDangGiaTheoVND(50000.0));
                    txt_Max.setText(dinhDangGiaTheoVND(100000.0));
                    range_slider.setLowValue(50000);
                    range_slider.setHighValue(100000);
                } else if(selectedButton == btn_100_200) {
                    txt_Min.setText(dinhDangGiaTheoVND(100000.0));
                    txt_Max.setText(dinhDangGiaTheoVND(200000.0));
                    range_slider.setLowValue(100000);
                    range_slider.setHighValue(200000);
                } else if(selectedButton == btn_200_500) {
                    txt_Min.setText(dinhDangGiaTheoVND(200000.0));
                    txt_Max.setText(dinhDangGiaTheoVND(500000.0));
                    range_slider.setLowValue(200000);
                    range_slider.setHighValue(500000);
                } else if(selectedButton == btn_500) {
                    txt_Min.setText(dinhDangGiaTheoVND(500000.0));
                    txt_Max.setText(dinhDangGiaTheoVND(5000000.0));
                    range_slider.setLowValue(500000);
                    range_slider.setHighValue(5000000);
               }
                txt_Min.setDisable(true);
                txt_Max.setDisable(true);
                range_slider.setDisable(true);
            }
        });
    }


    private void formatAndSetSliderValue() {
        // Xóa ,000 VNĐ để lấy số nguyên người dùng nhập vào
        String inputMin = txt_Min.getText().replaceAll("[^-0-9]", "");
        String inputMax = txt_Max.getText().replaceAll("[^-0-9]", "");

        // Chuyển giá trị từ String sang Double
        giaMin = Double.parseDouble(inputMin);
        giaMax = Double.parseDouble(inputMax);

        // Cập nhật giá trị của RangeSlider
        if (giaMin < 0) {
            range_slider.setLowValue(0);
            txt_Min.setText(dinhDangGiaTheoVND(0.0));
            giaMin = 0;
        } else if (giaMin > 5000000) {
            range_slider.setLowValue(5000000);
            txt_Min.setText(dinhDangGiaTheoVND(5000000.0));
            giaMin = 5000000;
        } else {
            txt_Min.setText(dinhDangGiaTheoVND(giaMin));
            range_slider.setLowValue(giaMin);
        }
        if (giaMax > 5000000) {
            range_slider.setHighValue(5000000);
            txt_Max.setText(dinhDangGiaTheoVND(5000000.0));
            giaMax = 5000000;
        } else if (giaMax < 0) {
            range_slider.setHighValue(0);
            txt_Max.setText(dinhDangGiaTheoVND(0.0));
            giaMax = 0;
        } else {
            range_slider.setHighValue(giaMax);
            txt_Max.setText(dinhDangGiaTheoVND(giaMax));
        }
    }

    public List<SanPham> locSanPham() {
        String loaiSanPham = chb_LoaiSanPham.getValue();
        String nhomSanPham = chb_NhomSanPham.getValue();
        String donViTinh = chb_DonViTinh.getValue();
        String quocGiaSanXuat = chb_QuocGiaSanXuat.getValue();

        HashMap<String, String> dieuKienLoc = new HashMap<>();
        if(loaiSanPham != null) {
            List<LoaiSanPham> dsLoaiSanPham = LoaiSanPhamDAO.getInstance().getDanhSachLoaiSanPham();
            String maLoaiSanPham = dsLoaiSanPham.get(chb_LoaiSanPham.getSelectionModel().getSelectedIndex()).getMaLoaiSP();
            dieuKienLoc.put("maLoaiSanPham", maLoaiSanPham);
        }
        if(nhomSanPham != null) {
            List<NhomSanPham> dsNhomSanPham = NhomSanPhamDAO.getInstance().getDanhSachNhomSanPham();
            String maNhomSanPham = dsNhomSanPham.get(chb_NhomSanPham.getSelectionModel().getSelectedIndex()).getMaNhomSP();
            dieuKienLoc.put("maNhomSanPham", maNhomSanPham);
        }
        if(donViTinh != null) {
            List<DonViTinh> dsDonViTinh = DonViTinhDAO.getInstance().getDanhSachDonViTinh();
            String maDonViTinh = dsDonViTinh.get(chb_DonViTinh.getSelectionModel().getSelectedIndex()).getMaDonViTinh();
            dieuKienLoc.put("maDonViTinh", maDonViTinh);
        }
        if(quocGiaSanXuat != null) {
            dieuKienLoc.put("quocGiaSanXuat", quocGiaSanXuat);
        }
        dieuKienLoc.put("giaBatDau", String.valueOf(giaMin));
        dieuKienLoc.put("giaKetThuc", String.valueOf(giaMax));

        // Gọi hàm lấy danh sách sản phẩm theo điều kiện lọc
        List<SanPham> dsSanPham = SanPhamDAO.getInstance().timKiemSanPham(dieuKienLoc);
        return dsSanPham;
    }




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hienThiQuocGiaSanXuat();
        hienThiLoaiSanPham();
        hienThiNhomSanPham();
        hienThiDonViTinh();
        suKienChonGia();
        suKienChonKhoangGia();
        //suKienGiaMin
        // Đặt sự kiện khi nhấn Enter
        txt_Min.setOnAction(event -> {
            formatAndSetSliderValue();
        });

        // Đặt sự kiện khi bấm ra ngoài TextField
        txt_Min.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Bị mất focus
                formatAndSetSliderValue();
            }
        });

        //suKienGiaMax
        // Đặt sự kiện khi nhấn Enter
        txt_Max.setOnAction(event -> {
            formatAndSetSliderValue();
        });

        // Đặt sự kiện khi bấm ra ngoài TextField
        txt_Max.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Bị mất focus
                formatAndSetSliderValue();
            }
        });

        //suKienHuy
        btn_Huy.setOnAction(event -> {
            thoatKhoiTrang();
        });

        //suKienLamMoi
        btn_LamMoi.setOnAction(event -> {
            lamMoi();
        });

        //suKienLoc
        btn_Loc.setOnAction(event -> {
            formatAndSetSliderValue(); // Cập nhật giá trị giaMin và giaMax trước khi tìm kiếm
            List<SanPham> dsSanPham = locSanPham();
            // Hiển thị danh sách sản phẩm sau khi lọc
               Model.getInstance().getViewFactory().closeStage((Stage) btn_Loc.getScene().getWindow());
               Model.getInstance().hienThiDuLieuSauKhiLoc(dsSanPham);
        });


    }
}


