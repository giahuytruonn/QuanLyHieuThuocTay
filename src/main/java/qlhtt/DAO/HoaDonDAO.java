package qlhtt.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import qlhtt.ConnectDB.ConnectDB;
import qlhtt.ConnectDB.JPAUtil;
import qlhtt.Entity.ChiTietPhieuNhap;
import qlhtt.Entity.HoaDon;
import qlhtt.Enum.PhuongThucThanhToan;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HoaDonDAO {
    private static HoaDonDAO instance = new HoaDonDAO();

    public static HoaDonDAO getInstance() {
        return instance;
    }

    private EntityManager entityManager = JPAUtil.getEntityManager();
    public List<HoaDon> getDanhSachHoaDon() {
        String jpql = "SELECT h FROM HoaDon h";
        return entityManager.createQuery(jpql, HoaDon.class).getResultList();
    }


    public List<HoaDon> getDanhSachHoaDonTheoThang() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusMonths(1);
        LocalDate endDate = today;

        String jpql = """
        SELECT hd FROM HoaDon hd
        JOIN FETCH hd.nhanVien nv
        WHERE hd.ngayTao >= :startDate AND hd.ngayTao < :endDate
    """;

        return entityManager.createQuery(jpql, HoaDon.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }


    public List<HoaDon> getDanhSachHoaDonTheo7Ngay() {
        long startTime = System.currentTimeMillis();

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(7);

        String jpql = """
        SELECT hd FROM HoaDon hd
        JOIN FETCH hd.nhanVien nv
        WHERE hd.ngayTao >= :startDate AND hd.ngayTao <= :endDate
    """;

        List<HoaDon> dsHoaDon = entityManager.createQuery(jpql, HoaDon.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();

        System.out.println("Thời gian xử lý getDanhSachHoaDonTheo7Ngay(): " + (System.currentTimeMillis() - startTime) + "ms");

        return dsHoaDon;
    }


    public List<HoaDon> getDanhSachHoaDonTheoYeuCau(LocalDate dateStart, LocalDate dateEnd) {
        String jpql = "SELECT h FROM HoaDon h " +
                "INNER JOIN h.nhanVien nv " +
                "WHERE h.ngayTao BETWEEN :dateStart AND :dateEnd";
        return entityManager.createQuery(jpql, HoaDon.class)
                .setParameter("dateStart", dateStart)
                .setParameter("dateEnd", dateEnd)
                .getResultList();
    }


    public Map<Integer, Double> getDanhSachHoaDonTheoQuy(int year) {
        Map<Integer, Double> doanhThuTheoQuy = new HashMap<>();

        String jpql = """
        SELECT CEILING(MONTH(hd.ngayTao) / 3.0), SUM(hd.tongGiaTriHoaDon)
        FROM HoaDon hd
        WHERE FUNCTION('YEAR', hd.ngayTao) = :year
        GROUP BY CEILING(MONTH(hd.ngayTao) / 3.0)
        ORDER BY CEILING(MONTH(hd.ngayTao) / 3.0)
    """;

        List<Object[]> results = entityManager.createQuery(jpql, Object[].class)
                .setParameter("year", year)
                .getResultList();

        for (Object[] result : results) {
            Integer quy = ((Number) result[0]).intValue();
            Double doanhThu = ((Number) result[1]).doubleValue();
            doanhThuTheoQuy.put(quy, doanhThu);
        }

        return doanhThuTheoQuy;
    }



    public HoaDon getHoaDonBangMaHoaDon(String maHD) {
        String jpql = "SELECT h FROM HoaDon h WHERE h.maHoaDon = :maHD";
        try {
            return entityManager.createQuery(jpql, HoaDon.class)
                    .setParameter("maHD", maHD)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


    public HoaDon getHoaDonMoiNhat() {
        HoaDon hoaDon = null;
        try {
            TypedQuery<HoaDon> query = entityManager.createQuery(
                    "SELECT h FROM HoaDon h ORDER BY h.ngayTao DESC, h.maHoaDon DESC", HoaDon.class);
            query.setMaxResults(1); // Giới hạn chỉ lấy 1 kết quả đầu tiên
            hoaDon = query.getSingleResult();
        } catch (NoResultException e) {
            // Không tìm thấy hóa đơn nào
            hoaDon = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hoaDon;
    }


    public void taoHoaDon(HoaDon hoaDon) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(hoaDon);
            entityManager.flush();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    public Boolean capNhatHoaDon(HoaDon hoaDon) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(hoaDon);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return false;
        }
    }




    public List<HoaDon> getDsHoaDonBangTrangThai(boolean trangThai) {
        String jpql = "SELECT hd FROM HoaDon hd WHERE hd.trangThaiHoaDon = :trangThai";

        return entityManager.createQuery(jpql, HoaDon.class)
                .setParameter("trangThai", trangThai)
                .getResultList();
    }

    public Boolean capNhatTrangThaiHoaDon(HoaDon hoaDon) {
        try {
            entityManager.getTransaction().begin();
            HoaDon existingHoaDon = entityManager.find(HoaDon.class, hoaDon.getMaHoaDon());
            if (existingHoaDon != null) {
                existingHoaDon.setTrangThaiHoaDon(true);
                entityManager.merge(existingHoaDon);
                entityManager.getTransaction().commit();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
        return false;
    }


    public void xoaHoaDonGanNhat() {
        try {
            entityManager.getTransaction().begin();
            String jpql = "DELETE FROM HoaDon h WHERE h.ngayTao = (SELECT MAX(hd.ngayTao) FROM HoaDon hd)";
            int deletedCount = entityManager.createQuery(jpql).executeUpdate();
            entityManager.getTransaction().commit();
            if (deletedCount > 0) {
                System.out.println("Hoa don xoa thanh cong");
            } else {
                System.out.println("Khong tim thay hoa don de xoa");
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }


    public List<HoaDon> getDsHoaDonBangTrangThaiVaCoThanhVien(boolean trangThai) {
        String jpql = "SELECT hd FROM HoaDon hd WHERE hd.trangThaiHoaDon = :trangThai AND hd.khachHang IS NOT NULL";
        return entityManager.createQuery(jpql, HoaDon.class)
                .setParameter("trangThai", trangThai)
                .getResultList();
    }

    public List<HoaDon> getDsHoaDonBangTrangThaiVaKhongThanhVien(boolean trangThai) {
        String jpql = "SELECT hd FROM HoaDon hd WHERE hd.trangThaiHoaDon = :trangThai AND hd.khachHang IS NULL";
        return entityManager.createQuery(jpql, HoaDon.class)
                .setParameter("trangThai", trangThai)
                .getResultList();
    }


    public List<HoaDon> getHoaDonTheoTenThuoc(String tenThuoc) {
        String jpql = """
        SELECT DISTINCT hd
        FROM HoaDon hd
        JOIN hd.dsChiTietHoaDon ct
        JOIN ct.sanPham sp
        WHERE sp.tenSanPham = :tenThuoc
    """;
        return entityManager.createQuery(jpql, HoaDon.class)
                .setParameter("tenThuoc", tenThuoc)
                .getResultList();
    }

    public List<HoaDon> getDsHoaDonTheoSoDienThoai(String soDienThoai) {
        String jpql = """
        SELECT hd FROM HoaDon hd
        WHERE hd.khachHang.soDienThoai = :soDienThoai
    """;
        return entityManager.createQuery(jpql, HoaDon.class)
                .setParameter("soDienThoai", soDienThoai)
                .getResultList();
    }
}
