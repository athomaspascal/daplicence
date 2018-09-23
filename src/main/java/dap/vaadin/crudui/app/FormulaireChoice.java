package dap.vaadin.crudui.app;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import dap.vaadin.crudui.entities.formulaire.Formulaire;
import dap.vaadin.crudui.entities.formulaire.FormulaireRepository;

import java.util.ArrayList;
import java.util.List;

@Theme("mytheme")
public class FormulaireChoice extends VerticalLayout implements View{

    public FormulaireChoice() {
        JPAService.init();
        List<Formulaire> listFormulaire = FormulaireRepository.findAll();
        List<String> data = new ArrayList<String>();

        listFormulaire.forEach(formulaire -> {
            data.add(formulaire.getLibelleFormulaire());
        });

        Label titre = new Label("Liste des formulaires");
        ListSelect sample = new ListSelect<>("Select an option", data);
        sample.setRows(listFormulaire.size());
        sample.select(data.get(0));
        sample.setSizeUndefined();
        sample.addValueChangeListener(event -> Notification.show("Value changed:", String.valueOf(event.getValue()),
                Notification.Type.TRAY_NOTIFICATION));

        addComponents(sample);

    }
}
