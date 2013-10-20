package liquid.validation.constraints;

import liquid.metadata.DatePattern;
import liquid.validation.validator.ContainerQtyValidator;
import liquid.validation.validator.DateFormatValidator;

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
 * Date: 10/17/13
 * Time: 11:45 PM
 */
@Documented
@Constraint(validatedBy = DateFormatValidator.class)
@Target(FIELD)
@Retention(RUNTIME)
public @interface DateFormat {
    String message() default "{liquid.validation.constraints.DateFormat.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    DatePattern value();
}
