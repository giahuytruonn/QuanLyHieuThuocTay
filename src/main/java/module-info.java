module qlhtt {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;
    requires com.microsoft.sqlserver.jdbc;

    requires org.kordamp.bootstrapfx.core;

    opens qlhtt to javafx.fxml;
    exports qlhtt;
    exports qlhtt.Controllers;
    opens qlhtt.Models to javafx.fxml;
    opens qlhtt.Views to javafx.fxml;

    opens qlhtt.Controllers to javafx.fxml;
    exports qlhtt.Controllers.NguoiQuanLy to javafx.fxml;
    exports qlhtt.Controllers.NhanVien to javafx.fxml;





}