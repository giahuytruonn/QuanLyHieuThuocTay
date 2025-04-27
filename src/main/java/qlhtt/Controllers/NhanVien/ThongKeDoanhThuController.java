package qlhtt.Controllers.NhanVien;

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
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import qlhtt.DAO.ChiTietHoaDonDAO;
import qlhtt.DAO.HoaDonDAO;
import qlhtt.DAO.SanPhamDAO;
import qlhtt.Entity.ChiTietHoaDon;
import qlhtt.Entity.HoaDon;
import qlhtt.Entity.SanPham;
import org.apache.poi.ss.usermodel.*;
import qlhtt.Models.Model;

import java.awt.*;
import java.io.*;

import java.net.Socket;
import java.time.LocalDate;
import java.util.*;
import java.util.List;


//+doanh thu 1 tháng , date picker : line , bar (date picker, css)


// only CSS more

public class ThongKeDoanhThuController {




    private final List<HoaDon> dsHoaDonHienTai = HoaDonDAO.getInstance().getDanhSachHoaDonTheoYeuCau(LocalDate.now(),LocalDate.now());

    private final List<HoaDon> dsHoaDon7Ngay = HoaDonDAO.getInstance().getDanhSachHoaDonTheo7Ngay();

    private final List<SanPham> dsSP = SanPhamDAO.getInstance().getDanhSachSanPham();

    //Hash Map Thống Kê


    ObservableList<LocalDate> selectedDates = FXCollections.observableArrayList();


    @FXML
    private Label label_tongDoanhThu;

    @FXML
    private Label label_NgayDoanhThu;

    @FXML
    private Label label_soHoaDon;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<Integer> comboBox_DoanhThuQuy;

    //Khai bao Doanh Thu Thong Ke
    @FXML
    private BarChart<Number,String> barChart_DoanhThuNhanVien;

    @FXML
    private LineChart<String,Number> lineChart_DoanhThuNgay;

    @FXML
    private PieChart pieChart_doanhThuNSP;

    @FXML
    private PieChart pieChart_DoanhThuQuy;


    private static final Dotenv dotenv = Dotenv.load();
    private static final String SERVER_HOST = dotenv.get("SERVER_HOST");// Địa chỉ server
    private static final int SERVER_PORT = Integer.parseInt(dotenv.get("SERVER_PORT"));

    //BUTTON EVENT
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

            // Gửi yêu cầu thống kê đến server
            String request = String.format("THONG_KE_DOANH_THU %s %s", startDate, endDate);
            out.println(request);

            // Nhận phản hồi từ server
            String response = in.readLine();
            System.out.println("Phản hồi từ server: " + response);

            // Chuyển đổi JSON thành HashMap
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            HashMap<LocalDate, Double> dsDoanhThu = objectMapper.readValue(response, HashMap.class);

            // Hiển thị dữ liệu trên giao diện
            taoDuLieuLineChartDoanhThu(dsDoanhThu);
            double tongTien = tinhTongDoanhThu(dsDoanhThu);
            label_tongDoanhThu.setText("" + tongTien);
            label_NgayDoanhThu.setText(startDate + " -> " + endDate);

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
//        List<ChiTietHoaDon> dsCTHD = ChiTietHoaDonDAO.getInstance().getDanhSachChiTietHoaDon();
//
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
//        Collections.sort(selectedDates);
//        List<HoaDon> dsHoaDon = new ArrayList<>();
//        dsHoaDon = HoaDonDAO.getInstance().getDanhSachHoaDonTheoYeuCau(selectedDates.get(0), selectedDates.get(1));
//        HashMap<LocalDate, Double> dsDoanhThuYC = loadDataThongKeDoanhThu(dsHoaDon,dsCTHD);
//
//        taoDuLieuLineChartDoanhThu(dsDoanhThuYC);
//        taoDuLieuBarChartDoanhThu(dsHoaDon,dsCTHD);
//        taoDuLieuPieChartNhomSanPham(dsHoaDon,dsCTHD);
//
//        double tongTien =  tinhTongDoanhThu(dsDoanhThuYC);
//        label_tongDoanhThu.setText("" + tongTien);
//        label_soHoaDon.setText("" + tinhTongHoaDon(dsHoaDon));
//
//        label_NgayDoanhThu.setText(selectedDates.get(0) + "->"+ selectedDates.get(1));
//    }

    @FXML
    void xuatExcel(MouseEvent event) {
        List<ChiTietHoaDon> dsCTHD = ChiTietHoaDonDAO.getInstance().getDanhSachChiTietHoaDon();

        List<HoaDon> dsHoaDonXuat;
        HashMap<String, Double> dsDT;
        if (selectedDates.size() >= 2) {
            dsHoaDonXuat = HoaDonDAO.getInstance().getDanhSachHoaDonTheoYeuCau(selectedDates.get(0), selectedDates.get(1));
        } else {
            dsHoaDonXuat = HoaDonDAO.getInstance().getDanhSachHoaDonTheoYeuCau(LocalDate.now(), LocalDate.now());
        }
        dsDT = loadDataThongKeDoanhThuTheoNhanVien(dsHoaDonXuat, dsCTHD);
        String fileName = generateExcelFileName(dsHoaDonXuat);
        exportToExcelAndOpen(fileName, dsDT, dsHoaDonXuat);
    }


    public void restart(MouseEvent event) {
        List<ChiTietHoaDon> dsCTHD = ChiTietHoaDonDAO.getInstance().getDanhSachChiTietHoaDon();
        HashMap<LocalDate, Double> dsDoanhThu = loadDataThongKeDoanhThu(dsHoaDonHienTai,dsCTHD);
        HashMap<LocalDate, Double> dsDoanhThu7Ngay = loadDataThongKeDoanhThu(dsHoaDon7Ngay,dsCTHD);
        doanhThuTheoQuy();
        datePicker.setValue(null);
        taoDuLieuLineChartDoanhThu(dsDoanhThu7Ngay);
        taoDuLieuBarChartDoanhThu(dsHoaDonHienTai,dsCTHD);
        taoDuLieuPieChartNhomSanPham(dsHoaDonHienTai,dsCTHD);
        taoDuLieuPieChartDoanhThuTheoQuy(LocalDate.now().getYear());
        double tongTien =  tinhTongDoanhThu(dsDoanhThu);
        label_tongDoanhThu.setText("" + tongTien);
        label_soHoaDon.setText(""+tinhTongHoaDon(dsHoaDonHienTai));
        label_NgayDoanhThu.setText("ngày");
        comboBox_DoanhThuQuy.setValue(null);
    }

    @FXML
    public void initialize() {
        List<ChiTietHoaDon> dsCTHD = ChiTietHoaDonDAO.getInstance().getDanhSachChiTietHoaDon();

        Model.getInstance().setThongKeDoanhThuController(this);

        HashMap<LocalDate, Double> dsDoanhThu = loadDataThongKeDoanhThu(dsHoaDonHienTai,dsCTHD);
        HashMap<LocalDate, Double> dsDoanhThu7Ngay = loadDataThongKeDoanhThu(dsHoaDon7Ngay,dsCTHD);
        chooseDate();

//      Doanh Thu
        doanhThuTheoQuy();

        taoDuLieuLineChartDoanhThu(dsDoanhThu7Ngay);

        taoDuLieuBarChartDoanhThu(dsHoaDonHienTai,dsCTHD);

        taoDuLieuPieChartNhomSanPham(dsHoaDonHienTai,dsCTHD);
        taoDuLieuPieChartDoanhThuTheoQuy(LocalDate.now().getYear());
        double tongTien =  tinhTongDoanhThu(dsDoanhThu);
        label_tongDoanhThu.setText("" + tongTien);
        label_soHoaDon.setText(""+tinhTongHoaDon(dsHoaDonHienTai));

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

    private String generateExcelFileName(List<HoaDon> dsHoaDon) {

        String folderPath = "FileExcel";
        File folder = new File(folderPath);
        if (!folder.exists()) {

            folder.mkdirs();
        }


        if (dsHoaDon == null || dsHoaDon.isEmpty()) {
            return folderPath + File.separator + "DoanhThuNhanVien_" + LocalDate.now() + ".xlsx";
        }

        LocalDate startDate = dsHoaDon.get(0).getNgayTao();
        LocalDate endDate = dsHoaDon.get(dsHoaDon.size() - 1).getNgayTao();

        return folderPath + File.separator + "DoanhThuNhanVien_" + startDate + "_to_" + endDate + ".xlsx";
    }

    private double tinhTongDoanhThu( HashMap<LocalDate, Double> dsDT) {
        return dsDT.values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    private int tinhTongHoaDon(List<HoaDon> dsHD) {
        return dsHD.size();
    }

    private void doanhThuTheoQuy() {
        ObservableList<Integer> options = FXCollections.observableArrayList();
        int currentYear = LocalDate.now().getYear();
        comboBox_DoanhThuQuy.setValue(2025);
        for (int i = currentYear; i >= currentYear - 3; i--) {
            options.add(i);
        }
        comboBox_DoanhThuQuy.setItems(options);
        comboBox_DoanhThuQuy.setOnAction(event -> {
            Integer selectedYear = comboBox_DoanhThuQuy.getValue();
            if (selectedYear != null) {
                taoDuLieuPieChartDoanhThuTheoQuy(selectedYear);
            }
        });
    }


    // THỐNG KÊ DOANH THU
    private void taoDuLieuLineChartDoanhThu(HashMap<LocalDate, Double> dsDT) {
        lineChart_DoanhThuNgay.getData().clear();
        lineChart_DoanhThuNgay.setAnimated(false);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<LocalDate, Double> entry : dsDT.entrySet()) {
            String localDateDT = entry.getKey().toString();
            series.getData().add(new XYChart.Data<>(localDateDT, entry.getValue()));
        }

        lineChart_DoanhThuNgay.getData().add(series);
        lineChart_DoanhThuNgay.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent;");
        series.getNode().setStyle("-fx-stroke: #68CFFF");

        Timeline timeline = new Timeline();
        for (int i = 0; i < series.getData().size(); i++) {
            XYChart.Data<String, Number> data = series.getData().get(i);
            data.getNode().setOpacity(0);

            KeyFrame keyFrame = new KeyFrame(Duration.seconds(i * 0.5), event -> {
                data.getNode().setOpacity(1);
            });

            timeline.getKeyFrames().add(keyFrame);
        }
        timeline.play();
    }



    private void taoDuLieuBarChartDoanhThu(List<HoaDon> dsHD, List<ChiTietHoaDon> dsCTHD) {
        HashMap<String, Double> dsDT = loadDataThongKeDoanhThuTheoNhanVien(dsHD, dsCTHD);
        barChart_DoanhThuNhanVien.getData().clear();
        barChart_DoanhThuNhanVien.setAnimated(false);

        XYChart.Series<Number, String> series = new XYChart.Series<>();
        for (Map.Entry<String, Double> entry : dsDT.entrySet()) {
            String maNhanVien = entry.getKey();
            String tenNhanVien = "";
            for(HoaDon hd : dsHD) {
                if(hd.getNhanVien().getMaNhanVien().equals(maNhanVien)) {
                    tenNhanVien = hd.getNhanVien().getTenNhanVien();
                }
            }
            Double doanhThu = entry.getValue();
            series.getData().add(new XYChart.Data<>(doanhThu, tenNhanVien));
        }

        barChart_DoanhThuNhanVien.getData().add(series);
        barChart_DoanhThuNhanVien.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent;");
        Timeline timeline = new Timeline();
        int index = 0;
        for (XYChart.Data<Number, String> data : series.getData()) {
            Number originalValue = data.getXValue();
            data.setXValue(0);
            KeyValue keyValue = new KeyValue(data.XValueProperty(), originalValue);
            KeyFrame keyFrame = new KeyFrame(
                    Duration.millis(500 + index * 300),
                    keyValue
            );
            timeline.getKeyFrames().add(keyFrame);
            index++;
        }
        timeline.play();

    }


    private void taoDuLieuPieChartNhomSanPham(List<HoaDon> dsHD , List<ChiTietHoaDon> dsCTHD) {
        HashMap<String, Double>dsNhomSanPham = loadDataThongKeNhomSanPham(dsHD, dsCTHD);
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        int totalValue = dsNhomSanPham.values().stream().mapToInt(Double::intValue).sum();

        for (Map.Entry<String, Double> entry : dsNhomSanPham.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), 0));
        }

        pieChart_doanhThuNSP.setData(pieChartData);
        Timeline timeline = new Timeline();

        int index = 0;
        for (PieChart.Data data : pieChartData) {
            Double finalValue = dsNhomSanPham.get(data.getName());
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
                String tenMaNhomSP = "";
                for(SanPham sp : dsSP) {
                    if(sp.getNhomSanPham().getMaNhomSP().equals(data.getName())) {
                        tenMaNhomSP = sp.getNhomSanPham().getTenNhomSP();
                        break;
                    }
                }
                alert.setTitle("Thông tin các sản phẩm theo nhóm " + tenMaNhomSP);
                alert.setHeaderText(null);
                alert.setContentText("Tên Nhóm Sản Phẩm: " + tenMaNhomSP +
                        "\nDoanh Thu: " + data.getPieValue() +
                        "\nPhần Trăm: " + String.format("%.2f", percentage) + "%" +
                        "\n");

                alert.showAndWait();
            });
        }
        timeline.play();
    }

    private void taoDuLieuPieChartDoanhThuTheoQuy(int year) {
        HashMap<Integer, Double> dsDoanhThuQuy = (HashMap<Integer, Double>) HoaDonDAO.getInstance().getDanhSachHoaDonTheoQuy(year);
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        double totalValue = dsDoanhThuQuy.values().stream().mapToDouble(Double::doubleValue).sum();

        for (Map.Entry<Integer, Double> entry : dsDoanhThuQuy.entrySet()) {
            int quy = entry.getKey();
            double doanhThu = entry.getValue();
            pieChartData.add(new PieChart.Data("Quý " + quy, doanhThu));
        }

        pieChart_DoanhThuQuy.setData(pieChartData);

        Timeline timeline = new Timeline();
        int index = 0;
        for (PieChart.Data data : pieChartData) {
            double finalValue = data.getPieValue(); // Lấy giá trị doanh thu
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
                alert.setTitle("Thông tin doanh thu theo quý");
                alert.setHeaderText(null);
                alert.setContentText(data.getName() +
                        "\nDoanh Thu: " + String.format("%.2f", finalValue) +
                        "\nPhần Trăm: " + String.format("%.2f", percentage) + "%");

                alert.showAndWait();
            });
        }

        timeline.play();
    }


    // Hash Map Thong Ke Doanh Thu
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

//    private HashMap<String,Double> loadDataThongKeDoanhThuTheoNhanVien(List<HoaDon> dsHD, List<ChiTietHoaDon> dsCTHD) {
//        HashMap<String, Double> dsDoanhThu = new HashMap<>();
//        String maNhanVien = LoginController.getTaiKhoan().getNhanVien().getMaNhanVien();
//        for (HoaDon hoaDon : dsHD) {
//            if(hoaDon.getNhanVien().getMaNhanVien().equals(maNhanVien)) {
//                double tongTien = 0.0;
//                for (ChiTietHoaDon chiTietHoaDon : dsCTHD) {
//                    if (chiTietHoaDon.getHoaDon().getMaHoaDon().equals(hoaDon.getMaHoaDon())) {
//                        tongTien += chiTietHoaDon.getTongTien();
//                    }
//                }
//                dsDoanhThu.merge(maNhanVien,  tongTien, Double::sum);
//            }
//        }
//        return dsDoanhThu;
//    }

    private HashMap<String, Double> loadDataThongKeDoanhThuTheoNhanVien(List<HoaDon> dsHD, List<ChiTietHoaDon> dsCTHD) {
        HashMap<String, Double> dsDoanhThu = new HashMap<>();
        String maNhanVien = Model.getInstance().getTaiKhoan().getNhanVien().getMaNhanVien();
        ChiTietHoaDonController chiTietHoaDonController = new ChiTietHoaDonController();


        List<HoaDon> hoaDonCuaNhanVien = dsHD.stream()
                .filter(hoaDon -> hoaDon.getNhanVien().getMaNhanVien().equals(maNhanVien))
                .toList();

        System.out.println(maNhanVien);

        double tongTien = 0.0;
        for (HoaDon hoaDon : hoaDonCuaNhanVien) {
            tongTien += dsCTHD.stream()
                    .filter(chiTiet -> chiTiet.getHoaDon().getMaHoaDon().equals(hoaDon.getMaHoaDon()))
                    .mapToDouble(ChiTietHoaDon::getTongTien)
                    .sum();
//            System.out.println(tongTien);
//            tongTien += chiTietHoaDonController.getDsChiTietHoaDonTheoMaHoaDon(hoaDon.getMaHoaDon())
//                    .stream().mapToDouble(ChiTietHoaDon::getTongTien).sum();
//            System.out.println(tongTien);
//            if(chiTietHoaDonController.getDsChiTietHoaDonTheoMaHoaDon(hoaDon.getMaHoaDon().isEmpty()) {
//
//            }
        }

        dsDoanhThu.put(maNhanVien, tongTien);

        return dsDoanhThu;
    }

    private HashMap<String, Double> loadDataThongKeNhomSanPham(List<HoaDon> dsHD, List<ChiTietHoaDon> dsCTHD) {
        HashMap<String, Double> dsDoanhThu = new HashMap<>();
        for (HoaDon hoaDon : dsHD) {
            for (ChiTietHoaDon chiTietHoaDon : dsCTHD) {
                if (chiTietHoaDon.getHoaDon().getMaHoaDon().equals(hoaDon.getMaHoaDon())) {
                    String maNhomSanPham = chiTietHoaDon.getSanPham().getNhomSanPham().getMaNhomSP();

                    double tongTien = chiTietHoaDon.getTongTien();

                    dsDoanhThu.put(maNhomSanPham, dsDoanhThu.getOrDefault(maNhomSanPham, 0.0) + tongTien);
                }
            }
        }
        return dsDoanhThu;
    }


    public void exportToExcelAndOpen(String fileName, HashMap<String, Double> dsDT, List<HoaDon> dsHD) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("DoanhThuNhanVien");


        Row headerRow = sheet.createRow(0);
        String[] headers = {"Ngày/Tháng/Năm", "Mã Nhân Viên", "Tên Nhân Viên", "Doanh Thu", "Tổng Doanh Thu"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(createHeaderCellStyle(workbook));
        }


        int rowIndex = 1;
        double totalRevenue = 0;

        for (Map.Entry<String, Double> entry : dsDT.entrySet()) {
            String maNhanVien = entry.getKey();
            String tenNhanVien = "";
            Double doanhThu = entry.getValue();
            LocalDate ngayThang = null;


            for (HoaDon hd : dsHD) {
                if (hd.getNhanVien().getMaNhanVien().equals(maNhanVien)) {
                    tenNhanVien = hd.getNhanVien().getTenNhanVien();
                    ngayThang = hd.getNgayTao();
                    break;
                }
            }

            // Tính tổng doanh thu
            totalRevenue += doanhThu;

            // Ghi dòng dữ liệu
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(ngayThang != null ? ngayThang.toString() : "");
            row.createCell(1).setCellValue(maNhanVien);
            row.createCell(2).setCellValue(tenNhanVien);
            row.createCell(3).setCellValue(doanhThu);
        }

        // Ghi tổng doanh thu
        Row totalRow = sheet.createRow(rowIndex);
        Cell totalLabelCell = totalRow.createCell(3);
        totalLabelCell.setCellValue("Tổng Doanh Thu:");
        totalLabelCell.setCellStyle(createHeaderCellStyle(workbook));

        Cell totalValueCell = totalRow.createCell(4);
        totalValueCell.setCellValue(totalRevenue);
        totalValueCell.setCellStyle(createHeaderCellStyle(workbook));

        // Tự động điều chỉnh độ rộng cột
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Xuất file
        try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
            workbook.write(fileOut);
            System.out.println("Xuất file Excel thành công: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Mở file Excel tự động
        try {
            File file = new File(fileName);
            if (file.exists() && Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }


}
