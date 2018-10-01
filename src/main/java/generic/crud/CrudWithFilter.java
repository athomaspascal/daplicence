package generic.crud;

import com.vaadin.ui.Component;
import generic.crudform.CrudFormFactory;
import generic.layout.CrudLayout;

/**
 * @author
 */
public interface CrudWithFilter<T> extends Component {

    void setAddOperationVisible(boolean visible);

    void setUpdateOperationVisible(boolean visible);

    void setDeleteOperationVisible(boolean visible);

    void setFindAllOperationVisible(boolean visible);

    CrudFormFactory<T> getCrudFormFactory();

    void setCrudFormFactory(CrudFormFactory<T> crudFormFactory);

    void setFindAllOperation(FindAllCrudOperationListenerWithFilter<T> findAllOperation);

    void setAddOperation(AddOperationListener<T> addOperation);

    void setUpdateOperation(UpdateOperationListener<T> updateOperation);

    void setDeleteOperation(DeleteOperationListener<T> deleteOperation);

    void setOperations(FindAllCrudOperationListenerWithFilter<T> findAllOperation, AddOperationListener<T> addOperation, UpdateOperationListener<T> updateOperation, DeleteOperationListener<T> deleteOperation);

    void setCrudListener(CrudListenerWithFilter<T> crudListener);

    CrudLayout getCrudLayout();

}
