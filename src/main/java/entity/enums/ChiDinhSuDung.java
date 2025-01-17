package entity;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public enum ChiDinhSuDung {
    KE_TOA("Kê toa"), KHONG_KE_TOA("Không kê toa");
    private final String chiDinhSuDung;
}
