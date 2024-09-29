package qlhtt.Controllers.NguoiQuanLy;

import javafx.fxml.FXML;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.XYChart;

public class NguoiQuanLyController {
    // Tạo biểu đồ bong bóng
    @FXML
    BubbleChart<Number, Number>  buble_id;

    @FXML
    public void initialize() {
        buble_id.setTitle("Biểu đồ Bong Bóng JavaFX");

        // Dữ liệu cho series 1
        XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
        series1.setName("Series 1");
        series1.getData().add(new XYChart.Data<>(10, 20, 5));
        series1.getData().add(new XYChart.Data<>(30, 40, 10));
        series1.getData().add(new XYChart.Data<>(50, 60, 15));

        // Dữ liệu cho series 2
        XYChart.Series<Number, Number> series2 = new XYChart.Series<>();
        series2.setName("Series 2");
        series2.getData().add(new XYChart.Data<>(20, 30, 7));
        series2.getData().add(new XYChart.Data<>(40, 50, 12));
        series2.getData().add(new XYChart.Data<>(60, 70, 20));

        // Thêm dữ liệu vào biểu đồ
        buble_id.getData().addAll(series1, series2);
    }
}
