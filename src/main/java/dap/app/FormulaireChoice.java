package dap.app;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import dap.entities.formulaire.*;
import dap.entities.team.Team;
import dap.entities.team.TeamRepository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Theme("mytheme")
public class FormulaireChoice extends VerticalLayout implements View{

    public FormulaireChoice() {
        JPAService.init();

        Label titre = new Label("Formulaire");
        titre.setStyleName("titre");
        addComponent(titre);
        List<Team> listTeam = TeamRepository.findAll();
        List<String> dataTeam = new ArrayList<String>();
        listTeam.forEach(team->{
            dataTeam.add(team.getNomteam());
        });

        HorizontalLayout h1 = new HorizontalLayout();
        /*
        Label teamTitle = new Label("Liste des équipes");
        teamTitle.addStyleName("formulaire");
        */
        ComboBox teamComboBox = new ComboBox("Liste des équipes");
        teamComboBox.addStyleName("inline-label");
        teamComboBox.setTextInputAllowed(false);
        teamComboBox.setItems( dataTeam);
        teamComboBox.setEmptySelectionAllowed(false);

        h1.addComponents(teamComboBox);
        /* Label formulaireTitle = new Label("Liste des équipe");
        formulaireTitle.setStyleName("inline-label");
        */

        // LISTE DES FORMULAIRES
        List<Formulaire> listFormulaire = FormulaireRepository.findAll();
        List<String> data = new ArrayList<String>();

        listFormulaire.forEach(formulaire -> {
            data.add(formulaire.getLibelleFormulaire());
        });


        ComboBox formulaireComboBox = new ComboBox("Liste des formulaires");
        formulaireComboBox.setStyleName("inline-label");
        formulaireComboBox.setTextInputAllowed(false);
        formulaireComboBox.setItems( data);
        formulaireComboBox.setEmptySelectionAllowed(false);

        HorizontalLayout h2 = new HorizontalLayout();
        h2.addComponents(formulaireComboBox);

        addComponents(teamComboBox,formulaireComboBox);
        Label liste = new Label("Liste des questions");
        liste.setStyleName("question");

        addComponent(liste);
        //sample.select(data.get(0));
        formulaireComboBox.setSizeUndefined();
        formulaireComboBox.addSelectionListener(event -> {

            String formulaire = (String) event.getValue();
            buildFormulaire(formulaire,this);
        });


    }

    private void buildFormulaire(String formulaire, VerticalLayout monlayout) {
        //VerticalLayout monlayout= new VerticalLayout();
        List <FormulaireQuestion> listF = FormulaireQuestionRepository.findAllByFormulaire(formulaire);
        listF.forEach(formulaireQuestion ->{
            String codeParameter = formulaireQuestion.getCodeParameter();
            if ( codeParameter == null || codeParameter.equalsIgnoreCase("")) {
                if (formulaireQuestion.getTypeField() == null || formulaireQuestion.getTypeField().equalsIgnoreCase(""))
                {
                    TextField textField = new TextField(formulaireQuestion.getLibelleField());
                    textField.addStyleName("inline-label");
                    textField.addStyleName("formulaire");
                    if (formulaireQuestion.getLargeur() == null)
                    textField.setWidthUndefined();
                    else
                        textField.setWidth(formulaireQuestion.getLargeur());
                    monlayout.addComponent(textField);

                } else {
                    TextArea textArea = new TextArea(formulaireQuestion.getLibelleField());
                    textArea.addStyleName("inline-label");
                    if (formulaireQuestion.getLargeur() == null)
                        textArea.setWidthUndefined();
                    else
                        textArea.setWidth(formulaireQuestion.getLargeur());

                    monlayout.addComponent(textArea);

                }
            }
            else {
                JPAService jpa = new JPAService();
                jpa.init();
                EntityManager em = jpa.getFactory().createEntityManager();

                FormulaireParameter fp = FormulaireParameterRepository.getById(codeParameter,em);
                if (fp.getTypeRepresentation().equalsIgnoreCase("COMBOBOX"))
                {
                    List<FormulaireValue> list = FormulaireValueRepository.findAll(codeParameter,em);
                    ComboBox<String> comboBox = new ComboBox<>(fp.getLibelle());
                    comboBox.addStyleName("inline-label");

                    list.forEach(formulaireValue->{
                        comboBox.setItems(formulaireValue.getLibelleValue());

                    });
                    monlayout.addComponent(comboBox);
                }
                if (fp.getTypeRepresentation().equalsIgnoreCase("RADIOBOUTON"))
                {
                    List<FormulaireValue> list = FormulaireValueRepository.findAll(codeParameter,em);
                    RadioButtonGroup radioBouton = new RadioButtonGroup<>(formulaireQuestion.getLibelleField(), list);
                    radioBouton.addStyleName("inline-label");
                    radioBouton.setItemCaptionGenerator(item -> ((FormulaireValue) item).getLibelleValue());
                    monlayout.addComponent(radioBouton);

                }
                if (fp.getTypeRepresentation().equalsIgnoreCase("CHECKBOX"))
                {
                    List<FormulaireValue> list = FormulaireValueRepository.findAll(codeParameter,em);
                    CheckBoxGroup checkBoxGroup = new CheckBoxGroup<>(formulaireQuestion.getLibelleField(), list);
                    checkBoxGroup.addStyleName("inline-label");
                    checkBoxGroup.setItemCaptionGenerator(item -> ((FormulaireValue) item).getLibelleValue());
                    monlayout.addComponent(checkBoxGroup);
                }

            }
        });

        HorizontalLayout h = new HorizontalLayout();

        Button OK = new Button("Save");
        Button Cancel = new Button("Cancel");
        h.addComponents(OK,Cancel);
        monlayout.addComponents(h);
        OK.focus();

    }
}
