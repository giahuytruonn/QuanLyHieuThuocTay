package qlhtt.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import qlhtt.ConnectDB.JPAUtil;
import qlhtt.Entity.PhieuNhap;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PhieuNhapDAO {
    private static PhieuNhapDAO instance = new PhieuNhapDAO();
    private EntityManager entityManager = JPAUtil.getEntityManager();

    public static PhieuNhapDAO getInstance() {
        return instance;
    }

    public List<PhieuNhap> getDanhSachPhieuNhapTheoThang() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusMonths(1);
        LocalDate endDate = today;

        String jpql = """
        SELECT pn FROM PhieuNhap pn
        WHERE pn.ngayTao >= :startDate AND pn.ngayTao < :endDate
        """;

        return entityManager.createQuery(jpql, PhieuNhap.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }

    public List<PhieuNhap> getDanhSachPhieuNhapTheoYeuCau(LocalDate dateStart, LocalDate dateEnd) {
        String jpql = """
        SELECT pn FROM PhieuNhap pn
        WHERE pn.ngayTao >= :dateStart AND pn.ngayTao <= :dateEnd
        """;
        return entityManager.createQuery(jpql, PhieuNhap.class)
                .setParameter("dateStart", dateStart)
                .setParameter("dateEnd", dateEnd)
                .getResultList();
    }

    public PhieuNhap getPhieuNhapBangMaPhieuNhap(String maPhieuNhap) {
        String jpql = "SELECT pn FROM PhieuNhap pn WHERE pn.maPhieuNhap = :maPhieuNhap";
        try {
            return entityManager.createQuery(jpql, PhieuNhap.class)
                    .setParameter("maPhieuNhap", maPhieuNhap)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void taoPhieuNhap(PhieuNhap phieuNhap) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(phieuNhap);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    public PhieuNhap getPhieuNhapVuaTao() {
        String jpql = "SELECT pn FROM PhieuNhap pn ORDER BY pn.ngayTao DESC, pn.maPhieuNhap DESC";
        try {
            return entityManager.createQuery(jpql, PhieuNhap.class)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void capNhatTrangThaiPhieuNhap(String maPhieuNhap, boolean trangThaiPhieuNhap) {
        try {
            entityManager.getTransaction().begin();
            PhieuNhap phieuNhap = entityManager.find(PhieuNhap.class, maPhieuNhap);
            if (phieuNhap != null) {
                phieuNhap.setTrangThaiPhieuNhap(trangThaiPhieuNhap);
                entityManager.merge(phieuNhap);
                entityManager.getTransaction().commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    public Integer layTongSoPhieuNhap() {
        String jpql = "SELECT COUNT(pn) FROM PhieuNhap pn";
        return ((Long) entityManager.createQuery(jpql).getSingleResult()).intValue();
    }

    public List<PhieuNhap> layPhieuNhapTheoSoTrang(int soTrang) {
        String jpql = "SELECT pn FROM PhieuNhap pn ORDER BY pn.maPhieuNhap";
        return entityManager.createQuery(jpql, PhieuNhap.class)
                .setFirstResult((soTrang - 1) * 10)
                .setMaxResults(10)
                .getResultList();
    }
}
