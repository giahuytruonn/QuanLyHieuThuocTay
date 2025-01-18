package dao;

import entity.HoaDon;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class HoaDonDAO {
    private EntityManager em;

    public List<HoaDon> truyVanHoaDon(){
        String jpql = "SELECT hd FROM HoaDon hd";
        return em.createQuery(jpql, HoaDon.class)
                .getResultList();
    }

    public HoaDon truyVanHoaDonTheoMa(String maHoaDon){
        return em.find(HoaDon.class, maHoaDon);
    }

    public boolean themHoaDon(HoaDon hoaDon){
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.persist(hoaDon);
            tr.commit();
            return true;
        } catch (Exception ex){
            ex.printStackTrace();
            tr.rollback();
            return false;
        }
    }

    public boolean capNhatHoaDon(HoaDon hoaDon){
        EntityTransaction tr = em.getTransaction();
        try{
            tr.begin();
            em.merge(hoaDon);
            tr.commit();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            tr.rollback();
            return false;
        }
    }

    public boolean xoaHoaDon(String maHoaDon){
        EntityTransaction tr = em.getTransaction();
        try{
            tr.begin();
            HoaDon hoaDon = em.find(HoaDon.class, maHoaDon);
            if (hoaDon != null) {
                em.remove(hoaDon);
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
