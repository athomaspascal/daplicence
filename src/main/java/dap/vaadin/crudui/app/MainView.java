package dap.vaadin.crudui.app;

import com.vaadin.navigator.View;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.*;

import java.io.File;

public class MainView extends HorizontalLayout implements View {

    public MainView()
    {
        Label titre = new Label("License management");
        titre.setStyleName("titre");
        String width = "100";
        String height = "100";

        Image imageTeam = setImage("VAADIN/themes/mytheme/images/Team.jpg","my-img-button",width,height,"Team");
        Image imageProduct = setImage("VAADIN/themes/mytheme/images/Software.png","my-img-button",width,height,"Product");
        Image imageContacts = setImage("VAADIN/themes/mytheme/images/Contacts.png","my-img-button",width,height,"Contacts");

        Image image = setImage("VAADIN/themes/mytheme/images/Users.jpg","my-img-button",width,height,"Licence");

        HorizontalLayout h = new HorizontalLayout();
        HorizontalLayout h2 = new HorizontalLayout(imageContacts);
        h.addComponents(image,imageTeam,imageProduct);
        VerticalLayout v1 = new VerticalLayout();
        v1.addComponents(titre,h,h2);
        CssLayout v2 = new CssLayout();
        v2.setWidth("900px");

        String URL="http://localhost:5601/goto/dd6ffa8f217efdfac0d48a4ebfa00cf0?embed=true";
        BrowserFrame browser = setBrowser(URL,"300px","300px");
        URL="http://localhost:5601/goto/a862502fdabb6e2cfa2bf98e28eca785?embed=true";
        BrowserFrame browser2 = setBrowser(URL,"600px","300px");
        URL="http://localhost:5601/goto/cf1c6745656931cc0cb7305ac1481215?embed=true";
        BrowserFrame browser3 = setBrowser(URL,"200px","300px");
        URL="http://localhost:5601/goto/57375cfe54d468556e4ed99f957a8f2f?embed=true";
        BrowserFrame browser4 = setBrowser(URL,"500px","300px");
        v2.addComponents(browser4,browser,browser3,browser2);

        addComponents(v1,v2);
    }

    public BrowserFrame setBrowser(String URL,String width,String heigth)
    {
        BrowserFrame browser2 = new BrowserFrame("",
                new ExternalResource(URL));
        browser2.setWidth(width);
        browser2.setHeight(heigth);
        return browser2;
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
