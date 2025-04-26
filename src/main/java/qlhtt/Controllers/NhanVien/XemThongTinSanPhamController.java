package qlhtt.Controllers.NhanVien;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import qlhtt.Entity.SanPham;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class XemThongTinSanPhamController implements Initializable {

    @FXML public TextField txt_MaSanPham;
    @FXML public TextField txt_DoiTuongSuDung;
    @FXML public TextField txt_HamLuong;
    @FXML public TextField txt_NhaSanXuat;
    @FXML public TextField txt_DangBaoChe;
    @FXML public TextField txt_DuongDung;
    @FXML public TextField txtDonGia;
    @FXML public TextArea txa_MoTaSanPham;
    @FXML public TextField txt_TenSanPham;
    @FXML public ChoiceBox chb_ChiDinhSuDung;
    @FXML public ChoiceBox chb_QuyCachDongGoi;
    @FXML public ChoiceBox chb_QuocGiaSanXuat;
    @FXML public Spinner spn_SoLuong;
    @FXML public ChoiceBox chb_LoaiSanPham;
    @FXML public ChoiceBox chb_NhomSanPham;
    @FXML public ChoiceBox chb_DonViTinh;
    @FXML public TextArea txa_ThanhPhan;

    private static final Dotenv dotenv = Dotenv.load();
    private static final String SERVER_HOST = dotenv.get("SERVER_HOST");// Địa chỉ server
    private static final int SERVER_PORT = Integer.parseInt(dotenv.get("SERVER_PORT"));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Có thể setup mặc định cho các ChoiceBox nếu cần
    }

    public void xemThongTinSanPham(String maSanPham) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Gửi yêu cầu tìm kiếm sản phẩm theo mã
            out.println("SEARCH " + maSanPham);

            // Nhận dữ liệu JSON từ server
            String jsonResponse = in.readLine();

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            // Ép kiểu về danh sách sản phẩm
            List<SanPham> danhSachSanPham = objectMapper.readValue(
                    jsonResponse,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, SanPham.class)
            );

            if (!danhSachSanPham.isEmpty()) {
                hienThiSanPham(danhSachSanPham.get(0));
            } else {
                System.out.println("Không tìm thấy sản phẩm.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hienThiSanPham(SanPham sanPham) {
        txt_MaSanPham.setText(sanPham.getMaSanPham());
        txt_TenSanPham.setText(sanPham.getTenSanPham());
        txt_DoiTuongSuDung.setText(sanPham.getDoiTuongSuDung());
        txt_HamLuong.setText(sanPham.getHamLuong());
        txt_NhaSanXuat.setText(sanPham.getNhaSanXuat());
        txt_DangBaoChe.setText(sanPham.getDangBaoChe());
        txt_DuongDung.setText(sanPham.getDuongDung());
        txtDonGia.setText(String.valueOf(sanPham.getDonGia()));
        txa_MoTaSanPham.setText(sanPham.getMoTaSanPham());
        chb_ChiDinhSuDung.setValue(sanPham.getChiDinhSuDung());
        chb_QuyCachDongGoi.setValue(sanPham.getQuyCachDongGoi());
        chb_QuocGiaSanXuat.setValue(sanPham.getQuocGiaSanXuat());

        if (spn_SoLuong.getValueFactory() == null) {
            spn_SoLuong.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000));
        }
        spn_SoLuong.getValueFactory().setValue(sanPham.getSoLuong());

        chb_LoaiSanPham.setValue(sanPham.getLoaiSanPham().getTenLoaiSP());
        chb_NhomSanPham.setValue(sanPham.getNhomSanPham().getTenNhomSP());
        chb_DonViTinh.setValue(sanPham.getDonViTinh().getTenDonViTinh());
        txa_ThanhPhan.setText(sanPham.getThanhPhan());
    }
}
