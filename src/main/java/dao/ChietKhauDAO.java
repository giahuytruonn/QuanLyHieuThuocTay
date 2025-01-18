package dao;

import entity.ChietKhau;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class ChietKhauDAO {
    private EntityManager em;

    public List<ChietKhau> truyVanChietKhau(){
        String jpql = "SELECT ck FROM ChietKhau ck";
        return em.createQuery(jpql, ChietKhau.class)
                .getResultList();
    }

    public ChietKhau truyVanChietKhauTheoMa(String maChietKhau){
        String jpql = "SELECT ck FROM ChietKhau ck WHERE ck.maChietKhau = :maChietKhau";
        return em.createQuery(jpql, ChietKhau.class)
                .setParameter("maChietKhau", maChietKhau)
                .getSingleResult();
    }

    public boolean themChietKhau(ChietKhau chietKhau){
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.persist(chietKhau);
            tr.commit();
            return true;
        } catch (Exception ex){
            ex.printStackTrace();
            tr.rollback();
            return false;
        }
    }

    public boolean capNhatChietKhau(ChietKhau chietKhau){
        EntityTransaction tr = em.getTransaction();
        try{
            tr.begin();
            em.merge(chietKhau);
            tr.commit();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            tr.rollback();
            return false;
        }
    }

    public boolean xoaChietKhau(String maChietKhau){
        EntityTransaction tr = em.getTransaction();
        try{
            tr.begin();
            ChietKhau chietKhau = truyVanChietKhauTheoMa(maChietKhau);
            if (chietKhau != null) {
                em.remove(chietKhau);
                tr.commit();
                return true;
            }
            tr.commit();
            return false;
        }catch (Exception ex){
            ex.printStackTrace();
            tr.rollback();
            return false;
        }
    }
}
