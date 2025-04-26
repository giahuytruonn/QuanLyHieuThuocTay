module qlhtt {
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires com.microsoft.sqlserver.jdbc;
    requires org.kordamp.bootstrapfx.core;
    requires spring.web;
    requires spring.core;
    requires com.google.gson;
    requires javafx.swing;
    requires spring.boot.autoconfigure;
    requires java.mail;
    requires spring.security.crypto;
    requires java.dotenv;
    requires com.jfoenix;
    requires javafx.controls;
    requires spring.context;
    requires spring.security.core;
    requires org.controlsfx.controls;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires jasperreports;
    requires org.bouncycastle.provider;
    requires static lombok;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.databind;

    // Mở package cho Hibernate
    opens qlhtt.Entity to org.hibernate.orm.core, com.fasterxml.jackson.databind, com.google.gson;

    opens qlhtt to javafx.fxml;
    exports qlhtt;
    exports qlhtt.Enum;
    exports qlhtt.Controllers;
    exports qlhtt.Models;
    exports qlhtt.Views;

    opens qlhtt.Controllers.NhanVien to javafx.fxml;
    exports qlhtt.Controllers.NhanVien;

    opens qlhtt.Controllers.NguoiQuanLy to javafx.fxml;
    exports qlhtt.Controllers.NguoiQuanLy;

    // JSON libraries
    opens qlhtt.Service to com.fasterxml.jackson.databind, com.google.gson;

    // Hibernate ORM (export là không đủ, cần opens)
    exports qlhtt.Entity;
}
