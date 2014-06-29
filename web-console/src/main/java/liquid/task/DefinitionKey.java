package liquid.task;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by redbrick9 on 6/7/14.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DefinitionKey {
    String value();
}
