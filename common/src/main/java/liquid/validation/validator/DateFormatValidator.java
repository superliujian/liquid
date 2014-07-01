package liquid.validation.validator;

import liquid.util.DatePattern;
import liquid.util.DateUtil;
import liquid.validation.constraints.DateFormat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/17/13
 * Time: 11:45 PM
 */
public class DateFormatValidator implements ConstraintValidator<DateFormat, String> {
    private DatePattern datePattern;

    @Override
    public void initialize(DateFormat constraintAnnotation) {
        datePattern = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        switch (datePattern) {
            case UNTIL_DAY:
                try {
                    DateUtil.dayOf(value);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            case UNTIL_MINUTE:
                try {
                    DateUtil.dateOf(value);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            default:
                return false;
        }
    }
}
