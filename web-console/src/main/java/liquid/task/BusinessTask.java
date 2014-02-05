package liquid.task;

import org.springframework.validation.BindingResult;

/**
 * Created by tao on 12/19/13.
 */
public interface BusinessTask<T> {
    void claim();

    /**
     * Validate the data before completing a task.
     *
     * @return
     */
    ValidationResult validate(T formBean, BindingResult result);

    T summary();

    void complete();
}
