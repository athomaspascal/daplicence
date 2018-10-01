package dap.app;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import generic.crud.Crud;
import generic.crud.CrudListener;
import generic.crud.CrudOperation;
import generic.crud.impl.GridCrud;
import generic.crudform.impl.form.factory.GridLayoutCrudFormFactory;
import generic.layout.impl.HorizontalSplitCrudLayout;
import dap.entities.product.Product;
import dap.entities.product.ProductRepository;

import java.util.Collection;

@Theme("mytheme")
public class ProductView extends VerticalLayout implements View, CrudListener<Product> {


    private TabSheet tabSheet = new TabSheet();



    public ProductView() {
        Label titre = new Label("Product management");
        titre.setStyleName("titre");
        tabSheet.setSizeFull();

        addCrud(getConfiguredCrud(), "All Products");

        addComponents(titre,tabSheet);


    }

    private void addCrud(Crud crud, String caption) {
        VerticalLayout layout = new VerticalLayout(crud);
        layout.setSizeFull();
        layout.setMargin(true);
        tabSheet.addTab(layout, caption);
    }

    private Crud getDefaultCrud() {
        return new GridCrud<>(Product.class, this);
    }

  
    private Crud getConfiguredCrud() {
        GridCrud<Product> crud = new GridCrud<>(Product.class, new HorizontalSplitCrudLayout());
        crud.setCrudListener(this);

        GridLayoutCrudFormFactory<Product> formFactory = new GridLayoutCrudFormFactory<>(Product.class, 2, 2);
        crud.setCrudFormFactory(formFactory);

        formFactory.setUseBeanValidation(true);

        //formFactory.setErrorListener(e -> Notification.show("Custom error message (simulated error)", Notification.Type.ERROR_MESSAGE));

        formFactory.setVisibleProperties(CrudOperation.ADD, "name","admin");
        formFactory.setVisibleProperties(CrudOperation.UPDATE, "name","admin");
        formFactory.setVisibleProperties(CrudOperation.DELETE, "name","admin");

        formFactory.setDisabledProperties("id");

        crud.getGrid().setColumns("id","name","admin");
        
        
        formFactory.setButtonCaption(CrudOperation.ADD, "Add new product");
        crud.setRowCountCaption("%d product(s) found");

        crud.setClickRowToUpdate(true);
        crud.setUpdateOperationVisible(false);

        return crud;
    }

 
    @Override
    public Product add(Product product) {
        ProductRepository.save(product);
        return product;
    }

    @Override
    public Product update(Product product) {
        /*
        if (product.getId().equals(5l)) {
            throw new RuntimeException("A simulated error has occurred");
        }
        */
        return ProductRepository.save(product);
    }

    @Override
    public void delete(Product product) {
        ProductRepository.delete(product);
    }

    @Override
    public Collection<Product> findAll() {
        return ProductRepository.findAll();
    }

}
