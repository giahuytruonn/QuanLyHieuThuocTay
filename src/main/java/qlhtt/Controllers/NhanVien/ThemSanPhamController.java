package qlhtt.Controllers.NhanVien;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import qlhtt.DAO.DonViTinhDAO;
import qlhtt.DAO.LoaiSanPhamDAO;
import qlhtt.DAO.NhomSanPhamDAO;
import qlhtt.DAO.SanPhamDAO;
import qlhtt.Entity.DonViTinh;
import qlhtt.Entity.LoaiSanPham;
import qlhtt.Entity.NhomSanPham;
import qlhtt.Entity.SanPham;
import qlhtt.Enum.ChiDinhSuDung;
import qlhtt.Models.Model;
import qlhtt.ThongBao.ThongBao;
import qlhtt.ThongBaoLoi.ThongBaoLoi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ThemSanPhamController implements Initializable {
    @FXML
    public TextField txt_TenSanPham;
    @FXML
    public TextField txt_DoiTuongSuDung;
    @FXML
    public TextField txt_HamLuong;
    @FXML
    public TextField txt_NhaSanXuat;
    @FXML
    public TextField txt_DangBaoChe;
    @FXML
    public TextField txt_DuongDung;
    @FXML
    public TextArea txa_MoTaSanPham;
    @FXML
    public ChoiceBox<String> chb_QuyCachDongGoi;
    @FXML
    public ChoiceBox<String> chb_ChiDinhSuDung;
    @FXML
    public ChoiceBox<String> chb_QuocGiaSanXuat;
    @FXML
    public ChoiceBox<String> chb_LoaiSanPham;
    @FXML
    public ChoiceBox<String> chb_NhomSanPham;
    @FXML
    public ChoiceBox<String> chb_DonViTinh;
    @FXML
    public TextArea txa_ThanhPhan;
    @FXML
    public Button btn_Them;
    @FXML
    public Button btn_Huy;
    @FXML
    public Button btn_LamMoi;
    @FXML
    public TextField txt_MaSanPham;

    private static final Dotenv dotenv = Dotenv.load();

    public void thoatKhoiTrang(){
        Model.getInstance().getViewFactory().closeStage((Stage) btn_Huy.getScene().getWindow());
    }

    public Boolean validData(){
        String maSanPham = txt_MaSanPham.getText();
        if(SanPhamDAO.getInstance().getSanPhamBangMaSanPham(maSanPham) != null){
            ThongBaoLoi.thongBao("Mã sản phẩm đã tồn tại");
            txt_MaSanPham.requestFocus();
            return false;
        }
        if(txt_MaSanPham.getText().isEmpty()){
            ThongBaoLoi.thongBao("Mã sản phẩm không được để trống");
            txt_MaSanPham.requestFocus();
            return false;
        }else{
            if(!(txt_MaSanPham.getText().matches("[0-9]{13}"))){
                ThongBaoLoi.thongBao("Mã sản phẩm phải là số và có 13 kí tự");
                txt_MaSanPham.requestFocus();
                return false;
            }
        }

        if(txt_TenSanPham.getText().isEmpty()){
            ThongBaoLoi.thongBao("Tên sản phẩm không được để trống");
            txt_TenSanPham.requestFocus();
            return false;
        }


        if(txt_DoiTuongSuDung.getText().isEmpty()){
            ThongBaoLoi.thongBao("Đối tượng sử dụng không được để trống");
            txt_DoiTuongSuDung.requestFocus();
            return false;
        }

        if(txt_HamLuong.getText().isEmpty()){
            ThongBaoLoi.thongBao("Hàm lượng không được để trống");
            txt_HamLuong.requestFocus();
            return false;
        }

        if(txt_NhaSanXuat.getText().isEmpty()){
            ThongBaoLoi.thongBao("Nhà sản xuất không được để trống");
            txt_NhaSanXuat.requestFocus();
            return false;
        }

        if(txt_DangBaoChe.getText().isEmpty()){
            ThongBaoLoi.thongBao("Dạng bào chế không được để trống");
            txt_DangBaoChe.requestFocus();
            return false;
        }

        if(txt_DuongDung.getText().isEmpty()){
            ThongBaoLoi.thongBao("Đường dùng không được để trống");
            txt_DuongDung.requestFocus();
            return false;
        }


        if(txa_MoTaSanPham.getText().isEmpty()){
            ThongBaoLoi.thongBao("Mô tả sản phẩm không được để trống");
            txa_MoTaSanPham.requestFocus();
            return false;
        }

        if(chb_QuyCachDongGoi.getValue().isEmpty()){
            ThongBaoLoi.thongBao("Quy cách đóng gói không được để trống");
            chb_QuyCachDongGoi.requestFocus();
            return false;
        }

        if(chb_ChiDinhSuDung.getValue().isEmpty()){
            ThongBaoLoi.thongBao("Chỉ định sử dụng không được để trống");
            chb_ChiDinhSuDung.requestFocus();
            return false;
        }

        if(chb_QuocGiaSanXuat.getValue().isEmpty()){
            ThongBaoLoi.thongBao("Quốc gia sản xuất không được để trống");
            chb_QuocGiaSanXuat.requestFocus();
            return false;
        }


        if(chb_LoaiSanPham.getValue().isEmpty()){
            ThongBaoLoi.thongBao("Loại sản phẩm không được để trống");
            chb_LoaiSanPham.requestFocus();
            return false;
        }

        if(chb_NhomSanPham.getValue().isEmpty()){
            ThongBaoLoi.thongBao("Nhóm sản phẩm không được để trống");
            chb_NhomSanPham.requestFocus();
            return false;
        }

        if(chb_DonViTinh.getValue().isEmpty()){
            ThongBaoLoi.thongBao("Đơn vị tính không được để trống");
            chb_DonViTinh.requestFocus();
            return false;
        }

        if(txa_ThanhPhan.getText().isEmpty()){
            ThongBaoLoi.thongBao("Thành phần không được để trống");
            txa_ThanhPhan.requestFocus();
            return false;
        }
        return true;
    }

    public void themSanPham() {
        if (validData()) {
            try (Socket socket = new Socket(dotenv.get("SERVER_HOST"), Integer.parseInt(dotenv.get("SERVER_PORT")));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                SanPham sanPham = new SanPham();
                sanPham.setMaSanPham(txt_MaSanPham.getText());
                sanPham.setTenSanPham(txt_TenSanPham.getText());
                sanPham.setDoiTuongSuDung(txt_DoiTuongSuDung.getText());
                sanPham.setHamLuong(txt_HamLuong.getText());
                sanPham.setNhaSanXuat(txt_NhaSanXuat.getText());
                sanPham.setDangBaoChe(txt_DangBaoChe.getText());
                sanPham.setDuongDung(txt_DuongDung.getText());
                sanPham.setMoTaSanPham(txa_MoTaSanPham.getText());
                sanPham.setQuyCachDongGoi(chb_QuyCachDongGoi.getValue());
                sanPham.setChiDinhSuDung(
                        chb_ChiDinhSuDung.getValue().equals("Kê toa") ?
                                ChiDinhSuDung.KE_TOA :
                                ChiDinhSuDung.KHONG_KE_TOA);
                sanPham.setQuocGiaSanXuat(chb_QuocGiaSanXuat.getValue());
                sanPham.setLoaiSanPham(LoaiSanPhamDAO.getInstance().getLoaiSanPhamBangTenLoaiSanPham(chb_LoaiSanPham.getValue()));
                sanPham.setNhomSanPham(NhomSanPhamDAO.getInstance().getNhomSanPhamBangTenNhomSanPham(chb_NhomSanPham.getValue()));
                sanPham.setDonViTinh(DonViTinhDAO.getInstance().getDonViTinhBangTenDonViTinh(chb_DonViTinh.getValue()));
                sanPham.setThanhPhan(txa_ThanhPhan.getText());

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
                String sanPhamJson = objectMapper.writeValueAsString(sanPham);

                out.println("THEM_SANPHAM " + sanPhamJson);

                String response = in.readLine();
                if ("OK".equals(response)) {
                    ThongBao.thongBaoThanhCong("Thêm sản phẩm thành công");
                    Model.getInstance().getViewFactory().closeStage((Stage) btn_Them.getScene().getWindow());
                    Model.getInstance().lamMoiDanhSachSanPham();
                } else {
                    ThongBaoLoi.thongBao("Thêm sản phẩm thất bại");
                }

            } catch (IOException e) {
                e.printStackTrace();
                ThongBaoLoi.thongBao("Không thể kết nối tới server");
            }
        }
    }


    public void lamMoi(){
        txt_MaSanPham.requestFocus();
        txt_MaSanPham.setText("");
        txt_TenSanPham.setText("");
        txt_DoiTuongSuDung.setText("");
        txt_HamLuong.setText("");
        txt_NhaSanXuat.setText("");
        txt_DangBaoChe.setText("");
        txt_DuongDung.setText("");
        txa_MoTaSanPham.setText("");
        chb_QuyCachDongGoi.setValue("Viên");
        chb_ChiDinhSuDung.setValue("Kê toa");
        chb_QuocGiaSanXuat.setValue("Vietnam");
        chb_LoaiSanPham.setValue("Thực phẩm chức năng");
        chb_NhomSanPham.setValue("Vitamin");
        chb_DonViTinh.setValue("Viên");
        txa_ThanhPhan.setText("");
    }

    public void hienThiChiDinhSuDung(){
        chb_ChiDinhSuDung.getItems().add("Kê toa");
        chb_ChiDinhSuDung.getItems().add("Không kê toa");
        chb_ChiDinhSuDung.setValue("Kê toa");
    }

    public void hienThiQuyCachDongGoi(){
        List<DonViTinh> dsQuyCachDongGoi = DonViTinhDAO.getInstance().getDanhSachDonViTinh();
        for(DonViTinh donViTinh : dsQuyCachDongGoi){
            chb_QuyCachDongGoi.getItems().add(donViTinh.getTenDonViTinh());
        }
        chb_QuyCachDongGoi.setValue(dsQuyCachDongGoi.get(0).getTenDonViTinh());
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
        chb_QuocGiaSanXuat.setValue("Vietnam");
    }

    public void hienThiLoaiSanPham(){
        List<LoaiSanPham> dsLoaiSanPham = LoaiSanPhamDAO.getInstance().getDanhSachLoaiSanPham();
        for(LoaiSanPham loaiSanPham : dsLoaiSanPham){
            chb_LoaiSanPham.getItems().add(loaiSanPham.getTenLoaiSP());
        }
        chb_LoaiSanPham.setValue(dsLoaiSanPham.get(0).getTenLoaiSP());
    }

    public void hienThiNhomSanPham(){
        List<NhomSanPham> dsNhomSanPham = NhomSanPhamDAO.getInstance().getDanhSachNhomSanPham();
        for(NhomSanPham nhomSanPham : dsNhomSanPham){
            chb_NhomSanPham.getItems().add(nhomSanPham.getTenNhomSP());
        }
        chb_NhomSanPham.setValue(dsNhomSanPham.get(0).getTenNhomSP());
    }

    public void hienThiDonViTinh(){
        List<DonViTinh> dsDonViTinh = DonViTinhDAO.getInstance().getDanhSachDonViTinh();
        for(DonViTinh donViTinh : dsDonViTinh){
            chb_DonViTinh.getItems().add(donViTinh.getTenDonViTinh());
        }
        chb_DonViTinh.setValue(dsDonViTinh.get(0).getTenDonViTinh());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hienThiChiDinhSuDung();
        hienThiQuyCachDongGoi();
        hienThiQuocGiaSanXuat();
        hienThiLoaiSanPham();
        hienThiNhomSanPham();
        hienThiDonViTinh();
        btn_Huy.setOnAction(event -> thoatKhoiTrang());
        btn_Them.setOnAction(event -> themSanPham());
        btn_LamMoi.setOnAction(event ->
                lamMoi()
        );
    }


}
