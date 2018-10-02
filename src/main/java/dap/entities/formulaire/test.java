package dap.entities.formulaire;

import dap.app.JPAService;

import javax.persistence.EntityManager;

public class test {
    public static void main(String[] args) {
        JPAService.init();
        EntityManager em = JPAService.getFactory().createEntityManager();
        //User u = UserRepository.getById(1L, em);

        FormulaireValueRepository.getById(1,JPAService.getFactory().createEntityManager());

        JPAService.close();
    }
}
