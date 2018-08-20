package dap.vaadin.crudui.app;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.renderers.TextRenderer;
import dap.vaadin.crudui.crud.Crud;
import dap.vaadin.crudui.crud.CrudListener;
import dap.vaadin.crudui.crud.CrudOperation;
import dap.vaadin.crudui.crud.impl.EditableGridCrud;
import dap.vaadin.crudui.crud.impl.GridCrud;
import dap.vaadin.crudui.form.impl.field.provider.CheckBoxGroupProvider;
import dap.vaadin.crudui.form.impl.field.provider.ComboBoxProvider;
import dap.vaadin.crudui.form.impl.form.factory.GridLayoutCrudFormFactory;
import dap.vaadin.crudui.layout.impl.HorizontalSplitCrudLayout;
import org.apache.bval.util.StringUtils;

import java.sql.Date;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

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

        formFactory.setVisibleProperties(CrudOperation.ADD, "nomteam");
        formFactory.setVisibleProperties(CrudOperation.UPDATE, "nomteam");
        formFactory.setVisibleProperties(CrudOperation.DELETE, "nomteam");

        formFactory.setDisabledProperties("id");

        crud.getGrid().setColumns("id","nomteam");
        
        
        formFactory.setButtonCaption(CrudOperation.ADD, "Add new Team");
        crud.setRowCountCaption("%d Team(s) found");

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
