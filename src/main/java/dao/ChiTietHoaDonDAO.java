package dao;

import entity.ChiTietHoaDon;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class ChiTietHoaDonDAO {
    private EntityManager em;

    public List<ChiTietHoaDon> truyVanChiTietHoaDon(){
        String jpql = "SELECT cthd FROM ChiTietHoaDon cthd";
        return em.createQuery(jpql, ChiTietHoaDon.class)
                .getResultList();
    }

    public ChiTietHoaDon truyVanChiTietHoaDonTheoMa(String maHoaDon, String maSanPham){
        String jpql = "SELECT cthd FROM ChiTietHoaDon cthd " +
                "WHERE cthd.hoaDon.maHoaDon = :maHoaDon AND cthd.sanPham.maSanPham = :maSanPham";
        return em.createQuery(jpql, ChiTietHoaDon.class)
                .setParameter("maHoaDon", maHoaDon)
                .setParameter("maSanPham", maSanPham)
                .getSingleResult();
    }

    public boolean themChiTietHoaDon(ChiTietHoaDon chiTietHoaDon){
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.persist(chiTietHoaDon);
            tr.commit();
            return true;
        } catch (Exception ex){
            ex.printStackTrace();
            tr.rollback();
            return false;
        }
    }

    public boolean capNhatChiTietHoaDon(ChiTietHoaDon chiTietHoaDon){
        EntityTransaction tr = em.getTransaction();
        try{
            tr.begin();
            em.merge(chiTietHoaDon);
            tr.commit();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            tr.rollback();
            return false;
        }
    }

    public boolean xaoChiTietHoaDon(String maHoaDon, String maSanPham){
        EntityTransaction tr = em.getTransaction();
        try{
            tr.begin();
            ChiTietHoaDon chiTietHoaDon = truyVanChiTietHoaDonTheoMa(maHoaDon, maSanPham);
            if (chiTietHoaDon != null) {
                em.remove(chiTietHoaDon);
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
