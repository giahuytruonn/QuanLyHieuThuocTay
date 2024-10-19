module qlhtt {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;
    requires com.microsoft.sqlserver.jdbc;
    requires org.kordamp.bootstrapfx.core;
    requires spring.web;
    requires spring.core; // Thêm dòng này
    requires java.base;
    requires com.fasterxml.jackson.databind;
    requires com.google.gson;
    requires java.desktop;
    requires javafx.swing;
    //requires jakarta.mail;
    requires spring.boot.autoconfigure;
    requires java.mail;
    //requires com.fasterxml.jackson.databind;


    opens qlhtt to javafx.fxml;
    exports qlhtt;
    exports qlhtt.Controllers;
    exports qlhtt.Models;
    exports qlhtt.Views;

    opens qlhtt.Controllers.NhanVien to javafx.fxml;
    exports qlhtt.Controllers.NhanVien;

    opens qlhtt.Controllers.NguoiQuanLy to javafx.fxml;
    exports qlhtt.Controllers.NguoiQuanLy;

    opens qlhtt.Entity to com.fasterxml.jackson.databind, com.google.gson;
    opens qlhtt.Service to com.fasterxml.jackson.databind, com.google.gson;
    exports qlhtt.Controllers.Menu;
    opens qlhtt.Controllers.Menu to javafx.fxml;

    //exports qlhtt.Entity to com.fasterxml.jackson.databind, com.google.gson;


}
