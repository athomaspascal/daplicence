package dap.view;


import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import dap.crudview.ContactsCrudView;
import dap.crudview.FormulaireCrudView;
import dap.crudview.ProductCrudView;
import dap.crudview.UserCrudView;

import javax.servlet.annotation.WebServlet;

@Theme("mytheme")
public class NavigatoreUI extends UI {

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = NavigatoreUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }


    public Navigator navigator;

    @Override
    protected void init(VaadinRequest request) {
        navigator = new Navigator( this,this);
        navigator.addView("Licence", UserCrudView.class);
        navigator.addView("Team", TeamView.class);
        navigator.addView("Product", ProductCrudView.class);
        navigator.addView("Main", MainView.class);
        navigator.addView("Contacts", ContactsCrudView.class);
        navigator.addView("Formulaire", FormulaireChoice.class);
        navigator.addView("Config", FormulaireCrudView.class);
        navigator.navigateTo("Main");
    }
}
