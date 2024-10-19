package qlhtt.Controllers.NguoiQuanLy;




import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import qlhtt.Entity.ApiRequest;
import qlhtt.Entity.ApiResponse;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

public class NguoiQuanLyController {

    @FXML
    private TextField soTien;

    @FXML
    private Button taoMa;

    @FXML
    private ImageView imgView;

    @FXML
    public void initialize() {
        taoMa.setOnAction(event -> {
            Integer amount = Integer.valueOf(soTien.getText().trim());
            String accountNo = "1033547785";
            String accountName = "VO THAI DUY";
            int acqId = 970436;
            String addInfo = "Xin ti tien an com di";
            String format = "text";
            String template = "compact";

            if (soTien.getText().isEmpty()) {
                // Nếu trường soTien trống, hiển thị thông báo hoặc xử lý theo cách của bạn
                System.out.println("Vui lòng nhập số tiền vào trường.");
                return;
            }

            ApiRequest apiRequest = new ApiRequest();
            apiRequest.setAccountNo(Long.parseLong(accountNo));
            apiRequest.setAccountName(accountName);
            apiRequest.setAcqId(acqId);
            apiRequest.setAmount(amount);
            apiRequest.setAddInfo(addInfo);
            apiRequest.setFormat(format);
            apiRequest.setTemplate(template);

            if (!soTien.getText().isEmpty()) {
                try {
                   String response = createRquest(apiRequest);
                    // Chuyển đổi JSON thành đối tượng ApiResponse
                    Gson gson = new Gson();
                    ApiResponse dataResult = gson.fromJson(response, ApiResponse.class);
                    // Set image cho ImageView
                    BufferedImage image = base64ToImage(dataResult.getData().getQrDataURL());
                    javafx.scene.image.Image fxImage = SwingFXUtils.toFXImage(image, null);
                    imgView.setImage(fxImage);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Vui lòng nhập số tiền vào trường.");
            }
        });
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
