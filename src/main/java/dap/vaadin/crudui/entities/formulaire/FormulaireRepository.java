package dap.vaadin.crudui.entities.formulaire;

import dap.vaadin.crudui.app.JPAService;


import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class FormulaireRepository {
    public Formulaire getFormulaire() {
        return formulaire;
    }

    public void setFormulaire(Formulaire formulaire) {
        this.formulaire = formulaire;
    }

    static Formulaire formulaire;

    public static List<Formulaire> findAll() {
        return JPAService.runInTransaction(em ->
                em.createQuery("select f from Formulaire f").getResultList()
        );
    }

    public static Formulaire save(Formulaire formulaire) {
        return JPAService.runInTransaction(em -> em.merge(formulaire));
    }

    public static Formulaire add(Formulaire myFormulaire) {
        JPAService.runInTransaction(em -> {
            em.persist(myFormulaire);
            FormulaireRepository.formulaire = myFormulaire;
            return null;
        });
        return FormulaireRepository.formulaire;
    }


    public static void delete(Formulaire myFormulaire) {
        JPAService.runInTransaction(em -> {
            em.remove(getById((long) myFormulaire.getId(), em));
            return null;
        });
    }

    private static Formulaire getById(Long id, EntityManager em) {
        Query query = em.createQuery("select u from Formulaire u where u.id=:id");
        query.setParameter("id", id);

        return (Formulaire) query.getResultList().stream().findFirst().orElse(null);
    }

    public static List<String> listAllFormulaire( EntityManager em) {
        Query query = em.createQuery("select distinct name from Formulaire u");
        List<String> listeDesFormulaire = query.getResultList();
        return listeDesFormulaire;
    }


}
