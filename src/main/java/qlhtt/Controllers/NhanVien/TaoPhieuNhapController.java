
package qlhtt.Controllers.NhanVien;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.cdimascio.dotenv.Dotenv;
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
import java.io.*;
import java.net.Socket;
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

    private static final Dotenv dotenv = Dotenv.load();
    private static final String SERVER_HOST = dotenv.get("SERVER_HOST");
    private static final int SERVER_PORT = Integer.parseInt(dotenv.get("SERVER_PORT"));

    private PhieuNhap phieuNhap = new PhieuNhap();
    private TaiKhoan taiKhoan = Model.getInstance().getTaiKhoan();

    ObservableList<ChiTietPhieuNhap> chiTietPhieuNhapList = FXCollections.observableArrayList();
    ObservableList<String> donViTinhList = FXCollections.observableArrayList();
    ObservableList<String> nhaCungCapList = FXCollections.observableArrayList();

    private Map<String, Double> thanhTienMap = new HashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper();
    int count = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        objectMapper.registerModule(new JavaTimeModule());
        setupTableColumns();
        setGiaTriCacComboBox();
        suKien();

        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().length() > 4) return null;
            if (!change.getControlNewText().matches("[0-9]*")) return null;
            return change;
        });
        soLuong_tf.setTextFormatter(formatter);

        TextFormatter<String> formatter1 = new TextFormatter<>(change -> {
            if (change.getControlNewText().length() > 9) return null;
            if (!change.getControlNewText().matches("[0-9]*")) return null;
            return change;
        });
        giaNhap_tf.setTextFormatter(formatter1);
    }

    private String sendRequestToServer(String request) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            out.println(request);
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    public void setGiaTriCacComboBox() {
        try {
            // Lấy danh sách đơn vị tính
            String donViTinhResponse = sendRequestToServer("GET_DONVITINH");
            if (!donViTinhResponse.equals("ERROR")) {
                List<DonViTinh> donViTinhs = objectMapper.readValue(donViTinhResponse, objectMapper.getTypeFactory().constructCollectionType(List.class, DonViTinh.class));
                donViTinhList.addAll(donViTinhs.stream().map(DonViTinh::getTenDonViTinh).toList());
                donViTinh_cb.setItems(donViTinhList);
                donViTinh_cb.setValue(donViTinhList.get(0));
            }

            // Lấy danh sách nhà cung cấp
            String nhaCungCapResponse = sendRequestToServer("GET_NHACUNGCAP");
            if (!nhaCungCapResponse.equals("ERROR")) {
                List<NhaCungCap> nhaCungCaps = objectMapper.readValue(nhaCungCapResponse, objectMapper.getTypeFactory().constructCollectionType(List.class, NhaCungCap.class));
                nhaCungCapList.addAll(nhaCungCaps.stream().map(NhaCungCap::getTenNhaCungCap).toList());
                nhaCungCap_cb.setItems(nhaCungCapList);
                nhaCungCap_cb.setValue(nhaCungCapList.get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
            ThongBao.thongBaoLoi("Lỗi khi lấy dữ liệu từ server");
        }
    }

    public ChiTietPhieuNhap checkValidAndGetChiTietPhieuNhap(String maSanPham) {
        if (maSanPham.isEmpty()) {
            ThongBao.thongBaoLoi("Mã sản phẩm không được để trống");
            return null;
        }

        // Kiểm tra sản phẩm tồn tại
        String sanPhamResponse = sendRequestToServer("GET_SANPHAM " + maSanPham);
        if (sanPhamResponse.equals("PRODUCT_NOT_FOUND") || sanPhamResponse.equals("ERROR")) {
            ThongBao.thongBaoLoi("Sản phẩm không tồn tại");
            return null;
        }

        try {
            SanPham sanPham = objectMapper.readValue(sanPhamResponse, SanPham.class);
            String donViTinh = donViTinh_cb.getSelectionModel().getSelectedItem();
            if (!sanPham.getDonViTinh().getTenDonViTinh().equals(donViTinh)) {
                ThongBao.thongBaoLoi("Đơn vị tính phải là " + sanPham.getDonViTinh().getTenDonViTinh());
                return null;
            }

            LocalDate dateHanSuDung = hanSuDung_dp.getValue();
            LocalDate dateSanXuat = ngaySanXuat_dp.getValue();
            if (dateSanXuat.isAfter(dateHanSuDung)) {
                ThongBao.thongBaoLoi("Ngày sản xuất phải trước hạn sử dụng");
                return null;
            }

            Period period = Period.between(dateSanXuat, dateHanSuDung);
            if (period.getMonths() < 3 && period.getYears() == 0 || (period.getMonths() == 3 && period.getDays() < 0 && period.getYears() == 0)) {
                ThongBaoLoi.thongBao("Hạn sử dụng phải hơn 3 tháng");
                return null;
            }

            if (dateHanSuDung == null || dateSanXuat == null) {
                ThongBao.thongBaoLoi("Hạn sử dụng hoặc ngày sản xuất không được để trống");
                return null;
            }

            String strSoLuong = soLuong_tf.getText().trim();
            int soLuong = 0;
            if (strSoLuong.isEmpty()) {
                ThongBao.thongBaoLoi("Số lượng không được để trống");
                return null;
            }
            if (!strSoLuong.matches("\\d+")) {
                ThongBao.thongBaoLoi("Giá trị số lượng không hợp lệ");
                return null;
            }
            soLuong = Integer.parseInt(strSoLuong);

            String strGiaNhap = giaNhap_tf.getText().trim();
            double giaNhap = 0.0;
            if (strGiaNhap.isEmpty()) {
                ThongBao.thongBaoLoi("Không được để trống giá nhập");
                return null;
            }
            if (!strGiaNhap.matches("\\d+(\\.\\d+)?")) {
                ThongBao.thongBaoLoi("Giá nhập phải là một số");
                return null;
            }
            giaNhap = Double.parseDouble(strGiaNhap);

            thanhTienMap.put(maSanPham, soLuong * giaNhap);
            return new ChiTietPhieuNhap(sanPham, soLuong, giaNhap, donViTinh, dateHanSuDung, dateSanXuat, thanhTienMap.get(maSanPham));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void taoPhieuNhap() {
        String nhaCungCap = nhaCungCap_cb.getSelectionModel().getSelectedItem();
        try {
            // Lấy thông tin nhân viên
            String nhanVienResponse = sendRequestToServer("GET_NHANVIEN " + taiKhoan.getNhanVien().getMaNhanVien());
            if (nhanVienResponse.equals("ERROR")) {
                ThongBao.thongBaoLoi("Lỗi khi lấy thông tin nhân viên");
                return;
            }
            NhanVien nhanVien = objectMapper.readValue(nhanVienResponse, NhanVien.class);

            // Lấy thông tin nhà cung cấp
            String nhaCungCapResponse = sendRequestToServer("GET_NHACUNGCAP_BY_TEN " + nhaCungCap);
            if (nhaCungCapResponse.equals("ERROR")) {
                ThongBao.thongBaoLoi("Lỗi khi lấy thông tin nhà cung cấp");
                return;
            }
            NhaCungCap nhaCungCapEntity = objectMapper.readValue(nhaCungCapResponse, NhaCungCap.class);

            // Tạo phiếu nhập
            phieuNhap = new PhieuNhap(nhanVien, nhaCungCapEntity, true, LocalDate.now(), thanhTienMap.values().stream().mapToDouble(Double::doubleValue).sum(), soLo_tf.getText());
            String phieuNhapJson = objectMapper.writeValueAsString(phieuNhap);
            String createResponse = sendRequestToServer("CREATE_PHIEUNHAP " + phieuNhapJson);
            if (!createResponse.equals("SUCCESS")) {
                ThongBao.thongBaoLoi("Lỗi khi tạo phiếu nhập");
                return;
            }

            // Lấy phiếu nhập vừa tạo
            String latestPhieuNhapResponse = sendRequestToServer("GET_PHIEUNHAP_MOINHAT");
            if (latestPhieuNhapResponse.equals("ERROR")) {
                ThongBao.thongBaoLoi("Lỗi khi lấy phiếu nhập vừa tạo");
                return;
            }
            phieuNhap = objectMapper.readValue(latestPhieuNhapResponse, PhieuNhap.class);

            addChiTietVaoPhieuNhap();
        } catch (Exception e) {
            e.printStackTrace();
            ThongBao.thongBaoLoi("Lỗi khi tạo phiếu nhập");
        }
    }

    public void addChiTietVaoPhieuNhap() {
        chiTietPhieuNhapList.forEach(i -> {
            i.setPhieuNhap(phieuNhap);
            try {
                String chiTietJson = objectMapper.writeValueAsString(i);
                String response = sendRequestToServer("CREATE_CHITIETPHIEUNHAP " + chiTietJson);
                if (!response.equals("SUCCESS")) {
                    ThongBao.thongBaoLoi("Lỗi khi tạo chi tiết phiếu nhập cho sản phẩm " + i.getSanPham().getMaSanPham());
                }
            } catch (Exception e) {
                e.printStackTrace();
                ThongBao.thongBaoLoi("Lỗi khi tạo chi tiết phiếu nhập");
            }
        });
    }

    public void suKienThemPhieuNhap() {
        themPhieuNhap_btn.setOnAction(event -> {
            if (!checkEmptyField()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Xác nhận");
                alert.setHeaderText("Bạn có muốn xuất phiếu nhập sau khi thêm không ?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    taoPhieuNhap();
                    String updateStatusResponse = sendRequestToServer("UPDATE_PHIEUNHAP_STATUS " + phieuNhap.getMaPhieuNhap() + " true");
                    if (!updateStatusResponse.equals("SUCCESS")) {
                        ThongBao.thongBaoLoi("Lỗi khi cập nhật trạng thái phiếu nhập");
                        return;
                    }
                    chiTietPhieuNhapList.forEach(i -> {
                        String updateSoLuongResponse = sendRequestToServer("UPDATE_SANPHAM_SOLUONG " + i.getSanPham().getMaSanPham() + " " + i.getSoLuong());
                        if (!updateSoLuongResponse.equals("SUCCESS")) {
                            ThongBao.thongBaoLoi("Lỗi khi cập nhật số lượng sản phẩm " + i.getSanPham().getMaSanPham());
                        }
                    });
                    count = 0;
                    resetAll();
                    ThongBao.thongBaoThanhCong("Thêm phiếu nhập thành công! Thực hiện xuất Phiếu Nhập!");
                    suKienXuatPhieuNhap();
                } else {
                    taoPhieuNhap();
                    String updateStatusResponse = sendRequestToServer("UPDATE_PHIEUNHAP_STATUS " + phieuNhap.getMaPhieuNhap() + " true");
                    if (!updateStatusResponse.equals("SUCCESS")) {
                        ThongBao.thongBaoLoi("Lỗi khi cập nhật trạng thái phiếu nhập");
                        return;
                    }
                    chiTietPhieuNhapList.forEach(i -> {
                        String updateSoLuongResponse = sendRequestToServer("UPDATE_SANPHAM_SOLUONG " + i.getSanPham().getMaSanPham() + " " + i.getSoLuong());
                        if (!updateSoLuongResponse.equals("SUCCESS")) {
                            ThongBao.thongBaoLoi("Lỗi khi cập nhật số lượng sản phẩm " + i.getSanPham().getMaSanPham());
                        }
                    });
                    count = 0;
                    resetAll();
                    ThongBao.thongBaoThanhCong("Thêm phiếu nhập thành công!");
                }
            }
        });
    }

    public void suKienXuatPhieuNhap() {
        String maPN = phieuNhap.getMaPhieuNhap();
        String response = sendRequestToServer("CREATE_PHIEUNHAP_PDF " + maPN);
        if (response.startsWith("SUCCESS")) {
            String pdfPath = response.split(" ")[1];
            File pdfFile = new File(pdfPath);
            if (pdfFile.exists()) {
                try {
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(pdfFile);
                    } else {
                        System.out.println("Mở file không được hỗ trợ trên hệ thống này.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    ThongBao.thongBaoLoi("Lỗi khi mở file PDF");
                }
            } else {
                ThongBao.thongBaoLoi("File PDF không tồn tại: " + pdfPath);
            }
        } else {
            ThongBao.thongBaoLoi("Lỗi khi xuất phiếu nhập PDF: " + response);
        }
    }

    public void suKienCapNhatNhaCungCap() {
        try {
            String response = sendRequestToServer("GET_NHACUNGCAP");
            if (!response.equals("ERROR")) {
                List<NhaCungCap> nhaCungCaps = objectMapper.readValue(response, objectMapper.getTypeFactory().constructCollectionType(List.class, NhaCungCap.class));
                ObservableList<String> nhaCungCapListNew = FXCollections.observableArrayList();
                nhaCungCapListNew.addAll(nhaCungCaps.stream().map(NhaCungCap::getTenNhaCungCap).toList());
                nhaCungCap_cb.setItems(nhaCungCapListNew);
                if (!nhaCungCapListNew.isEmpty()) {
                    nhaCungCap_cb.setValue(nhaCungCapListNew.get(0));
                }
            } else {
                ThongBao.thongBaoLoi("Lỗi khi cập nhật danh sách nhà cung cấp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ThongBao.thongBaoLoi("Lỗi khi cập nhật danh sách nhà cung cấp");
        }
    }

    public void suKien() {
        xoaHangDangChon();
        suKienReset();
        docFileExcel();
        suKienThemChiTietPhieuNhap();
        suKienClickTrenTable();
        suKienThemPhieuNhap();
        suKienThemNhaCungCap();
    }

    public void themDataVaoBang(ChiTietPhieuNhap chiTietPhieuNhap) {
        thanhTienMap.put(chiTietPhieuNhap.getSanPham().getMaSanPham(), chiTietPhieuNhap.getSoLuong() * chiTietPhieuNhap.getGiaNhap());
        chiTietPhieuNhapList.add(chiTietPhieuNhap);
        chiTietPhieuNhap_tableView.setItems(chiTietPhieuNhapList);
        count++;
        capNhatValueTong();
    }

    private void setupTableColumns() {
        if (stt_tblColumn != null) {
            stt_tblColumn.setCellValueFactory(cellData -> {
                int index = chiTietPhieuNhapList.indexOf(cellData.getValue()) + 1;
                return new SimpleStringProperty(String.valueOf(index));
            });
        }

        if (maSP_tblColumn != null) {
            maSP_tblColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSanPham().getMaSanPham()));
        }

        if (hanSuDung_tblColumn != null) {
            hanSuDung_tblColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getHanSuDung()));
            hanSuDung_tblColumn.setCellFactory(column -> new TableCell<ChiTietPhieuNhap, LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                    }
                }
            });
        }

        if (ngaySanXuat_tblColumn != null) {
            ngaySanXuat_tblColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getNgaySanXuat()));
            ngaySanXuat_tblColumn.setCellFactory(column -> new TableCell<ChiTietPhieuNhap, LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                    }
                }
            });
        }

        if (donViTinh_tblColumn != null) {
            donViTinh_tblColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDonViTinh()));
        }

        if (soLuong_tblColumn != null) {
            soLuong_tblColumn.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        }

        if (giaNhap_tblColumn != null) {
            giaNhap_tblColumn.setCellValueFactory(new PropertyValueFactory<>("giaNhap"));
        }

        if (thanhTien_tblColumn != null) {
            thanhTien_tblColumn.setCellValueFactory(cellData -> {
                String code = cellData.getValue().getSanPham().getMaSanPham();
                Double thanhTien = thanhTienMap.get(code);
                return new SimpleObjectProperty<>(thanhTien);
            });
        }
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

    public void suKienThemChiTietPhieuNhap() {
        them_btn.setOnAction(event -> {
            String maSP = maSanPham_tf.getText();
            if (!maSP.isEmpty()) {
                ChiTietPhieuNhap chiTietPhieuNhap = checkValidAndGetChiTietPhieuNhap(maSP);
                if (chiTietPhieuNhap != null) {
                    boolean sanPhamDaTonTai = false;
                    for (ChiTietPhieuNhap existingChiTiet : chiTietPhieuNhapList) {
                        if (existingChiTiet.getSanPham().getMaSanPham().equals(chiTietPhieuNhap.getSanPham().getMaSanPham())) {
                            existingChiTiet.setHanSuDung(chiTietPhieuNhap.getHanSuDung());
                            existingChiTiet.setNgaySanXuat(chiTietPhieuNhap.getNgaySanXuat());
                            existingChiTiet.setSoLuong(chiTietPhieuNhap.getSoLuong());
                            existingChiTiet.setGiaNhap(chiTietPhieuNhap.getGiaNhap());
                            thanhTienMap.put(maSP, existingChiTiet.getGiaNhap() * existingChiTiet.getSoLuong());
                            sanPhamDaTonTai = true;
                            break;
                        }
                    }
                    if (!sanPhamDaTonTai) {
                        themDataVaoBang(chiTietPhieuNhap);
                    }
                    chiTietPhieuNhap_tableView.refresh();
                    capNhatValueTong();
                    reset_Field();
                }
            } else {
                ThongBao.thongBaoLoi("Mã sản phẩm không được để trống");
            }
        });
    }

    public boolean capNhatChiTietPhieuNhap(String maSanPham) {
        ChiTietPhieuNhap chiTietPhieuNhap = checkValidAndGetChiTietPhieuNhap(maSanPham);
        if (chiTietPhieuNhap != null) {
            for (ChiTietPhieuNhap existingChiTiet : chiTietPhieuNhapList) {
                if (existingChiTiet.getSanPham().getMaSanPham().equals(chiTietPhieuNhap.getSanPham().getMaSanPham())) {
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

    public void capNhatValueTong() {
        valueSoLuong_lbl.setText(count + " sản phẩm");
        valueThanhTien_lbl.setText(thanhTienMap.values().stream().mapToDouble(Double::doubleValue).sum() + " VNĐ");
    }

    public void reset_Field() {
        maSanPham_tf.setText("");
        soLuong_tf.setText("");
        giaNhap_tf.setText("");
        hanSuDung_dp.setValue(null);
        ngaySanXuat_dp.setValue(null);
        donViTinh_cb.setValue(donViTinhList.stream().findFirst().orElse(null));
    }

    public void resetAll() {
        chiTietPhieuNhapList.clear();
        thanhTienMap.clear();
        chiTietPhieuNhap_tableView.refresh();
        capNhatValueTong();
        soLo_tf.setText("");
        nhaCungCap_cb.setValue(nhaCungCapList.stream().findFirst().orElse(null));
        reset_Field();
    }

    public boolean checkEmptyField() {
        if (soLo_tf.getText().isEmpty()) {
            ThongBao.thongBaoLoi("Số lô không được để trống");
            return true;
        }
        if (chiTietPhieuNhap_tableView.getItems().isEmpty()) {
            ThongBao.thongBaoLoi("Không tồn tại sản phẩm cần nhập nào trong danh sách.");
            return true;
        }
        return false;
    }

    public void docFileExcel() {
        import_btn.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Chọn file Excel");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel File", "*.xls", "*.xlsx"));
            File file = fileChooser.showOpenDialog(new Stage());
            if (file != null) {
                List<ChiTietPhieuNhap> dsChiTietPhieuNhap = ExcelReader.getInstance().getDsChiTietPhieuNhap(file);
                if (!dsChiTietPhieuNhap.isEmpty()) {
                    dsChiTietPhieuNhap.forEach(this::themDataVaoBang);
                    capNhatValueTong();
                    ChiTietPhieuNhap chiTietPhieuNhap = dsChiTietPhieuNhap.stream().findFirst().orElse(null);
                    nhaCungCap_cb.setValue(chiTietPhieuNhap.getPhieuNhap().getNhaCungCap().getTenNhaCungCap());
                    soLo_tf.setText(chiTietPhieuNhap.getPhieuNhap().getSoLo());
                }
            } else {
                ThongBao.thongBaoLoi("Vui lòng chọn đúng file.xlsx");
            }
        });
    }

    public void suKienClickTrenTable() {
        chiTietPhieuNhap_tableView.setOnMouseClicked(event -> {
            ChiTietPhieuNhap chiTietPhieuNhap = (ChiTietPhieuNhap) chiTietPhieuNhap_tableView.getSelectionModel().getSelectedItem();
            if (chiTietPhieuNhap != null) {
                maSanPham_tf.setText(chiTietPhieuNhap.getSanPham().getMaSanPham());
                soLuong_tf.setText(chiTietPhieuNhap.getSoLuong() + "");
                String giaNhap = chiTietPhieuNhap.getGiaNhap() / 10 + "";
                giaNhap = giaNhap.replace(".", "");
                giaNhap_tf.setText(giaNhap);
                hanSuDung_dp.setValue(chiTietPhieuNhap.getHanSuDung());
                ngaySanXuat_dp.setValue(chiTietPhieuNhap.getNgaySanXuat());
                donViTinh_cb.setValue(chiTietPhieuNhap.getDonViTinh());
            }
        });
    }

    public void suKienThemNhaCungCap() {
        themNhaCungCap_btn.setOnAction(actionEvent -> {
            Model.getInstance().setTaoPhieuNhapController(this);
            Model.getInstance().getViewFactory().hienThiThemNhaCungCap((Stage) themNhaCungCap_btn.getScene().getWindow());
        });
    }
}
