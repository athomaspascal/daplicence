package dap.vaadin.crudui.entities.enterprise;

import dap.vaadin.crudui.app.JPAService;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * @author
 */
public class DivisionRepository {

    public static List<Division> findAll() {
        return JPAService.runInTransaction(em ->
                em.createQuery("select p from Division p").getResultList()
        );
    }

    public static Division save(Division division) {
        return JPAService.runInTransaction(em -> em.merge(division));
    }
    public static void delete(Division division) {
        JPAService.runInTransaction(em -> {
            em.remove(getById((long) division.getId(), em));
            return null;
        });
    }

    private static Division getById(Long id, EntityManager em) {
        Query query = em.createQuery("select u from Division u where u.id=:id");
        query.setParameter("id", id);

        return (Division) query.getResultList().stream().findFirst().orElse(null);
    }
}
