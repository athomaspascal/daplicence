package dap.vaadin.crudui.app;

import com.vaadin.navigator.View;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import java.io.File;

public class MainView extends VerticalLayout implements View {

    public MainView()
    {
        Label titre = new Label("License management");
        titre.setStyleName("titre");

        Image imageTeam = setImage("VAADIN/themes/mytheme/images/Team.jpg","my-img-button","200","200","Team");
        Image imageProduct = setImage("VAADIN/themes/mytheme/images/Software.png","my-img-button","200","200","Product");
        Image imageContacts = setImage("VAADIN/themes/mytheme/images/Contacts.png","my-img-button","200","200","Contacts");

        Image image = setImage("VAADIN/themes/mytheme/images/Users.jpg","my-img-button","200","200","Licence");

        HorizontalLayout h = new HorizontalLayout();
        HorizontalLayout h2 = new HorizontalLayout(imageContacts);
        h.addComponents(image,imageTeam,imageProduct);
        addComponents(titre,h,h2);
    }

    public Image setImage(String path, String style, String width, String height,String viewName)
    {
        String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
        FileResource resourceTeam = new FileResource(new File( basepath + "/" +path ));
        Image imageTeam = new Image("", resourceTeam);
        imageTeam.addStyleName(style);
        imageTeam.setWidth(width);
        imageTeam.setHeight(height);
        imageTeam.addClickListener(e->{
            getUI().getNavigator().navigateTo(viewName);
        });
        return imageTeam;

    }
}
