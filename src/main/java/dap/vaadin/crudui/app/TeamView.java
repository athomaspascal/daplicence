package dap.vaadin.crudui.app;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.TextRenderer;
import dap.vaadin.crudui.crud.Crud;
import dap.vaadin.crudui.crud.CrudListener;
import dap.vaadin.crudui.crud.CrudOperation;
import dap.vaadin.crudui.crud.impl.GridCrud;
import dap.vaadin.crudui.entities.enterprise.Division;
import dap.vaadin.crudui.entities.enterprise.DivisionRepository;
import dap.vaadin.crudui.entities.team.Team;
import dap.vaadin.crudui.entities.team.TeamRepository;
import dap.vaadin.crudui.form.impl.field.provider.ComboBoxProvider;
import dap.vaadin.crudui.form.impl.form.factory.GridLayoutCrudFormFactory;
import dap.vaadin.crudui.layout.impl.HorizontalSplitCrudLayout;

import java.util.Collection;

@Theme("mytheme")
public class TeamView extends VerticalLayout implements View, CrudListener<Team> {


    private TabSheet tabSheet = new TabSheet();



    public TeamView() {
        tabSheet.setSizeFull();

        addCrud(getConfiguredCrud(), "Configured");

        addComponent(tabSheet);


    }

    private void addCrud(Crud crud, String caption) {
        VerticalLayout layout = new VerticalLayout(crud);
        layout.setSizeFull();
        layout.setMargin(true);
        tabSheet.addTab(layout, caption);
    }

    private Crud getDefaultCrud() {
        return new GridCrud<>(Team.class, this);
    }

  
    private Crud getConfiguredCrud() {
        GridCrud<Team> crud = new GridCrud<>(Team.class, new HorizontalSplitCrudLayout());
        crud.setCrudListener(this);

        GridLayoutCrudFormFactory<Team> formFactory = new GridLayoutCrudFormFactory<>(Team.class, 2, 2);
        crud.setCrudFormFactory(formFactory);

        formFactory.setUseBeanValidation(true);

        //formFactory.setErrorListener(e -> Notification.show("Custom error message (simulated error)", Notification.Type.ERROR_MESSAGE));

        formFactory.setVisibleProperties(CrudOperation.ADD, "nomteam","userDivision");
        formFactory.setVisibleProperties(CrudOperation.UPDATE, "nomteam","userDivision");
        formFactory.setVisibleProperties(CrudOperation.DELETE, "nomteam","userDivision");

        formFactory.setDisabledProperties("id");

        crud.getGrid().setColumns("id","nomteam","userDivision");
        crud.getGrid().getColumn("userDivision").setRenderer(userDivision -> userDivision == null ? "" : ((Division) userDivision).getNameDivision(), new TextRenderer());
        formFactory.setFieldProvider("userDivision", new ComboBoxProvider<>("Enterprise Division", DivisionRepository.findAll(), Division::getNameDivision));
        formFactory.setButtonCaption(CrudOperation.ADD, "Add new team");
        crud.setRowCountCaption("%d team(s) found");

        crud.setClickRowToUpdate(true);
        crud.setUpdateOperationVisible(false);

        return crud;
    }

 
    @Override
    public Team add(Team team) {
        TeamRepository.save(team);
        return team;
    }

    @Override
    public Team update(Team team) {
        /*
        if (team.getId().equals(5l)) {
            throw new RuntimeException("A simulated error has occurred");
        }
        */
        return TeamRepository.save(team);
    }

    @Override
    public void delete(Team team) {
        TeamRepository.delete(team);
    }

    @Override
    public Collection<Team> findAll() {
        return TeamRepository.findAll();
    }

}
