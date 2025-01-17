package dao;

import entity.PhieuNhap;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class PhieuNhapDAO {
    private EntityManager em;

    public List<PhieuNhap> truyVanPhieuNhap(){
        String jpql = "SELECT pn FROM PhieuNhap pn";
        return em.createQuery(jpql, PhieuNhap.class)
                .getResultList();
    }

    public PhieuNhap truyVanPhieuNhapTheoMa(String maPhieuNhap){
        return em.find(PhieuNhap.class, maPhieuNhap);
    }

    public boolean themPhieuNhap(PhieuNhap phieuNhap){
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.persist(phieuNhap);
            tr.commit();
            return true;
        } catch (Exception ex){
            ex.printStackTrace();
            tr.rollback();
            return false;
        }
    }

    public boolean capNhatPhieuNhap(PhieuNhap phieuNhap){
        EntityTransaction tr = em.getTransaction();
        try{
            tr.begin();
            em.merge(phieuNhap);
            tr.commit();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            tr.rollback();
            return false;
        }
    }

    public boolean xaoPhieuNhap(String maPhieuNhap){
        EntityTransaction tr = em.getTransaction();
        try{
            tr.begin();
            PhieuNhap pn = em.find(PhieuNhap.class, maPhieuNhap);
            if (pn != null) {
                em.remove(pn);
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
