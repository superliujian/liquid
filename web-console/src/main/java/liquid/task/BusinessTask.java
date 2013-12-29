package liquid.task;

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
    ValidationResult validate();

    T summary();

    void complete();
}
