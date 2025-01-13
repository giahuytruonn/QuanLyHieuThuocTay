package entity;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public enum PhuongThucThanhToan {
    TIEN_MAT("Tiền mặt"), CHUYEN_KHOAN("Chuyển khoản");

    private String value;
}
