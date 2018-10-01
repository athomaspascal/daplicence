package dap.app;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;


public class test extends UI {
    public static void main(String[] args) throws Exception {

    }


    @Override
    protected void init(VaadinRequest vaadinRequest) {
        HorizontalLayout h1 = new HorizontalLayout();
        Label l = new Label("test");

        setContent(l);
    }
}
