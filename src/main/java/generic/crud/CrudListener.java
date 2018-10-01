package generic.crud;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author
 */
public interface CrudListener<T> extends Serializable {

    Collection<T> findAll();

    T add(T domainObjectToAdd);

    T update(T domainObjectToUpdate);

    void delete(T domainObjectToDelete);

}
