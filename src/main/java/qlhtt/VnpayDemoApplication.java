package qlhtt;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VnpayDemoApplication {
    public static void main(String[] args) {
        // Chạy Spring Boot trong một luồng khác
        Thread springThread = new Thread(() -> SpringApplication.run(VnpayDemoApplication.class, args));
        springThread.setDaemon(true); // Đặt là luồng nền để nó không chặn ứng dụng chính
        springThread.start();

        // Khởi chạy JavaFX trên luồng chính
        Application.launch(App.class, args);
    }
}
