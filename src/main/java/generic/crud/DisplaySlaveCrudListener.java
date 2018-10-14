package generic.crud;


import java.io.Serializable;

@FunctionalInterface
public interface DisplaySlaveCrudListener<T> extends Serializable {
    T perform(T domainObject);

}
