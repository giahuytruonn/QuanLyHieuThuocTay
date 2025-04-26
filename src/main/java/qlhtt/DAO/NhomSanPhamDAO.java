package qlhtt.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import qlhtt.ConnectDB.JPAUtil;
import qlhtt.Entity.NhomSanPham;

import java.util.List;

public class NhomSanPhamDAO {
    private static NhomSanPhamDAO instance = new NhomSanPhamDAO();

    public static NhomSanPhamDAO getInstance() {
        return instance;
    }

    private EntityManager entityManager = JPAUtil.getEntityManager();

    /**
     * <B>Note:</B> Lấy danh sách nhóm sản phẩm
     * @return danhSachNSP
     */
    public List<NhomSanPham> getDanhSachNhomSanPham() {
        String jpql = "SELECT n FROM NhomSanPham n";
        return entityManager.createQuery(jpql, NhomSanPham.class).getResultList();
    }

    /**
     * <B>Note:</B> Lấy nhóm sản phẩm bằng mã nhóm sản phẩm
     * @return nhomSanPham
     */
    public NhomSanPham getNhomSanPhamBangMaNhomSanPham(String maNSP) {
        String jpql = "SELECT n FROM NhomSanPham n WHERE n.maNhomSp = :maNSP";
        try {
            return entityManager.createQuery(jpql, NhomSanPham.class)
                    .setParameter("maNSP", maNSP)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * <B>Note:</B> Lấy nhóm sản phẩm bằng tên nhóm sản phẩm
     * @return nhomSanPham
     */
    public NhomSanPham getNhomSanPhamBangTenNhomSanPham(String tenNSP) {
        String jpql = "SELECT n FROM NhomSanPham n WHERE n.tenNhomSanPham = :tenNSP";
        try {
            return entityManager.createQuery(jpql, NhomSanPham.class)
                    .setParameter("tenNSP", tenNSP)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
