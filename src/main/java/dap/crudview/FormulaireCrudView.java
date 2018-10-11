package dap.crudview;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import dap.entities.formulaire.Formulaire;
import dap.entities.formulaire.FormulaireRepository;
import generic.crud.Crud;
import generic.crud.CrudListener;
import generic.crud.CrudOperation;
import generic.crud.impl.GridCrud;
import generic.crudform.impl.form.factory.GridLayoutCrudFormFactory;
import generic.layout.impl.HorizontalSplitCrudLayout;

import java.util.Collection;

@Theme("mytheme")
public class FormulaireCrudView extends VerticalLayout implements View, CrudListener<Formulaire> {


    private TabSheet tabSheet = new TabSheet();
    FormulaireQuestionCrudView q;
    View parent;

    public FormulaireCrudView(FormulaireQuestionCrudView fc, View theParent)
    {
        q= fc;
        parent = theParent;
        new FormulaireCrudView();
    }


    public FormulaireCrudView() {
        tabSheet.setSizeFull();
        addCrud(getConfiguredCrud(), "All Formulaire");

        addComponent(tabSheet);
    }

    private void addCrud(Crud crud, String caption) {
        VerticalLayout layout = new VerticalLayout(crud);
        layout.setSizeFull();
        layout.setMargin(true);
        tabSheet.addTab(layout, caption);
    }

    private Crud getDefaultCrud() {
        return new GridCrud<>(Formulaire.class, this);
    }

  
    private Crud getConfiguredCrud() {
        GridCrud<Formulaire> crud = new GridCrud<>(Formulaire.class, new HorizontalSplitCrudLayout());
        crud.setCrudListener(this);

        GridLayoutCrudFormFactory<Formulaire> formFactory = new GridLayoutCrudFormFactory<>(Formulaire.class, 2, 2);
        crud.setCrudFormFactory(formFactory);

        formFactory.setUseBeanValidation(true);

        //formFactory.setErrorListener(e -> Notification.show("Custom error message (simulated error)", Notification.Type.ERROR_MESSAGE));

        formFactory.setVisibleProperties(CrudOperation.ADD, "libelleFormulaire","dateCreation");
        formFactory.setVisibleProperties(CrudOperation.UPDATE, "libelleFormulaire","dateCreation");
        formFactory.setVisibleProperties(CrudOperation.DELETE, "libelleFormulaire","dateCreation");

        formFactory.setDisabledProperties("id");

        crud.getGrid().setColumns("libelleFormulaire","dateCreation");

        formFactory.setButtonCaption(CrudOperation.ADD, "Add new formulaire");
        crud.setRowCountCaption("%d formulaire(s) found");

        crud.setClickRowToUpdate(true);
        crud.setUpdateOperationVisible(false);
        return crud;
    }

 
    @Override
    public Formulaire add(Formulaire formulaire) {
        FormulaireRepository.add(formulaire);

        return formulaire;
    }

    @Override
    public Formulaire update(Formulaire formulaire) {
        return FormulaireRepository.save(formulaire);
    }

    @Override
    public void delete(Formulaire formulaire) {
        FormulaireRepository.delete(formulaire);
    }

    @Override
    public Collection<Formulaire> findAll() {
        return FormulaireRepository.findAll();
    }

    @Override
    public void displaySlaveCrud()
    {

    }
}
