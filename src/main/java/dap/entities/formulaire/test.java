package dap.entities.formulaire;

import dap.app.JPAService;

public class test {
    public static void main(String[] args) {
        JPAService.init();
        FormulaireFieldRepository.findAllByFormulaire("FORMULAIRE 1");

        JPAService.close();
    }
}
