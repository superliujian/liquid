package liquid.validation.constraints;


import liquid.validation.validator.ContainerQtyValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/15/13
 * Time: 7:12 PM
 */
@Documented
@Constraint(validatedBy = ContainerQtyValidator.class)
@Target(FIELD)
@Retention(RUNTIME)
public @interface ContainerQtyMax {
    String message() default "{liquid.validation.constraints.ContainerQtyMax}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String value();
}
