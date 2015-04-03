package liquid.converter;

import liquid.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

/**
 * Created by Tao Ma on 4/2/15.
 */
public abstract class GenericFormatter<E extends Text> implements Formatter<E> {
    @Autowired
    private CrudService<E> service;

    @Override
    public E parse(String text, Locale locale) throws ParseException {
        if (text.length() == 0) {
            return null;
        }
        return service.find(Long.valueOf(text));
    }

    @Override
    public String print(E object, Locale locale) {
        if (null == object) {
            return "";
        }
        return object.toText();
    }
}
