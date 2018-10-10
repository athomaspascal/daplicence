package dap.view;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import dap.crudview.DivisionCrudView;
import dap.crudview.TeamCrudView;

@Theme("mytheme")
@Push
public class TeamView extends VerticalLayout implements View {


       public TeamView() {
           Label titre = new Label("Team and Division");
           titre.setStyleName("titre");
           TeamCrudView tcv = new TeamCrudView();
           DivisionCrudView dcv = new DivisionCrudView(tcv);
           addComponents(titre,tcv,dcv);

    }



}
