package qlhtt.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import qlhtt.ConnectDB.JPAUtil;
import qlhtt.Entity.DonViTinh;
import qlhtt.Entity.LoaiSanPham;
import qlhtt.Entity.NhomSanPham;
import qlhtt.Entity.SanPham;

import java.util.HashMap;
import java.util.List;

public class SanPhamDAO {
    private static final SanPhamDAO instance = new SanPhamDAO();

    public static SanPhamDAO getInstance() {
        return instance;
    }

    private EntityManager entityManager = JPAUtil.getEntityManager();

    // Get the list of all products
    public List<SanPham> getDanhSachSanPham() {
        String jpql = "SELECT sp FROM SanPham sp";
        return entityManager.createQuery(jpql, SanPham.class).getResultList();
    }

    // Get a specific product by its product code
    public SanPham getSanPhamBangMaSanPham(String maSP) {
        try {
            String jpql = "SELECT sp FROM SanPham sp WHERE sp.maSanPham = :maSP";
            TypedQuery<SanPham> query = entityManager.createQuery(jpql, SanPham.class);
            query.setParameter("maSP", maSP);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    // Get products by page number (pagination)
    public List<SanPham> laySanPhamTheoSoTrang(int soTrang) {
        String jpql = "SELECT sp FROM SanPham sp ORDER BY sp.maSanPham";
        TypedQuery<SanPham> query = entityManager.createQuery(jpql, SanPham.class);
        query.setFirstResult((soTrang - 1) * 10); // Pagination: skip the previous pages
        query.setMaxResults(10); // Limit the results to 10 per page
        return query.getResultList();
    }

    // Get the total number of products
    public Integer layTongSoSanPham() {
        String jpql = "SELECT COUNT(sp) FROM SanPham sp";
        Long count = (Long) entityManager.createQuery(jpql).getSingleResult();
        return count.intValue();
    }

    // Search products based on conditions in the filter map
    public List<SanPham> timKiemSanPham(HashMap<String, String> dieuKien) {
        StringBuilder jpql = new StringBuilder("SELECT sp FROM SanPham sp WHERE 1=1 ");
        for (String key : dieuKien.keySet()) {
            if (key.equals("giaBatDau")) {
                jpql.append("AND sp.donGia >= :giaBatDau ");
            } else if (key.equals("giaKetThuc")) {
                jpql.append("AND sp.donGia <= :giaKetThuc ");
            } else if (key.equals("maLoaiSanPham")) {
                jpql.append("AND sp.loaiSanPham.maLoaiSanPham = :maLoaiSanPham ");
            } else if (key.equals("maNhomSanPham")) {
                jpql.append("AND sp.nhomSanPham.maNhomSanPham = :maNhomSanPham ");
            } else if (key.equals("maDonViTinh")) {
                jpql.append("AND sp.donViTinh.maDonViTinh = :maDonViTinh ");
            } else {
                jpql.append("AND sp.").append(key).append(" = :").append(key).append(" ");
            }
        }

        TypedQuery<SanPham> query = entityManager.createQuery(jpql.toString(), SanPham.class);

        for (String key : dieuKien.keySet()) {
            if (key.equals("giaBatDau") || key.equals("giaKetThuc")) {
                query.setParameter(key, Double.parseDouble(dieuKien.get(key)));
            } else {
                query.setParameter(key, dieuKien.get(key));
            }
        }

        return query.getResultList();
    }


    // Insert a new product
    public Boolean themSanPham(SanPham sanPham) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            if (!transaction.isActive()) {
                transaction.begin();
            }
            entityManager.persist(sanPham);  // Sử dụng merge thay vì persist
            transaction.commit();  // Commit sau khi thêm sản phẩm
            return true;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();  // Nếu có lỗi, rollback
            }
            e.printStackTrace();
            return false;
        }
    }



    // Update product details
    public boolean capNhatSanPham(SanPham sanPham) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(sanPham);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update product price
    public Boolean capNhatGiaTienCuaSanPham(String maSanPham, double giaTienCuaSanPham) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            String jpql = "UPDATE SanPham sp SET sp.donGia = :giaTienCuaSanPham WHERE sp.maSanPham = :maSanPham";
            Query query = entityManager.createQuery(jpql);
            query.setParameter("giaTienCuaSanPham", giaTienCuaSanPham);
            query.setParameter("maSanPham", maSanPham);
            int rowsUpdated = query.executeUpdate();
            transaction.commit();
            return rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update the product stock quantity
    public void capNhatSoLuongSanPham(String maSanPham, int soLuong) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin(); // Bắt đầu giao dịch
            String jpql = "UPDATE SanPham sp SET sp.soLuong = sp.soLuong + :soLuong WHERE sp.maSanPham = :maSanPham";
            Query query = entityManager.createQuery(jpql);
            query.setParameter("soLuong", soLuong);
            query.setParameter("maSanPham", maSanPham);
            query.executeUpdate();
            tx.commit(); // Commit giao dịch
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback(); // Rollback nếu có lỗi
            }
            e.printStackTrace();
        }
    }

    // Search products by product name (using LIKE)
    public List<SanPham> getDanhSachSanPhamTheoTenSanPham(String text) {
        String jpql = "SELECT sp FROM SanPham sp WHERE sp.tenSanPham LIKE :text";
        TypedQuery<SanPham> query = entityManager.createQuery(jpql, SanPham.class);
        query.setParameter("text", "%" + text + "%");
        return query.getResultList();
    }

    public List<SanPham> timKiemSanPhamTheoMa(String maSP) {
        try {
            String jpql = "SELECT sp FROM SanPham sp WHERE sp.maSanPham = :maSP";
            TypedQuery<SanPham> query = entityManager.createQuery(jpql, SanPham.class);
            query.setParameter("maSP", maSP);
            return query.getResultList(); // Trả về list, ngay cả khi có 1 hoặc 0 kết quả
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
