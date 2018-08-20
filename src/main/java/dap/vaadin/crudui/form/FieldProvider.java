package dap.vaadin.crudui.form;

import com.vaadin.data.HasValue;

import java.io.Serializable;

/**
 * @author  .
 */
@FunctionalInterface
public interface FieldProvider extends Serializable {

    HasValue buildField();

}
