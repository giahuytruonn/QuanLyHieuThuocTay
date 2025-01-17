package dao;

import entity.LoaiSanPham;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class LoaiSanPhamDAO {
    private EntityManager em;

    public List<LoaiSanPham> truyVanLoaiSanPham(){
        String jpql = "SELECT lsp FROM LoaiSanPham lsp";
        return em.createQuery(jpql, LoaiSanPham.class)
                .getResultList();
    }

    public LoaiSanPham truyVanLoaiSanPhamTheoMa(String maLoaiSp){
        return em.find(LoaiSanPham.class, maLoaiSp);
    }

    public boolean themLoaiSanPham(LoaiSanPham loaiSanPham){
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.persist(loaiSanPham);
            tr.commit();
            return true;
        } catch (Exception ex){
            ex.printStackTrace();
            tr.rollback();
            return false;
        }
    }

    public boolean capNhatLoaiSanPham(LoaiSanPham loaiSanPham){
        EntityTransaction tr = em.getTransaction();
        try{
            tr.begin();
            em.merge(loaiSanPham);
            tr.commit();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            tr.rollback();
            return false;
        }
    }

    public boolean xaoLoaiSanPham(String maLoaiSp){
        EntityTransaction tr = em.getTransaction();
        try{
            tr.begin();
            LoaiSanPham loaiSanPham = em.find(LoaiSanPham.class, maLoaiSp);
            if (loaiSanPham != null) {
                em.remove(loaiSanPham);
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
