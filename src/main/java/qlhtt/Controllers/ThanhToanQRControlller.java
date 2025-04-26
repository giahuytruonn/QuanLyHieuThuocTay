package qlhtt.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import org.bouncycastle.math.raw.Mod;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import qlhtt.Controllers.NhanVien.*;
import qlhtt.Controllers.NhanVien.*;
import qlhtt.DAO.HoaDonDAO;
import qlhtt.Entity.ChiTietHoaDon;
import qlhtt.Entity.HoaDon;
import qlhtt.Models.Model;
import qlhtt.Service.ApiRequest;
import qlhtt.Service.ApiResponse;
import qlhtt.ThongBao.ThongBao;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;

public class ThanhToanQRControlller implements Initializable {
    @FXML
    public Button btn_XacNhan;
    @FXML
    public Button btn_Huy;
    @FXML
    public ImageView img_QR;
    private HoaDonController hoaDonController = new HoaDonController();
    private ChiTietHoaDonController chiTietHoaDonController = new ChiTietHoaDonController();
    private KhachHangController khachHangController = new KhachHangController();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_XacNhan.setOnAction(event -> {
            HoaDon hoaDon = Model.getInstance().getHoaDon();
            hoaDon.setTrangThaiHoaDon(true);
            BanHangController.updateHD(hoaDon);
            List<ChiTietHoaDon> dsChiTietHoaDon = Model.getInstance().getChiTietHoaDonList();
            BanHangController.updateDTL(Model.getInstance().getKhachHang().getMaKhachHang(),Model.getInstance().getKhachHang().getDiemTichLuy());
            BanHangController.deleteCTHD(hoaDon.getMaHoaDon());
            dsChiTietHoaDon.forEach(i->{
                BanHangController.createCTHD(i);
            });

            ChietKhauController chietKhauController = new ChietKhauController();
            BanHangController.updateSoLuongCK(hoaDon.getChietKhau());
//            ChietKhauController chietKhauController = new ChietKhauController();
            if(hoaDon.getChietKhau()!=null){
                BanHangController.updateSoLuongCK(hoaDon.getChietKhau());
            }

            if(Model.getInstance().getKhachHang()!=null){
                BanHangController.updateDTL(Model.getInstance().getKhachHang().getMaKhachHang(),Model.getInstance().getDiemTichLuy());
            }


            Model.getInstance().getViewFactory().closeStage(
                    (javafx.stage.Stage) img_QR.getScene().getWindow()
            );
            Model.getInstance().getViewFactory().hienThiTrangHoaDon(Model.getInstance().getKhachHang().getEmail());
            new BanHangController().suKienTaoHoaDon(hoaDon);
        });
        btn_Huy.setOnAction(event -> {
            Model.getInstance().getViewFactory().closeStage(
                    (javafx.stage.Stage) img_QR.getScene().getWindow()
            );
        });
    }

    public void hienThiQR(String giaTriThanhToan){
        if (giaTriThanhToan.isEmpty()) {
            ThongBao.getInstance().thongBaoLoi("Phải có  số tiền cần thanh toán.");
            return;
        }else{
            Dotenv dotenv = Dotenv.load();
            Integer amount = Integer.valueOf(giaTriThanhToan.trim());
            String accountNo = dotenv.get("ACCOUNT_NO");
            String accountName = dotenv.get("ACCOUNT_NAME");
            int acqId = Integer.parseInt(dotenv.get("ACQ_ID"));
            String addInfo = dotenv.get("ADD_INFO");
            String format = dotenv.get("FORMAT");
            String template = dotenv.get("TEMPLATE");

            //Khởi tạo request
            ApiRequest apiRequest = new ApiRequest();
            apiRequest.setAccountNo(Long.parseLong(accountNo));
            apiRequest.setAccountName(accountName);
            apiRequest.setAcqId(acqId);
            apiRequest.setAmount(amount);
            apiRequest.setAddInfo(addInfo);
            apiRequest.setFormat(format);
            apiRequest.setTemplate(template);

            try {
                String response = createRquest(apiRequest);
                // Chuyển đổi JSON thành đối tượng ApiResponse
                Gson gson = new Gson();
                ApiResponse dataResult = gson.fromJson(response, ApiResponse.class);
                // Set image cho ImageView
                BufferedImage image = base64ToImage(dataResult.getData().getQrDataURL());
                javafx.scene.image.Image fxImage = SwingFXUtils.toFXImage(image, null);
                img_QR.setImage(fxImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String createRquest(ApiRequest apiRequest) throws Exception {
        String apiUrl = "https://api.vietqr.io/v2/generate";
        // Tạo RestTemplate để thực hiện yêu cầu HTTP
        RestTemplate restTemplate = new RestTemplate();
        // Tạo headers cho request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(MediaType.parseMediaTypes("application/json"));
        // Chuyển đổi đối tượng ApiRequest thành JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(apiRequest);
        // Tạo HttpEntity với request body là JSON và headers đã thiết lập
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonRequest, headers);
        // Thực hiện yêu cầu POST
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
        // Trả về nội dung của response
        return response.getBody();
    }

    public BufferedImage base64ToImage(String base64String) {
        // Tách chuỗi Base64 thành phần dữ liệu
        String base64Image = base64String.split(",")[1]; // header như data:image/png;base64
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        try (ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes)) {
            BufferedImage image = ImageIO.read(bais);
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
