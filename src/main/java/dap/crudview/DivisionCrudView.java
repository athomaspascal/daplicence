package dap.crudview;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import dap.entities.JPAService;
import dap.entities.enterprise.Division;
import dap.entities.enterprise.DivisionRepository;
import generic.crud.Crud;
import generic.crud.CrudListener;
import generic.crud.CrudOperation;
import generic.crud.impl.GridCrud;
import generic.crudform.impl.form.factory.GridLayoutCrudFormFactory;
import generic.layout.impl.HorizontalSplitCrudLayout;

import javax.persistence.EntityManager;
import java.util.Collection;

@Theme("mytheme")
public class DivisionCrudView extends VerticalLayout implements CrudListener<Division> {


    private TabSheet tabSheet = new TabSheet();
    private TeamCrudView t;


    public DivisionCrudView(TeamCrudView tt) {
        tabSheet.setSizeFull();
        addCrud(getConfiguredCrud(), "All Division");
        t = tt;
        addComponent(tabSheet);
    }

    private void addCrud(Crud crud, String caption) {
        VerticalLayout layout = new VerticalLayout(crud);
        layout.setSizeFull();
        layout.setMargin(true);
        tabSheet.addTab(layout, caption);
    }

    private Crud getDefaultCrud() {
        return new GridCrud<>(Division.class, this);
    }

  
    private Crud getConfiguredCrud() {
        GridCrud<Division> crud = new GridCrud<>(Division.class, new HorizontalSplitCrudLayout());
        crud.setCrudListener(this);

        GridLayoutCrudFormFactory<Division> formFactory = new GridLayoutCrudFormFactory<>(Division.class, 2, 2);
        crud.setCrudFormFactory(formFactory);

        formFactory.setUseBeanValidation(true);

        //formFactory.setErrorListener(e -> Notification.show("Custom error message (simulated error)", Notification.Type.ERROR_MESSAGE));

        formFactory.setVisibleProperties(CrudOperation.ADD, "nameDivision","description");
        formFactory.setVisibleProperties(CrudOperation.UPDATE, "nameDivision","description");
        formFactory.setVisibleProperties(CrudOperation.DELETE, "nameDivision","description");

        formFactory.setDisabledProperties("id");

        crud.getGrid().setColumns("nameDivision","description");

        formFactory.setButtonCaption(CrudOperation.ADD, "Add new division");
        crud.setRowCountCaption("%d division(s) found");

        crud.setClickRowToUpdate(true);
        crud.setUpdateOperationVisible(false);
        return crud;
    }

 
    @Override
    public Division add(Division division) {
        EntityManager em = JPAService.getFactory().createEntityManager();
        DivisionRepository.add(division,em);
        em.close();
        VerticalLayout v = (VerticalLayout) this.getParent();
        TeamCrudView newTeamCrudView = new TeamCrudView();
        v.replaceComponent(t,newTeamCrudView);
        t = newTeamCrudView;
        return division;
    }

    @Override
    public Division update(Division division) {
        return DivisionRepository.save(division);
    }

    @Override
    public void delete(Division division) {
        DivisionRepository.delete(division);
    }

    @Override
    public Collection<Division> findAll() {
        return DivisionRepository.findAll();
    }

}
