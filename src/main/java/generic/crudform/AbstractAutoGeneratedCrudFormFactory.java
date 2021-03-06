package generic.crudform;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.data.HasValue;
import com.vaadin.data.converter.*;
import com.vaadin.data.util.BeanUtil;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.shared.util.SharedUtil;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import generic.crud.CrudOperation;
import generic.crud.CrudOperationException;
import generic.crudform.impl.field.provider.DefaultFieldProvider;
import dap.data.converter.StringToByteConverter;
import dap.data.converter.StringToCharacterConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author  .
 */
public abstract class AbstractAutoGeneratedCrudFormFactory<T> extends AbstractCrudFormFactory<T> {
    static Logger logger = LogManager.getLogger("elastic-generator");
    protected Map<CrudOperation, String> buttonCaptions = new HashMap<>();
    protected Map<CrudOperation, Resource> buttonIcons = new HashMap<>();
    protected Map<CrudOperation, Set<String>> buttonStyleNames = new HashMap<>();

    protected String cancelButtonCaption = "Cancel";
    protected String validationErrorMessage = "Please fix the errors and try again";
    protected Class<T> domainType;

    protected Binder<T> binder;

    public AbstractAutoGeneratedCrudFormFactory(Class<T> domainType) {
        this.domainType = domainType;

        setButtonCaption(CrudOperation.READ, "Ok");
        setButtonCaption(CrudOperation.ADD, "Add");
        setButtonCaption(CrudOperation.UPDATE, "Update");
        setButtonCaption(CrudOperation.DELETE, "Yes, delete");

        setButtonIcon(CrudOperation.READ, null);
        setButtonIcon(CrudOperation.ADD, VaadinIcons.CHECK);
        setButtonIcon(CrudOperation.UPDATE, VaadinIcons.CHECK);
        setButtonIcon(CrudOperation.DELETE, VaadinIcons.TRASH);

        addButtonStyleName(CrudOperation.READ, null);
        addButtonStyleName(CrudOperation.ADD, ValoTheme.BUTTON_PRIMARY);
        addButtonStyleName(CrudOperation.UPDATE, ValoTheme.BUTTON_PRIMARY);
        addButtonStyleName(CrudOperation.DELETE, ValoTheme.BUTTON_DANGER);

        setVisibleProperties(discoverProperties().toArray(new String[0]));
    }

    public void setButtonCaption(CrudOperation operation, String caption) {
        buttonCaptions.put(operation, caption);
    }

    public void setButtonIcon(CrudOperation operation, Resource icon) {
        buttonIcons.put(operation, icon);
    }

    public void addButtonStyleName(CrudOperation operation, String styleName) {
        buttonStyleNames.putIfAbsent(operation, new HashSet<>());
        buttonStyleNames.get(operation).add(styleName);
    }

    public void setCancelButtonCaption(String cancelButtonCaption) {
        this.cancelButtonCaption = cancelButtonCaption;
    }

    public void setValidationErrorMessage(String validationErrorMessage) {
        this.validationErrorMessage = validationErrorMessage;
    }

    protected List<String> discoverProperties() {
        try {
            List<PropertyDescriptor> descriptors = BeanUtil.getBeanPropertyDescriptors(domainType);
            return descriptors.stream()
                    .filter(d -> !d.getName().equals("class"))
                    .map(d -> d.getName()).collect(Collectors.toList());
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }

    protected List<HasValue> buildFields(CrudOperation operation, T domainObject, boolean readOnly) {
        binder = buildBinder(operation, domainObject);
        ArrayList<HasValue> fields = new ArrayList<>();
        CrudFormConfiguration configuration = getConfiguration(operation);

        for (int i = 0; i < configuration.getVisibleProperties().size(); i++) {
            String property = configuration.getVisibleProperties().get(i);
            try {
                String fieldCaption = null;
                if (!configuration.getFieldCaptions().isEmpty()) {

                    if (configuration.getFieldCaptions().size() > i)
                    {
                        fieldCaption = configuration.getFieldCaptions().get(i);
                        //logger.info("field:" + configuration.getVisibleProperties().get(i) + " caption:" + fieldCaption);
                    }
                    /*
                    else
                        logger.info("field:" + configuration.getVisibleProperties().get(i) + " caption:not set");
                        */
                }

                int largeur =0;
                if (!configuration.getHashTableWidth().isEmpty())
                {
                    Hashtable<String, Integer> hashTableWidth = configuration.getHashTableWidth();

                    Set <String> fieldNames = hashTableWidth.keySet();


                    for(String theField:fieldNames)
                    {
                        if (property.equalsIgnoreCase(theField))
                        largeur = hashTableWidth.get(theField);
                    }

                }

                Class<?> propertyType = BeanUtil.getPropertyType(domainObject.getClass(), property);

                if (propertyType == null) {
                    throw new RuntimeException("Cannot find type for property " + domainObject.getClass().getName() + "." + property);
                }

                HasValue<Object> field = buildField(configuration, property, propertyType);
                configureField(field, property, fieldCaption, readOnly, configuration,largeur);
                bindField(field, property, propertyType);
                fields.add(field);

                FieldCreationListener creationListener = configuration.getFieldCreationListeners().get(property);
                if (creationListener != null) {
                    creationListener.fieldCreated(field);
                }

            } catch (Exception e) {
                throw new RuntimeException("Error creating Field for property " + domainObject.getClass().getName() + "." + property, e);
            }
        }

        binder.readBean(domainObject);

        if (!fields.isEmpty() && !readOnly) {
            HasValue field = fields.get(0);
            if (field instanceof Component.Focusable) {
                ((Component.Focusable) field).focus();
            }
        }

        return fields;
    }

    protected HasValue<Object> buildField(CrudFormConfiguration configuration, String property, Class<?> propertyType) throws InstantiationException, IllegalAccessException {
        HasValue<Object> field;
        FieldProvider provider = configuration.getFieldProviders().get(property);

        if (provider != null) {
            field = provider.buildField();
        } else {
            Class<? extends HasValue> fieldType = configuration.getFieldTypes().get(property);
            if (fieldType != null) {
                field = fieldType.newInstance();
            } else {
                field = new DefaultFieldProvider(propertyType).buildField();
            }
        }

        return field;
    }

    private void configureField(HasValue<Object> field, String property, String fieldCaption, boolean readOnly, CrudFormConfiguration configuration,int largeur) {
        logger.info("field:" + property + " largeur:" + largeur);
        if (field instanceof AbstractComponent) {
            if (fieldCaption != null) {
                ((AbstractComponent) field).setCaption(fieldCaption);
            } else {
                ((AbstractComponent) field).setCaption(SharedUtil.propertyIdToHumanFriendly(property));
            }
        }

        /*
        if (field != null && field instanceof Component) {
            ((Component) field).setWidth("100%");
        }
        */
        if (largeur>0  && field instanceof Component)
            ((Component) field).setWidth(String.valueOf(largeur));


        field.setReadOnly(readOnly);

        if (!configuration.getDisabledProperties().isEmpty()) {
            ((Component) field).setEnabled(!configuration.getDisabledProperties().contains(property));
        }
    }

    protected void bindField(HasValue<Object> field, String property, Class<?> propertyType) {
        Binder.BindingBuilder bindingBuilder = binder.forField(field);

        if (AbstractTextField.class.isAssignableFrom(field.getClass())) {
            bindingBuilder = bindingBuilder.withNullRepresentation("");
        }

        if (Double.class.isAssignableFrom(propertyType) || double.class.isAssignableFrom(propertyType)) {
            bindingBuilder = bindingBuilder.withConverter(new StringToDoubleConverter(null, "Must be a number"));

        } else if (Long.class.isAssignableFrom(propertyType) || long.class.isAssignableFrom(propertyType)) {
            bindingBuilder = bindingBuilder.withConverter(new StringToLongConverter(null, "Must be a number"));

        } else if (BigDecimal.class.isAssignableFrom(propertyType)) {
            bindingBuilder = bindingBuilder.withConverter(new StringToBigDecimalConverter(null, "Must be a number"));

        } else if (BigInteger.class.isAssignableFrom(propertyType)) {
            bindingBuilder = bindingBuilder.withConverter(new StringToBigIntegerConverter(null, "Must be a number"));

        } else if (Integer.class.isAssignableFrom(propertyType) || int.class.isAssignableFrom(propertyType)) {
            bindingBuilder = bindingBuilder.withConverter(new StringToIntegerConverter(null, "Must be a number"));

        } else if (Byte.class.isAssignableFrom(propertyType) || byte.class.isAssignableFrom(propertyType)) {
            bindingBuilder = bindingBuilder.withConverter(new StringToByteConverter(null, "Must be a number"));

        } else if (Character.class.isAssignableFrom(propertyType) || char.class.isAssignableFrom(propertyType)) {
            bindingBuilder = bindingBuilder.withConverter(new StringToCharacterConverter());

        } else if (Float.class.isAssignableFrom(propertyType) || float.class.isAssignableFrom(propertyType)) {
            bindingBuilder = bindingBuilder.withConverter(new StringToFloatConverter(null, "Must be a number"));

        } else if (Date.class.isAssignableFrom(propertyType)) {
            bindingBuilder = bindingBuilder.withConverter(new LocalDateToDateConverter());
        }

        bindingBuilder.bind(property);
    }

    protected Binder<T> buildBinder(CrudOperation operation, T domainObject) {
        Binder<T> binder;

        if (getConfiguration(operation).isUseBeanValidation()) {
            binder = new BeanValidationBinder(domainObject.getClass());
        } else {
            binder = new Binder(domainObject.getClass());
        }

        return binder;
    }

    protected Button buildOperationButton(CrudOperation operation, T domainObject, Button.ClickListener clickListener) {
        if (clickListener == null) {
            return null;
        }

        Button button = new Button(buttonCaptions.get(operation), buttonIcons.get(operation));
        buttonStyleNames.get(operation).forEach(styleName -> button.addStyleName(styleName));
        button.addClickListener(event -> {
            if (binder.writeBeanIfValid(domainObject)) {
                try {
                    clickListener.buttonClick(event);

                } catch (Exception e) {
                    showError(operation, e);
                }
            } else {
                Notification.show(validationErrorMessage);
            }
        });
        return button;
    }

    @Override
    public void showError(CrudOperation operation, Exception e) {
        if (errorListener != null) {
            errorListener.accept(e);
        } else {
            if (CrudOperationException.class.isAssignableFrom(e.getClass())) {
                Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);
            } else {
                Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);
                throw new RuntimeException("Error executing " + operation.name() + " operation", e);
            }
        }
    }

    protected Button buildCancelButton(Button.ClickListener clickListener) {
        if (clickListener == null) {
            return null;
        }

        return new Button(cancelButtonCaption, clickListener);
    }

    protected Layout buildFooter(CrudOperation operation, T domainObject, Button.ClickListener cancelButtonClickListener, Button.ClickListener operationButtonClickListener) {
        Button operationButton = buildOperationButton(operation, domainObject, operationButtonClickListener);

        Button cancelButton = buildCancelButton(cancelButtonClickListener);

        HorizontalLayout footerLayout = new HorizontalLayout();
        footerLayout.setSizeUndefined();
        footerLayout.setSpacing(true);

        if (cancelButton != null) {
            footerLayout.addComponent(cancelButton);
        }

        if (operationButton != null) {
            footerLayout.addComponent(operationButton);
        }

        return footerLayout;
    }

}
