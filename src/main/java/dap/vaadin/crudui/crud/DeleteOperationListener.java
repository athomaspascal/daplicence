package dap.vaadin.crudui.crud;

import java.io.Serializable;

/**
 * @author  .
 */
@FunctionalInterface
public interface DeleteOperationListener<T> extends Serializable {

    void perform(T domainObject);

}
