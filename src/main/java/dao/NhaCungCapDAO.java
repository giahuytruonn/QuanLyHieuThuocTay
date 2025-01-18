package dao;

import entity.NhaCungCap;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class NhaCungCapDAO {
    private EntityManager em;

    public List<NhaCungCap> truyVanNhaCungCap(){
        String jpql = "SELECT ncc FROM NhaCungCap ncc";
        return em.createQuery(jpql, NhaCungCap.class)
                .getResultList();
    }

    public NhaCungCap truyVanNhaCungCapTheoMa(String maNhaCungCap){
        return em.find(NhaCungCap.class, maNhaCungCap);
    }

    public boolean themNhaCungCap(NhaCungCap nhaCungCap){
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.persist(nhaCungCap);
            tr.commit();
            return true;
        } catch (Exception ex){
            ex.printStackTrace();
            tr.rollback();
            return false;
        }
    }

    public boolean capNhatNhaCungCap(NhaCungCap nhaCungCap){
        EntityTransaction tr = em.getTransaction();
        try{
            tr.begin();
            em.merge(nhaCungCap);
            tr.commit();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            tr.rollback();
            return false;
        }
    }

    public boolean xoaNhaCungCap(String maNhaCungCap){
        EntityTransaction tr = em.getTransaction();
        try{
            tr.begin();
            NhaCungCap ncc = em.find(NhaCungCap.class, maNhaCungCap);
            if (ncc != null) {
                em.remove(ncc);
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
