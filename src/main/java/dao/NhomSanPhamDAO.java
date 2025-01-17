package dao;

import entity.NhomSanPham;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class NhomSanPhamDAO {
    private EntityManager em;

    public List<NhomSanPham> truyVanNhomSanPham(){
        String jpql = "SELECT nsp FROM NhomSanPham nsp";
        return em.createQuery(jpql, NhomSanPham.class)
                .getResultList();
    }

    public NhomSanPham truyVanNhomSanPhamTheoMa(String maNhomSp){
        return em.find(NhomSanPham.class, maNhomSp);
    }

    public boolean themNhomSanPham(NhomSanPham nhomSanPham){
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.persist(nhomSanPham);
            tr.commit();
            return true;
        } catch (Exception ex){
            ex.printStackTrace();
            tr.rollback();
            return false;
        }
    }

    public boolean capNhatNhomSanPham(NhomSanPham nhomSanPham){
        EntityTransaction tr = em.getTransaction();
        try{
            tr.begin();
            em.merge(nhomSanPham);
            tr.commit();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            tr.rollback();
            return false;
        }
    }

    public boolean xaoNhomSanPham(String maNhomSp){
        EntityTransaction tr = em.getTransaction();
        try{
            tr.begin();
            NhomSanPham nhomSanPham = em.find(NhomSanPham.class, maNhomSp);
            if (nhomSanPham != null) {
                em.remove(nhomSanPham);
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
