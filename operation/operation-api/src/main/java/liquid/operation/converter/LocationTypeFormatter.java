package liquid.operation.converter;

import liquid.operation.domain.LocationType;
import liquid.operation.service.LocationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

/**
 * Created by Tao Ma on 4/5/15.
 */
@Component
public class LocationTypeFormatter implements Formatter<LocationType> {

    @Autowired
    private LocationTypeService locationTypeService;

    @Override
    public LocationType parse(String text, Locale locale) throws ParseException {
        if (text.length() == 0) {
            return null;
        }
        return locationTypeService.find(Byte.valueOf(text));
    }

    @Override
    public String print(LocationType object, Locale locale) {
        if (null == object) {
            return "";
        }
        return String.valueOf(object.getId());
    }
}
