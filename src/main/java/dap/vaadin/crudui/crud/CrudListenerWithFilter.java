package dap.vaadin.crudui.crud;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author
 */
public interface CrudListenerWithFilter<T> extends Serializable {

    Collection<T> findAll(String filtre);

    T add(T domainObjectToAdd);

    T update(T domainObjectToUpdate);

    void delete(T domainObjectToDelete);


}
