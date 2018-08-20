package dap.vaadin.crudui.form;

import com.vaadin.data.HasValue;

import java.io.Serializable;

/**
 * @author  .
 */
@FunctionalInterface
public interface FieldCreationListener extends Serializable {

    void fieldCreated(HasValue field);

}
