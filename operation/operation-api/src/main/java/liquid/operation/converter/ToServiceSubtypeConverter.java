package liquid.operation.converter;

import liquid.operation.domain.ServiceSubtype;
import liquid.operation.service.ServiceSubtypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Created by Tao Ma on 4/3/15.
 */
@Component
public class ToServiceSubtypeConverter implements Converter<String, ServiceSubtype> {
    @Autowired
    private ServiceSubtypeService serviceSubtypeService;

    @Override
    public ServiceSubtype convert(String text) {
        if (text.length() == 0) {
            return null;
        }
        return serviceSubtypeService.find(Long.valueOf(text));
    }
}
