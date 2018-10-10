package dap.view;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import dap.entities.JPAService;
import dap.entities.formulaire.*;
import dap.entities.team.Team;
import dap.entities.team.TeamRepository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Theme("mytheme")
public class FormulaireChoice extends VerticalLayout implements View{

    List<TextField> listTextField = new ArrayList();
    List<TextArea> listTextArea = new ArrayList();
    List<ComboBox> listComboBox = new ArrayList();
    List<RadioButtonGroup> listRadioButtonGroup = new ArrayList();
    List<CheckBoxGroup> listCheckBoxGroup = new ArrayList();
    Team teamSelected;
    Formulaire formulaireSelected;
    List<String> typeQuestion = new ArrayList<String>();
    List<Integer> numQuestion = new ArrayList<Integer>();

    public FormulaireChoice() {
        JPAService.init();

        Label titre = new Label("Formulaire");
        titre.setStyleName("titre");
        addComponent(titre);
        List<Team> listTeam = TeamRepository.findAll();
        ComboBox<Team> teamComboBox = new ComboBox("Liste des équipes");
        teamComboBox.addStyleName("inline-label");
        teamComboBox.setTextInputAllowed(false);
        teamComboBox.setItems(listTeam);
        teamComboBox.setItemCaptionGenerator(Team::getNomteam);
        teamComboBox.setEmptySelectionAllowed(false);
        teamComboBox.addValueChangeListener(event -> {
            teamSelected = teamComboBox.getValue();
            Window window = new Window();
            VerticalLayout v = new VerticalLayout();
            List<FormulaireResultat> res= FormulaireResultatRepository.findAll(teamSelected.getId());
            FormulaireResultat r = new FormulaireResultat();

            if (res.size()> 0)
            {

                Label Titre = new Label("FORMULAIRES EN COURS POUR CETTE TEAM");
                Titre.setStyleName("titrewindow");
                FormLayout f = new FormLayout();

                ListSelect<FormulaireResultat> formulaireList = new ListSelect("Liste des formulaires");
                formulaireList.setStyleName("inline-label");
                formulaireList.setItems(res);
                formulaireList.setWidth("300");
                formulaireList.setItemCaptionGenerator(FormulaireResultat::libelleComplet);
                Button updateFormulaire = new Button("Update");
                Button newFormulaire = new Button("New FORM");
                HorizontalLayout h = new HorizontalLayout();

                h.addComponents(updateFormulaire,newFormulaire);
                f.addComponents(formulaireList,h);
                v.addComponents(Titre,f);
                window.setContent(v);
                window.center();
                window.setWidth("650");
                this.getParent().getUI().addWindow(window);
            }

        });

        // LISTE DES FORMULAIRES
        List<Formulaire> listFormulaire = FormulaireRepository.findAll();
        ComboBox<Formulaire> formulaireComboBox = new ComboBox("Liste des formulaires");
        formulaireComboBox.setStyleName("inline-label");
        formulaireComboBox.setTextInputAllowed(false);
        formulaireComboBox.setItems(listFormulaire);
        formulaireComboBox.setItemCaptionGenerator(Formulaire::getLibelleFormulaire);
        formulaireComboBox.setEmptySelectionAllowed(false);

        VerticalLayout formulaire = new VerticalLayout();
        formulaireComboBox.addValueChangeListener(event -> {
            formulaireSelected = formulaireComboBox.getValue();
            buildFormulaire(formulaireSelected.getLibelleFormulaire(),formulaire);
            addComponent(formulaire);
        });

        // On ajoute les 2 combobox
        addComponents(teamComboBox,formulaireComboBox);

        //On fabrique le formulaire
        Label liste = new Label("Liste des questions");
        liste.setStyleName("question");

        addComponent(liste);


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
                    listTextField.add(textField);
                    typeQuestion.add("TEXTFIELD");
                    numQuestion.add(formulaireQuestion.getId());
                    monlayout.addComponent(textField);
                } else {
                    TextArea textArea = new TextArea(formulaireQuestion.getLibelleField());
                    textArea.addStyleName("inline-label");
                    if (formulaireQuestion.getLargeur() == null)
                        textArea.setWidthUndefined();
                    else
                        textArea.setWidth(formulaireQuestion.getLargeur());
                    listTextArea.add(textArea);
                    typeQuestion.add("TEXTAREA");
                    numQuestion.add(formulaireQuestion.getId());
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
                    List<String> list = FormulaireValueRepository.listValues(codeParameter,em);
                    ComboBox<String> comboBox = new ComboBox<String>(fp.getLibelle());
                    comboBox.addStyleName("inline-label");

                    list.forEach(formulaireValue->{
                        comboBox.setItems(formulaireValue);
                    });
                    listComboBox.add(comboBox);
                    typeQuestion.add("COMBOBOX");
                    numQuestion.add(formulaireQuestion.getId());
                    monlayout.addComponent(comboBox);
                }
                if (fp.getTypeRepresentation().equalsIgnoreCase("RADIOBOUTON"))
                {
                    List<String> list = FormulaireValueRepository.listValues(codeParameter,em);
                    RadioButtonGroup<String> radioBouton = new RadioButtonGroup<String>(formulaireQuestion.getLibelleField(), list);
                    radioBouton.addStyleName("inline-label");
                    radioBouton.setItemCaptionGenerator(item -> item);
                    listRadioButtonGroup.add(radioBouton);
                    typeQuestion.add("RADIOBUTTON");
                    numQuestion.add(formulaireQuestion.getId());
                    monlayout.addComponent(radioBouton);

                }
                if (fp.getTypeRepresentation().equalsIgnoreCase("CHECKBOX"))
                {
                    List<String> list = FormulaireValueRepository.listValues(codeParameter,em);
                    CheckBoxGroup<String> checkBoxGroup = new CheckBoxGroup<String>(formulaireQuestion.getLibelleField(), list);
                    checkBoxGroup.addStyleName("inline-label");
                    checkBoxGroup.setItemCaptionGenerator(item -> item);
                    listCheckBoxGroup.add(checkBoxGroup);
                    typeQuestion.add("CHECKBOX");
                    numQuestion.add(formulaireQuestion.getId());
                    monlayout.addComponent(checkBoxGroup);
                }

            }
        });

        HorizontalLayout h = new HorizontalLayout();

        Button OK = new Button("Save",this::ok);

        Button Cancel = new Button("Cancel");
        h.addComponents(OK,Cancel);
        monlayout.addComponents(h);
        OK.focus();

    }

    public void ok(Button.ClickEvent e)
    {
        Window window = new Window("Ecriture du formulaire");
        VerticalLayout v = new VerticalLayout();
        Label l1 = new Label("Team:" + teamSelected.getNomteam());
        Label l2 = new Label("Formulaire:" + formulaireSelected.getLibelleFormulaire());
        v.addComponents(l1,l2);
        int nbTextField=0;
        int nbTextArea=0;
        int nbComboBox=0;
        int nbRadioButton=0;
        int nbCheckBox=0;
        int nbQuestion=0;
        FormulaireResultat FormRes = new FormulaireResultat(teamSelected.getId(), formulaireSelected.getId());

        FormulaireResultat resultat = FormulaireResultatRepository.add(FormRes);

        for(String reponse: typeQuestion) {
            FormulaireReponse fr = new FormulaireReponse();

            switch (reponse) {

                case "TEXTFIELD":
                    TextField t = listTextField.get(nbTextField);
                    Label ll1 = new Label(numQuestion.get(nbQuestion) + ":" + t.getValue());
                    v.addComponent(ll1);
                    fr = new FormulaireReponse(resultat.getId(),numQuestion.get(nbQuestion), t.getValue());
                    FormulaireReponseRepository.add(fr);
                    nbTextField++;
                    nbQuestion++;

                    break; // optional
                case "TEXTAREA":
                    TextArea t2 = listTextArea.get(nbTextArea);
                    Label ll2 = new Label(numQuestion.get(nbQuestion) + ":" + t2.getValue());
                    v.addComponent(ll2);
                    fr = new FormulaireReponse(resultat.getId(), numQuestion.get(nbQuestion), t2.getValue());
                    FormulaireReponseRepository.add(fr);
                    nbTextArea++;
                    nbQuestion++;

                    break; // optional
                // You can have any number of case statements.
                case "COMBOBOX":
                    ComboBox<String> c = listComboBox.get(nbComboBox);
                    Label ll3 = new Label(numQuestion.get(nbQuestion) + ":" + c.getValue());
                    v.addComponent(ll3);
                    fr = new FormulaireReponse(resultat.getId(), numQuestion.get(nbQuestion), c.getValue());
                    FormulaireReponseRepository.add(fr);
                    nbComboBox++;
                    nbQuestion++;

                    break; // optional
                case "CHECKBOX":
                    CheckBoxGroup<String> r = listCheckBoxGroup.get(nbRadioButton);
                    Label ll4 = new Label(numQuestion.get(nbQuestion) + ":" + r.getValue().toString());

                    v.addComponent(ll4);
                    fr = new FormulaireReponse(resultat.getId(), numQuestion.get(nbQuestion), r.getValue().toString());
                    FormulaireReponseRepository.add(fr);
                    nbRadioButton++;
                    nbQuestion++;
                    break; // optional
                case "RADIOBUTTON":
                    RadioButtonGroup<String> rb = listRadioButtonGroup.get(nbCheckBox);
                    Label ll5 = new Label(numQuestion.get(nbQuestion) + ":" + rb.getValue());
                    v.addComponent(ll5);
                    fr = new FormulaireReponse(resultat.getId(), numQuestion.get(nbQuestion), rb.getValue());
                    FormulaireReponseRepository.add(fr);
                    nbCheckBox++;
                    nbQuestion++;

                    break; // optional
                default: // Optional
                    // Statements

            }
        }

        window.setContent(v);

        this.getParent().getUI().addWindow(window);


    }
}
