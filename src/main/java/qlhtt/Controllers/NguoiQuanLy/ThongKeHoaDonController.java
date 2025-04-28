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
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.Duration;

import qlhtt.Controllers.LoginController;
import qlhtt.DAO.HoaDonDAO;

import qlhtt.Entity.HoaDon;
import qlhtt.Entity.NhanVien;
import qlhtt.Enum.PhuongThucThanhToan;
import qlhtt.Models.Model;
import qlhtt.ThongBao.ThongBao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.util.*;




//+thống kê hóa đơn 1 tháng, date picker : bar , pie


public class ThongKeHoaDonController {


    @FXML
    private Label label_soThanhToan;

    @FXML
    private Label label_soChuaThanhToan;

    @FXML
    private Label label_ngayThanhToan;

    @FXML
    private Label label_ngayChuaThanhToan;

    @FXML
    private Label label_tongHoaDon;

    @FXML
    private Label label_ngayTienMat;

    @FXML
    private Label label_ngayQR;

    @FXML
    private Label label_tienMat;

    @FXML
    private Label label_QR;



    @FXML
    private BarChart<String,Number> barChart_PTTT;


    @FXML
    private BarChart<Number,String> barChart_hoaDonNhanVien;

    @FXML
    private ComboBox<PhuongThucThanhToan> comboBox_PTTT;


    ObservableList<LocalDate> selectedDates = FXCollections.observableArrayList();

    private static final Dotenv dotenv = Dotenv.load();
    private static final String SERVER_HOST = dotenv.get("SERVER_HOST");// Địa chỉ server
    private static final int SERVER_PORT = Integer.parseInt(dotenv.get("SERVER_PORT"));

    @FXML
    private DatePicker datePicker;

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
        tinhTongHoaDonHomNay(selectedDates.get(0), selectedDates.get(1));
        tinhHoaDonDaThanhToan(selectedDates.get(0), selectedDates.get(1));
        tinhHoaDonChuaThanhToan(selectedDates.get(0), selectedDates.get(1));
        tinhHoaDonTienMat(selectedDates.get(0), selectedDates.get(1));
        tinhHoaDonQR(selectedDates.get(0), selectedDates.get(1));
        taoDuLieuBarChartHoaDon(selectedDates.get(0), selectedDates.get(1));
        taoDuLieuBarChartPTTT(selectedDates.get(0), selectedDates.get(1));



        //Style sau khi nhấn DatePicker
        label_ngayQR.setText(selectedDates.get(0) + "->" + selectedDates.get(1));
        label_ngayQR.setStyle("-fx-font-size: 15px");
        label_ngayChuaThanhToan.setText(selectedDates.get(0) + "->" + selectedDates.get(1));
        label_ngayChuaThanhToan.setStyle("-fx-font-size: 15px");
        label_ngayThanhToan.setText(selectedDates.get(0) + "->" + selectedDates.get(1));
        label_ngayThanhToan.setStyle("-fx-font-size: 15px");
        label_ngayTienMat.setText(selectedDates.get(0) + "->" + selectedDates.get(1));
        label_ngayTienMat.setStyle("-fx-font-size: 15px");

    }


    @FXML
    public void initialize() {
        List<HoaDon> dsHD = getDanhSachHoaDonTheoYeuCau(LocalDate.now(), LocalDate.now());
        if(dsHD != null) {
            setComboBox_PTTT();
            chooseDate();

            tinhTongHoaDonHomNay(LocalDate.now(),LocalDate.now());
            tinhHoaDonDaThanhToan(LocalDate.now(),LocalDate.now());
            tinhHoaDonChuaThanhToan(LocalDate.now(),LocalDate.now());
            tinhHoaDonTienMat(LocalDate.now(),LocalDate.now());
            tinhHoaDonQR(LocalDate.now(),LocalDate.now());
            taoDuLieuBarChartHoaDon(LocalDate.now(),LocalDate.now());
            taoDuLieuBarChartPTTT(LocalDate.now(),LocalDate.now());
        } else {
            setComboBox_PTTT();
            chooseDate();
        }
        //Set cac label

        //Set thongKeHoaDon

    }

    private void setComboBox_PTTT() {
        ObservableList<PhuongThucThanhToan> options = FXCollections.observableArrayList(PhuongThucThanhToan.QRCODE, PhuongThucThanhToan.TIENMAT);
        comboBox_PTTT.setItems(options);
        comboBox_PTTT.setOnAction(event -> {
            PhuongThucThanhToan pttt = comboBox_PTTT.getValue();
            if (pttt != null) {
                if(selectedDates.size() >= 2) {
                    taoDuLieuBarChartTheoPTTT(selectedDates.get(0),selectedDates.get(1),pttt);
                } else {
                    taoDuLieuBarChartTheoPTTT(LocalDate.now(),LocalDate.now(),pttt);
                }
            }
        });
    }

    private List<HoaDon> getDanhSachHoaDonTheoYeuCau(LocalDate date, LocalDate date2) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Gửi yêu cầu đăng nhập tới server
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String request = String.format("GET_LIST_HD_YEU_CAU %s %s",date.toString(), date2.toString());
            out.println(request);

            //Nhan dữ liệu từ server
            String response = in.readLine();
            //Chuyen doi Json
            if(response.equals("NOT_FOUND")){
                return null;
            }else {
                List<HoaDon> dsHoaDon = Arrays.asList(objectMapper.readValue(response, HoaDon[].class));
                if (dsHoaDon.size() > 0) {
                    return dsHoaDon;
                } else {
                    ThongBao.thongBaoLoi("Không có dữ liệu để thống kê");
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void tinhTongHoaDonHomNay(LocalDate date, LocalDate date2) {
        List<HoaDon> dsHoaDon = getDanhSachHoaDonTheoYeuCau(date,date2);
        label_tongHoaDon.setText(dsHoaDon.size() + "");
    }

    private void tinhHoaDonDaThanhToan(LocalDate date, LocalDate date2){
        List<HoaDon> dsHoaDon = getDanhSachHoaDonTheoYeuCau(date,date2);
        long tong = dsHoaDon.stream().filter(hoaDon -> hoaDon.getTrangThaiHoaDon()==true)
                .count();
        label_soThanhToan.setText((int)tong + "");
    }

    private void tinhHoaDonChuaThanhToan(LocalDate date, LocalDate date2){
        List<HoaDon> dsHoaDon = getDanhSachHoaDonTheoYeuCau(date,date2);
        long tong = dsHoaDon.stream().filter(hoaDon -> hoaDon.getTrangThaiHoaDon()==false)
                .count();
        label_soChuaThanhToan.setText((int)tong + "");
    }

    private void tinhHoaDonTienMat(LocalDate date, LocalDate date2) {
        List<HoaDon> dsHoaDon = getDanhSachHoaDonTheoYeuCau(date,date2);
        long tong = dsHoaDon.stream().filter(hoaDon -> hoaDon.getPhuongThucThanhToan().equals(PhuongThucThanhToan.TIENMAT))
                .count();
        label_tienMat.setText((int)tong + "");
    }

    private void tinhHoaDonQR(LocalDate date, LocalDate date2) {
        List<HoaDon> dsHoaDon = getDanhSachHoaDonTheoYeuCau(date,date2);
        long tong = dsHoaDon.stream().filter(hoaDon -> hoaDon.getPhuongThucThanhToan().equals(PhuongThucThanhToan.QRCODE))
                .count();
        label_QR.setText((int)tong + "");
    }

    public void restart(MouseEvent event) {
        comboBox_PTTT.setValue(null);
        datePicker.setValue(null);
        label_ngayQR.setText("hôm nay");
        label_ngayQR.setStyle("-fx-font-size: 20px");
        label_ngayChuaThanhToan.setText("hôm nay");
        label_ngayChuaThanhToan.setStyle("-fx-font-size: 20px");
        label_ngayTienMat.setText("hôm nay");
        label_ngayTienMat.setStyle("-fx-font-size: 20px");
        label_ngayThanhToan.setText("hôm nay");
        label_ngayThanhToan.setStyle("-fx-font-size: 20px");

        barChart_hoaDonNhanVien.getData().clear();
        barChart_PTTT.getData().clear();

        tinhTongHoaDonHomNay(LocalDate.now(),LocalDate.now());
        tinhHoaDonDaThanhToan(LocalDate.now(),LocalDate.now());
        tinhHoaDonChuaThanhToan(LocalDate.now(),LocalDate.now());
        tinhHoaDonTienMat(LocalDate.now(),LocalDate.now());
        tinhHoaDonQR(LocalDate.now(),LocalDate.now());
        taoDuLieuBarChartHoaDon(LocalDate.now(),LocalDate.now());
        taoDuLieuBarChartPTTT(LocalDate.now(),LocalDate.now());
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

    //   THỐNG KÊ HÓA ĐƠN
    private void taoDuLieuBarChartPTTT(LocalDate date, LocalDate date2) {
        HashMap<PhuongThucThanhToan, Integer> dsHoaDonPTTT = getSoHoaDonTheoPTTT(date,date2);

        barChart_PTTT.getData().clear();
        barChart_PTTT.setAnimated(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<PhuongThucThanhToan, Integer> entry : dsHoaDonPTTT.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue()));
        }

        barChart_PTTT.getData().add(series);
        barChart_PTTT.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent;");

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
    private void taoDuLieuBarChartTheoPTTT(LocalDate date, LocalDate date2, PhuongThucThanhToan pttt) {
        HashMap<PhuongThucThanhToan, Integer> dsHoaDonPTTT = getSoHoaDonTheoPTTT(date,date2);

        barChart_PTTT.getData().clear();
        barChart_PTTT.setAnimated(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<PhuongThucThanhToan, Integer> entry : dsHoaDonPTTT.entrySet()) {
            if(entry.getKey().equals(pttt)) {
                series.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue()));
            }
        }

        barChart_PTTT.getData().add(series);
        barChart_PTTT.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent;");

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

    private void taoDuLieuBarChartHoaDon(LocalDate date, LocalDate date2) {
        List<HoaDon> dsHD = getDanhSachHoaDonTheoYeuCau(date,date2);
        HashMap<String, Integer> dsHoaDonNhanVien = getSoHoaDonTheoNhanVien(date,date2);
        barChart_hoaDonNhanVien.getData().clear();
        barChart_hoaDonNhanVien.setAnimated(false);

        XYChart.Series<Number, String> series = new XYChart.Series<>();
        for (Map.Entry<String, Integer> entry : dsHoaDonNhanVien.entrySet()) {
            String maNhanVien = entry.getKey();
            String tenNhanVien = "";
            for(HoaDon hd : dsHD) {
                if(hd.getNhanVien().getMaNhanVien().equals(maNhanVien)) {
                    tenNhanVien = hd.getNhanVien().getTenNhanVien();
                }
            }
            Integer soHoaDon = entry.getValue();
            series.getData().add(new XYChart.Data<>(soHoaDon, tenNhanVien));
        }

        barChart_hoaDonNhanVien.getData().add(series);
        barChart_hoaDonNhanVien.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent;");

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


    private HashMap<String, Integer> getSoHoaDonTheoNhanVien(LocalDate date, LocalDate date2) {


        List<HoaDon> dsHD = getDanhSachHoaDonTheoYeuCau(date, date2);


        String maNhanVien = Model.getInstance().getTaiKhoan().getNhanVien().getMaNhanVien();


        long soHoaDon = dsHD.stream()
                .filter(hd -> hd.getNhanVien().getMaNhanVien().equals(maNhanVien))
                .count();


        HashMap<String, Integer> map = new HashMap<>();
        map.put(maNhanVien, (int) soHoaDon);

        return map;
    }

    private HashMap<PhuongThucThanhToan, Integer> getSoHoaDonTheoPTTT(LocalDate date, LocalDate date2) {
        List<HoaDon> dsHD = getDanhSachHoaDonTheoYeuCau(date,date2);
        HashMap<PhuongThucThanhToan, Integer> map = new HashMap<>();
        for(HoaDon hd : dsHD) {
            PhuongThucThanhToan pttt = hd.getPhuongThucThanhToan();
            map.merge(pttt, 1 , Integer::sum);
        }
        return map;
    }


}
