package dap.vaadin.crudui.app;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import dap.vaadin.crudui.entities.formulaire.Formulaire;
import dap.vaadin.crudui.entities.formulaire.FormulaireField;
import dap.vaadin.crudui.entities.formulaire.FormulaireFieldRepository;
import dap.vaadin.crudui.entities.formulaire.FormulaireRepository;

import java.util.ArrayList;
import java.util.List;

@Theme("mytheme")
public class FormulaireChoice extends VerticalLayout implements View{

    public FormulaireChoice() {
        JPAService.init();
        VerticalLayout layout = new VerticalLayout();
        List<Formulaire> listFormulaire = FormulaireRepository.findAll();
        List<String> data = new ArrayList<String>();

        listFormulaire.forEach(formulaire -> {
            data.add(formulaire.getLibelleFormulaire());
        });

        Label titre = new Label("Liste des formulaires");
        ComboBox sample = new ComboBox<>("Select an option", data);


        //sample.select(data.get(0));
        sample.setSizeUndefined();
        sample.addSelectionListener(event -> {

            String formulaire = (String) event.getValue();
            CssLayout formulaireLayout = buildFormulaire(formulaire);
            layout.addComponent(formulaireLayout);
            Button OK = new Button("Save");
            Button Cancel = new Button("Cancel");
            layout.addComponents(OK,Cancel);
            OK.focus();
        });

        layout.addComponents(titre,sample);

        addComponents(layout);

    }

    private CssLayout buildFormulaire(String formulaire) {
        CssLayout monlayout= new CssLayout();
        List <FormulaireField> listF = FormulaireFieldRepository.findAllByFormulaire(formulaire);
        listF.forEach(formulaireField->{
            if (formulaireField.getCodeParameter() == null || formulaireField.getCodeParameter().equalsIgnoreCase(""))
                monlayout.addComponent(new TextField(formulaireField.getLibelleField()));
        });


        return monlayout;
    }
}
