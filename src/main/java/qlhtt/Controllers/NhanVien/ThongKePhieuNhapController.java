package qlhtt.Controllers.NhanVien;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import org.springframework.cglib.core.Local;
import qlhtt.DAO.PhieuNhapDAO;
import qlhtt.Entity.HoaDon;
import qlhtt.Entity.PhieuNhap;

import java.time.LocalDate;
import java.util.*;


//+phiếu nhập 1 tháng, date picker : bar , pie

//Only CSS

public class ThongKePhieuNhapController {


    ObservableList<LocalDate> selectedDates = FXCollections.observableArrayList();

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label label_phieuNhapMoi;

    @FXML
    private Label label_ngayPhieuNhapMoi;



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
        taoDuLieuBarChartPhieuNhap(selectedDates.get(0), selectedDates.get(1));
        taoDuLieuPieChartPhieuNhap(selectedDates.get(0), selectedDates.get(1));
        label_phieuNhapMoi.setText(tinhSoPhieuNhapYeuCau(selectedDates.get(0), selectedDates.get(1)) + "");

        label_ngayPhieuNhapMoi.setText(selectedDates.get(0) + "->" + selectedDates.get(1));
        label_ngayPhieuNhapMoi.setStyle("-fx-font-size: 15px");

    }
    //Khai bao Phieu Nhap Thong Ke

    @FXML
    private BarChart<Number,String> barChart_phieuNhap;

    @FXML
    private PieChart pieChart_phieuNhap;

    @FXML
    public void initialize() {
        chooseDate();

//        PhieuNhap
        taoDuLieuBarChartPhieuNhap(LocalDate.now(), LocalDate.now());
        taoDuLieuPieChartPhieuNhap(LocalDate.now(), LocalDate.now());

        label_phieuNhapMoi.setText(tinhSoPhieuNhapYeuCau(LocalDate.now(),LocalDate.now())+ "");
    }

    public void restart(MouseEvent event) {
        pieChart_phieuNhap.getData().clear();
        barChart_phieuNhap.getData().clear();

        datePicker.setValue(null);
        label_ngayPhieuNhapMoi.setText("hôm nay");
        label_ngayPhieuNhapMoi.setStyle("-fx-font-size: 20px");

        label_phieuNhapMoi.setText(tinhSoPhieuNhapYeuCau(LocalDate.now(),LocalDate.now())+ "");

        taoDuLieuBarChartPhieuNhap(LocalDate.now(),LocalDate.now());
        taoDuLieuPieChartPhieuNhap(LocalDate.now(),LocalDate.now());

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

    private void taoDuLieuBarChartPhieuNhap(LocalDate date1, LocalDate date2) {
        barChart_phieuNhap.getData().clear();
        barChart_phieuNhap.setAnimated(false);

        XYChart.Series<Number, String> series = new XYChart.Series<>();
        HashMap<String , Double> dsPN = getDSTongTienPhieuNhap(date1, date2);

        for(Map.Entry<String , Double> entry : dsPN.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getValue(),entry.getKey()));
        }

        barChart_phieuNhap.getData().add(series);
        barChart_phieuNhap.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent;");

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


    private void taoDuLieuPieChartPhieuNhap(LocalDate date1, LocalDate date2) {
        pieChart_phieuNhap.getData().clear();

        HashMap<String, Integer> dsPhieuNhap = getSoPhieuNhapTheoNhaCungCap(date1, date2);

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        int total = dsPhieuNhap.values().stream().mapToInt(Integer::intValue).sum();

        for (Map.Entry<String, Integer> entry : dsPhieuNhap.entrySet()) {
            String nhaCungCap = entry.getKey();
            int soPhieuNhap = entry.getValue();
            pieChartData.add(new PieChart.Data(nhaCungCap, soPhieuNhap));
        }


        pieChart_phieuNhap.setData(pieChartData);
        Timeline timeline = new Timeline();
        int index = 0;

        for (PieChart.Data data : pieChartData) {
            double finalValue = data.getPieValue();
            KeyValue keyValue = new KeyValue(data.pieValueProperty(), finalValue);
            KeyFrame keyFrame = new KeyFrame(
                    Duration.millis(500 + index * 300),
                    keyValue
            );
            timeline.getKeyFrames().add(keyFrame);
            index++;

            // Gắn sự kiện hiển thị thông tin khi click vào phần PieChart
            data.getNode().setOnMouseClicked(event -> {
                double percentage = (data.getPieValue() / total) * 100;
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông tin phần trăm");
                alert.setHeaderText(null);
                alert.setContentText(data.getName() + ": " + String.format("%.2f", percentage) + "%");
                alert.showAndWait();
            });
        }

        // Chạy hiệu ứng
        timeline.play();
    }


    private HashMap<String , Double> getDSTongTienPhieuNhap(LocalDate date, LocalDate date1) {
        List<PhieuNhap> dsPN = PhieuNhapDAO.getInstance().getDanhSachPhieuNhapTheoYeuCau(date,date1);
        HashMap<String , Double> map = new HashMap<>();
        for( PhieuNhap pn :  dsPN) {
            map.put(pn.getMaPhieuNhap(), pn.getTongTien());
        }
        return map;
    }

    private HashMap<String, Integer> getSoPhieuNhapTheoNhaCungCap(LocalDate date, LocalDate date1) {
        List<PhieuNhap> dsPhieuNhap = PhieuNhapDAO.getInstance().getDanhSachPhieuNhapTheoYeuCau(date, date1);
        HashMap<String, Integer> map = new HashMap<>();
        for (PhieuNhap phieuNhap : dsPhieuNhap) {
            String maNhaCungCap = phieuNhap.getNhaCungCap().getMaNhaCungCap();
            map.merge(maNhaCungCap, 1, Integer::sum);
        }
        return map;
    }

    private int tinhSoPhieuNhapYeuCau(LocalDate date, LocalDate date1) {
        List<PhieuNhap> dsPhieuNhap = PhieuNhapDAO.getInstance().getDanhSachPhieuNhapTheoYeuCau(date, date1);
        return dsPhieuNhap.size();
    }

}
