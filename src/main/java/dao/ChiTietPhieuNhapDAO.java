package dao;

import entity.ChiTietPhieuNhap;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class ChiTietPhieuNhapDAO {
    private EntityManager em;

    public List<ChiTietPhieuNhap> truyVanChiTietPhieuNhap(){
        String jpql = "SELECT ctpn FROM ChiTietPhieuNhap ctpn";
        return em.createQuery(jpql, ChiTietPhieuNhap.class)
                .getResultList();
    }

    public ChiTietPhieuNhap truyVanChiTietPhieuNhapTheoMa(String maPhieuNhap, String maSanPham){
        String jpql = "SELECT ctpn FROM ChiTietPhieuNhap ctpn WHERE ctpn.phieuNhap.maPhieuNhap = :maPhieuNhap AND ctpn.sanPham.maSanPham = :maSanPham";
        return em.createQuery(jpql, ChiTietPhieuNhap.class)
                .setParameter("maPhieuNhap", maPhieuNhap)
                .setParameter("maSanPham", maSanPham)
                .getSingleResult();
    }

    public boolean themChiTietPhieuNhap(ChiTietPhieuNhap chiTietPhieuNhap){
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.persist(chiTietPhieuNhap);
            tr.commit();
            return true;
        } catch (Exception ex){
            ex.printStackTrace();
            tr.rollback();
            return false;
        }
    }

    public boolean capNhatChiTietPhieuNhap(ChiTietPhieuNhap chiTietPhieuNhap){
        EntityTransaction tr = em.getTransaction();
        try{
            tr.begin();
            em.merge(chiTietPhieuNhap);
            tr.commit();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            tr.rollback();
            return false;
        }
    }

    public boolean xaoChiTietPhieuNhap(String maPhieuNhap, String maSanPham){
        EntityTransaction tr = em.getTransaction();
        try{
            tr.begin();
            ChiTietPhieuNhap chiTiet = truyVanChiTietPhieuNhapTheoMa(maPhieuNhap, maSanPham);
            if (chiTiet != null) {
                em.remove(chiTiet);
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
