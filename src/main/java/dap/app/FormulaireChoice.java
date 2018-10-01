package dap.app;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import dap.entities.formulaire.Formulaire;
import dap.entities.formulaire.FormulaireField;
import dap.entities.formulaire.FormulaireFieldRepository;
import dap.entities.formulaire.FormulaireRepository;
import dap.entities.team.Team;
import dap.entities.team.TeamRepository;

import java.util.ArrayList;
import java.util.List;

@Theme("mytheme")
public class FormulaireChoice extends VerticalLayout implements View{

    public FormulaireChoice() {
        JPAService.init();
        VerticalLayout layout = new VerticalLayout();
        Label titre = new Label("Liste des formulaires");
        titre.setStyleName("titre");
        List<Team> listTeam = TeamRepository.findAll();
        List<String> dataTeam = new ArrayList<String>();
        listTeam.forEach(team->{
            dataTeam.add(team.getNomteam());
        });

        HorizontalLayout h1 = new HorizontalLayout();
        Label teamTitle = new Label("Liste des équipes");
        teamTitle.addStyleName("formulaire");
        ComboBox teamComboBox = new ComboBox();
        teamComboBox.setTextInputAllowed(false);
        teamComboBox.setItems( dataTeam);
        teamComboBox.setEmptySelectionAllowed(false);

        h1.addComponents(teamTitle,teamComboBox);
        Label formulaireTitle = new Label("Liste des équipes");
        formulaireTitle.setStyleName("v-label-formulaire");

        // LISTE DES FORMULAIRES
        List<Formulaire> listFormulaire = FormulaireRepository.findAll();
        List<String> data = new ArrayList<String>();

        listFormulaire.forEach(formulaire -> {
            data.add(formulaire.getLibelleFormulaire());
        });


        ComboBox formulaireComboBox = new ComboBox();
        formulaireComboBox.setTextInputAllowed(false);
        formulaireComboBox.setItems( data);
        formulaireComboBox.setEmptySelectionAllowed(false);

        HorizontalLayout h2 = new HorizontalLayout();
        h2.addComponents(formulaireTitle,formulaireComboBox);

        layout.addComponents(titre,
                h1,
                h2);

        //sample.select(data.get(0));
        formulaireComboBox.setSizeUndefined();
        formulaireComboBox.addSelectionListener(event -> {

            String formulaire = (String) event.getValue();
            VerticalLayout formulaireLayout = buildFormulaire(formulaire);
            layout.addComponent(formulaireLayout);
            HorizontalLayout h = new HorizontalLayout();

            Button OK = new Button("Save");
            Button Cancel = new Button("Cancel");
            h.addComponents(OK,Cancel);
            layout.addComponents(h);
            OK.focus();
        });



        addComponents(layout);

    }

    private VerticalLayout buildFormulaire(String formulaire) {
        VerticalLayout monlayout= new VerticalLayout();
        List <FormulaireField> listF = FormulaireFieldRepository.findAllByFormulaire(formulaire);
        listF.forEach(formulaireField->{
            if (formulaireField.getCodeParameter() == null || formulaireField.getCodeParameter().equalsIgnoreCase("")) {
                if (formulaireField.getTypeField() == null || formulaireField.getTypeField().equalsIgnoreCase(""))
                {
                    TextField textField = new TextField(formulaireField.getLibelleField());
                    textField.addStyleName("inline-label");
                    textField.addStyleName("formulaire");
                    textField.setWidthUndefined();
                    monlayout.addComponent(textField);

                } else {
                    TextArea textArea = new TextArea(formulaireField.getLibelleField());
                    textArea.addStyleName("inline-label");
                    textArea.setWidthUndefined();
                    monlayout.addComponent(textArea);

                }


            }
        });


        return monlayout;
    }
}
