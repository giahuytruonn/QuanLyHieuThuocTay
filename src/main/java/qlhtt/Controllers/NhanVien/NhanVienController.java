package qlhtt.Controllers.NhanVien;

import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;

public class NhanVienController {
    @FXML
    private AreaChart<String, Number> areaChart;
    @FXML
    public void initialize() {
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Series 1");

        series1.getData().add(new XYChart.Data<>("Jan", 200));
        series1.getData().add(new XYChart.Data<>("Feb", 300));
        series1.getData().add(new XYChart.Data<>("Mar", 150));

        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Series 2");

        series2.getData().add(new XYChart.Data<>("Jan", 100));
        series2.getData().add(new XYChart.Data<>("Feb", 200));
        series2.getData().add(new XYChart.Data<>("Mar", 250));

        areaChart.getData().addAll(series1, series2);
    }
}
