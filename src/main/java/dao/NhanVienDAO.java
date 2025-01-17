package dao;

import entity.KhachHang;
import entity.NhanVien;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class NhanVienDAO {
    private EntityManager em;

    public List<NhanVien> truyVanNhanVien() {
        String jpql = "SELECT nv FROM NhanVien nv";
        return em.createQuery(jpql, NhanVien.class)
                .getResultList();
    }

    public NhanVien truyVanNhanVienTheoMa(String id){
        return em.find(NhanVien.class, id);
    }

    public boolean themNhanVien(NhanVien nhanVien){
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.persist(nhanVien);
            tr.commit();
            return true;
        } catch (Exception ex){
            ex.printStackTrace();
            tr.rollback();
            return false;
        }
    }

    public boolean capNhatNhanVien(NhanVien nhanVien){
        EntityTransaction tr = em.getTransaction();
        try{
            tr.begin();
            em.merge(nhanVien);
            tr.commit();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            tr.rollback();
            return false;
        }
    }

    public boolean xoaNhanVien(String id){
        EntityTransaction tr = em.getTransaction();
        try{
            tr.begin();
            NhanVien cls = em.find(NhanVien.class, id);
            em.remove(cls);
            tr.commit();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            tr.rollback();
            return false;
        }
    }
}
