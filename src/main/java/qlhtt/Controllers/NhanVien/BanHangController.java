package qlhtt.Controllers.NhanVien;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.sf.jasperreports.engine.*;
import qlhtt.ConnectDB.ConnectDB;
import qlhtt.DAO.HoaDonDAO;
import qlhtt.Entity.*;
import qlhtt.Enum.ChiDinhSuDung;
import qlhtt.Enum.PhuongThucThanhToan;
import qlhtt.Models.Model;
import qlhtt.ThongBao.ThongBao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class BanHangController implements Initializable {

    @FXML
    public TextField diemApDung_tf;
    @FXML
    public Button datLai_btn;
    @FXML
    private Button lapHoaDonTamThoi_btn;

    @FXML
    public Button thanhToan_btn;

    @FXML
    private TextField maSanPham_tf;

    @FXML
    private TableView sanPham_tableView;

    @FXML
    private TableColumn<SanPham, String> stt_tblColumn;

    @FXML
    private TableColumn<SanPham, String> maSP_tblColumn;

    @FXML
    private TableColumn<SanPham, String> tenSP_tblColumn;

    @FXML
    private TableColumn<SanPham, String> donViTinh_tblColumn;

    @FXML
    private TableColumn<SanPham, Integer> soLuong_tblColumn;

    @FXML
    private TableColumn<SanPham, String> chiDinhSuDung_tblColumn;

    @FXML
    private TableColumn<SanPham, Double> thanhTien_tblColumn;

    @FXML
    private Button xoa_btn;

    @FXML
    private Button reset_btn;

    @FXML
    private Button themKhachHang_btn;

    @FXML
    private Button timKiemKhachHang_btn;

    @FXML
    private TextField soDienThoai_tf;

    @FXML
    private Label diemTichLuy_lbl;

    @FXML
    private Label tenKhachHang_lbl;

    @FXML
    private TextField maChietKhau_tf;

    @FXML
    private Label tongTien_lbl;


    @FXML
    private Label thanhTien_lbl;

    @FXML
    private ComboBox<PhuongThucThanhToan> phuongThuc_cb;

    @FXML
    private Button tien1_btn;

    @FXML
    private Button tien2_btn;

    @FXML
    private Button tien3_btn;

    @FXML
    private Button tien4_btn;

    @FXML
    private TextField khachDua_tf;

    @FXML
    private TextField tienTraLai_tf;

    private final SanPhamController sanPhamController = new SanPhamController();

    private final KhachHangController khachHangController = new KhachHangController();

    private final ChietKhauController chietKhauController = new ChietKhauController();

    private final ChiTietPhieuNhapController chiTietPhieuNhapController = new ChiTietPhieuNhapController();

    private final ChiTietHoaDonController chiTietHoaDonController = new ChiTietHoaDonController();

    private final HoaDonController hoaDonController = new HoaDonController();

    private final Map<String, Integer> soLuongConLaiCuaSanPham = new HashMap<>();

    private final Map<String, Integer> soLuongMap = new HashMap<String, Integer>();

    private final Map<String, Double> thanhTienMap = new HashMap<>();


    private KhachHang khachHang;
    private Button[] buttons;
    private HoaDon hoaDon;

    long lastKeyPressTime = 0;
    final long SCAN_THRESHOLD = 50;

    double tienGiam = 0;
    double thanhTien = 0;
    double tienChietKhau = 0;
    int diemTichLuy = 0;
    double tongTien = 0;
    PhuongThucThanhToan phuongThucThanhToan = PhuongThucThanhToan.TIENMAT;
    double tienTraLai = 0;
    double tienKhachDua = 0;
    double tongHoaDon = 0;
    ChietKhau chietKhau;

    final float VAT = 0.08f;

    ObservableList<SanPham> sanPhamList = FXCollections.observableArrayList();

    HoaDonDAO hoaDonDAO = new HoaDonDAO();
    ConnectDB connectDB = new ConnectDB();



    private static final Dotenv dotenv = Dotenv.load();
    private static final String SERVER_HOST = dotenv.get("SERVER_HOST");// Địa chỉ server
    private static final int SERVER_PORT = Integer.parseInt(dotenv.get("SERVER_PORT"));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().setBanHangController(this);
        suKien();

        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            // Giới hạn chiều dài văn bản
            if (change.getControlNewText().length() > 9) {
                return null;
            }

            if (!change.getControlNewText().matches("[0-9]*")) {
                return null;
            }

            return change;
        });
        khachDua_tf.setTextFormatter(formatter);
    }

    public void suKien() {
        suKienTimKiem();
        suKienSpinner();
        suKienXoa();
        suKienKiemTraThanhVien();
        suKienApMaChietKhauVaDiemTichLuy();
        suKienDiemApDung();
        suKienChonPhuongThucThanhToan();
        suKienLamMoi();
        datLai_btn.setDisable(true);
        if (hoaDon != null) {
            datLai_btn.setDisable(false);
        }
        suKienDatLai();
        phuongThuc_cb.getItems().addAll(PhuongThucThanhToan.values());
        phuongThuc_cb.getSelectionModel().select(PhuongThucThanhToan.TIENMAT);
        diemApDung_tf.setDisable(true);
        suKienTienTraLai();
        suKienTienGoiY();
        suKienThanhToan();
        suKienThemKhachHang();
        suKienLuuHoaDonTamThoi();
    }


    public void suKienThemKhachHang() {
        themKhachHang_btn.setOnAction(event -> {
            Model.getInstance().getViewFactory().hienThiTrangThemKhachHang((Stage) themKhachHang_btn.getScene().getWindow());
        });
    }

    public void suKienTimKiem() {
//        maSanPham_tf.setOnKeyReleased(event -> {
//            timKiemBangQuetMa();
//        });
        maSanPham_tf.setOnAction(event -> {
            timKiemBangNhap();
        });
    }

    public void suKienKiemTraThanhVien() {
        timKiemKhachHang_btn.setOnAction(event -> {
            String soDienThoai = soDienThoai_tf.getText().trim();
            String temp = khachHang == null ? "" : khachHang.getSoDienThoai();
            khachHang = getKhachHangBySoDienThoai(soDienThoai);
            if (khachHang != null) {
                tenKhachHang_lbl.setText(khachHang.getHoTen());
                diemTichLuy_lbl.setText(khachHang.getDiemTichLuy() + "");
                diemApDung_tf.setDisable(false);
            } else {
                boolean checkKeToa = sanPhamList.stream().anyMatch(i -> i.getChiDinhSuDung() == ChiDinhSuDung.KE_TOA);
                if (!checkKeToa) {
                    diemApDung_tf.setDisable(true);
                    diemTichLuy_lbl.setText("");
                    tenKhachHang_lbl.setText("");
                } else {
                    ThongBao.thongBaoLoi("Đang có thuốc kê toa trong danh sách không thể hủy thành viên");
                    khachHang = getKhachHangBySoDienThoai(temp);
                    soDienThoai_tf.setText(khachHang.getSoDienThoai());
                }
            }

            if (!soDienThoai.isEmpty() && khachHang == null) {
                ThongBao.thongBaoLoi("Không tìm thấy khách hàng");
            }
        });
    }

    public void suKienSpinner() {
        soLuong_tblColumn.setCellFactory(new Callback<TableColumn<SanPham, Integer>, TableCell<SanPham, Integer>>() {
            @Override
            public TableCell<SanPham, Integer> call(TableColumn<SanPham, Integer> param) {
                return new TableCell<SanPham, Integer>() {
                    private final Spinner<Integer> spinner = new Spinner<>(1, 100, 1); // Giới hạn từ 1 đến 100

                    @Override
                    protected void updateItem(Integer soLuong, boolean empty) {
                        super.updateItem(soLuong, empty);

                        if (empty || soLuong == null) {
                            setGraphic(null);
                        } else {
                            // Đặt giá trị cho spinner
                            spinner.getValueFactory().setValue(soLuong);

                            // Xóa listener cũ nếu có
                            spinner.valueProperty().removeListener(valueChangeListener);

                            // Thêm listener cho spinner
                            spinner.valueProperty().addListener(valueChangeListener);

                            setGraphic(spinner); // Hiển thị spinner
                        }
                    }

                    // Định nghĩa listener bên ngoài để dễ dàng gỡ bỏ
                    private final ChangeListener<Integer> valueChangeListener = (obs, oldValue, newValue) -> {
                        int index = getIndex();
                        if (index >= 0 && index < getTableView().getItems().size()) {
                            String code = getTableView().getItems().get(index).getMaSanPham().trim();

                            // Lấy số lượng còn lại cho mã sản phẩm
                            int soLuongConLai = getSanPhamById(code).getSoLuong();
                            // Kiểm tra xem số lượng mới có hợp lệ không
                            if (newValue.intValue() <= soLuongConLai) {
                                soLuongMap.put(code, newValue.intValue()); // Cập nhật giá trị trong soLuongMap
                                double donGia = getSanPhamById(code).getDonGia();
                                double thanhTien = newValue.doubleValue() * donGia;
                                thanhTienMap.put(code, thanhTien);

                                // Cập nhật giá trị cho cột thanh tiền
                                thanhTien_tblColumn.setCellValueFactory(cellData -> {
                                    String code1 = cellData.getValue().getMaSanPham();
                                    Double value = thanhTienMap.getOrDefault(code1, 0.0);
                                    return new SimpleObjectProperty<>(value);
                                });

                                getTableView().refresh(); // Cập nhật bảng
                                capNhatThongTinHoaDon();
                            } else {
                                // Nếu số lượng lớn hơn số lượng còn lại, đặt lại giá trị của spinner
                                spinner.getValueFactory().setValue(soLuongConLai);
                                ThongBao.thongBaoLoi("Số lượng không đủ");
                            }
                        }
                    };


                };
            }
        });
    }

    public void suKienXoa() {
        xoa_btn.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận");
            alert.setHeaderText("Bạn có chắc chắn xóa sản phẩm này không?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                SanPham sanPham = (SanPham) sanPham_tableView.getSelectionModel().getSelectedItem();
                if (sanPham != null) {
                    sanPhamList.remove(sanPham);
                    soLuongMap.remove(sanPham.getMaSanPham());
                    thanhTienMap.remove(sanPham.getMaSanPham());
                    sanPham_tableView.refresh();
                    capNhatThongTinHoaDon();
                    kiemTraPhuongThucThanhToan();
                }
            }
        });
    }

    public void suKienTienGoiY() {
        buttons = new Button[]{tien1_btn, tien2_btn, tien3_btn, tien4_btn};

        for (Button button : buttons) {
            button.setOnAction(event -> {
                String txt = button.getText().replace(".", "");
                khachDua_tf.setText(txt);
                tinhTienTraLai();
            });
        }
    }

    public void suKienTienTraLai() {
        khachDua_tf.setOnAction(event -> {
            if (!sanPhamList.isEmpty()) {
                tinhTienTraLai();
            } else {
                ThongBao.thongBaoLoi("Chưa có sản phẩm nào");
            }
        });
    }

    public void themDataVaoBang(SanPham sanPham) {
        if (checkChiDinhSuDungThuoc(sanPham)) {
            stt_tblColumn.setCellValueFactory(cellData -> {
                int index = cellData.getTableView().getItems().indexOf(cellData.getValue());
                return new SimpleStringProperty(String.valueOf(index + 1));
            });
            maSP_tblColumn.setCellValueFactory(new PropertyValueFactory<>("maSanPham"));
            tenSP_tblColumn.setCellValueFactory(new PropertyValueFactory<>("tenSanPham"));
            chiDinhSuDung_tblColumn.setCellValueFactory(cellData -> {
                SanPham sp = cellData.getValue();
                return new SimpleStringProperty(sp.getChiDinhSuDung().toString());
            });
            donViTinh_tblColumn.setCellValueFactory(cellData -> {
                SanPham sp = cellData.getValue();
                return new SimpleStringProperty(sp.getDonViTinh().getTenDonViTinh());
            });

            soLuong_tblColumn.setCellValueFactory(cellData -> {
                String code = cellData.getValue().getMaSanPham();
                Integer soLuong = soLuongMap.getOrDefault(code, 0);
                return new SimpleObjectProperty<>(soLuong);
            });

            thanhTien_tblColumn.setCellValueFactory(cellData -> {
                String code = cellData.getValue().getMaSanPham();
                Double thanhTien = thanhTienMap.get(code);
                return new SimpleObjectProperty<>(thanhTien);
            });

            sanPhamList.add(sanPham);

            sanPham_tableView.setItems(sanPhamList);
            capNhatThongTinHoaDon();
        } else {
            ThongBao.thongBaoLoi("Thuốc kê toa cần có thông tin của khách hàng");
            soLuongMap.remove(sanPham.getMaSanPham());
            thanhTienMap.remove(sanPham.getMaSanPham());
        }
        sanPham_tableView.refresh();
    }

    public boolean checkChiDinhSuDungThuoc(SanPham sanPham) {
        if (khachHang != null) {
            return true;
        } else {
            return sanPham.getChiDinhSuDung() != ChiDinhSuDung.KE_TOA;
        }
    }

//    public void timKiemBangQuetMa() {
//        long currentTime = System.currentTimeMillis();
//        String code = maSanPham_tf.getText().trim();
//
////        SanPham sanPham = sanPhamController.getSanPhamById(code);
//        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
//             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
//
//            // Gửi yêu cầu đăng nhập tới server
//            String request = String.format("GET_SANPHAM %s ", code);
//            out.println(request);
//
//            //Nhan dữ liệu từ server
//            String response = in.readLine();
//
//            //Chuyen doi Json sang SanPham
//            ObjectMapper objectMapper = new ObjectMapper();
//            SanPham sanPham = objectMapper.readValue(response, SanPham.class);
//            // Nếu thời gian nhập giữa các ký tự < ngưỡng, coi là quét mã
//            if ((currentTime - lastKeyPressTime) < SCAN_THRESHOLD && !code.isEmpty()) {
//                if (sanPham != null) {
//                    if(kiemTraVaCapNhatSoLuong(code)){
//                        if(soLuongMap.get(code) == 1){
//                            themDataVaoBang(sanPham);
//                        }else{
//                            sanPham_tableView.refresh();
//                        }
//                    }else{
//                        ThongBao.thongBaoLoi("Sản phẩm không đủ số lượng");
//                    }
//                    maSanPham_tf.clear();
//                }
//            }
//            lastKeyPressTime = currentTime;
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public void timKiemBangNhap() {
        String code = maSanPham_tf.getText().trim();

        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Gửi yêu cầu đăng nhập tới server
            String request = String.format("GET_SANPHAM %s ", code);
            out.println(request);

            //Nhan dữ liệu từ server
            String response = in.readLine();
            if(response.equals("PRODUCT_NOT_FOUND")){
                ThongBao.thongBaoLoi("Sản phẩm không tồn tại vui lòng nhập lại mã");
                return;
            }else{
                ObjectMapper objectMapper = new ObjectMapper();
                SanPham sanPham = objectMapper.readValue(response, SanPham.class);
                if (sanPham != null) {
                    if (kiemTraVaCapNhatSoLuong(code)) {
                        if (soLuongMap.get(code) == 1) {
                            themDataVaoBang(sanPham);
                        } else {
                            sanPham_tableView.refresh();
                        }
                    } else {
                        ThongBao.thongBaoLoi("Sản phẩm không đủ số lượng");
                    }
                    maSanPham_tf.clear();
                } else {
                    ThongBao.thongBaoLoi("Sản phẩm này không tồn tại vui lòng nhập lại mã");
                }
            }
            //Chuyen doi Json sang SanPham


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean kiemTraVaCapNhatSoLuong(String maSanPham) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Gửi yêu cầu đăng nhập tới server
            String request = String.format("GET_CTPNNOW %s ", maSanPham);
            out.println(request);
            //Nhan dữ liệu từ server
            String response = in.readLine();
            //Chuyen doi Json sang ChiTietPhieuNhap
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            ChiTietPhieuNhap chiTietPhieuNhap = objectMapper.readValue(response, ChiTietPhieuNhap.class);
            if (chiTietPhieuNhap != null) {

                String json = objectMapper.writeValueAsString(chiTietPhieuNhap);
                request = String.format("UPDATE_PRICE %s ", json);
                out.println(request);
                //Nhan dữ liệu từ server
                response = in.readLine();


                request = String.format("GET_SANPHAM %s ", maSanPham);
                out.println(request);
                //Nhan dữ liệu từ server
                response = in.readLine();
                //Chuyen doi Json sang SanPham
                SanPham sanPham = objectMapper.readValue(response, SanPham.class);
                soLuongMap.put(maSanPham, soLuongMap.getOrDefault(maSanPham, 0));
                if (soLuongMap.get(maSanPham) == 0) {
                    if (sanPham.getSoLuong() >= soLuongMap.get(maSanPham) + 1) {
                        soLuongMap.put(maSanPham, soLuongMap.get(maSanPham) + 1);
                        thanhTienMap.put(maSanPham, sanPham.getDonGia() * soLuongMap.get(maSanPham));
                        return true;
                    }
                } else {
                    if (sanPham.getSoLuong() >= soLuongMap.get(maSanPham) + 1) {
                        soLuongMap.put(maSanPham, soLuongMap.get(maSanPham) + 1);
                        thanhTienMap.put(maSanPham, sanPham.getDonGia() * soLuongMap.get(maSanPham));
                        return true;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return false;
    }

    public void suKienLamMoi() {
        reset_btn.setOnAction(event -> {
            lamMoi();
            kiemTraPhuongThucThanhToan();
        });
    }

    public void suKienDatLai() {
        datLai_btn.setOnAction(event -> {
            if (hoaDon != null) {
                datLai();
                kiemTraPhuongThucThanhToan();
                datLai_btn.setDisable(true);
            }
        });
    }

    //
    public void lamMoi() {
        sanPhamList.clear();
        soLuongMap.clear();
        thanhTienMap.clear();
        sanPham_tableView.refresh();
        soDienThoai_tf.clear();
        tenKhachHang_lbl.setText("");
        diemTichLuy_lbl.setText("");
        khachDua_tf.setText("");
        tienTraLai_tf.setText("");
        chietKhau = null;
        khachHang = null;
        tienGiam = 0;
        diemTichLuy = 0;
        tienChietKhau = 0;
        capNhatThongTinHoaDon();

    }

    public void datLai() {
        lamMoi();
        hoaDon = null;
    }

    //
//
    public void capNhatThongTinHoaDon() {
        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        tongTien = thanhTienMap.values().stream().mapToDouble(Double::doubleValue).sum();
        tongTien_lbl.setText(nf.format(tongTien) + " VNĐ");
        thanhTien = tongTien * (1 + VAT);

        thanhTien = thanhTien - tienGiam;
        thanhTien = Math.round(thanhTien);
        System.out.println(thanhTien);
        thanhTien_lbl.setText(nf.format(thanhTien) + " VNĐ");
        kiemTraPhuongThucThanhToan();
    }


    public void suKienDiemApDung() {
        diemApDung_tf.setOnAction(event -> {
            if (khachHang != null) {
                apMaChietKhauVaDiemTichLuy();
            } else {
                throw new IllegalArgumentException("asdbasdbasd");
            }
        });
    }

    //
    public double doiDiemTichLuy(int diem) {
        return (double) diem * 1000;
    }

    public void suKienApMaChietKhauVaDiemTichLuy() {
        maChietKhau_tf.setOnAction(event -> {
            if (maChietKhau_tf.getText().isEmpty()) {
                apMaChietKhauVaDiemTichLuy();
            } else {
                if (thanhTien != 0) {
                    apMaChietKhauVaDiemTichLuy();
                } else {
                    ThongBao.thongBaoLoi("Thành tiền bằng 0 không thể áp mã giảm giá");
                }
            }
        });
    }

    public void apMaChietKhauVaDiemTichLuy() {
        String diem = diemApDung_tf.getText().trim();
        int diemApDung = diem.isEmpty() ? 0 : Integer.parseInt(diem);
        double result = thanhTien + tienGiam;

        String maChietKhau = maChietKhau_tf.getText().trim();
        ChietKhau chietKhau = getCK(maChietKhau);

        if (diemApDung != 0) {
            if (result >= 50000) {
                if (diemApDung <= khachHang.getDiemTichLuy()) {
                    tienGiam = doiDiemTichLuy(diemApDung);
                    diemTichLuy = diemApDung;
                    result -= tienGiam;
                    if (result < (result + tienGiam) * 0.5) {
                        result += tienGiam;
                        tienGiam -= doiDiemTichLuy(diemApDung);
                        diemTichLuy = 0;
                        ThongBao.thongBaoLoi("Điểm chỉ có thể giảm được 50% hóa đơn và bạn chỉ có thể sử dụng dưới " + (int) result / 2 / 1000 + " điểm");
                    }
                    System.out.println(result);
                } else {
                    ThongBao.thongBaoLoi("Điểm áp dụng không được lớn hơn điểm tích lũy của khách hàng.");
                    tienGiam = 0;
                }
            } else {
                ThongBao.thongBaoLoi("Hóa đơn cần trên 50.000 VNĐ để áp dụng điểm tích lũy");
                tienGiam = 0;
            }

            if (chietKhau != null) {
                if (!checkChietKhau(chietKhau)) {
                    tienChietKhau = result * chietKhau.getGiaTriChietKhau();
                    tienGiam += tienChietKhau;
                    result -= tienGiam;
                } else {
                    ThongBao.thongBaoLoi("Mã giảm giá này đã quá hạn");
                }
            }

        } else {
            if (chietKhau != null) {
                if (!checkChietKhau(chietKhau)) {
                    tienChietKhau = result * chietKhau.getGiaTriChietKhau();
                    tienGiam = tienChietKhau;
                    result -= tienGiam;
                }
            } else {
                tienGiam = 0;
            }
            diemTichLuy = 0;
        }
        if (chietKhau == null && !maChietKhau_tf.getText().isEmpty()) {
            ThongBao.thongBaoLoi("Không tồn tại mã giảm giá này");
        }
        capNhatThongTinHoaDon();
    }

    //
    public void suKienChonPhuongThucThanhToan() {
        phuongThuc_cb.setOnAction(actionEvent -> {
            phuongThucThanhToan = phuongThuc_cb.getSelectionModel().getSelectedItem();
            kiemTraPhuongThucThanhToan();
        });
    }

    //
    public boolean checkChietKhau(ChietKhau chietKhau) {
        if (chietKhau.getSoLuong() <= 0) {
            ThongBao.thongBaoLoi("Số lượng chiết khấu đã hết");
            return true;
        } else {
            if (chietKhau.getNgayKetThucApDung().isBefore(LocalDate.now())) {
                ThongBao.thongBaoLoi("Mã giảm giá đã hết hạn");
                return true;
            }
        }
        return false;
    }

    //
    public boolean kiemTraPhuongThucThanhToan() {
        if (phuongThucThanhToan == PhuongThucThanhToan.TIENMAT) {
            setGiaTienGoiY(thanhTien);
            khachDua_tf.setDisable(false);
            return true;
        }
        for (Button button : buttons) {
            button.setText("");
            button.setCancelButton(true);
            khachDua_tf.setDisable(true);
        }
        khachDua_tf.setText("");
        tienTraLai_tf.setText("");
        return false;
    }

    public void setGiaTienGoiY(double totalAmount) {
        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        double[] denominations = {1000, 2000, 5000, 10000, 20000, 50000, 100000, 200000, 500000, 1000000, 2000000, 5000000};

        // Danh sách kết quả gợi ý
        List<Double> suggestions = new ArrayList<>();

        // Thuật toán tham lam: Tìm mệnh giá lớn hơn hoặc bằng tổng tiền
        boolean isLargeAmount = totalAmount > 5000000; // Kiểm tra nếu tiền lớn hơn 5 triệu
        if (isLargeAmount) {
            // Làm tròn lên đến bội số 1 triệu
            double roundedAmount = ((totalAmount + 999999) / 1000000) * 1000000;
            suggestions.add(roundedAmount);

            // Gợi ý các bội số lớn hơn của 5 triệu
            for (double i = roundedAmount + 5000000; i <= roundedAmount + 20000000; i += 5000000) {
                suggestions.add(i);
            }
        } else {
            // Dùng danh sách mệnh giá thông thường
            for (double denomination : denominations) {
                if (denomination >= totalAmount) {
                    suggestions.add(denomination); // Thêm mệnh giá lớn hơn hoặc bằng
                    break;
                }
            }
            // Thêm các mệnh giá lớn hơn
            for (double denomination : denominations) {
                if (denomination > suggestions.get(0)) {
                    suggestions.add(denomination);
                }
            }
        }
        buttons = new Button[]{tien1_btn, tien2_btn, tien3_btn, tien4_btn};
        if (tongTien != 0) {
            tien1_btn.setText(nf.format(thanhTien == 0 ? "" : thanhTien));
            if (thanhTien == suggestions.get(0)) {
                tien2_btn.setText(nf.format(suggestions.get(1)));
                tien3_btn.setText(nf.format(suggestions.get(2)));
                tien4_btn.setText(nf.format(suggestions.get(3)));
            } else {
                tien2_btn.setText(nf.format(suggestions.get(0)));
                tien3_btn.setText(nf.format(suggestions.get(1)));
                tien4_btn.setText(nf.format(suggestions.get(2)));
            }
        } else {
            tien1_btn.setText("");
            tien2_btn.setText("");
            tien3_btn.setText("");
            tien4_btn.setText("");
        }

        for (Button button : buttons) {
            button.setCancelButton(false);
        }

    }

    public void tinhTienTraLai() {
        String tien = khachDua_tf.getText().trim();

        tien = tien.replace(".", "");

        tienKhachDua = tien.isEmpty() ? 0 : Double.parseDouble(tien);
        System.out.println(tienKhachDua);
        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        if (tienKhachDua >= thanhTien) {
            tienTraLai = tienKhachDua - thanhTien;
            tienTraLai_tf.setText(nf.format(tienTraLai) + " VNĐ");
        } else {
            ThongBao.thongBaoLoi("Tiền khách hàng đưa không được nhỏ hơn thành tiền");
        }
    }

    public void setThongTinThanhToan(HoaDon hoaDon) {
        hoaDon = Model.getInstance().getHoaDon();
        if (hoaDon != null) {
            String maChietKhau = hoaDon.getChietKhau().getMaChietKhau();
            maChietKhau_tf.setText(maChietKhau != null ? maChietKhau : "");

            if (hoaDon.getKhachHang() != null) {
                String soDienThoai = hoaDon.getKhachHang().getSoDienThoai();
                khachHang = getKhachHangBySoDienThoai(soDienThoai);
                if (khachHang != null) {
                    tenKhachHang_lbl.setText(khachHang.getHoTen());
                    diemTichLuy_lbl.setText(khachHang.getDiemTichLuy() + "");
                    soDienThoai_tf.setText(khachHang.getSoDienThoai());
                    diemApDung_tf.setDisable(false);
                    capNhatThongTinHoaDon();
                }
            } else {
                diemApDung_tf.setDisable(true);
            }

            try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                // Gửi yêu cầu đăng nhập tới server
                String request = String.format("GET_LISTCTHD %s ", hoaDon.getMaHoaDon());
                out.println(request);

                //Nhan dữ liệu từ server
                String response = in.readLine();

                //Chuyen doi Json sang SanPham
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                List<ChiTietHoaDon> chiTietHoaDonList = objectMapper.readValue(response, List.class);
                chiTietHoaDonList.forEach(i->{
                    SanPham sanPham = getSanPhamById(i.getSanPham().getMaSanPham());
                    if (sanPham.getSoLuong() >= i.getSoLuong()) {
                        soLuongMap.put(i.getSanPham().getMaSanPham(), i.getSoLuong() - 1);
                        thanhTienMap.put(i.getSanPham().getMaSanPham(), i.getTongTien());
                    } else {
                        soLuongMap.put(i.getSanPham().getMaSanPham(), 0);
                        thanhTienMap.put(i.getSanPham().getMaSanPham(), i.getSanPham().getDonGia());
                    }
                    themDataVaoBang(getSanPhamById(i.getSanPham().getMaSanPham()));
                    kiemTraVaCapNhatSoLuong(sanPham.getMaSanPham());
                });
                phuongThuc_cb.setValue(hoaDon.getPhuongThucThanhToan());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //
    public void suKienThanhToan() {
        thanhToan_btn.setOnAction(event -> {
            Model.getInstance().setHoaDon(hoaDon);
            if (phuongThuc_cb.getSelectionModel().getSelectedItem().equals(PhuongThucThanhToan.QRCODE)) {
                if (!sanPhamList.isEmpty()) {
                    if (khachHang != null) {
                        Model.getInstance().setKhachHang(khachHang);
                    }
                    Model.getInstance().getViewFactory().hienMaQR((int) thanhTien + "");
                    xacNhanThanhToanQR();
                    diemApDung_tf.setText("");
                    maChietKhau_tf.setText("");
                    tienTraLai_tf.setText("");
                    khachDua_tf.setText("");
                } else {
                    ThongBao.thongBaoLoi("Không có sản phẩm để thanh toán");
                }
            } else {
                if (!tienTraLai_tf.getText().isEmpty()) {
                    if (khachHang != null) {
                        Model.getInstance().setKhachHang(khachHang);
                        Model.getInstance().getViewFactory().hienThiTrangHoaDon(khachHang.getEmail());
                    } else {
                        Model.getInstance().getViewFactory().hienThiTrangHoaDon(null);
                    }
                    xacNhanThanhToanTienMat();
                    Model.getInstance().setHoaDon(getHoaDonMoiNhat());
                    diemApDung_tf.setText("");
                    maChietKhau_tf.setText("");
                } else {
                    if (sanPhamList.isEmpty()) {
                        ThongBao.thongBaoLoi("Không có sản phẩm để thanh toán");
                    } else {
                        if (tienTraLai_tf.getText().isEmpty()) {
                            ThongBao.thongBaoLoi("Tiền trả lại chưa có");
                        }
                    }
                }

            }
        });
    }

    public void xacNhanThanhToanTienMat() {
        if (hoaDon == null) {
            HoaDon hoaDon1 = new HoaDon(true, LocalDate.now(), thanhTien + tienGiam, tienGiam, thanhTien, tienKhachDua, phuongThuc_cb.getSelectionModel().getSelectedItem(), tienTraLai, Model.getInstance().getTaiKhoan().getNhanVien(), chietKhau, khachHang);
            createHoaDon(hoaDon1);
            deleteCTHD(getHoaDonMoiNhat().getMaHoaDon());
            sanPhamList.forEach(i -> {
                ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon(i, getHoaDonMoiNhat(), thanhTienMap.get(i.getMaSanPham()), soLuongMap.get(i.getMaSanPham()));
                createCTHD(chiTietHoaDon);
            });
            suKienTaoHoaDon(getHoaDonMoiNhat());
        } else {
            hoaDon = new HoaDon(hoaDon.getMaHoaDon(), true, hoaDon.getNgayTao(), thanhTien + tienGiam, tienGiam, thanhTien, tienKhachDua, phuongThuc_cb.getSelectionModel().getSelectedItem(), tienTraLai, hoaDon.getNhanVien(), hoaDon.getKhachHang(), hoaDon.getChietKhau());
            updateHD(hoaDon);
            deleteCTHD(hoaDon.getMaHoaDon());
            sanPhamList.forEach(i -> {
                ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon(i, hoaDon, thanhTienMap.get(i.getMaSanPham()), soLuongMap.get(i.getMaSanPham()));
                createCTHD(chiTietHoaDon);
            });
            suKienTaoHoaDon(hoaDon);
        }


        if (khachHang != null) {
            System.out.println(khachHang);
            ;
            int diem = khachHang.getDiemTichLuy() - diemTichLuy + (int) (thanhTien / 10000);
            System.out.println(diem);
            updateDTL(khachHang.getMaKhachHang(), diem);
        }

        if (chietKhau != null) {
            updateSoLuongCK(chietKhau);
        }
        Model.getInstance().setBanHangController(this);
    }

    public void xacNhanThanhToanQR() {
        if (hoaDon == null) {
            HoaDon hoaDon1 = new HoaDon(false, LocalDate.now(), thanhTien + tienGiam, tienGiam, thanhTien, thanhTien, phuongThuc_cb.getSelectionModel().getSelectedItem(), 0.0, Model.getInstance().getTaiKhoan().getNhanVien(), chietKhau, khachHang);
            createHoaDon(hoaDon1);
            Model.getInstance().setHoaDon(getHoaDonMoiNhat());
            List<ChiTietHoaDon> chiTietHoaDonList = new ArrayList<>();
            sanPhamList.forEach(i -> {
                ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon(i, getHoaDonMoiNhat(), thanhTienMap.get(i.getMaSanPham()), soLuongMap.get(i.getMaSanPham()));
                chiTietHoaDonList.add(chiTietHoaDon);
            });
            Model.getInstance().setChiTietHoaDonList(chiTietHoaDonList);
        } else {
            hoaDon = new HoaDon(hoaDon.getMaHoaDon(), false, hoaDon.getNgayTao(), thanhTien + tienGiam, tienGiam, thanhTien, thanhTien, phuongThuc_cb.getSelectionModel().getSelectedItem(), 0.0, hoaDon.getNhanVien(), hoaDon.getKhachHang(), hoaDon.getChietKhau());
            Model.getInstance().setHoaDon(hoaDon);
            List<ChiTietHoaDon> chiTietHoaDonList = new ArrayList<>();
            sanPhamList.forEach(i -> {
                ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon(i, hoaDon, thanhTienMap.get(i.getMaSanPham()), soLuongMap.get(i.getMaSanPham()));
                chiTietHoaDonList.add(chiTietHoaDon);
            });
            Model.getInstance().setChiTietHoaDonList(chiTietHoaDonList);
        }
        if (khachHang != null) {
            int diem = khachHang.getDiemTichLuy() - diemTichLuy + (int) (thanhTien / 10000);
            Model.getInstance().setKhachHang(khachHang);
            Model.getInstance().setDiemTichLuy(diem);
        }

        Model.getInstance().setBanHangController(this);
    }

    public void suKienTaoHoaDon(HoaDon JRhoaDon) {
        String maHD = JRhoaDon.getMaHoaDon();
        try {
            connectDB.connect();
            Connection connection = connectDB.getConnection();

            // Biên dịch báo cáo Jasper
            JasperReport jasperReport = JasperCompileManager.compileReport("src/main/resources/Fxml/MauHoaDon.jrxml");

            // Đặt tham số cho báo cáo
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("ReportTitle", "My Report Title");
            parameters.put("maHoaDonParam", maHD);  // Truyền maHoaDon vào báo cáo
            // Điền dữ liệu vào báo cáo
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

            // Xuất báo cáo ra file PDF
            JasperExportManager.exportReportToPdfFile(jasperPrint, "src/main/resources/HoaDon/" + maHD + ".pdf");

            System.out.println("src/main/resources/HoaDon/" + maHD + ".pdf");

        } catch (JRException | SQLException e) {
            e.printStackTrace();
        }
    }

    //
    public void suKienLuuHoaDonTamThoi() {
        lapHoaDonTamThoi_btn.setOnAction(event -> {
            if (!sanPhamList.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Xác nhận");
                alert.setHeaderText("Bạn có chắc chắn muốn lưu tạm thời hóa đơn này không?");

                // Hiển thị thông báo và chờ người dùng phản hồi
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    if (hoaDon != null) {
                        hoaDon = new HoaDon(hoaDon.getMaHoaDon(), false, LocalDate.now(), thanhTien + tienGiam, tienGiam, thanhTien, tienKhachDua, phuongThuc_cb.getSelectionModel().getSelectedItem(), tienTraLai, Model.getInstance().getTaiKhoan().getNhanVien(), khachHang, chietKhau);
                        updateHD(hoaDon);
                        deleteCTHD(hoaDon.getMaHoaDon());
                        sanPhamList.forEach(i -> {
                            ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon(i, hoaDon, thanhTienMap.get(i.getMaSanPham()), soLuongMap.get(i.getMaSanPham()));
                            createCTHD(chiTietHoaDon);
                        });
                    } else {
                        HoaDon hoaDon1 = new HoaDon(false, LocalDate.now(), thanhTien + tienGiam, tienGiam, thanhTien, thanhTien, phuongThuc_cb.getSelectionModel().getSelectedItem(), 0.0, Model.getInstance().getTaiKhoan().getNhanVien(), chietKhau, khachHang);
                        createHoaDon(hoaDon1);
                        sanPhamList.forEach(i -> {
                            ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon(i, getHoaDonMoiNhat(), thanhTienMap.get(i.getMaSanPham()), soLuongMap.get(i.getMaSanPham()));
                            createCTHD(chiTietHoaDon);
                        });
                    }
                    datLai();
                    diemApDung_tf.setText("");
                    maChietKhau_tf.setText("");
                    ThongBao.thongBaoThanhCong("Lưu tạm hóa đơn thành công.");
                    khachHang = null;
                }
            } else {
                ThongBao.thongBaoLoi("Không có sản phẩm để lưu tạm");
            }
        });
    }

    //
    public void loadData(HoaDon hoaDon1) {
        this.hoaDon = hoaDon1;
        Model.getInstance().setHoaDon(hoaDon1);
        setThongTinThanhToan(hoaDon1);
        datLai_btn.setDisable(hoaDon1 == null);

    }

    public void capNhatChietKhau() {
        updateSoLuongCK(chietKhau);
    }

    public KhachHang getKhachHangBySoDienThoai(String soDienThoai) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Gửi yêu cầu đăng nhập tới server
            String request = String.format("GET_KHACHHANGBYSODIENTHOAI %s ", soDienThoai);
            out.println(request);

            //Nhan dữ liệu từ server
            String response = in.readLine();

            //Chuyen doi Json sang SanPham
            if(response.equals("NOT_FOUND")){
                return null;
            }else{
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                KhachHang khachHang1 = objectMapper.readValue(response, KhachHang.class);

                if (khachHang1 != null) {
                    return khachHang1;
                } else {
                    ThongBao.thongBaoLoi("Không tìm thấy khách hàng này");
                    return null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SanPham getSanPhamById(String id) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Gửi yêu cầu đăng nhập tới server
            String request = String.format("GET_SANPHAM %s ", id);
            out.println(request);

            //Nhan dữ liệu từ server
            String response = in.readLine();

            //Chuyen doi Json sang SanPham
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            SanPham sanPham = objectMapper.readValue(response, SanPham.class);

            if (sanPham != null) {
                return sanPham;
            } else {
                ThongBao.thongBaoLoi("Không tìm thấy khách hàng này");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static HoaDon getHoaDonMoiNhat() {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Gửi yêu cầu đăng nhập tới server
            String request = String.format("GET_HOADONMOINHAT");
            out.println(request);

            //Nhan dữ liệu từ server
            String response = in.readLine();

            //Chuyen doi Json sang SanPham
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            HoaDon hoaDon1 = objectMapper.readValue(response, HoaDon.class);

            if (hoaDon1 != null) {
                return hoaDon1;
            } else {
                ThongBao.thongBaoLoi("Không tìm thấy khách hàng này");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HoaDon createHoaDon(HoaDon hoaDon1) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Gửi yêu cầu đăng nhập tới server
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String json = objectMapper.writeValueAsString(hoaDon1);

            String request = String.format("CREATE_HOADON %s ",json);
            out.println(request);

            //Nhan dữ liệu từ server
            String response = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void deleteCTHD(String maHoaDon) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Gửi yêu cầu đăng nhập tới server
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            String request = String.format("DELETE_CTHD %s ",maHoaDon);
            out.println(request);

            //Nhan dữ liệu từ server
            String response = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ChiTietHoaDon createCTHD(ChiTietHoaDon chiTietHoaDon) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Gửi yêu cầu đăng nhập tới server
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String json = objectMapper.writeValueAsString(chiTietHoaDon);

            String request = String.format("CREATE_CTHD %s ",json);
            out.println(request);

            //Nhan dữ liệu từ server
            String response = in.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updateHD(HoaDon hoaDon1) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Gửi yêu cầu đăng nhập tới server
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String json = objectMapper.writeValueAsString(hoaDon1);

            String request = String.format("UPDATE_HD %s ",json);
            out.println(request);

            //Nhan dữ liệu từ server
            String response = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateDTL(String maKH, int diem) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Gửi yêu cầu đăng nhập tới server
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            String request = String.format("UPDATE_DTL %s %d ",maKH,diem);
            out.println(request);

            //Nhan dữ liệu từ server
            String response = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateSoLuongCK(ChietKhau chietKhau) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Gửi yêu cầu đăng nhập tới server
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String json = objectMapper.writeValueAsString(chietKhau);
            String request = String.format("UPDATE_DTL %s ",json);
            out.println(request);

            //Nhan dữ liệu từ server
            String response = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ChietKhau getCK(String maChietKhau){
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Gửi yêu cầu đăng nhập tới server
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String request = String.format("GET_CK %s ",maChietKhau);
            out.println(request);

            //Nhan dữ liệu từ server
            String response = in.readLine();
            //Chuyen doi Json sang ChiTietPhieuNhap
            if(response.equals("NOT_FOUND")){
                return null;
            }else {
                ChietKhau chietKhau = objectMapper.readValue(response, ChietKhau.class);
                if (chietKhau != null) {
                    return chietKhau;
                } else {
                    ThongBao.thongBaoLoi("Không tìm thấy mã chiết khấu này");
                    return null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
