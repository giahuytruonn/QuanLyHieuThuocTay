package qlhtt.Test;

import qlhtt.DAO.DonViTinhDAO;
import qlhtt.Entity.DonViTinh;

import java.util.List;

public class TestDonViTinh {
    public static void main(String[] args) {
        DonViTinhDAO donViTinhDAO = DonViTinhDAO.getInstance();
        List<DonViTinh> danhSachDVT = donViTinhDAO.getDanhSachDonViTinh();
        for (DonViTinh donViTinh : danhSachDVT) {
            System.out.println(donViTinh);
        }
        DonViTinh donViTinh = donViTinhDAO.getDonViTinhBangMaDonViTinh("DVT001");
        System.out.println(donViTinh);
    }
}
