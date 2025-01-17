package dao;

import entity.KhachHang;
import entity.SanPham;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class SanPhamDAO {
    private EntityManager em;

    public List<SanPham> truyVanSanPham() {
        String jpql = "SELECT sp FROM SanPham sp";
        return em.createQuery(jpql, SanPham.class)
                .getResultList();
    }

    public SanPham truyVanSanPhamTheoMa(String id){
        return em.find(SanPham.class, id);
    }

    public boolean themSanPham(SanPham sanPham){
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.persist(sanPham);
            tr.commit();
            return true;
        } catch (Exception ex){
            ex.printStackTrace();
            tr.rollback();
            return false;
        }
    }

    public boolean capNhatSanPham(SanPham sanPham){
        EntityTransaction tr = em.getTransaction();
        try{
            tr.begin();
            em.merge(sanPham);
            tr.commit();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            tr.rollback();
            return false;
        }
    }

    public boolean xoaSanPham(String id){
        EntityTransaction tr = em.getTransaction();
        try{
            tr.begin();
            SanPham cls = em.find(SanPham.class, id);
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
