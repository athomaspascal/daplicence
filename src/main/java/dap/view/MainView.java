package dap.view;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

@Theme("mytheme")
public class MainView extends HorizontalLayout implements View {
    static Logger logger = LogManager.getLogger("elastic-generator");
    String pathImage = "VAADIN/themes/mytheme/images/";

    public MainView()
    {
        Label titre = new Label("License management");
        titre.setStyleName("titre");


        String width = "100";
        String height = "100";
        String imageStyle = "my-img-button";

        Image imageTeam = setImage(pathImage+ "Team.jpg",imageStyle,width,height,"Team");
        Image imageProduct = setImage(pathImage+ "Software.png",imageStyle,width,height,"Product");
        Image imageContacts = setImage(pathImage+ "Contacts.png",imageStyle,width,height,"Contacts");
        Image image = setImage(pathImage+ "Users.jpg",imageStyle,width,height,"Licence");
        Image imageFormulaire = setImage(pathImage+ "formulaire.gif",imageStyle,width,height,"Formulaire");
        Image imageConfigFormulaire = setImage(pathImage+ "configure-formulaire.jpg",imageStyle,width,height,"Config");
        Image imageGraph= setImage(pathImage+ "graph.png",imageStyle,width,height,"graph");

        HorizontalLayout h = new HorizontalLayout(image,imageTeam,imageProduct);
        HorizontalLayout h2 = new HorizontalLayout(imageContacts,imageFormulaire,imageConfigFormulaire);
        HorizontalLayout h3 = new HorizontalLayout(imageGraph);
        VerticalLayout v1 = new VerticalLayout();

        v1.addComponents(titre,h,h2,h3);
        CssLayout v2 = new CssLayout();
        Properties p = new Properties();
        try {
            p.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("dashboard.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        v2.setWidth(p.getProperty("width-window"));

        for(int i = 1; i <= Integer.parseInt(p.getProperty("nb-dashboard")); i++)
        {
            String URL=p.getProperty("dashboard-" + i);
            BrowserFrame browser = setBrowser(URL,p.getProperty("width-" + i),p.getProperty("heigth-" + i));
            v2.addComponent(browser);
        }


        addComponents(v1,v2);

    }

    public BrowserFrame setBrowser(String URL,String width,String heigth)
    {
        //logger.info("URL:" +URL);
        //logger.info("width:" +width);
        //logger.info("heigth:" +heigth);
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
        Image imageTeam = new Image(viewName, resourceTeam);
        imageTeam.addStyleName(style);
        imageTeam.setWidth(width);
        imageTeam.setHeight(height);
        imageTeam.addClickListener(e->{
            getUI().getNavigator().navigateTo(viewName);
        });
        return imageTeam;

    }
}
