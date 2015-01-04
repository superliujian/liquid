package liquid.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Locale;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/16/13
 * Time: 9:24 PM
 */
@Component
public abstract class BaseController {
    protected static final int size = 20;

    @Autowired
    protected MessageSource messageSource;

    @Autowired
    private Locale locale;

    public void addFieldError(BindingResult result, String objectName, String fieldName, Object rejectedValue,
                              Object... arguments) {
        String defaultMessage = messageSource.getMessage(objectName + "." + fieldName, arguments, locale);
        FieldError filedError = new FieldError(objectName, fieldName, rejectedValue, false, null, null, defaultMessage);
        result.addError(filedError);
    }

    public String getMessage(String code, Object... arguments) {
        return messageSource.getMessage(code, arguments, locale);
    }
}
