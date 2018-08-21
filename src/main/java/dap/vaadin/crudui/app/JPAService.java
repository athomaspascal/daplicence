package dap.vaadin.crudui.app;

import dap.vaadin.crudui.user.User;
import dap.vaadin.crudui.user.UserRepository;
import dap.vaadin.crudui.product.Product;
import dap.vaadin.crudui.product.ProductRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author
 */
public class JPAService {

    private static EntityManagerFactory factory;

    public static void init() {
        if (factory == null) {
            factory = Persistence.createEntityManagerFactory("test-pu");
            createTestData();
        }
    }

    private static void createTestData() {

        if (ProductRepository.findAll().size()> 0) return ;
        Product product = new Product();
        product.setName("SAS BASE");
        product.setAdmin(false);
        ProductRepository.save(product);
        product = new Product();
        product.setName("SAS EG");
        product.setAdmin(false);
        ProductRepository.save(product);
        product = new Product();
        product.setName("SAS addin");
        product.setAdmin(false);
        ProductRepository.save(product);
        product = new Product();
        product.setName("SAS Web");
        product.setAdmin(false);
        ProductRepository.save(product);


        Set<Product> products = ProductRepository.findAll().stream()
                .filter(r -> r.getId() <= 2)
                .collect(Collectors.toSet());

        for (int i = 1; i <= 50; i++) {
            User user = new User();
            user.setNom("User " + i);
            user.setDateCreation(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            user.setEmail("email" + i + "@test.com");
            user.setMatricule(i * 101001);
            user.setPassword("password" + i);
            user.setActive(true);
            user.setProducts(products);
            user.setMainProduct(products.stream().findFirst().get());

            UserRepository.save(user);
        }
    }

    public static void close() {
        factory.close();
    }

    public static EntityManagerFactory getFactory() {
        return factory;
    }

    public static <T> T runInTransaction(Function<EntityManager, T> function) {
        EntityManager entityManager = null;

        try {
            if (JPAService.getFactory() == null) JPAService.init();
            entityManager = JPAService.getFactory().createEntityManager();
            entityManager.getTransaction().begin();

            T result = function.apply(entityManager);

            entityManager.getTransaction().commit();
            return result;

        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

}
