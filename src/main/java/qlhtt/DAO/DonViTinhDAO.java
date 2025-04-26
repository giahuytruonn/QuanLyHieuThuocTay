package qlhtt.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import qlhtt.ConnectDB.JPAUtil;
import qlhtt.Entity.DonViTinh;
import java.util.List;

public class DonViTinhDAO {
    private static DonViTinhDAO instance = new DonViTinhDAO();

    public static DonViTinhDAO getInstance() {
        return instance;
    }

    /**
     *<B>Note:</B> Lấy danh sách đơn vị tính
     *@return danhSachDVT
     *
     */
    public List<DonViTinh> getDanhSachDonViTinh() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT d FROM DonViTinh d";
            TypedQuery<DonViTinh> query = em.createQuery(jpql, DonViTinh.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     *<B>Note:</B> Lấy đơn vị tính bằng mã đơn vị tính
     *@return donViTinh
     *
     */
    public DonViTinh getDonViTinhBangMaDonViTinh(String maDVT) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT d FROM DonViTinh d WHERE d.maDonViTinh = :maDVT";
            TypedQuery<DonViTinh> query = em.createQuery(jpql, DonViTinh.class);
            query.setParameter("maDVT", maDVT);
            return query.getSingleResult();  // It will return the first result or throw an exception if not found
        } catch (Exception e) {
            return null;  // Handle the case where no result is found
        } finally {
            em.close();
        }
    }

    /**
     *<B>Note:</B> Lấy đơn vị tính bằng tên đơn vị tính
     *@return donViTinh
     *
     */
    public DonViTinh getDonViTinhBangTenDonViTinh(String tenDVT) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT d FROM DonViTinh d WHERE d.tenDonViTinh = :tenDVT";
            TypedQuery<DonViTinh> query = em.createQuery(jpql, DonViTinh.class);
            query.setParameter("tenDVT", tenDVT);
            return query.getSingleResult();  // It will return the first result or throw an exception if not found
        } catch (Exception e) {
            return null;  // Handle the case where no result is found
        } finally {
            em.close();
        }
    }
}
