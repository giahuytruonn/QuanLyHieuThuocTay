package dao;

import entity.KhachHang;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class KhachHangDAO {
    private EntityManager em;

    public List<KhachHang> truyVanKhachHang(){
        String query = "select kh " +
                "from KhachHang kh";
        return em.createQuery(query, KhachHang.class)
                .getResultList();
    }

    public KhachHang truyVanKhachHangTheoMa(String id){
        return em.find(KhachHang.class, id);
    }

    public boolean themKhachHang(KhachHang khachHang){
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.persist(khachHang);
            tr.commit();
            return true;
        } catch (Exception ex){
            ex.printStackTrace();
            tr.rollback();
            return false;
        }
    }

    public boolean capNhatKhachHang(KhachHang khachHang){
        EntityTransaction tr = em.getTransaction();
        try{
            tr.begin();
            em.merge(khachHang);
            tr.commit();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            tr.rollback();
            return false;
        }
    }

    public boolean xoaKhachHang(String id){
        EntityTransaction tr = em.getTransaction();
        try{
            tr.begin();
            KhachHang cls = em.find(KhachHang.class, id);
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
