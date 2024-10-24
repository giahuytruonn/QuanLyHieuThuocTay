package qlhtt.Test;

import qlhtt.Controllers.TaiKhoanController;

public class TestCheckPass {
    public static void main(String[] args) {
        String matKhau = "1";
        String matKhauBCrypt = "$2a$12$uH4VF35FQHcNioWHJWeLLOFWgUGbmUUgihRENSs7IAeSraKpbEbCK";
        System.out.println(TaiKhoanController.kiemTraMatKhau(matKhau, matKhauBCrypt));
    }
}
