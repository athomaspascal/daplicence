package generic.crud.impl;

import com.vaadin.data.provider.Query;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import generic.crud.*;
import generic.crudform.CrudFormFactory;
import generic.crudform.impl.form.factory.VerticalCrudFormFactory;
import generic.layout.CrudLayout;
import generic.layout.impl.WindowBasedCrudLayout;

import java.util.Collection;

/**
 * @author
 */
public class GridCrudWithFilter<T> extends AbstractCrudWithFilter<T> {

    protected String rowCountCaption = "%d items(s) found";
    protected String savedMessage = "Item saved";
    protected String deletedMessage = "Item deleted";

    protected Button findAllButton;
    protected Button addButton;
    protected Button updateButton;
    protected Button deleteButton;

    protected Grid<T> grid;

    protected Collection<T> items;
    private boolean clickRowToUpdate;

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    String filter;

    public GridCrudWithFilter(Class<T> domainType) {
        this(domainType, new WindowBasedCrudLayout(), new VerticalCrudFormFactory<>(domainType), null);
    }

    public GridCrudWithFilter(Class<T> domainType, CrudLayout crudLayout) {
        this(domainType, crudLayout, new VerticalCrudFormFactory<>(domainType), null);
    }

    public GridCrudWithFilter(Class<T> domainType, CrudFormFactory<T> crudFormFactory) {
        this(domainType, new WindowBasedCrudLayout(), crudFormFactory, null);
    }

    public GridCrudWithFilter(Class<T> domainType, CrudListenerWithFilter<T> crudListener) {
        this(domainType, new WindowBasedCrudLayout(), new VerticalCrudFormFactory<>(domainType), crudListener);
    }

    public GridCrudWithFilter(Class<T> domainType, CrudLayout crudLayout, CrudFormFactory<T> crudFormFactory) {
        this(domainType, crudLayout, crudFormFactory, null);
    }

    public GridCrudWithFilter(Class<T> domainType, CrudLayout crudLayout, CrudFormFactory<T> crudFormFactory, CrudListenerWithFilter<T> crudListener) {
        super(domainType, crudLayout, crudFormFactory, crudListener);
        initLayout();
    }

    protected void initLayout() {
        findAllButton = new Button("", e -> findAllButtonClicked());
        findAllButton.setDescription("Refresh list");
        findAllButton.setIcon(VaadinIcons.REFRESH);
        crudLayout.addToolbarComponent(findAllButton);

        addButton = new Button("", e -> addButtonClicked());
        addButton.setDescription("Add");
        addButton.setIcon(VaadinIcons.PLUS);
        crudLayout.addToolbarComponent(addButton);

        updateButton = new Button("", e -> updateButtonClicked());
        updateButton.setDescription("Update");
        updateButton.setIcon(VaadinIcons.PENCIL);
        crudLayout.addToolbarComponent(updateButton);

        deleteButton = new Button("", e -> deleteButtonClicked());
        deleteButton.setDescription("Delete");
        deleteButton.setIcon(VaadinIcons.TRASH);
        crudLayout.addToolbarComponent(deleteButton);

        grid = new Grid<>(domainType);
        grid.setSizeFull();
        grid.addSelectionListener(e -> gridSelectionChanged());
        crudLayout.setMainComponent(grid);

        updateButtons();
    }

    @Override
    public void attach() {
        super.attach();
        refreshGrid();
    }

    @Override
    public void setAddOperationVisible(boolean visible) {
        addButton.setVisible(visible);
    }

    @Override
    public void setUpdateOperationVisible(boolean visible) {
        updateButton.setVisible(visible);
    }

    @Override
    public void setDeleteOperationVisible(boolean visible) {
        deleteButton.setVisible(visible);
    }

    @Override
    public void setFindAllOperationVisible(boolean visible) {
        findAllButton.setVisible(false);
    }

    public void refreshGrid() {
        items = findAllOperation.findAll(filter);
        if (items != null)
           grid.setItems(items);
    }

    public void setClickRowToUpdate(boolean clickRowToUpdate) {
        this.clickRowToUpdate = clickRowToUpdate;
    }

    protected void updateButtons() {
        boolean rowSelected = !grid.asSingleSelect().isEmpty();
        updateButton.setEnabled(rowSelected);
        deleteButton.setEnabled(rowSelected);
    }

    protected void gridSelectionChanged() {
        updateButtons();
        T domainObject = grid.asSingleSelect().getValue();

        if (domainObject != null) {
            Component form = crudFormFactory.buildNewForm(CrudOperation.READ, domainObject, true, null, event -> {
                grid.asSingleSelect().clear();
            });

            crudLayout.showForm(CrudOperation.READ, form);

            if (clickRowToUpdate) {
                updateButtonClicked();
            }

        } else {
            crudLayout.hideForm();
        }
    }

    protected void findAllButtonClicked() {
        grid.asSingleSelect().clear();
        refreshGrid();
        Notification.show(String.format(rowCountCaption, grid.getDataProvider().size(new Query())));
    }

    protected void addButtonClicked() {
        try {
            T domainObject = domainType.newInstance();
            showForm(CrudOperation.ADD, domainObject, false, savedMessage, event -> {
                try {
                    T addedObject = addOperation.perform(domainObject);
                    refreshGrid();
                    if (items.contains(addedObject)) {
                        grid.asSingleSelect().setValue(addedObject);
                        // TODO: grid.scrollTo(addedObject);
                    }
                } catch (CrudOperationException e1) {
                    refreshGrid();
                } catch (Exception e2) {
                    refreshGrid();
                    throw e2;
                }
            });
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected void updateButtonClicked() {
        T domainObject = grid.asSingleSelect().getValue();
        showForm(CrudOperation.UPDATE, domainObject, false, savedMessage, event -> {
            try {
                T updatedObject = updateOperation.perform(domainObject);
                grid.asSingleSelect().clear();
                refreshGrid();
                if (items.contains(updatedObject)) {
                    grid.asSingleSelect().setValue(updatedObject);
                    // TODO: grid.scrollTo(updatedObject);
                }
            } catch (CrudOperationException e1) {
                refreshGrid();
            } catch (Exception e2) {
                refreshGrid();
                throw e2;
            }
        });
    }

    protected void deleteButtonClicked() {
        T domainObject = grid.asSingleSelect().getValue();
        showForm(CrudOperation.DELETE, domainObject, true, deletedMessage, event -> {
            try {
                deleteOperation.perform(domainObject);
                refreshGrid();
                grid.asSingleSelect().clear();
            } catch (CrudOperationException e1) {
                refreshGrid();
            } catch (Exception e2) {
                refreshGrid();
                throw e2;
            }
        });
    }

    protected void showForm(CrudOperation operation, T domainObject, boolean readOnly, String successMessage, Button.ClickListener buttonClickListener) {
        Component form = crudFormFactory.buildNewForm(operation, domainObject, readOnly,
                cancelClickEvent -> {
                    if (clickRowToUpdate) {
                        grid.asSingleSelect().clear();
                        // modif 11/10/2018
                        crudLayout.hideForm();
                    } else {
                        T selected = grid.asSingleSelect().getValue();
                        crudLayout.hideForm();
                        grid.asSingleSelect().clear();
                        grid.asSingleSelect().setValue(selected);
                    }
                },
                operationPerformedClickEvent -> {
                    crudLayout.hideForm();
                    buttonClickListener.buttonClick(operationPerformedClickEvent);
                    Notification.show(successMessage);
                });

        crudLayout.showForm(operation, form);
    }

    public Grid<T> getGrid() {
        return grid;
    }

    public Button getFindAllButton() {
        return findAllButton;
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getUpdateButton() {
        return updateButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public void setRowCountCaption(String rowCountCaption) {
        this.rowCountCaption = rowCountCaption;
    }

    public void setSavedMessage(String savedMessage) {
        this.savedMessage = savedMessage;
    }

    public void setDeletedMessage(String deletedMessage) {
        this.deletedMessage = deletedMessage;
    }

}