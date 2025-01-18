package dao;

import entity.KhachHang;
import entity.TaiKhoan;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class TaiKhoanDAO {
    private EntityManager em;

    public List<TaiKhoan> truyVanTaiKhoan(){
        String jpql = "SELECT tk " +
                "FROM TaiKhoan tk";
        return em.createQuery(jpql, TaiKhoan.class)
                .getResultList();
    }

    public TaiKhoan truyVanTaiKhoanTheoMa(String id){
        return em.find(TaiKhoan.class, id);
    }

    public boolean themTaiKhoan(TaiKhoan taiKhoan){
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.persist(taiKhoan);
            tr.commit();
            return true;
        } catch (Exception ex){
            ex.printStackTrace();
            tr.rollback();
            return false;
        }
    }

    public boolean capNhatTaiKhoan(TaiKhoan taiKhoan){
        EntityTransaction tr = em.getTransaction();
        try{
            tr.begin();
            em.merge(taiKhoan);
            tr.commit();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            tr.rollback();
            return false;
        }
    }

    public boolean xoaTaiKhoan(String id){
        EntityTransaction tr = em.getTransaction();
        try{
            tr.begin();
            TaiKhoan cls = em.find(TaiKhoan.class, id);
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
