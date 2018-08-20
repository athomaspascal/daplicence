package dap.vaadin.crudui.form.impl.field.provider;

import com.vaadin.ui.ItemCaptionGenerator;
import com.vaadin.ui.TwinColSelect;

import java.util.Collection;

/**
 * @author
 */
public class TwinColSelectProvider<T> extends AbstractListingProvider<TwinColSelect<T>, T> {

    public TwinColSelectProvider(Collection<T> items) {
        super(items);
    }

    public TwinColSelectProvider(String caption, Collection<T> items) {
        super(caption, items);
    }

    public TwinColSelectProvider(String caption, Collection<T> items, ItemCaptionGenerator<T> itemCaptionGenerator) {
        super(caption, items, itemCaptionGenerator);
    }

    @Override
    protected TwinColSelect<T> buildAbstractListing() {
        TwinColSelect<T> field = new TwinColSelect<>(caption, items);
        field.setItemCaptionGenerator(itemCaptionGenerator);
        return field;
    }

}
