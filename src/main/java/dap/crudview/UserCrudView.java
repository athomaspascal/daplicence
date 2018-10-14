package dap.crudview;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.renderers.TextRenderer;
import dap.entities.JPAService;
import generic.crud.CrudListenerWithFilter;
import generic.crud.CrudOperation;
import generic.crud.CrudWithFilter;
import generic.crud.impl.GridCrudWithFilter;
import generic.crudform.impl.field.provider.CheckBoxGroupProvider;
import generic.crudform.impl.field.provider.ComboBoxProvider;
import generic.crudform.impl.form.factory.GridLayoutCrudFormFactory;
import dap.entities.enterprise.Environnement;
import dap.entities.enterprise.EnvironnementRepository;
import dap.entities.product.Product;
import dap.entities.product.ProductRepository;
import dap.entities.team.Team;
import dap.entities.team.TeamRepository;
import dap.entities.user.User;
import dap.entities.user.UserRepository;
import generic.layout.impl.HorizontalSplitCrudLayout;
import elastic.ElasticClient;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

@Theme("mytheme")
public class UserCrudView extends VerticalLayout implements View, CrudListenerWithFilter<User> {


    private TabSheet tabSheet = new TabSheet();



    public UserCrudView() {
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

        formFactory.setVisibleProperties(CrudOperation.ADD, "nom", "dateCreation", "email", "matricule","teamid", "products","environnements", "password", "mainProduct", "active");
        formFactory.setVisibleProperties(CrudOperation.UPDATE, "id", "nom", "dateCreation", "gender","email", "matricule", "teamid","password", "products","environnements", "active", "mainProduct");
        formFactory.setVisibleProperties(CrudOperation.DELETE, "nom", "email", "matricule");

        formFactory.setDisabledProperties("id");

        crud.getGrid().setColumns("nom", "dateCreation", "email", "matricule", "teamid", "mainProduct", "active");
        crud.getGrid().getColumn("mainProduct").setRenderer(product -> product == null ? "" : ((Product) product).getName(), new TextRenderer());
        crud.getGrid().getColumn("teamid").setRenderer(group -> group == null ? "" : ((Team) group).getNomteam(), new TextRenderer());
        crud.getGrid().setHeightMode(HeightMode.UNDEFINED);

        ((Grid.Column<User, Date>) crud.getGrid().getColumn("dateCreation")).setRenderer(new DateRenderer("%1$tY-%1$tm-%1$te"));

        formFactory.setFieldType("password", PasswordField.class);
        formFactory.setFieldCreationListener("dateCreation", field -> ((DateField) field).setDateFormat("yyyy-MM-dd"));

        formFactory.setFieldProvider("products",
                new CheckBoxGroupProvider<>("Produit", ProductRepository.findAll(), Product::getName));
        formFactory.setFieldProvider("environnements",
                new CheckBoxGroupProvider<>("Environnement", EnvironnementRepository.findAll(), Environnement::getEnvironnementName));
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
        SimpleDateFormat format = null;
        Properties p =        new Properties();
        try {
            p.load(Thread.currentThread().
                            getContextClassLoader().
                            getResourceAsStream("general.properties"));
            format = new SimpleDateFormat(p.getProperty("date-format"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        String timestamp = format.format(user.getDateCreation());
        String document = "\"timestamp\":"+ "\"" + timestamp + p.getProperty("Timezone") + "\"";
        ElasticClient.connectAndIndex(document);
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
