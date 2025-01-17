package dao;

import entity.NhanVien;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class NhanVienDao {
    private EntityManager em;

    public boolean themNhanVien(NhanVien nhanVien) {
        EntityTransaction tr = em.getTransaction();
        try {
            tr.begin();
            em.persist(nhanVien);
            tr.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            tr.rollback();
        }
        return false;
    }
}
