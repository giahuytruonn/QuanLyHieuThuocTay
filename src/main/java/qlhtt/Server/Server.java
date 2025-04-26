package qlhtt.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
     //   Config config = Config.getInstance();
        int port = 12345; // Cổng server
//        config.setSERVER_HOST("localhost");
//        config.setSERVER_PORT(12345);
//
//        System.out.println(config.getSERVER_PORT());
//        System.out.println(config.getSERVER_HOST());
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server đang chạy trên cổng " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Kết nối từ client: " + clientSocket.getInetAddress());

                // Tạo một luồng riêng để xử lý client
                HandleClient clientHandler = new HandleClient(clientSocket);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi chạy server: " + e.getMessage());
        }
    }
}