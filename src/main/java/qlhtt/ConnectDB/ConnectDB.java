package qlhtt.ConnectDB;
import java.sql.*;
public class ConnectDB {
    public static Connection con = null;
    private static ConnectDB instance = new ConnectDB();

    public static ConnectDB getInstance() {
        return instance;
    }

    public void connect() throws SQLException {
        String serverName = "localhost";
        String databaseName = "QuanLyHieuThuocTay";
        String username = "sa";
        String password = "sa";
        String url = "jdbc:sqlserver://" + serverName + ":1433;databaseName=" + databaseName + ";encrypt=true;trustServerCertificate=true;";

        con = DriverManager.getConnection(url, username, password);
    }


    public static Connection getConnection() {
        return con;
    }

}