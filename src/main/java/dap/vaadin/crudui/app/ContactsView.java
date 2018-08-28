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
import dap.vaadin.crudui.entities.contacts.Contacts;
import dap.vaadin.crudui.entities.contacts.ContactsRepository;
import dap.vaadin.crudui.entities.team.Team;
import dap.vaadin.crudui.entities.team.TeamRepository;
import dap.vaadin.crudui.crudform.impl.field.provider.ComboBoxProvider;
import dap.vaadin.crudui.crudform.impl.form.factory.GridLayoutCrudFormFactory;
import dap.vaadin.crudui.layout.impl.HorizontalSplitCrudLayout;

import java.util.Collection;

@Theme("mytheme")
public class ContactsView extends VerticalLayout implements View, CrudListener<Contacts> {


    private TabSheet tabSheet = new TabSheet();



    public ContactsView() {
        tabSheet.setSizeFull();
        addCrud(getConfiguredCrud(), "All Contacts");
        addComponent(tabSheet);


    }

    private void addCrud(Crud crud, String caption) {
        VerticalLayout layout = new VerticalLayout(crud);
        layout.setSizeFull();
        layout.setMargin(true);
        tabSheet.addTab(layout, caption);
    }

    private Crud getDefaultCrud() {
        return new GridCrud<>(Contacts.class, this);
    }

  
    private Crud getConfiguredCrud() {
        GridCrud<Contacts> crud = new GridCrud<>(Contacts.class, new HorizontalSplitCrudLayout());
        crud.setCrudListener(this);

        GridLayoutCrudFormFactory<Contacts> formFactory = new GridLayoutCrudFormFactory<>(Contacts.class, 2, 2);
        crud.setCrudFormFactory(formFactory);

        formFactory.setUseBeanValidation(true);

        //formFactory.setErrorListener(e -> Notification.show("Custom error message (simulated error)", Notification.Type.ERROR_MESSAGE));

        formFactory.setVisibleProperties(CrudOperation.ADD, "name","contactEmail","team" );
        formFactory.setVisibleProperties(CrudOperation.UPDATE, "name","contactEmail","team");
        formFactory.setVisibleProperties(CrudOperation.DELETE, "name","contactEmail");

        formFactory.setDisabledProperties("id");

        crud.getGrid().setColumns("name","contactEmail","team");
        crud.getGrid().getColumn("team").setRenderer(team -> team == null ? "" : ((Team) team).getNomteam(), new TextRenderer());

        formFactory.setButtonCaption(CrudOperation.ADD, "Add new contacts");
        formFactory.setFieldProvider("team", new ComboBoxProvider<>("team", TeamRepository.findAll(), Team::getNomteam));
        crud.setRowCountCaption("%d contacts(s) found");

        crud.setClickRowToUpdate(true);
        crud.setUpdateOperationVisible(false);

        return crud;
    }

 
    @Override
    public Contacts add(Contacts contacts) {
        ContactsRepository.save(contacts);

        return contacts;
    }

    @Override
    public Contacts update(Contacts contacts) {
        return ContactsRepository.save(contacts);
    }

    @Override
    public void delete(Contacts contacts) {
        ContactsRepository.delete(contacts);
    }

    @Override
    public Collection<Contacts> findAll() {
        return ContactsRepository.findAll();
    }

}