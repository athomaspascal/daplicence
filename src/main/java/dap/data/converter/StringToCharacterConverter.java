package dap.data.converter;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;

public class StringToCharacterConverter implements Converter<String, Character> {

    @Override
    public Result<Character> convertToModel(String value, ValueContext context) {
        if (value == null) {
            return Result.ok(null);
        }

        if (value.length()>1) {
            return Result.error("Could not convert '" + value);
        }

        return Result.ok(value.charAt(0));
    }

    @Override
    public String convertToPresentation(Character value, ValueContext context) {
        if (value == null) {
            return null;
        }

        return value.toString();
    }

}
