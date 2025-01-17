import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

public class TestCRUD {
    public static void main(String[] args) {
        EntityManager em = Persistence.createEntityManagerFactory("default")
                .createEntityManager();


    }
}
