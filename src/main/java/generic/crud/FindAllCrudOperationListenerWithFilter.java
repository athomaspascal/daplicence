package generic.crud;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author  .
 */
@FunctionalInterface
public interface FindAllCrudOperationListenerWithFilter<T> extends Serializable {

    Collection<T> findAll(String filter);

}
