import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class Generate {
    public static void main(String[] args) {
        EntityManager em = Persistence.createEntityManagerFactory("default")
                .createEntityManager();
        EntityTransaction tr = em.getTransaction();

        try{
            tr.begin();
            tr.commit();
        }catch (Exception ex){
            ex.printStackTrace();
            tr.rollback();
        }

        em.close();
    }
}
