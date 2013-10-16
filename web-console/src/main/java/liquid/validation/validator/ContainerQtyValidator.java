package liquid.validation.validator;

import liquid.validation.constraints.ContainerQtyMax;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/15/13
 * Time: 7:25 PM
 */
public class ContainerQtyValidator implements ConstraintValidator<ContainerQtyMax, Integer> {
    private String fieldName;

    @Override
    public void initialize(ContainerQtyMax constraintAnnotation) {
        fieldName = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value < 5;
    }
}
