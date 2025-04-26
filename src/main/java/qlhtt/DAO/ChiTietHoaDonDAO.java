package qlhtt.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import qlhtt.ConnectDB.JPAUtil;
import qlhtt.Entity.ChiTietHoaDon;
import qlhtt.Entity.ChiTietHoaDonId;

import java.util.List;

public class ChiTietHoaDonDAO {
    private static ChiTietHoaDonDAO instance = new ChiTietHoaDonDAO();

    public static ChiTietHoaDonDAO getInstance() {
        return instance;
    }

    public List<ChiTietHoaDon> getDanhSachChiTietHoaDon() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = """
                SELECT c FROM ChiTietHoaDon c
                JOIN FETCH c.hoaDon h
                JOIN FETCH h.nhanVien nv
                JOIN FETCH c.sanPham sp
                JOIN FETCH sp.nhomSanPham
            """;
            TypedQuery<ChiTietHoaDon> query = em.createQuery(jpql, ChiTietHoaDon.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<ChiTietHoaDon> getDsChiTietHoaDonBangMaHoaDon(String maHoaDon) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT c FROM ChiTietHoaDon c WHERE c.hoaDon.maHoaDon = :maHoaDon";
            TypedQuery<ChiTietHoaDon> query = em.createQuery(jpql, ChiTietHoaDon.class);
            query.setParameter("maHoaDon", maHoaDon);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public ChiTietHoaDon getChiTietHoaDonBangMaHoaDon(String maHoaDon) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT c FROM ChiTietHoaDon c WHERE c.hoaDon.maHoaDon = :maHoaDon";
            TypedQuery<ChiTietHoaDon> query = em.createQuery(jpql, ChiTietHoaDon.class);
            query.setParameter("maHoaDon", maHoaDon);
            List<ChiTietHoaDon> result = query.setMaxResults(1).getResultList();
            return result.isEmpty() ? null : result.get(0);
        } finally {
            em.close();
        }
    }

    public void taoChiTietHoaDon(ChiTietHoaDon chiTietHoaDon) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            ChiTietHoaDonId chiTietHoaDonId = new ChiTietHoaDonId(chiTietHoaDon.getSanPham().getMaSanPham(), chiTietHoaDon.getHoaDon().getMaHoaDon());
            chiTietHoaDon.setChiTietHoaDonId(chiTietHoaDonId);
            em.persist(chiTietHoaDon);
            em.flush();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void xoaChiTietHoaDonBangMaHoaDon(String maHoaDon) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            String jpql = "DELETE FROM ChiTietHoaDon c WHERE c.hoaDon.maHoaDon = :maHoaDon";
            em.createQuery(jpql)
                    .setParameter("maHoaDon", maHoaDon)
                    .executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
