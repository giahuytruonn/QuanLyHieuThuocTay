package qlhtt.Controllers;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import qlhtt.ConnectDB.ConnectDB;
import qlhtt.Controllers.NhanVien.BanHangController;
import qlhtt.DAO.ChiTietHoaDonDAO;
import qlhtt.DAO.HoaDonDAO;
import qlhtt.DAO.PhieuNhapDAO;
import qlhtt.Entity.ChiTietHoaDon;
import qlhtt.Entity.HoaDon;
import qlhtt.Entity.KhachHang;
import qlhtt.Models.Model;
import qlhtt.ThongBao.ThongBao;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class XacNhanHoaDonController implements Initializable {
    @FXML
    public ImageView img_ThanhCong;
    @FXML
    public Button btn_XuatHoaDon;
    @FXML
    public Button btn_GuiHoaDon;
    @FXML
    public Button btn_Huy;

    ConnectDB connectDB = new ConnectDB();

    private HoaDonDAO hoaDonDAO = new HoaDonDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //ImageView img_ThanhCong = new ImageView();
        img_ThanhCong.setImage(new Image("file:src/main/resources/Fxml/Images/ThanhCong.png"));
        //Button btn_XuatHoaDon = new Button();
        btn_XuatHoaDon.setOnAction(event -> {
            HoaDon hoaDon = Model.getInstance().getHoaDon();
            if (hoaDon != null ) {
                xuatHoaDon(hoaDon);
            } else {
                xuatHoaDon(BanHangController.getHoaDonMoiNhat());
            }
        });
        btn_Huy.setOnAction(event -> {
            Model.getInstance().getBanHangController().datLai();
            Model.getInstance().getViewFactory().closeStage(
                    (Stage) img_ThanhCong.getScene().getWindow()
            );
            Model.getInstance().setHoaDon(null);
            Model.getInstance().getBanHangController().loadData(Model.getInstance().getHoaDon());
        });
        btn_GuiHoaDon.setOnAction(event -> {
            try {
                // phải lấy cái email từ hóa đơn
//                KhachHang khachHang = Model.getInstance().getTaiKhoan().getKhachHang();
                KhachHang khachHang = Model.getInstance().getKhachHang();
                HoaDon hoaDon = Model.getInstance().getHoaDon();
                if (khachHang.getMaKhachHang() != null) {
                    guiHoaDon(khachHang.getEmail(), hoaDon);
                    Model.getInstance().getViewFactory().closeStage(
                            (Stage) img_ThanhCong.getScene().getWindow()
                    );
                    Model.getInstance().getBanHangController().datLai();
                    ThongBao.thongBaoThanhCong("Gửi hóa đơn thành công");
                } else {
                    ThongBao.thongBaoLoi("Không thể gửi hóa đơn, Khách hàng không tồn tại");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }


    public void xuatHoaDon(HoaDon hoaDon) {
        String maHD = hoaDon.getMaHoaDon();
        try {
            File pdfFile = new File("src/main/resources/HoaDon/"+maHD+".pdf");
            if (pdfFile.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(pdfFile);
                } else {
                    System.out.println("Mở file không được hỗ trợ trên hệ thống này.");
                }
            } else {
                System.out.println("File PDF không tồn tại: " + "src/main/resources/HoaDon/"+maHD+".pdf");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void guiHoaDon(String gmail, HoaDon hoaDon) throws IOException {
        if (gmail != null) {
            Properties properties = new Properties();

            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");

            String myEmail = "duythvo2004@gmail.com";
            String recipientEmail = gmail;
            String password = "wdij irrl uetm nrbg";

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(myEmail, password);
                }
            });
            // sửa tùy theo hóa đơn
            String html = taoHtmlHoaDon(hoaDon);

            Message message = prepareMessage(session, myEmail, recipientEmail, "Thông tin hóa đơn", html);

            try {
                Transport.send(message);
                System.out.println("Message sent successfully");

            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    private static Message prepareMessage(Session session, String myEmail, String recipientEmail, String subject, String content) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            message.setSubject(subject);
            message.setContent(content, "text/html ; charset=utf-8");
            return message;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String taoHtmlHoaDon(HoaDon hoaDon) {
        // Lấy thông tin chi tiết hóa đơn
        StringBuilder thongTinDong = new StringBuilder();
        List<ChiTietHoaDon> chiTietHoaDonList = ChiTietHoaDonDAO.getInstance().getDsChiTietHoaDonBangMaHoaDon(hoaDon.getMaHoaDon());

        for (int i = 0; i < chiTietHoaDonList.size(); i++) {
            ChiTietHoaDon chiTiet = chiTietHoaDonList.get(i);
            thongTinDong.append("<tr>\n")
                    .append("    <td>").append(i + 1).append("</td>\n")
                    .append("    <td>").append(chiTiet.getSanPham().getTenSanPham()).append("</td>\n")
                    .append("    <td>").append(chiTiet.getSanPham().getDonViTinh().getTenDonViTinh()).append("</td>\n")
                    .append("    <td>").append(chiTiet.getSoLuong()).append("</td>\n")
                    .append("    <td>").append(String.format("%,.0f VNĐ", chiTiet.getTongTien()/chiTiet.getSoLuong())).append("</td>\n")
                    .append("    <td>").append(String.format("%,.0f VNĐ", hoaDon.getTongTienThanhToan())).append("</td>\n")
                    .append("</tr>\n");
        }

        // Lấy thông tin hóa đơn
        String tongGiaTriHoaDon = String.format("%,.0f VNĐ", hoaDon.getTongGiaTriHoaDon());
        String tongTienDaGiam = String.format("%,.0f VNĐ", hoaDon.getTienDaGiam());
        String tongTienThanhToan = String.format("%,.0f VNĐ", hoaDon.getTongTienThanhToan());
        String tongTienKhachTra = String.format("%,.0f VNĐ", hoaDon.getTongTienKhachTra());
        String tongTienTraLai = String.format("%,.0f VNĐ", hoaDon.getTongTienTraLai());
        String tenKhachHang = hoaDon.getKhachHang().getHoTen();
        String email = hoaDon.getKhachHang().getEmail();
        String ngayLap = hoaDon.getNgayTao().toString();
        String phuongThucThanhToan = String.valueOf(hoaDon.getPhuongThucThanhToan());
        String maHoaDon = hoaDon.getMaHoaDon();
        String nhanVienBanHang = hoaDon.getNhanVien().getTenNhanVien();

        // Trả về HTML
        return """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Hóa Đơn Bán Thuốc</title>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
            <style>
                body { font-family: Arial, sans-serif; }
                .highlight { color: #007bff; font-weight: bold; }
                .success-icon { color: #28a745; font-size: 2rem; }
                .total-row th, .total-row td { font-weight: bold; font-size: 1.2rem; background-color: #f8f9fa; }
                .text-important { color: #dc3545; }
                /* Đảm bảo hiển thị viền bảng */
                        table {
                            border-collapse: collapse;
                            width: 100%;
                            table-layout: fixed; /* Đảm bảo các cột có kích thước cố định */
                        }
                        
                        th, td {
                            border: 1px solid #dee2e6; /* Viền bảng */
                            padding: 8px;
                            text-align: left;
                            word-wrap: break-word; /* Chia nhỏ từ nếu quá dài */
                            overflow-wrap: break-word; /* Hỗ trợ trình duyệt khác */
                        }
                        
                        th {
                            background-color: #f8f9fa;
                        }
                        
                        /* Đảm bảo chữ dài vẫn xuống dòng */
                        td {
                            white-space: normal; /* Cho phép xuống dòng */
                            overflow: hidden; /* Ngăn nội dung tràn ra ngoài cột */
                        }
                        
                        /* Cho phép cuộn ngang trên màn hình nhỏ */
                        .table-responsive {
                            overflow-x: auto;
                        }
            </style>
        </head>
        <body>
        <div class="container my-5">
            <div class="text-center">
                <h2 style="color: red;"><b>Hóa Đơn Bán Thuốc</b></h2>
                <p class="text-muted">Cửa hàng thuốc tây 2H2D</p>
                <div class="my-3">
                    <i class="bi bi-check-circle-fill success-icon"></i>
                    <p class="text-success">Thanh toán thành công</p>
                </div>
            </div>
            <div class="mb-4">
                <p><strong>Mã hóa đơn:</strong> <span class="highlight">""" + maHoaDon + """
                <p><strong>Tên nhân viên:</strong> <span class="highlight">""" + nhanVienBanHang + """
                
                <p><strong>Tên khách hàng:</strong> <span class="highlight">""" + tenKhachHang + """
                </span></p>
                <p><strong>Email:</strong> <span class="highlight">
                """ + email +
                """
                </span></p>
                <p><strong>Ngày lập hóa đơn:</strong> <span class="highlight">""" + ngayLap + """
                </span></p>
                <p><strong>Phương thức thanh toán:</strong> <span class="text-important">""" + phuongThucThanhToan + """
            </span></p>
            </div>
            <div class="table-responsive">
                <table class="table table-bordered">
                    <thead class="table-light">
                        <tr>
                            <th>#</th>
                            <th>Tên thuốc</th>
                            <th>Đơn vị</th>
                            <th>Số lượng</th>
                            <th>Đơn giá</th>
                            <th>Thành tiền</th>
                        </tr>
                    </thead>
                    <tbody>
                        """ + thongTinDong + """
                    </tbody>
                    <tfoot>
                        <tr class="total-row">
                            <td colspan="6" class="text-start">Tổng giá trị hóa đơn: <span class="text-important">""" + tongGiaTriHoaDon + """
                        </span></td>
                        </tr>
                        <tr class="total-row">
                            <td colspan="6" class="text-start">Tổng tiền đã giảm: <span class="text-important">""" + tongTienDaGiam + """
                        </span></td>
                        </tr>
                        <tr class="total-row">
                            <td colspan="6" class="text-start">Tổng tiền thanh toán: <span class="text-important">""" + tongTienThanhToan + """
                        </span></td>
                        </tr>
                        <tr class="total-row">
                            <td colspan="6" class="text-start">Tổng tiền khách trả: <span class="text-important">""" + tongTienKhachTra + """
                        </span></td>
                        </tr>
                        <tr class="total-row">
                            <td colspan="6" class="text-start">Tổng tiền trả lại: <span class="text-important">""" + tongTienTraLai + """
                        </span></td>
                        </tr>
                    </tfoot>
                </table>
            </div>
            
            <p class="mt-4"><strong>Ghi chú:</strong> Giá đã bao gồm thuế GTGT</p>
            <p class="mt-4">Cảm ơn quý khách đã mua hàng tại cửa hàng chúng tôi. Hẹn gặp lại!</p>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.js"></script>
        </body>
        </html>
    """;
    }
}
