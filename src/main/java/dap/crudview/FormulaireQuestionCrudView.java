package dap.crudview;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.ui.ItemCaptionGenerator;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.TextRenderer;
import dap.entities.formulaire.FormulaireParameter;
import dap.entities.formulaire.FormulaireParameterRepository;
import dap.entities.formulaire.FormulaireQuestion;
import dap.entities.formulaire.FormulaireQuestionRepository;
import generic.crud.Crud;
import generic.crud.CrudListener;
import generic.crud.CrudOperation;
import generic.crud.impl.GridCrud;
import generic.crudform.impl.field.provider.ComboBoxProvider;
import generic.crudform.impl.field.provider.RadioButtonGroupProvider;
import generic.crudform.impl.form.factory.GridLayoutCrudCSSFormFactory;
import generic.layout.impl.HorizontalSplitCrudLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

@Theme("mytheme")
public class FormulaireQuestionCrudView extends VerticalLayout implements View, CrudListener<FormulaireQuestion> {

    int numFormulaire;
    private TabSheet tabSheet = new TabSheet();


    public FormulaireQuestionCrudView(int myFormulaire) {
        numFormulaire= myFormulaire;
        tabSheet.setSizeFull();
        addCrud(getConfiguredCrud(), "All FormulaireQuestion");

        addComponent(tabSheet);
    }

    private void addCrud(Crud crud, String caption) {
        VerticalLayout layout = new VerticalLayout(crud);
        layout.setSizeFull();
        layout.setMargin(true);
        tabSheet.addTab(layout, caption);
    }


    private Crud getConfiguredCrud() {
        List<String> flag = new ArrayList<String>();
        flag.add("0");
        flag.add("1");
        GridCrud<FormulaireQuestion> crud = new GridCrud<>(FormulaireQuestion.class, new HorizontalSplitCrudLayout());
        crud.setCrudListener(this);

        GridLayoutCrudCSSFormFactory<FormulaireQuestion> formFactory = new GridLayoutCrudCSSFormFactory<>(FormulaireQuestion.class, 2, 2);
        crud.setCrudFormFactory(formFactory);

        formFactory.setUseBeanValidation(true);

        //formFactory.setErrorListener(e -> Notification.show("Custom error message (simulated error)", Notification.Type.ERROR_MESSAGE));


        formFactory.setDisabledProperties("id");

        crud.getGrid().setColumns("questionOrder","codeParameter","libelleField","flagObligatoire","typeField","largeur");
        crud.getGrid().getColumn("questionOrder").setCaption("No");
        crud.getGrid().getColumn("codeParameter").setCaption("Parameter");
        crud.getGrid().getColumn("libelleField").setCaption("Question");
        crud.getGrid().getColumn("flagObligatoire").setCaption("Mandatory");
        crud.getGrid().getColumn("largeur").setCaption("Width");
        crud.getGrid().getColumn("flagObligatoire").setRenderer(value -> value == null ? "" : this.getFlagLibelle(value), new TextRenderer());
        crud.getGrid().getColumn("typeField").setCaption("TYPE OF TEXT");
        formFactory.setVisibleProperties(CrudOperation.ADD, "questionOrder","codeParameter","libelleField","descriptionField","flagObligatoire","typeField","largeur");
        formFactory.setVisibleProperties(CrudOperation.UPDATE, "questionOrder","codeParameter","libelleField","descriptionField","flagObligatoire","typeField","largeur");
        formFactory.setVisibleProperties(CrudOperation.DELETE, "questionOrder","codeParameter","libelleField","descriptionField","flagObligatoire","typeField","largeur");
        RadioButtonGroupProvider<String> rg = new RadioButtonGroupProvider<String>("Mandatory",flag,getIg());

        crud.getCrudFormFactory().setFieldProvider("flagObligatoire",rg);
        crud.getCrudFormFactory().setFieldProvider("codeParameter",
                new ComboBoxProvider<>("Codes Parametre",
                        FormulaireParameterRepository.findAll(), FormulaireParameter::getLibelle));
                formFactory.setButtonCaption(CrudOperation.ADD, "Add new Question");
        List<String> listTypes = new ArrayList<String>();
        listTypes.add(" ");
        listTypes.add("TEXT");
        listTypes.add("TEXTAREA");

        crud.getCrudFormFactory().setFieldProvider("typeField",
                new RadioButtonGroupProvider<>("Field Text Type",
                        listTypes));
        formFactory.setButtonCaption(CrudOperation.ADD, "Add new Question");
        formFactory.setFieldCaptions("Order No","Parameter", "Question","Description","Mandatory","Type of Text","Width");
        //Hashtable<String><Integer> listWidth = new Hashtable<String><Integer>();


        Hashtable<String, Integer> listWidth = new Hashtable<>();
        listWidth.put("libelleField",300);
        listWidth.put("largeur",50);
        listWidth.put("descriptionField",300);
        listWidth.put("questionOrder",50);

        formFactory.setFieldWidth(CrudOperation.UPDATE, listWidth);
        formFactory.setFieldType("descriptionField", TextArea.class);
        crud.setRowCountCaption("%d Question(s) found");

        crud.setClickRowToUpdate(true);
        crud.setUpdateOperationVisible(false);
        return crud;
    }

    private String getFlagLibelle(Object value) {
        int flag = (int) Integer.parseInt(value.toString());
        if (flag == 0)
            return "";
        else
            return "Mandatory";
    }



 
    @Override
    public FormulaireQuestion add(FormulaireQuestion formulaireQuestion) {
        formulaireQuestion.setIdFormulaire(numFormulaire);
        FormulaireQuestionRepository.add(formulaireQuestion);
      
        return formulaireQuestion;
    }

    @Override
    public FormulaireQuestion update(FormulaireQuestion formulaireQuestion) {
        return FormulaireQuestionRepository.save(formulaireQuestion);
    }

    @Override
    public void delete(FormulaireQuestion formulaireQuestion) {
        FormulaireQuestionRepository.delete(formulaireQuestion);
    }

    @Override
    public Collection<FormulaireQuestion> findAll() {
        return FormulaireQuestionRepository.findAll(numFormulaire);
    }

    @Override
    public FormulaireQuestion displaySlaveCrud(FormulaireQuestion formulaireQuestion)
    {
        return null;

    }

    public ItemCaptionGenerator<String> getIg() {
        ItemCaptionGenerator<String> ig = new ItemCaptionGenerator<String>() {
            @Override
            public String apply(String i) {
                if (Integer.parseInt(i) == 0)
                    return "NO";
                if (Integer.parseInt(i) == 1)
                    return "YES";
                return "";
            }
        };
        return ig;
    }

}
