package qlhtt.Controllers.NhanVien;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import qlhtt.DAO.SanPhamDAO;
import qlhtt.Entity.NhomSanPham;
import qlhtt.Entity.SanPham;

import javafx.fxml.Initializable;
import qlhtt.Models.Model;
import qlhtt.ThongBao.ThongBao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DanhSachSanPhamController implements Initializable {
    long lastKeyPressTime = 0;
    final long SCAN_THRESHOLD = 30;
    @FXML
    public TableView<SanPham> tbv_DanhSachSanPham;
    @FXML
    public Label lbl_HienThi;
    @FXML
    public Button btn_Next;
    @FXML
    public ChoiceBox<String> chb_Trang;
    @FXML
    public Button btn_Back;
    @FXML
    public TextField txt_TimSanPham;
    @FXML
    public Button btn_TimSanPham;
    @FXML
    public Button btn_ThemMoi;
    @FXML
    public Button btn_Loc;
    @FXML
    public TableColumn<SanPham, String> tbc_STT;
    @FXML
    public TableColumn<SanPham, String> tbc_TenSanPham;
    @FXML
    public TableColumn<SanPham, String> tbc_NhomSanPham;
    @FXML
    public TableColumn<SanPham, String> tbc_DonGia;
    @FXML
    public TableColumn<SanPham, String> tbc_SoLuong;
    @FXML
    public TableColumn<SanPham, String> tbc_HanhDong;
    @FXML
    public TableColumn<SanPham, String> tbc_MaSanPham;
    @FXML
    public ProgressIndicator progressIndicator;
    @FXML
    public Button btn_LamMoi;

    private static final Dotenv dotenv = Dotenv.load();
    private static final String SERVER_HOST = dotenv.get("SERVER_HOST");// Địa chỉ server
    private static final int SERVER_PORT = Integer.parseInt(dotenv.get("SERVER_PORT"));


    private int tongSoTrang;
    private int trangHienTai;
    private List<SanPham> danhSachSanPham;

    private Boolean isSearching = false;

    public void setTongSoTrang(int tongSoTrang) {
        this.tongSoTrang = tongSoTrang;
    }

    public void setTrangHienTai(int trangHienTai) {
        this.trangHienTai = trangHienTai;
    }

    public void setDanhSachSanPham(List<SanPham> danhSachSanPham) {
        this.danhSachSanPham = danhSachSanPham;
    }

    public int getTongSoTrang() {
        return tongSoTrang;
    }

    public int getTrangHienTai() {
        return trangHienTai;
    }

    public List<SanPham> getDanhSachSanPham() {
        return danhSachSanPham;
    }

    public static DanhSachSanPhamController instance = new DanhSachSanPhamController();

    public static DanhSachSanPhamController getInstance() {
        return instance;
    }

    public DanhSachSanPhamController() {
    }




    //Hiển thị dữ liệu sau khi lọc
    @FXML
    public void hienThiDuLieuSauKhiLoc(List<SanPham> danhSachSanPham) {
        tbv_DanhSachSanPham.getItems().clear();
        hienThiDuLieuLenTableView(danhSachSanPham);
        chb_Trang.setDisable(true);
        btn_Next.setDisable(true);
        btn_Back.setDisable(true);
        lbl_HienThi.setText("Hiển thị tổng cộng " + danhSachSanPham.size() + " sản phẩm đã lọc");
    }

    //Hiển thị dữ liệu lên bảng
    @FXML
    public void hienThiDuLieuLenTableView(List<SanPham> danhSachSanPham) {
        // Cột số thứ tự (index)
        tbc_STT.setCellValueFactory(param -> {
            int index = param.getTableView().getItems().indexOf(param.getValue());
            return new SimpleStringProperty(String.valueOf(10*(getTrangHienTai()-1) + index + 1));
        });

        //Côt mã sản phẩm
        tbc_MaSanPham.setCellValueFactory(new PropertyValueFactory<>("maSanPham"));

        // Cột tên sản phẩm
        tbc_TenSanPham.setCellValueFactory(new PropertyValueFactory<>("tenSanPham"));

        // Cột nhóm sản phẩm
        tbc_NhomSanPham.setCellValueFactory(param -> {
            // Lấy nhomSanPham từ sản phẩm
            NhomSanPham nhomSanPham = param.getValue().getNhomSanPham();
            // Trả về tên nhóm sản phẩm
            return new SimpleStringProperty(nhomSanPham != null ? nhomSanPham.getTenNhomSP(): ""); // Kiểm tra null để tránh NullPointerException
        });

        // Cột đơn giá
        tbc_DonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));

        // Cột số lượng
        tbc_SoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));

        ObservableList<String> hanhDong = FXCollections.observableArrayList("Xem thông tin", "Sửa thông tin");

        tbc_HanhDong.setCellFactory(column -> new TableCell<SanPham, String>() {
            private final ChoiceBox<String> choiceBox = new ChoiceBox<>(hanhDong);
            {
                choiceBox.setValue("Xem thông tin");
                choiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        SanPham selectedProduct = getTableView().getItems().get(getIndex());
                        suKieKhiChonChoiBox(newSelection, selectedProduct);
                        //choiceBox.getSelectionModel().clearSelection();
                        // choiceBox.setValue("Xem thông tin");
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(choiceBox);
                }
            }
        });

        ObservableList<SanPham> sanPhamList = FXCollections.observableArrayList(danhSachSanPham);
        tbv_DanhSachSanPham.setItems(sanPhamList);
        lbl_HienThi.setText("Hiển thị tổng cộng " + SanPhamDAO.getInstance().layTongSoSanPham() + " sản phẩm");
    }


    private void suKieKhiChonChoiBox(String action, SanPham sanPham) {
        switch (action) {
            case "Xem thông tin":
                if (sanPham != null) {
                    hienThongTinSanPham(sanPham);
                }
                break;
            case "Sửa thông tin":
                if (sanPham != null) {
                    hienThiTrangSuaThongTinSanPham(sanPham);
                }
                break;
        }
    }

    private void hienThongTinSanPham(SanPham sanPham) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/NhanVien/XemThongTinSanPham.fxml"));
            Parent root = loader.load();
            String maSanPham = sanPham.getMaSanPham();
            // Lấy controller và truyền dữ liệu sản phẩm vào
            XemThongTinSanPhamController controller = loader.getController();
            controller.xemThongTinSanPham(maSanPham);

            // Hiển thị trang thông tin sản phẩm
            Stage stage = new Stage();
            stage.setTitle("Thông Tin Sản Phẩm");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void hienThiTrangSuaThongTinSanPham(SanPham sanPham){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/NhanVien/CapNhatThongTinSanPham.fxml"));
            Parent root = loader.load();

            // Lấy controller và truyền dữ liệu sản phẩm vào
            CapNhatThongTinSanPhamController controller = loader.getController();
            controller.hienThiThongTinSanPham(sanPham);

            // Hiển thị trang thông tin sản phẩm
            Stage stage = new Stage();
            stage.setTitle("Sửa Thông Tin Sản Phẩm");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public void thayDoiBangKhiChonTrang() {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT) ; // Kết nối đến server
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Gửi yêu cầu lấy sản phẩm theo trang
            String request = String.format("PAGE %d", getTrangHienTai());
            out.println(request);

            // Nhận phản hồi từ server
            String response = in.readLine();

            // Xử lý phản hồi từ server
            if (response.startsWith("[")) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, SanPham.class);
                List<SanPham> danhSachSanPham = objectMapper.readValue(response, type);
                hienThiDuLieuLenTableView(danhSachSanPham);
            }
            else {
                ThongBao thongBaoLoi = ThongBao.getInstance();
                thongBaoLoi.thongBaoLoi("Lỗi khi nhận dữ liệu phân trang từ server");
            }

        } catch (IOException e) {
            e.printStackTrace();
            ThongBao thongBaoLoi = ThongBao.getInstance();
            thongBaoLoi.thongBaoLoi("Không thể kết nối tới server");
        }
    }


    //Thay đổi bảng khi chọn trang tiếp theo
    public void thayDoiBangKhiChonTrangTiepTheo(){
        isSearching = true;
        if(getTrangHienTai() < getTongSoTrang()){
            setTrangHienTai(getTrangHienTai() + 1);
            List<SanPham> danhSachSanPham = SanPhamDAO.getInstance().laySanPhamTheoSoTrang(getTrangHienTai());
            hienThiDuLieuLenTableView(danhSachSanPham);
            chb_Trang.setValue("Trang " + getTrangHienTai());
        }
        isSearching = false;
    }


    //Thay đổi bảng khi chọn trang trước
    public void thayDoiBangKhiChonTrangTruoc(){
        isSearching = true;
        if(getTrangHienTai() > 1){
            setTrangHienTai(getTrangHienTai() - 1);
            List<SanPham> danhSachSanPham = SanPhamDAO.getInstance().laySanPhamTheoSoTrang(getTrangHienTai());
            hienThiDuLieuLenTableView(danhSachSanPham);
            chb_Trang.setValue("Trang " + getTrangHienTai());
        }
        isSearching = false;
    }

    //Hàm thực thi tìm kiếm
    @FXML
    public void thucHienTimKiem() {
        isSearching = true;
        tbv_DanhSachSanPham.getItems().clear();
        String maSanPham = txt_TimSanPham.getText().trim();

        ThongBao thongBaoLoi = ThongBao.getInstance();
        if (maSanPham.isEmpty()) {
            thongBaoLoi.thongBaoLoi("Vui lòng nhập mã sản phẩm");
        } else {
            try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT); // Kết nối đến server
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                // Gửi yêu cầu tìm kiếm sản phẩm
                String request = String.format("SEARCH %s", maSanPham);
                out.println(request); // Gửi yêu cầu tìm kiếm lên server

                // Nhận phản hồi từ server
                String response = in.readLine();

                // Xử lý phản hồi từ server
                if (response.startsWith("[")) { // Danh sách JSON
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.registerModule(new JavaTimeModule());
                    JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, SanPham.class);
                    List<SanPham> dsSanPham = objectMapper.readValue(response, type);

                    if (dsSanPham != null && !dsSanPham.isEmpty()) {
                        hienThiDuLieuLenTableView(dsSanPham);
                        int tongSoTrang = (int) Math.ceil((double) dsSanPham.size() / 10);
                        setTongSoTrang(tongSoTrang);
                        themSoTrangVaoChoiceBox(tongSoTrang);
                        btn_Next.setDisable(true);
                        btn_Back.setDisable(true);
                        lbl_HienThi.setText("Hiển thị tổng cộng " + dsSanPham.size() + " sản phẩm tìm thấy");
                    } else {
                        thongBaoLoi.thongBaoLoi("Không tìm thấy sản phẩm");
                    }
                } else {
                    thongBaoLoi.thongBaoLoi("Lỗi khi nhận phản hồi từ server");
                }

            } catch (IOException e) {
                e.printStackTrace();
                thongBaoLoi.thongBaoLoi("Không thể kết nối tới server");
            }
        }
        isSearching = false;
    }


    //Tim kiem san pham
    @FXML
    public void timKiemBangQuetMa(){
        txt_TimSanPham.setOnKeyReleased(event ->{
            long currentTime = System.currentTimeMillis();
            String code = txt_TimSanPham.getText().trim();

            // Nếu thời gian nhập giữa các ký tự < ngưỡng, coi là quét mã
            if ((currentTime - lastKeyPressTime) < SCAN_THRESHOLD && !code.isEmpty()) {
                thucHienTimKiem();
            }
            lastKeyPressTime = currentTime;
        });
    }

    @FXML
    public void timKiemBangNhap(){
        btn_TimSanPham.setOnAction(actionEvent -> {
            thucHienTimKiem();
        });

        // Thực hiện tìm kiếm khi nhấn phím Enter
        txt_TimSanPham.setOnAction(actionEvent -> {
            thucHienTimKiem();
        });
    }

    //Thêm mới sản phẩm
    @FXML
    public void themMoiSanPham(){
        Model.getInstance().getViewFactory().hienTrangThemSanPham((Stage) btn_ThemMoi.getScene().getWindow());
    }

    //Lam mới bảng
    @FXML
    public void lamMoiBang(){
        txt_TimSanPham.clear();
        tbv_DanhSachSanPham.getItems().clear();
        setTrangHienTai(1);
        setTongSoTrang((int)Math.ceil((double)SanPhamDAO.getInstance().layTongSoSanPham() / 10));
        List<SanPham> danhSachSanPham = SanPhamDAO.getInstance().laySanPhamTheoSoTrang(getTrangHienTai());
        hienThiDuLieuLenTableView(danhSachSanPham);
        themSoTrangVaoChoiceBox(getTongSoTrang());
        chb_Trang.setValue("Trang " + getTrangHienTai());
        lbl_HienThi.setText("Hiển thị tổng cộng " + SanPhamDAO.getInstance().layTongSoSanPham() + " sản phẩm");
        btn_Next.setDisable(false);
        btn_Back.setDisable(false);
        chb_Trang.setDisable(false);
    }

    public void suKienKhiChonDongSanPham(){
        tbv_DanhSachSanPham.setRowFactory(tv -> {
            TableRow<SanPham> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    SanPham rowData = row.getItem();
                    hienThongTinSanPham(rowData);
                }
            });
            return row;
        });
    }

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle){
        setTrangHienTai(1);
        setTongSoTrang((int)Math.ceil((double)SanPhamDAO.getInstance().layTongSoSanPham() / 10));
        List<SanPham> danhSachSanPham = SanPhamDAO.getInstance().laySanPhamTheoSoTrang(getTrangHienTai());
        Model.getInstance().setDanhSachSanPhamController(this);
        // Hiển thị dữ liệu lên TableView
        hienThiDuLieuLenTableView(danhSachSanPham);
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

        //Sự kiện chọn trang tiếp theo
        btn_Next.setOnAction(actionEvent -> {
            thayDoiBangKhiChonTrangTiepTheo();
        });

        //Sự kiện chọn trang trước
        btn_Back.setOnAction(actionEvent -> {
            thayDoiBangKhiChonTrangTruoc();
        });

        //Sự kiện tìm kiếm
        timKiemBangQuetMa();
        timKiemBangNhap();

        //Sự kiện làm mới bảng
        btn_LamMoi.setOnAction(actionEvent -> {
            lamMoiBang();
        });

        //Sự kiện thêm mới sản phẩm
        btn_ThemMoi.setOnAction(actionEvent -> {
            themMoiSanPham();
        });

        //Sự kiện lọc sản phẩm
        btn_Loc.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().hienTrangTraCuuSanPham((Stage) btn_Loc.getScene().getWindow());
        });

        suKienKhiChonDongSanPham();
    }
}