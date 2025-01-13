import dao.NhanVienDao;
import entity.NhanVien;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

public class TestCRUD {
    public static void main(String[] args) {
        EntityManager em = Persistence.createEntityManagerFactory("default")
                .createEntityManager();
        NhanVienDao nhanVienDao = new NhanVienDao(em);
        if (nhanVienDao.themNhanVien(new NhanVien("NV01", "Nguyen Van A", "123456"))) {
            System.out.println("Them thanh cong");
        }else{
            System.out.println("Them that bai");
        }
    }
}
