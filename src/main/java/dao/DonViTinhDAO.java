package dao;

import entity.DonViTinh;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class DonViTinhDAO {
    private EntityManager em;

    public List<DonViTinh> truyVanDonViTinh(){
        String jpql = "SELECT dvt FROM DonViTinh dvt";
        return em.createQuery(jpql, DonViTinh.class)
                .getResultList();
    }

    public DonViTinh truyVanDonViTinhTheoMa(String maDonViTinh){
        return em.find(DonViTinh.class, maDonViTinh);
    }

    public boolean themDonViTinh(DonViTinh donViTinh){
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.persist(donViTinh);
            tr.commit();
            return true;
        } catch (Exception ex){
            ex.printStackTrace();
            tr.rollback();
            return false;
        }
    }

    public boolean capNhatDonViTinh(DonViTinh donViTinh){
        EntityTransaction tr = em.getTransaction();
        try{
            tr.begin();
            em.merge(donViTinh);
            tr.commit();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            tr.rollback();
            return false;
        }
    }

    public boolean xaoDonViTinh(String maDonViTinh){
        EntityTransaction tr = em.getTransaction();
        try{
            tr.begin();
            DonViTinh donViTinh = em.find(DonViTinh.class, maDonViTinh);
            if (donViTinh != null) {
                em.remove(donViTinh);
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
