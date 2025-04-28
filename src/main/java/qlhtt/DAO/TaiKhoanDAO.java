package qlhtt.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import qlhtt.ConnectDB.JPAUtil;
import qlhtt.Entity.NhanVien;
import qlhtt.Entity.TaiKhoan;

import java.util.List;

public class TaiKhoanDAO {
    private static TaiKhoanDAO instance = new TaiKhoanDAO();

    public static TaiKhoanDAO getInstance() {
        return instance;
    }

    private EntityManager entityManager = JPAUtil.getEntityManager();

    public List<TaiKhoan> getDanhSachTaiKhoan() {
        entityManager.clear();
        String jpql = "SELECT t FROM TaiKhoan t";
        return entityManager.createQuery(jpql, TaiKhoan.class).getResultList();
    }

    public List<TaiKhoan> getDanhSachTaiKhoanTheoTrangThai() {
        String jpql = "SELECT t FROM TaiKhoan t WHERE t.trangThaiTaiKhoan = true";
        return entityManager.createQuery(jpql, TaiKhoan.class).getResultList();
    }

    public TaiKhoan getTaiKhoanBangMaTaiKhoan(String maTK) {
        String jpql = "SELECT t FROM TaiKhoan t WHERE t.maTaiKhoan = :maTK";
        try {
            return entityManager.createQuery(jpql, TaiKhoan.class)
                    .setParameter("maTK", maTK)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void updateDatabaseStatus(String maNhanVien, boolean trangThaiTaiKhoan) {
        String jpql = "UPDATE TaiKhoan t SET t.trangThaiTaiKhoan = :trangThaiTaiKhoan WHERE t.nhanVien.maNhanVien = :maNhanVien";
        entityManager.getTransaction().begin();
        entityManager.createQuery(jpql)
                .setParameter("trangThaiTaiKhoan", trangThaiTaiKhoan)
                .setParameter("maNhanVien", maNhanVien)
                .executeUpdate();
        entityManager.getTransaction().commit();
    }

    public boolean addTaiKhoan(TaiKhoan taiKhoan, String maNV) {
        try {
            entityManager.getTransaction().begin();
            taiKhoan.setNhanVien(entityManager.find(NhanVien.class, maNV));
            entityManager.persist(taiKhoan);
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

    public boolean updateMatKhau(String maTaiKhoan, String matKhauMoi) {
        String jpql = "UPDATE TaiKhoan t SET t.matKhau = :matKhauMoi WHERE t.maTaiKhoan = :maTaiKhoan";
        try {
            entityManager.getTransaction().begin();
            int rowsUpdated = entityManager.createQuery(jpql)
                    .setParameter("matKhauMoi", matKhauMoi)
                    .setParameter("maTaiKhoan", maTaiKhoan)
                    .executeUpdate();
            entityManager.getTransaction().commit();
            return rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return false;
        }
    }

    public TaiKhoan timTaiKhoanBangEmailNhanVien(String email) {
        String jpql = "SELECT t FROM TaiKhoan t WHERE t.nhanVien.email = :email";
        try {
            return entityManager.createQuery(jpql, TaiKhoan.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public boolean capNhatMatKhauMoi(String matKhau, TaiKhoan taiKhoan) {
        String jpql = "UPDATE TaiKhoan t SET t.matKhau = :matKhau WHERE t.maTaiKhoan = :maTaiKhoan";
        try {
            entityManager.getTransaction().begin();
            int rowsUpdated = entityManager.createQuery(jpql)
                    .setParameter("matKhau", matKhau)
                    .setParameter("maTaiKhoan", taiKhoan.getMaTaiKhoan())
                    .executeUpdate();
            entityManager.getTransaction().commit();
            return rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return false;
        }
    }
}
