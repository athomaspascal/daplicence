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
public class LicenceView extends VerticalLayout implements View, CrudListener<User> {


    private TabSheet tabSheet = new TabSheet();



    public LicenceView() {
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
        return new GridCrud<>(User.class, this);
    }

    private Crud getDefaultCrudWithFixes() {
        GridCrud<User> crud = new GridCrud<>(User.class);
        crud.setCrudListener(this);
        crud.getCrudFormFactory().setFieldProvider("products", new CheckBoxGroupProvider<>(ProductRepository.findAll()));
        crud.getCrudFormFactory().setFieldProvider("mainProduct", new ComboBoxProvider<>(ProductRepository.findAll()));
        crud.getCrudFormFactory().setFieldProvider("teamid", new ComboBoxProvider<>(TeamRepository.findAll()));

        return crud;
    }

    private Crud getConfiguredCrud() {
        GridCrud<User> crud = new GridCrud<>(User.class, new HorizontalSplitCrudLayout());
        crud.setCrudListener(this);

        GridLayoutCrudFormFactory<User> formFactory = new GridLayoutCrudFormFactory<>(User.class, 2, 2);
        crud.setCrudFormFactory(formFactory);

        formFactory.setUseBeanValidation(true);

        //formFactory.setErrorListener(e -> Notification.show("Custom error message (simulated error)", Notification.Type.ERROR_MESSAGE));

        formFactory.setVisibleProperties(CrudOperation.ADD, "nom", "dateCreation", "email", "matricule","teamid", "products", "password", "mainProduct", "active");
        formFactory.setVisibleProperties(CrudOperation.UPDATE, "id", "nom", "dateCreation", "gender","email", "matricule", "teamid","password", "products", "active", "mainProduct");
        formFactory.setVisibleProperties(CrudOperation.DELETE, "nom", "email", "matricule");

        formFactory.setDisabledProperties("id");

        crud.getGrid().setColumns("nom", "dateCreation", "email", "matricule", "teamid", "mainProduct", "active");
        crud.getGrid().getColumn("mainProduct").setRenderer(group -> group == null ? "" : ((Product) group).getName(), new TextRenderer());
        crud.getGrid().getColumn("teamid").setRenderer(group -> group == null ? "" : ((Team) group).getNomteam(), new TextRenderer());
        ((Grid.Column<User, Date>) crud.getGrid().getColumn("dateCreation")).setRenderer(new DateRenderer("%1$tY-%1$tm-%1$te"));

        formFactory.setFieldType("password", PasswordField.class);
        formFactory.setFieldCreationListener("dateCreation", field -> ((DateField) field).setDateFormat("yyyy-MM-dd"));

        formFactory.setFieldProvider("products", new CheckBoxGroupProvider<>("Produit", ProductRepository.findAll(), Product::getName));
        formFactory.setFieldProvider("mainProduct", new ComboBoxProvider<>("Produit Principal", ProductRepository.findAll(), Product::getName));
        formFactory.setFieldProvider("teamid", new ComboBoxProvider<>("Team", TeamRepository.findAll(), Team::getNomteam));

        formFactory.setButtonCaption(CrudOperation.ADD, "Add new user");
        crud.setRowCountCaption("%d user(s) found");

        crud.setClickRowToUpdate(true);
        crud.setUpdateOperationVisible(false);

        return crud;
    }

    private Crud getEditableGridCrud() {
        EditableGridCrud<User> crud = new EditableGridCrud<>(User.class, this);

        crud.getGrid().setColumns("nom", "dateCreation", "email", "matricule", "password", "products", "teamid","mainProduct", "active");
        crud.getCrudFormFactory().setVisibleProperties("nom", "dateCreation", "email", "matricule", "password", "products", "teamid","mainProduct", "active");

        crud.getGrid().getColumn("password").setRenderer(user -> "********", new TextRenderer());
        crud.getGrid().getColumn("mainProduct").setRenderer(group -> group == null ? "" : ((Product) group).getName(), new TextRenderer());
        crud.getGrid().getColumn("teamid").setRenderer(group -> group == null ? "" : ((Team) group).getNomteam(), new TextRenderer());
        crud.getGrid().getColumn("products").setRenderer(products -> StringUtils.join(((Set<Product>) products).stream().map(g -> g.getName()).collect(Collectors.toList()), ", "), new TextRenderer());

        crud.getCrudFormFactory().setFieldType("password", PasswordField.class);
        crud.getCrudFormFactory().setFieldProvider("products", new CheckBoxGroupProvider<>(null, ProductRepository.findAll(), group -> group.getName()));
        crud.getCrudFormFactory().setFieldProvider("mainProduct", new ComboBoxProvider<>(null, ProductRepository.findAll(), group -> group.getName()));
        crud.getCrudFormFactory().setFieldProvider("teamid", new ComboBoxProvider<>(null, TeamRepository.findAll(), group -> group.getNomteam()));


        crud.getCrudFormFactory().setUseBeanValidation(true);

        return crud;
    }

    @Override
    public User add(User user) {
        UserRepository.save(user);
        return user;
    }

    @Override
    public User update(User user) {
        /*
        if (user.getId().equals(5l)) {
            throw new RuntimeException("A simulated error has occurred");
        }
        */
        return UserRepository.save(user);
    }

    @Override
    public void delete(User user) {
        UserRepository.delete(user);
    }

    @Override
    public Collection<User> findAll() {
        return UserRepository.findAll();
    }

}
