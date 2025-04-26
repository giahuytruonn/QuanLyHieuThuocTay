package qlhtt.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import qlhtt.ConnectDB.JPAUtil;
import qlhtt.Entity.LoaiSanPham;

import java.util.List;

public class LoaiSanPhamDAO {
    private static LoaiSanPhamDAO instance = new LoaiSanPhamDAO();
    private EntityManager entityManager = JPAUtil.getEntityManager();

    public static LoaiSanPhamDAO getInstance() {
        return instance;
    }

    /**
     * <B>Note:</B> Lấy danh sách loại sản phẩm
     * @return danhSachLSP
     */
    public List<LoaiSanPham> getDanhSachLoaiSanPham() {
        String jpql = "SELECT l FROM LoaiSanPham l";
        return entityManager.createQuery(jpql, LoaiSanPham.class).getResultList();
    }

    /**
     * <B>Note:</B> Lấy loại sản phẩm bằng mã loại sản phẩm
     * @return loaiSanPham
     */
    public LoaiSanPham getLoaiSanPhamBangMaLoaiSanPham(String maLSP) {
        String jpql = "SELECT l FROM LoaiSanPham l WHERE l.maLoaiSanPham = :maLSP";
        try {
            return entityManager.createQuery(jpql, LoaiSanPham.class)
                    .setParameter("maLSP", maLSP)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * <B>Note:</B> Lấy loại sản phẩm bằng tên loại sản phẩm
     * @return loaiSanPham
     */
    public LoaiSanPham getLoaiSanPhamBangTenLoaiSanPham(String tenLSP) {
        String jpql = "SELECT l FROM LoaiSanPham l WHERE l.tenLoaiSanPham = :tenLSP";
        try {
            return entityManager.createQuery(jpql, LoaiSanPham.class)
                    .setParameter("tenLSP", tenLSP)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
