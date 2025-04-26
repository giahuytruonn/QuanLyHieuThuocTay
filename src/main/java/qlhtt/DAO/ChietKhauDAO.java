package qlhtt.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import qlhtt.ConnectDB.JPAUtil;
import qlhtt.Entity.ChietKhau;

import java.time.LocalDate;
import java.util.List;

public class ChietKhauDAO {
    private static ChietKhauDAO instance = new ChietKhauDAO();
    private EntityManager em;

    public ChietKhauDAO() {
        em = JPAUtil.getEntityManager();
    }

    public static ChietKhauDAO getInstance() {
        return instance;
    }

    public List<ChietKhau> getDanhSachChietKhau() {
        TypedQuery<ChietKhau> query = em.createQuery("SELECT c FROM ChietKhau c", ChietKhau.class);
        return query.getResultList();
    }

    public boolean capNhatSoLuongChietKhau(ChietKhau chietKhau) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            ChietKhau ck = em.find(ChietKhau.class, chietKhau.getMaChietKhau());
            if (ck != null && ck.getSoLuong() > 0) {
                ck.setSoLuong(ck.getSoLuong() - 1);
            }
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
            return false;
        }
    }

    public ChietKhau getChietKhauBangMaChietKhau(String maCK) {
        return em.find(ChietKhau.class, maCK);
    }

    public List<ChietKhau> layTatCaChietKhau() {
        LocalDate ngayHienTai = LocalDate.now();
        TypedQuery<ChietKhau> query = em.createQuery("SELECT c FROM ChietKhau c", ChietKhau.class);
        List<ChietKhau> danhSach = query.getResultList();
        for (ChietKhau c : danhSach) {
            if (c.getNgayKetThucApDung() != null && c.getNgayKetThucApDung().isBefore(ngayHienTai)) {
                c.setTrangThaiChietKhau(false);
            }
        }
        return danhSach;
    }

    public List<ChietKhau> timKiemTheoMa(String maChietKhau) {
        TypedQuery<ChietKhau> query = em.createQuery(
                "SELECT c FROM ChietKhau c WHERE c.maChietKhau = :ma", ChietKhau.class);
        query.setParameter("ma", maChietKhau);
        return query.getResultList();
    }

    public void updateTrangThaiChietKhau(String moTa, boolean trangThai) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            TypedQuery<ChietKhau> query = em.createQuery(
                    "SELECT c FROM ChietKhau c WHERE c.moTa = :moTa", ChietKhau.class);
            query.setParameter("moTa", moTa);
            List<ChietKhau> list = query.getResultList();
            for (ChietKhau c : list) {
                c.setTrangThaiChietKhau(trangThai);
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }
    }

    public boolean themChietKhau(ChietKhau chietKhau) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            if (chietKhau.getNgayKetThucApDung() != null &&
                    chietKhau.getNgayKetThucApDung().isBefore(LocalDate.now())) {
                chietKhau.setTrangThaiChietKhau(false);
            } else {
                chietKhau.setTrangThaiChietKhau(true);
            }
            em.persist(chietKhau);
            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
            return false;
        }
    }
}
