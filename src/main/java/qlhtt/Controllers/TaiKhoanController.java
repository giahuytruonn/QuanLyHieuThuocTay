package qlhtt.Controllers;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import qlhtt.DAO.TaiKhoanDAO;
import qlhtt.Entity.TaiKhoan;

import java.util.List;

public class TaiKhoanController {
    public static List<TaiKhoan> layDanhSachTaiKhoan() {
        return TaiKhoanDAO.getInstance().getDanhSachTaiKhoan();
    }

    public static String taoChuoiBCrypt(String matKhau) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(matKhau);
    }

    public static boolean kiemTraMatKhau(String matKhau, String matKhauBCrypt) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(matKhau, matKhauBCrypt);
    }
}
