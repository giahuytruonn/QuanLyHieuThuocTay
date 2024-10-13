module qlhtt {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;
    requires com.microsoft.sqlserver.jdbc;
    requires org.kordamp.bootstrapfx.core;
    requires spring.web;
    requires java.base;
    requires com.fasterxml.jackson.databind;
    requires com.google.gson;
    requires java.desktop;
    requires javafx.swing;


    opens qlhtt to javafx.fxml;
    exports qlhtt;
    exports qlhtt.Controllers;
    exports qlhtt.Models;
    exports qlhtt.Views;

    opens qlhtt.Controllers.NhanVien to javafx.fxml;
    exports qlhtt.Controllers.NhanVien;

    opens qlhtt.Controllers.NguoiQuanLy to javafx.fxml;
    exports qlhtt.Controllers.NguoiQuanLy;


    exports qlhtt.Entity to com.fasterxml.jackson.databind, com.google.gson;
    opens qlhtt.Entity to com.fasterxml.jackson.databind, com.google.gson;

}