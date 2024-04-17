import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.List;

public class MainManyToOne {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("UserPU");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            // Begin transaction
            tx.begin();

            // Create a new user
            User user = new User("John Doe", LocalDate.of(1990, 5, 15));
            Address address1 = new Address("123 Main St", "City A");
            Address address2 = new Address("456 Elm St", "City B");

            // Associate addresses with the user
            address1.setUser(user);
            address2.setUser(user);

            // Persist user and addresses
            em.persist(user);
            em.persist(address1);
            em.persist(address2);

            // Commit transaction
            tx.commit();

            // Query all addresses associated with the user
            List<Address> addresses = em.createQuery("SELECT a FROM Address a WHERE a.user = :user", Address.class)
                    .setParameter("user", user)
                    .getResultList();

            System.out.println("Addresses associated with " + user.getName() + ":");
            for (Address address : addresses) {
                System.out.println(address.getStreet() + ", " + address.getCity());
            }

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}
