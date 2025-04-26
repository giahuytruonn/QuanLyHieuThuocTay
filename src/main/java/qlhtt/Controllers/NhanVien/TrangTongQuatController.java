package qlhtt.Controllers.NhanVien;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import qlhtt.DAO.*;
import qlhtt.Entity.*;
import qlhtt.Enum.PhuongThucThanhToan;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TrangTongQuatController {

    private final List<SanPham> dsSP = SanPhamDAO.getInstance().getDanhSachSanPham();
    private final List<ChiTietPhieuNhap> dsCTPN_sapHetHan = ChiTietPhieuNhapDAO.getInstance().getDanhSachSapHetHan();
    private final HashMap<String, Integer> dsNhomSanPham = loadDataMaNhomSanPham();


    @FXML
    private Label taiKhoanHD_tongQuat;

    @FXML
    private Label nhanVienHD_tongQuat;





    ObservableList<ChiTietHoaDon> currentList = FXCollections.observableArrayList();


    @FXML
    private Label timeLabel;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");

    @FXML
    private Label label_tongDoanhThu;

    @FXML
    private Label label_soHoaDon;

    @FXML
    private Label label_soSuDung;


    @FXML
    private PieChart pieChart_nhomHetHan;


    @FXML
    private TableView tableView_hoaDon;

    @FXML
    private TableColumn<ChiTietHoaDon, String> ngayTao_tblColumn;

    @FXML
    private TableColumn<ChiTietHoaDon, String> maHoaDon_tblColumn;

    @FXML
    private TableColumn<ChiTietHoaDon, String> maSanPham_tblColumn;

    @FXML
    private TableColumn<ChiTietHoaDon, Integer> soLuong_tblColumn;

    @FXML
    private TableColumn<ChiTietHoaDon, PhuongThucThanhToan> phuongThuc_tblColumn;

    @FXML
    private TableColumn<ChiTietHoaDon, Double> tongTien_tblColumn;

    @FXML
    private TableColumn<ChiTietHoaDon, String> trangThaiThanhToan_tblColumn;




    @FXML
    public void initialize() {
        List<ChiTietHoaDon> dsCTHD = ChiTietHoaDonDAO.getInstance().getDanhSachChiTietHoaDon();
        List<HoaDon> dsHoaDonHienTai = HoaDonDAO.getInstance().getDanhSachHoaDonTheoYeuCau(LocalDate.now(),LocalDate.now());
        HashMap<LocalDate, Double> dsDoanhThu = loadDataThongKeDoanhThu(dsHoaDonHienTai,dsCTHD);
        List<NhanVien> dsNV = NhanVienDAO.getInstance().getDanhSachNhanVienTheoTrangThai();
        List<TaiKhoan> dsTK = TaiKhoanDAO.getInstance().getDanhSachTaiKhoanTheoTrangThai();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Lấy thời gian hiện tại và cập nhật Label
                LocalDateTime currentTime = LocalDateTime.now();
                timeLabel.setText(currentTime.format(formatter));
            }
        };

        // Bắt đầu AnimationTimer
        timer.start();

        taiKhoanHD_tongQuat.setText(""+dsTK.size());
        nhanVienHD_tongQuat.setText(""+dsNV.size());
        taoDuLieuPieChartNhomHetHan(dsNhomSanPham);

//        for (ChiTietHoaDon chiTietHoaDon : dsCTHD)  {
//            System.out.println(chiTietHoaDon);
//        }
//
//        for(HoaDon hd : dsHoaDonHienTai) {
//            System.out.println(hd);
//        }

        for(HoaDon hd : dsHoaDonHienTai){
            for(ChiTietHoaDon chiTietHoaDon : dsCTHD){
                if(hd.getMaHoaDon().equals(chiTietHoaDon.getHoaDon().getMaHoaDon())){
                    themDataVaoBang(chiTietHoaDon);
                }
            }
        }
        tableView_hoaDon.refresh();

        double tongTien =  tinhTongDoanhThu(dsDoanhThu);
        label_tongDoanhThu.setText("" + tongTien);
        label_soHoaDon.setText(""+tinhTongHoaDon(dsHoaDonHienTai));
        label_soSuDung.setText(""+tinhSoSanPham(dsCTHD, dsHoaDonHienTai));
    }

    private double tinhTongDoanhThu( HashMap<LocalDate, Double> dsDT) {
        return dsDT.values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    private int tinhTongHoaDon(List<HoaDon> dsHD) {
        return dsHD.size();
    }

    private int tinhSoSanPham(List<ChiTietHoaDon> cthd, List<HoaDon> dsHD) {
        int soLuong = 0;
        for(HoaDon hd : dsHD) {
            for(ChiTietHoaDon chiTietHoaDon :  cthd) {
                if(hd.getMaHoaDon().equals(chiTietHoaDon.getHoaDon().getMaHoaDon())) {
                    soLuong += chiTietHoaDon.getSoLuong();
                }
            }
        }
        return soLuong;
    }


    private void taoDuLieuPieChartNhomHetHan(HashMap<String, Integer> dsNhomSanPham) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        int totalValue = dsNhomSanPham.values().stream().mapToInt(Integer::intValue).sum();

        for (Map.Entry<String, Integer> entry : dsNhomSanPham.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), 0));
        }
        pieChart_nhomHetHan.setData(pieChartData);
        Timeline timeline = new Timeline();

        int index = 0;
        for (PieChart.Data data : pieChartData) {
            int finalValue = dsNhomSanPham.get(data.getName());
            KeyValue keyValue = new KeyValue(data.pieValueProperty(), finalValue);
            KeyFrame keyFrame = new KeyFrame(
                    Duration.millis(500 + index * 300),
                    keyValue
            );

            timeline.getKeyFrames().add(keyFrame);
            index++;

            data.getNode().setOnMouseClicked(event -> {
                double percentage = (finalValue * 100.0) / totalValue;
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông tin các sản phẩm theo nhóm " + data.getName());
                alert.setHeaderText(null);
                HashMap<String, Integer> dsSanPhamTheoMaNhom = loadDsSanPhamTheoMaNhom(data.getName());
                StringBuilder content = new StringBuilder();
                for (Map.Entry<String, Integer> entry : dsSanPhamTheoMaNhom.entrySet()) {
                    content.append("Tên Sản Phẩm: ").append(entry.getKey())
                            .append("\nSố Lượng: ").append(entry.getValue())
                            .append("\nPhần Trăm: ").append(String.format("%.2f", percentage)).append("%\n\n");
                }
                alert.setContentText(content.toString());
                alert.showAndWait();
            });
        }
        timeline.play();

    }
    public void themDataVaoBang(ChiTietHoaDon chiTietHoaDon) {

        // Cập nhật bảng với thông tin từ HoaDon và ChiTietHoaDon
        ngayTao_tblColumn.setCellValueFactory(cellData -> {
            LocalDate date = cellData.getValue().getHoaDon().getNgayTao();
            return new SimpleStringProperty(date.toString());
        });

        maSanPham_tblColumn.setCellValueFactory(cellData -> {
            String code = cellData.getValue().getSanPham().getMaSanPham(); // Lấy thông tin từ ChiTietHoaDon
            return new SimpleStringProperty(code);
        });

        maHoaDon_tblColumn.setCellValueFactory(cellData -> {
            String code = cellData.getValue().getHoaDon().getMaHoaDon(); // Lấy thông tin từ HoaDon
            return new SimpleStringProperty(code);
        });

        soLuong_tblColumn.setCellValueFactory(cellData -> {
            int code = cellData.getValue().getSoLuong(); // Lấy thông tin từ ChiTietHoaDon
            return new SimpleObjectProperty<>(code);
        });

        phuongThuc_tblColumn.setCellValueFactory(cellData -> {
            return new SimpleObjectProperty<>(cellData.getValue().getHoaDon().getPhuongThucThanhToan()); // Lấy thông tin từ HoaDon
        });

        tongTien_tblColumn.setCellValueFactory(new PropertyValueFactory<>("tongTien"));

        trangThaiThanhToan_tblColumn.setCellValueFactory(cellData -> {
            String trangThai = cellData.getValue().getHoaDon().getTrangThaiHoaDon() ? "Đã thanh toán" : "Chưa thanh toán";
            return new SimpleStringProperty(trangThai); // Thông tin từ HoaDon
        });

        currentList.add(chiTietHoaDon);
        tableView_hoaDon.setItems(currentList);

    }

    private HashMap<String, Integer> loadDsSanPhamTheoMaNhom(String maNhomSanPham) {
        HashMap<String, Integer> dsSanPham = new HashMap<>();
        String tenSanPham = "";
        for (ChiTietPhieuNhap ctpn : dsCTPN_sapHetHan) {
            String maSanPham = ctpn.getSanPham().getMaSanPham();
            int soLuong = ctpn.getSanPham().getSoLuong();
            for(SanPham sp : dsSP) {
                if (maSanPham.equals(sp.getMaSanPham()) && ctpn.getSanPham().getNhomSanPham().getMaNhomSP().equals(maNhomSanPham)) {
                    tenSanPham = ctpn.getSanPham().getTenSanPham();
                    dsSanPham.put(tenSanPham, soLuong);
                    break;
                }
            }
        }
        return dsSanPham;
    }

    private HashMap<String, Integer> loadDataMaNhomSanPham() {
        HashMap<String, Integer> dsMaNhomSanPham = new HashMap<>();
        HashMap<String, Integer> dsSanPhamHetHan = loadDataThongKeSanPhamHetHan();

        for (Map.Entry<String, Integer> entry : dsSanPhamHetHan.entrySet()) {
            String maSanPham = entry.getKey();
            int soLuong = entry.getValue();

            for (SanPham sp : dsSP) {
                if (sp.getMaSanPham().equals(maSanPham)) {
                    String maNhomSP = sp.getNhomSanPham().getMaNhomSP();
                    dsMaNhomSanPham.merge(maNhomSP, soLuong, Integer::sum);
                }
            }
        }
        return dsMaNhomSanPham;
    }

    private HashMap<String, Integer> loadDataThongKeSanPhamHetHan() {
        HashMap<String, Integer> dsSanPham = new HashMap<>();
        for (ChiTietPhieuNhap ctpn : dsCTPN_sapHetHan) {
            for(SanPham sp : dsSP) {
                String maSanPham = ctpn.getSanPham().getMaSanPham();
                int soLuong = ctpn.getSanPham().getSoLuong();
                if(ctpn.getSanPham().getMaSanPham().equals(sp.getMaSanPham())) {
                    dsSanPham.merge(maSanPham, soLuong, Integer::sum);
                }
            }
        }
        return dsSanPham;
    }

    private HashMap<LocalDate,Double> loadDataThongKeDoanhThu(List<HoaDon> dsHD, List<ChiTietHoaDon> dsCTHD) {
        HashMap<LocalDate, Double> dsDoanhThu = new HashMap<>();
        for (HoaDon hoaDon : dsHD) {
            LocalDate ngayTao = hoaDon.getNgayTao();
            double tongTien = 0.0;
            for (ChiTietHoaDon chiTietHoaDon : dsCTHD) {
                if (chiTietHoaDon.getHoaDon().getMaHoaDon().equals(hoaDon.getMaHoaDon())) {
                    tongTien += chiTietHoaDon.getTongTien();
                }
            }
            dsDoanhThu.put(ngayTao, dsDoanhThu.getOrDefault(ngayTao, 0.0) + tongTien);
        }
        return dsDoanhThu;
    }

    public void restart(MouseEvent event) {
        List<ChiTietHoaDon> dsCTHD = ChiTietHoaDonDAO.getInstance().getDanhSachChiTietHoaDon();
        List<HoaDon> dsHoaDonHienTai = HoaDonDAO.getInstance().getDanhSachHoaDonTheoYeuCau(LocalDate.now(),LocalDate.now());
        HashMap<LocalDate, Double> dsDoanhThu = loadDataThongKeDoanhThu(dsHoaDonHienTai,dsCTHD);
        List<NhanVien> dsNV = NhanVienDAO.getInstance().getDanhSachNhanVienTheoTrangThai();
        List<TaiKhoan> dsTK = TaiKhoanDAO.getInstance().getDanhSachTaiKhoanTheoTrangThai();
        taiKhoanHD_tongQuat.setText(""+dsTK.size());
        nhanVienHD_tongQuat.setText(""+dsNV.size());
        taoDuLieuPieChartNhomHetHan(dsNhomSanPham);
        for(HoaDon hd : dsHoaDonHienTai){
            for(ChiTietHoaDon chiTietHoaDon : dsCTHD){
                if(hd.getMaHoaDon().equals(chiTietHoaDon.getHoaDon().getMaHoaDon())){
                    themDataVaoBang(chiTietHoaDon);
                }
            }
        }
        tableView_hoaDon.refresh();

        double tongTien =  tinhTongDoanhThu(dsDoanhThu);
        label_tongDoanhThu.setText("" + tongTien);
        label_soHoaDon.setText(""+tinhTongHoaDon(dsHoaDonHienTai));
        label_soSuDung.setText(""+tinhSoSanPham(dsCTHD, dsHoaDonHienTai));
    }
}
