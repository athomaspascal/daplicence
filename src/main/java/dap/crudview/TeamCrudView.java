package dap.crudview;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.TextRenderer;
import dap.entities.enterprise.Division;
import dap.entities.enterprise.DivisionRepository;
import dap.entities.team.Team;
import dap.entities.team.TeamRepository;
import generic.crud.Crud;
import generic.crud.CrudListener;
import generic.crud.CrudOperation;
import generic.crud.impl.GridCrud;
import generic.crudform.impl.field.provider.ComboBoxProvider;
import generic.crudform.impl.form.factory.GridLayoutCrudFormFactory;
import generic.layout.impl.HorizontalSplitCrudLayout;

import java.util.Collection;

@Theme("mytheme")
public class TeamCrudView extends VerticalLayout implements View, CrudListener<Team> {


    private TabSheet tabSheet = new TabSheet();



    public TeamCrudView() {
        /*
        tabSheet.setSizeFull();
        addCrud(getConfiguredCrud(), "All Teams");
        */
        addComponent(getConfiguredCrud());


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
        crud.getGrid().addStyleName("border-grid");

        GridLayoutCrudFormFactory<Team> formFactory = new GridLayoutCrudFormFactory<>(Team.class, 2, 2);
        crud.setCrudFormFactory(formFactory);

        formFactory.setUseBeanValidation(true);

        //formFactory.setErrorListener(e -> Notification.show("Custom error message (simulated error)", Notification.Type.ERROR_MESSAGE));

        formFactory.setVisibleProperties(CrudOperation.ADD, "nomteam","userDivision","teamBossName","teamBossEmail");
        formFactory.setVisibleProperties(CrudOperation.UPDATE, "nomteam","userDivision","teamBossName","teamBossEmail");
        formFactory.setVisibleProperties(CrudOperation.DELETE, "nomteam","userDivision","teamBossName","teamBossEmail");

        formFactory.setDisabledProperties("id");

        crud.getGrid().setColumns("nomteam","userDivision","teamBossName","teamBossEmail");
        crud.getGrid().getColumn("userDivision").setRenderer(userDivision -> userDivision == null ? "" : ((Division) userDivision).getNameDivision(), new TextRenderer());
        crud.getGrid().getColumn("userDivision").setCaption("Division");
        crud.getGrid().getColumn("nomteam").setCaption("Team Name");
        formFactory.setFieldProvider("userDivision", new ComboBoxProvider<>("Enterprise Division", DivisionRepository.findAll(), Division::getNameDivision));
        formFactory.setButtonCaption(CrudOperation.ADD, "Add new team");
        formFactory.setFieldCaptions("Team nam","Division","Boss Name","Boss EMAIL");
        crud.setRowCountCaption("%d team(s) found");

        crud.setClickRowToUpdate(true);
        crud.setUpdateOperationVisible(false);

        return crud;
    }

 
    @Override
    public Team add(Team team) {
        TeamRepository.add(team);
        return team;
    }

    @Override
    public Team update(Team team) {
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


    @Override
    public Team displaySlaveCrud(Team team)
    {
        return null;

    }
}
