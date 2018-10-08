package dap.app;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.ui.VerticalLayout;

@Theme("mytheme")
@Push
public class TeamView extends VerticalLayout implements View {


       public TeamView() {
           TeamCrudView tcv = new TeamCrudView();
           DivisionCrudView dcv = new DivisionCrudView();

           addComponents(tcv,dcv);

    }



}
