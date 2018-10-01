package generic.crud;

import java.io.Serializable;

/**
 * @author  .
 */
@FunctionalInterface
public interface AddOperationListener<T> extends Serializable {

    T perform(T domainObject);

}
