package dap.vaadin.crudui.crud;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author  .
 */
@FunctionalInterface
public interface FindAllCrudOperationListener<T> extends Serializable {

    Collection<T> findAll();

}
