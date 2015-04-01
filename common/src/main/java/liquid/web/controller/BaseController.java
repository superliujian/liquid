package liquid.web.controller;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/16/13
 * Time: 9:24 PM
 */
@Component
public abstract class BaseController {
    protected static final int size = 20;

    public void addFieldError(BindingResult result, String objectName, String fieldName, Object rejectedValue,
                              Object... arguments) {
        FieldError filedError = new FieldError(objectName, fieldName, rejectedValue, false, null, null, "");
        result.addError(filedError);
    }
}
