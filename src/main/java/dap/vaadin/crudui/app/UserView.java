package dap.vaadin.crudui.app;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.renderers.TextRenderer;
import dap.vaadin.crudui.crud.CrudListenerWithFilter;
import dap.vaadin.crudui.crud.CrudOperation;
import dap.vaadin.crudui.crud.CrudWithFilter;
import dap.vaadin.crudui.crud.impl.GridCrudWithFilter;
import dap.vaadin.crudui.entities.product.Product;
import dap.vaadin.crudui.entities.product.ProductRepository;
import dap.vaadin.crudui.entities.team.Team;
import dap.vaadin.crudui.entities.team.TeamRepository;
import dap.vaadin.crudui.entities.user.User;
import dap.vaadin.crudui.entities.user.UserRepository;
import dap.vaadin.crudui.form.impl.field.provider.CheckBoxGroupProvider;
import dap.vaadin.crudui.form.impl.field.provider.ComboBoxProvider;
import dap.vaadin.crudui.form.impl.form.factory.GridLayoutCrudFormFactory;
import dap.vaadin.crudui.layout.impl.HorizontalSplitCrudLayout;

import javax.persistence.EntityManager;
import java.sql.Date;
import java.util.Collection;
import java.util.List;

@Theme("mytheme")
public class UserView extends VerticalLayout implements View, CrudListenerWithFilter<User> {


    private TabSheet tabSheet = new TabSheet();



    public UserView() {
        Label titre = new Label("Users");
        titre.setStyleName("titre");
        tabSheet.setSizeFull();
        if (JPAService.getFactory() == null) JPAService.init();
        EntityManager entityManager = JPAService.getFactory().createEntityManager();
        List<String> strings = TeamRepository.listAllTeam(entityManager);
        addCrud(getCrud(""), "All users");
        for (String nomTeam:strings)
        {
              addCrud(getCrud(nomTeam), nomTeam);
        };
        addComponents(titre,tabSheet);
    }

    private void addCrud(CrudWithFilter crud, String caption) {

        VerticalLayout layout = new VerticalLayout(crud);
        layout.setSizeFull();
        layout.setMargin(true);
        tabSheet.addTab(layout, caption);
    }


    private CrudWithFilter<User> getCrud(String filtre) {

        GridCrudWithFilter<User> crud = new GridCrudWithFilter<>(User.class, new HorizontalSplitCrudLayout());
        crud.setFilter(filtre);
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
        crud.getGrid().getColumn("mainProduct").setRenderer(product -> product == null ? "" : ((Product) product).getName(), new TextRenderer());
        crud.getGrid().getColumn("teamid").setRenderer(group -> group == null ? "" : ((Team) group).getNomteam(), new TextRenderer());
        ((Grid.Column<User, Date>) crud.getGrid().getColumn("dateCreation")).setRenderer(new DateRenderer("%1$tY-%1$tm-%1$te"));

        formFactory.setFieldType("password", PasswordField.class);
        formFactory.setFieldCreationListener("dateCreation", field -> ((DateField) field).setDateFormat("yyyy-MM-dd"));

        formFactory.setFieldProvider("products", new CheckBoxGroupProvider<>("Produit", ProductRepository.findAll(), Product::getName));
        formFactory.setFieldProvider("mainProduct", new ComboBoxProvider<>("Produit Principal", ProductRepository.findAll(), Product::getName));

        formFactory.setFieldProvider("teamid", new ComboBoxProvider<>("team", TeamRepository.findAll(), Team::getNomteam));

        formFactory.setButtonCaption(CrudOperation.ADD, "Add new user");
        crud.setRowCountCaption("%d user(s) found");

        crud.setClickRowToUpdate(true);
        crud.setUpdateOperationVisible(false);

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
    public Collection<User> findAll(String filtre) {
        return UserRepository.findAll(filtre);
    }

}
