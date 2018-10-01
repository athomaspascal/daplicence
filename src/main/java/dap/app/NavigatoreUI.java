package dap.app;


import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

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
        navigator.addView("Licence", UserView.class);
        navigator.addView("Team", TeamView.class);
        navigator.addView("Product", ProductView.class);
        navigator.addView("Main", MainView.class);
        navigator.addView("Contacts", ContactsView.class);
        navigator.addView("Formulaire", FormulaireChoice.class);
        navigator.navigateTo("Main");
    }
}
