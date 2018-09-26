package dap.vaadin.crudui.entities.formulaire;

import dap.vaadin.crudui.app.JPAService;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class FormulaireFieldRepository {
    public FormulaireField getFormulaireField() {
        return formulaireField;
    }

    public void setFormulaireField(FormulaireField formulaireField) {
        this.formulaireField = formulaireField;
    }

    static FormulaireField formulaireField;

    public static List<FormulaireField> findAll() {
        return JPAService.runInTransaction(em ->
                em.createQuery("select f from FormulaireField f").getResultList()
        );
    }

    public static List<FormulaireField> findAllByFormulaire(String formulaireName) {
        if (JPAService.getFactory() == null)
            JPAService.init();
        return JPAService.runInTransaction(em ->
                em.createQuery("select f from FormulaireField f,Formulaire fo" +
                        " where f.idFormulaire = fo.id" +
                        " and  fo.libelleFormulaire = '" + formulaireName + "'").getResultList()
        );
    }


    public static FormulaireField save(FormulaireField formulaireField) {
        return JPAService.runInTransaction(em -> em.merge(formulaireField));
    }

    public static FormulaireField add(FormulaireField myFormulaireField) {
        JPAService.runInTransaction(em -> {
            em.persist(myFormulaireField);
            FormulaireFieldRepository.formulaireField = myFormulaireField;
            return null;
        });
        return FormulaireFieldRepository.formulaireField;
    }


    public static void delete(FormulaireField myFormulaireField) {
        JPAService.runInTransaction(em -> {
            em.remove(getById((long) myFormulaireField.getId(), em));
            return null;
        });
    }

    private static FormulaireField getById(Long id, EntityManager em) {
        Query query = em.createQuery("select u from FormulaireField u where u.id=:id");
        query.setParameter("id", id);

        return (FormulaireField) query.getResultList().stream().findFirst().orElse(null);
    }

    public static List<String> listAllFormulaireField( EntityManager em) {
        Query query = em.createQuery("select distinct name from FormulaireField u");
        List<String> listeDesFormulaireField = query.getResultList();
        return listeDesFormulaireField;
    }


}
