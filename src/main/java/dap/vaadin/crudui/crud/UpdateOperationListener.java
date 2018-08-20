package dap.vaadin.crudui.crud;

import java.io.Serializable;

/**
 * @author  .
 */
@FunctionalInterface
public interface UpdateOperationListener<T> extends Serializable {

    T perform(T domainObject);

}
