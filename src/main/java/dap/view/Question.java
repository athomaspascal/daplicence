package dap.view;

import com.vaadin.navigator.View;
import com.vaadin.ui.VerticalLayout;
import dap.crudview.FormulaireCrudView;
import dap.crudview.FormulaireQuestionCrudView;

public class Question extends VerticalLayout implements View {
    public Question()
    {
        FormulaireQuestionCrudView q = new FormulaireQuestionCrudView(1);
        FormulaireCrudView fc = new FormulaireCrudView(q, this);
        addComponents(fc,q);

    }
}
