package qlhtt.Controllers.NguoiQuanLy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;

import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import javafx.util.Callback;
import javafx.util.Duration;

import qlhtt.Controllers.NhanVien.ChiTietPhieuNhapController;
import qlhtt.DAO.*;
import qlhtt.Entity.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ThongKeSanPhamController {




    //Chart
    @FXML
    private BarChart<String,Number> barChart_sanPham;

    @FXML
    private PieChart pieChart_nhomHetHan;

    //Progress Bar
    @FXML
    private ProgressBar bar_sp1;

    @FXML
    private ProgressBar bar_sp2;

    @FXML
    private ProgressBar bar_sp3;

    //Label
    @FXML
    private Label sanPhamBanDuoc;

    @FXML
    private Label soLuongHetHan;

    @FXML
    private Label label_NgaySP;

    @FXML
    private Label label_NgayHetHan;

    @FXML
    private Label sp1;

    @FXML
    private Label sp2;

    @FXML
    private Label sp3;

    @FXML
    private Label pt_sp1;

    @FXML
    private Label pt_sp2;

    @FXML
    private Label pt_sp3;

    //Datepicker
    @FXML
    private DatePicker datePicker;
    private static final Dotenv dotenv = Dotenv.load();
    private static final String SERVER_HOST = dotenv.get("SERVER_HOST");// Địa chỉ server
    private static final int SERVER_PORT = Integer.parseInt(dotenv.get("SERVER_PORT"));

    ObservableList<LocalDate> selectedDates = FXCollections.observableArrayList();

    @FXML
    void getDate(MouseEvent event) {
        if (datePicker.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng nhập vào ngày tương ứng");
            alert.showAndWait();
            return;
        }

        if (selectedDates.size() < 2) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Thông báo lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn ít nhất 2 ngày.");
            alert.showAndWait();
            return;
        }

        Collections.sort(selectedDates);
        LocalDate startDate = selectedDates.get(0);
        LocalDate endDate = selectedDates.get(1);

        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Gửi yêu cầu thống kê sản phẩm đến server
            String request = String.format("THONG_KE_SAN_PHAM %s %s", startDate, endDate);
            out.println(request);

            // Nhận phản hồi từ server
            String response = in.readLine();
            System.out.println("Phản hồi từ server: " + response);

            // Chuyển đổi JSON thành HashMap
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            HashMap<LocalDate, Integer> dsSPTheoNgay = objectMapper.readValue(response, HashMap.class);

            // Hiển thị dữ liệu trên giao diện
            taoDuLieuBarChartSanPham(dsSPTheoNgay);
            label_NgaySP.setText(startDate + " -> " + endDate);

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Không thể kết nối tới server thống kê.");
            alert.showAndWait();
        }
    }

//    @FXML
//    void getDate(MouseEvent event) {
//        if (datePicker.getValue() == null) {
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Thông báo lỗi");
//            alert.setHeaderText(null);
//            alert.setContentText("Vui lòng nhập vào ngày tương ứng");
//            alert.showAndWait();
//            return;
//        }
//
//        if (selectedDates.size() < 2) {
//            Alert alert = new Alert(Alert.AlertType.WARNING);
//            alert.setTitle("Thông báo lỗi");
//            alert.setHeaderText(null);
//            alert.setContentText("Vui lòng chọn ít nhất 2 ngày.");
//            alert.showAndWait();
//            return;
//        }
//
//        Collections.sort(selectedDates);
//
//
//
//        label_NgaySP.setText( selectedDates.get(0)+"->"+selectedDates.get(1));
//        label_NgaySP.setStyle("-fx-font-size: 17px");
//        sanPhamBanDuoc.setText(tinhSoSanPhamBanDuoc(selectedDates.get(0),selectedDates.get(1)) +"");
//
//
//        List<HoaDon> dsHoaDon = HoaDonDAO.getInstance().getDanhSachHoaDonTheoYeuCau(selectedDates.get(0),selectedDates.get(1));
//
//        HashMap<LocalDate, Integer> dsSPTheoNgay = loadDataThongKeSPTheoNgay(dsHoaDon);
//
//        taoDuLieuBarChartSanPham(dsSPTheoNgay);
//
//        taoDuLieuProgressBarBanChay(selectedDates.get(0),selectedDates.get(1));
//    }

    public void restart(MouseEvent event) {
        List<HoaDon> dsHD7Ngay = HoaDonDAO.getInstance().getDanhSachHoaDonTheo7Ngay();
        HashMap<LocalDate,Integer> dsSPTheoTuan = loadDataThongKeSPTheoNgay(dsHD7Ngay);
        HashMap<String, Integer> dsNhomSanPham = loadDataMaNhomSanPham();
        taoDuLieuProgressBarBanChay(LocalDate.now(),LocalDate.now());
        taoDuLieuPieChartNhomHetHan(dsNhomSanPham);
        taoDuLieuBarChartSanPham(dsSPTheoTuan);
        datePicker.setValue(null);
        label_NgaySP.setText("hôm nay");
        label_NgaySP.setStyle("-fx-font-size: 20px");

        List<ChiTietPhieuNhap> dsCTPN_SD = ChiTietPhieuNhapDAO.getInstance().getDanhSachSapHetHan();
        soLuongHetHan.setText(dsCTPN_SD.size() + "");
        sanPhamBanDuoc.setText(tinhSoSanPhamBanDuoc(LocalDate.now(),LocalDate.now()) +"");
    }


    @FXML
    public void initialize() {
        HashMap<String, Integer> dsNhomSanPham = loadDataMaNhomSanPham();
        List<HoaDon> dsHD7Ngay = HoaDonDAO.getInstance().getDanhSachHoaDonTheo7Ngay();
        HashMap<LocalDate,Integer> dsSPTheoTuan = loadDataThongKeSPTheoNgay(dsHD7Ngay);

//        San Pham
        chooseDate();
        taoDuLieuProgressBarBanChay(LocalDate.now(),LocalDate.now());
        taoDuLieuPieChartNhomHetHan(dsNhomSanPham);
        taoDuLieuBarChartSanPham(dsSPTheoTuan);


        List<ChiTietPhieuNhap> dsCTPN_SD = ChiTietPhieuNhapDAO.getInstance().getDanhSachSapHetHan();
        soLuongHetHan.setText(dsCTPN_SD.size() + "");
        sanPhamBanDuoc.setText(tinhSoSanPhamBanDuoc(LocalDate.now(),LocalDate.now()) +"");
    }

    private int tinhSoSanPhamBanDuoc(LocalDate date, LocalDate date2) {
        HashMap<String, Integer> dsSP = loadDataThongKeSP(date, date2);
        int tong =0;
        for(Map.Entry<String, Integer> entry : dsSP.entrySet()) {
            tong += entry.getValue();
        }
        return tong;
    }

    private void chooseDate() {
        datePicker.setOnAction(event -> {
            if (selectedDates.size() > 1){
                selectedDates.clear();
            }
            selectedDates.add(datePicker.getValue());}
        );

        datePicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        boolean alreadySelected = selectedDates.contains(item);
                        setDisable(alreadySelected);
                        setStyle(alreadySelected ? "-fx-background-color: #C06C84;" : "");
                    }
                };
            }
        });
    }




    private void taoDuLieuProgressBarBanChay(LocalDate date, LocalDate date1) {
        HashMap<String, Integer> dsSanPham = loadDataThongKeSP(date, date1);

        resetProgressBarsVaLabels();
        int totalProducts = dsSanPham.values().stream()
                .mapToInt(Integer::intValue)
                .sum();


        List<Map.Entry<String, Integer>> top3SanPham = dsSanPham.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(3)
                .collect(Collectors.toList());


        for (int i = 0; i < top3SanPham.size(); i++) {
            Map.Entry<String, Integer> entry = top3SanPham.get(i);
            String tenSanPham = entry.getKey();
            int soLuong = entry.getValue();
            double percentage = (soLuong * 100.0) / totalProducts;
            updateProgressBar(i, tenSanPham, soLuong, percentage);
        }
    }

    private void updateProgressBar(int index, String tenSanPham, int soLuong, double percentage) {
        ProgressBar progressBar = null;
        Label nameLabel = null;
        Label percentLabel = null;

        switch (index) {
            case 0:
                progressBar = bar_sp1;
                nameLabel = sp1;
                percentLabel = pt_sp1;
                break;
            case 1:
                progressBar = bar_sp2;
                nameLabel = sp2;
                percentLabel = pt_sp2;
                break;
            case 2:
                progressBar = bar_sp3;
                nameLabel = sp3;
                percentLabel = pt_sp3;
                break;
        }

        if (progressBar != null) {
            progressBar.setProgress(percentage / 100);
            nameLabel.setText(tenSanPham);
            percentLabel.setText(String.format("%.2f%%", percentage));
            nameLabel.setOnMouseClicked(event -> thongBaoSanPham(tenSanPham, soLuong, percentage));
        }
    }

    private void resetProgressBarsVaLabels() {
        ProgressBar[] progressBars = {bar_sp1, bar_sp2, bar_sp3};
        Label[] nameLabels = {sp1, sp2, sp3};
        Label[] percentLabels = {pt_sp1, pt_sp2, pt_sp3};

        for (int i = 0; i < progressBars.length; i++) {
            progressBars[i].setProgress(0);
            nameLabels[i].setText("");
            percentLabels[i].setText("");
        }
    }


    private void thongBaoSanPham(String tenSanPham, int soLuong, double percentage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Chi tiết sản phẩm");
        alert.setHeaderText(null);
        alert.setContentText(String.format(
                "Tên Sản Phẩm: %s\n" +
                        "Số Lượng: %d\n" +
                        "Phần Trăm: %.2f%%",
                tenSanPham, soLuong, percentage
        ));
        alert.showAndWait();
    }



    private void hienThongBao(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void taoDuLieuBarChartSanPham(HashMap<LocalDate, Integer> dsSPTheoNgay) {
        barChart_sanPham.getData().clear();
        barChart_sanPham.setAnimated(false);
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        List<Map.Entry<LocalDate, Integer>> sortedList = new ArrayList<>(dsSPTheoNgay.entrySet());
        sortedList.sort(Map.Entry.comparingByKey());

        for (Map.Entry<LocalDate, Integer> entry : sortedList) {
            String localDateDT = entry.getKey().toString();
            series.getData().add(new XYChart.Data<>(localDateDT, entry.getValue()));
        }

        barChart_sanPham.getData().add(series);
        barChart_sanPham.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent;");

        // Tạo hiệu ứng động cho biểu đồ
        Timeline timeline = new Timeline();
        int index = 0;
        for (XYChart.Data<String, Number> data : series.getData()) {
            Number originalValue = data.getYValue();
            data.setYValue(0);
            KeyValue keyValue = new KeyValue(data.YValueProperty(), originalValue);
            KeyFrame keyFrame = new KeyFrame(
                    Duration.millis(500 + index * 300),
                    keyValue
            );
            timeline.getKeyFrames().add(keyFrame);
            index++;
        }
        timeline.play();

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
                //Noi chuoi
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


    //LOAD DATA

    private HashMap<String, Integer> loadDataThongKeSP(LocalDate date, LocalDate date1) {
        HashMap<String, Integer> dsSPHoaDon = new HashMap<>();
        List<HoaDon> dsHD = HoaDonDAO.getInstance().getDanhSachHoaDonTheoYeuCau(date, date1);
        List<SanPham> dsSP = SanPhamDAO.getInstance().getDanhSachSanPham();
        List<ChiTietHoaDon> dsCTHD = ChiTietHoaDonDAO.getInstance().getDanhSachChiTietHoaDon();

        for (HoaDon hoaDon : dsHD) {
            for (ChiTietHoaDon chiTietHoaDon : dsCTHD) {
                if (chiTietHoaDon.getHoaDon().getMaHoaDon().equals(hoaDon.getMaHoaDon())) {
                    for (SanPham sp : dsSP) {
                        if (chiTietHoaDon.getSanPham().getMaSanPham().equals(sp.getMaSanPham())) {

                            dsSPHoaDon.merge(sp.getTenSanPham(), chiTietHoaDon.getSoLuong(), Integer::sum);
                        }
                    }
                }
            }
        }


        return dsSPHoaDon.entrySet()
                .stream()
                .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (integer, integer2) -> integer,
                        LinkedHashMap::new
                ));
    }






    private HashMap<LocalDate, Integer> loadDataThongKeSPTheoNgay(List<HoaDon> dsHD) {
        HashMap<LocalDate, Integer> dsSPTheoTuan = new HashMap<>();
        List<SanPham> dsSP = SanPhamDAO.getInstance().getDanhSachSanPham();
        List<ChiTietHoaDon> dsCTHD = ChiTietHoaDonDAO.getInstance().getDanhSachChiTietHoaDon();

        for(HoaDon hoaDon : dsHD) {
            for(ChiTietHoaDon chiTietHoaDon : dsCTHD) {
                if(chiTietHoaDon.getHoaDon().getMaHoaDon().equals(hoaDon.getMaHoaDon())) {
                    for (SanPham sp : dsSP) {
                        if(chiTietHoaDon.getSanPham().getMaSanPham().equals(sp.getMaSanPham())) {
                            dsSPTheoTuan.put(hoaDon.getNgayTao(), chiTietHoaDon.getSoLuong());
                        }
                    }
                }
            }
        }
        return dsSPTheoTuan.entrySet()
                .stream()
                .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (integer, integer2) -> integer,
                        LinkedHashMap::new
                ));
    }


    private HashMap<String, Integer> loadDataThongKeSanPhamHetHan() {
        List<ChiTietPhieuNhap> dsCTPN_sapHetHan = ChiTietPhieuNhapDAO.getInstance().getDanhSachSapHetHan();
        List<SanPham> dsSP = SanPhamDAO.getInstance().getDanhSachSanPham();

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

    private HashMap<String, Integer> loadDataMaNhomSanPham() {
        HashMap<String, Integer> dsMaNhomSanPham = new HashMap<>();
        HashMap<String, Integer> dsSanPhamHetHan = loadDataThongKeSanPhamHetHan();
        List<SanPham> dsSP = SanPhamDAO.getInstance().getDanhSachSanPham();

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

    private HashMap<String, Integer> loadDsSanPhamTheoMaNhom(String maNhomSanPham) {
        HashMap<String, Integer> dsSanPham = new HashMap<>();
        List<ChiTietPhieuNhap> dsCTPN_sapHetHan = ChiTietPhieuNhapDAO.getInstance().getDanhSachSapHetHan();
        List<SanPham> dsSP = SanPhamDAO.getInstance().getDanhSachSanPham();

        String tenSanPham = "";
        for (ChiTietPhieuNhap ctpn : dsCTPN_sapHetHan) {
            String maSanPham = ctpn.getSanPham().getMaSanPham();
            int soLuong = ctpn.getSanPham().getSoLuong();
            for(SanPham sp : dsSP) {
                if (maSanPham.equals(sp.getMaSanPham()) && ctpn.getSanPham().getNhomSanPham().getMaNhomSP().equals(maNhomSanPham)) {
                    tenSanPham = ctpn.getSanPham().getTenSanPham();
                    dsSanPham.put(tenSanPham, soLuong);
                }
            }
        }
        return dsSanPham;
    }



    /*
        co ds san pham het han
            dua ma vao vong lap => tim so luong con lai
                    dua vo hash map
    */
}