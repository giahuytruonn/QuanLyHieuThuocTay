package qlhtt.Entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class ChiTietHoaDonId implements Serializable {
    private String maSanPham;
    private String maHoaDon;

    public ChiTietHoaDonId() {}

    public ChiTietHoaDonId(String maSanPham, String maHoaDon) {
        this.maSanPham = maSanPham;
        this.maHoaDon = maHoaDon;
    }
}
