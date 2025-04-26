package qlhtt.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import qlhtt.ConnectDB.JPAUtil;
import qlhtt.Entity.NhaCungCap;
import java.util.List;

public class NhaCungCapDAO {
    private static NhaCungCapDAO instance = new NhaCungCapDAO();
    private EntityManager em = JPAUtil.getEntityManager();

    public static NhaCungCapDAO getInstance() {
        return instance;
    }

    /**
     * @return dsNhaCungCap
     */
    public List<NhaCungCap> getDanhSachNhaCungCap() {
        String jpql = "SELECT n FROM NhaCungCap n";
        TypedQuery<NhaCungCap> query = em.createQuery(jpql, NhaCungCap.class);
        return query.getResultList();
    }

    /**
     * @return nhaCungCap
     */
    public NhaCungCap getNhaCungCapBangMaNhaCungCap(String maNCC) {
        String jpql = "SELECT n FROM NhaCungCap n WHERE n.maNhaCungCap = :maNCC";
        TypedQuery<NhaCungCap> query = em.createQuery(jpql, NhaCungCap.class);
        query.setParameter("maNCC", maNCC);
        return query.getResultStream().findFirst().orElse(null);
    }

    public boolean themNhaCungCap(NhaCungCap nhaCungCap) {
        try {
            em.getTransaction().begin();
            em.persist(nhaCungCap);
            em.getTransaction().commit();
            return true; // Return true if added successfully
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            return false; // Return false if an error occurred
        }
    }

    public NhaCungCap timKiemBangSDT(String sdt) {
        String jpql = "SELECT n FROM NhaCungCap n WHERE n.soDienThoai = :sdt";
        TypedQuery<NhaCungCap> query = em.createQuery(jpql, NhaCungCap.class);
        query.setParameter("sdt", sdt);
        return query.getResultStream().findFirst().orElse(null);
    }

    public List<NhaCungCap> layDanhSachNCC() {
        String jpql = "SELECT n FROM NhaCungCap n";
        TypedQuery<NhaCungCap> query = em.createQuery(jpql, NhaCungCap.class);
        return query.getResultList();
    }

    public List<NhaCungCap> timKiem(String sdt) {
        String jpql = "SELECT n FROM NhaCungCap n WHERE n.soDienThoai = :sdt";
        TypedQuery<NhaCungCap> query = em.createQuery(jpql, NhaCungCap.class);
        query.setParameter("sdt", sdt);
        return query.getResultList();
    }

    public void capNhat(NhaCungCap nhaCungCap) {
        try {
            em.getTransaction().begin();
            em.merge(nhaCungCap); // Merges changes to existing entity
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        }
    }

    public NhaCungCap getNhaCungCapBangTen(String ten) {
        String jpql = "SELECT n FROM NhaCungCap n WHERE n.tenNhaCungCap = :ten";
        TypedQuery<NhaCungCap> query = em.createQuery(jpql, NhaCungCap.class);
        query.setParameter("ten", ten);
        return query.getResultStream().findFirst().orElse(null);
    }
}
