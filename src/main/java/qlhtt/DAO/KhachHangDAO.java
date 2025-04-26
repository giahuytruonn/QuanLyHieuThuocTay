package qlhtt.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import qlhtt.ConnectDB.JPAUtil;
import qlhtt.Entity.KhachHang;

import java.util.List;

public class KhachHangDAO {
    private static KhachHangDAO instance = new KhachHangDAO();

    public static KhachHangDAO getInstance() {
        return instance;
    }

    private EntityManager entityManager = JPAUtil.getEntityManager();

    public List<KhachHang> getDanhSachKhachHang() {
        String jpql = "SELECT k FROM KhachHang k";
        return entityManager.createQuery(jpql, KhachHang.class).getResultList();
    }

    public KhachHang getKhachHangBangMaKhachHang(String maKH) {
        String jpql = "SELECT k FROM KhachHang k WHERE k.maKhachHang = :maKH";
        try {
            return entityManager.createQuery(jpql, KhachHang.class)
                    .setParameter("maKH", maKH)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public boolean capNhatKhachHang(KhachHang khachHang) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(khachHang);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
        return false;
    }

    public boolean capNhatDiemTichLuyCuaKhachHang(String maKhachHang, int diemTichLuy) {
        entityManager.clear();
        String jpql = "UPDATE KhachHang k SET k.diemTichLuy = :diemTichLuy WHERE k.maKhachHang = :maKhachHang";
        try {
            entityManager.getTransaction().begin();
            int updatedCount = entityManager.createQuery(jpql)
                    .setParameter("diemTichLuy", diemTichLuy)
                    .setParameter("maKhachHang", maKhachHang)
                    .executeUpdate();
            entityManager.flush();
            entityManager.getTransaction().commit();
            return updatedCount > 0;
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
        return false;
    }

    public KhachHang getKhachHangBangSoDienThoai(String soDienThoai) {
        entityManager.clear();
        String jpql = "SELECT k FROM KhachHang k WHERE k.soDienThoai = :soDienThoai";
        try {
            return entityManager.createQuery(jpql, KhachHang.class)
                    .setParameter("soDienThoai", soDienThoai)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public boolean themKhachHang(KhachHang khachHang) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(khachHang);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
        return false;
    }
}
