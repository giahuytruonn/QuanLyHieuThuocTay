package qlhtt.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import qlhtt.ConnectDB.JPAUtil;
import qlhtt.ConnectDB.JPAUtil;
import qlhtt.Entity.ChiTietPhieuNhap;
import qlhtt.Entity.PhieuNhap;
import qlhtt.Entity.SanPham;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ChiTietPhieuNhapDAO {

    private static ChiTietPhieuNhapDAO instance = new ChiTietPhieuNhapDAO();

    public static ChiTietPhieuNhapDAO getInstance() {
        return instance;
    }

    public List<ChiTietPhieuNhap> getDanhSachChiTietPhieuNhap() {
        EntityManager em = JPAUtil.getEntityManager();
        String jpql = "SELECT c FROM ChiTietPhieuNhap c";
        return em.createQuery(jpql, ChiTietPhieuNhap.class).getResultList();
    }

    public List<ChiTietPhieuNhap> getDanhSachSapHetHan() {
        EntityManager em = JPAUtil.getEntityManager();
        LocalDate today = LocalDate.now();
        LocalDate threeMonthsLater = today.plusMonths(3);

        String jpql = """
        SELECT c FROM ChiTietPhieuNhap c 
        WHERE c.hanSuDung BETWEEN :today AND :threeMonthsLater
        ORDER BY c.phieuNhap.maPhieuNhap ASC
        """;

        TypedQuery<ChiTietPhieuNhap> query = em.createQuery(jpql, ChiTietPhieuNhap.class);
        query.setParameter("today", today);
        query.setParameter("threeMonthsLater", threeMonthsLater);

        return query.getResultList();
    }

    public List<ChiTietPhieuNhap> getDanhSachChiTietPhieuNhapBangMaSanPham(String maSanPham) {
        EntityManager em = JPAUtil.getEntityManager();
        LocalDate threeMonthsLater = LocalDate.now().plusMonths(3);

        String jpql = """
        SELECT c FROM ChiTietPhieuNhap c
        WHERE c.sanPham.maSanPham = :maSanPham
        AND c.hanSuDung >= :threeMonthsLater
        ORDER BY c.phieuNhap.maPhieuNhap ASC
        """;

        TypedQuery<ChiTietPhieuNhap> query = em.createQuery(jpql, ChiTietPhieuNhap.class);
        query.setParameter("maSanPham", maSanPham);
        query.setParameter("threeMonthsLater", threeMonthsLater);

        return query.getResultList();
    }

    public ChiTietPhieuNhap getChiTietPhieuNhapHienTaiBangMaSanPham(String maSanPham) {
        EntityManager em = JPAUtil.getEntityManager();
        LocalDate threeMonthsLater = LocalDate.now().plusMonths(3);

        String jpql = """
        SELECT c FROM ChiTietPhieuNhap c
        WHERE c.sanPham.maSanPham = :maSanPham
        AND c.hanSuDung >= :threeMonthsLater
        ORDER BY c.phieuNhap.maPhieuNhap ASC
        """;

        List<ChiTietPhieuNhap> results = em.createQuery(jpql, ChiTietPhieuNhap.class)
                .setParameter("maSanPham", maSanPham)
                .setParameter("threeMonthsLater", threeMonthsLater)
                .setMaxResults(1)
                .getResultList();

        return results.isEmpty() ? null : results.get(0);
    }



    public ChiTietPhieuNhap getChiTietPhieuNhapBangMaPhieuNhap(String maPhieuNhap) {
        EntityManager em = JPAUtil.getEntityManager();
        String jpql = "SELECT c FROM ChiTietPhieuNhap c WHERE c.phieuNhap.maPhieuNhap = :maPhieuNhap";
        List<ChiTietPhieuNhap> results = em.createQuery(jpql, ChiTietPhieuNhap.class)
                .setParameter("maPhieuNhap", maPhieuNhap)
                .setMaxResults(1)
                .getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    public ChiTietPhieuNhap getChiTietPhieuNhapBangMaSanPham(String maSanPham) {
        EntityManager em = JPAUtil.getEntityManager();
        String jpql = "SELECT c FROM ChiTietPhieuNhap c WHERE c.sanPham.maSanPham = :maSanPham";
        List<ChiTietPhieuNhap> results = em.createQuery(jpql, ChiTietPhieuNhap.class)
                .setParameter("maSanPham", maSanPham)
                .setMaxResults(1)
                .getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    public Boolean capNhatChiTietPhieuNhap(ChiTietPhieuNhap chiTietPhieuNhap) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(chiTietPhieuNhap);
            tx.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (tx.isActive()) tx.rollback();
        }
        return false;
    }

    public void taoChiTietPhieuNhap(ChiTietPhieuNhap chiTietPhieuNhap) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(chiTietPhieuNhap);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (tx.isActive()) tx.rollback();
        }
    }
}
