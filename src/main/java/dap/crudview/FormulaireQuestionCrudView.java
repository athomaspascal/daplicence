package dap.crudview;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import dap.entities.formulaire.FormulaireQuestion;
import dap.entities.formulaire.FormulaireQuestionRepository;
import generic.crud.Crud;
import generic.crud.CrudListener;
import generic.crud.CrudOperation;
import generic.crud.impl.GridCrud;
import generic.crudform.impl.form.factory.GridLayoutCrudFormFactory;
import generic.layout.impl.HorizontalSplitCrudLayout;

import java.util.Collection;

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

    private Crud getDefaultCrud() {
        return new GridCrud<>(FormulaireQuestion.class, this);
    }

  
    private Crud getConfiguredCrud() {
        GridCrud<FormulaireQuestion> crud = new GridCrud<>(FormulaireQuestion.class, new HorizontalSplitCrudLayout());
        crud.setCrudListener(this);

        GridLayoutCrudFormFactory<FormulaireQuestion> formFactory = new GridLayoutCrudFormFactory<>(FormulaireQuestion.class, 2, 2);
        crud.setCrudFormFactory(formFactory);

        formFactory.setUseBeanValidation(true);

        //formFactory.setErrorListener(e -> Notification.show("Custom error message (simulated error)", Notification.Type.ERROR_MESSAGE));

        formFactory.setVisibleProperties(CrudOperation.ADD, "questionOrder","codeParameter","libelleField","descriptionField","flagObligatoire","typeField","largeur");
        formFactory.setVisibleProperties(CrudOperation.UPDATE, "questionOrder","codeParameter","libelleField","descriptionField","flagObligatoire","typeField","largeur");
        formFactory.setVisibleProperties(CrudOperation.DELETE, "questionOrder","codeParameter","libelleField","descriptionField","flagObligatoire","typeField","largeur");

        formFactory.setDisabledProperties("id");

        crud.getGrid().setColumns("questionOrder","codeParameter","libelleField","descriptionField","flagObligatoire","typeField","largeur");

        formFactory.setButtonCaption(CrudOperation.ADD, "Add new Question");
        crud.setRowCountCaption("%d Question(s) found");

        crud.setClickRowToUpdate(true);
        crud.setUpdateOperationVisible(false);
        return crud;
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

}
