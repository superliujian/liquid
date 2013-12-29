package liquid.task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tao on 12/19/13.
 */
public class ValidationResult {
    private List<String> errors = new ArrayList<String>();

    public void addError(String error) {
        errors.add(error);
    }

    public boolean isPassed() {
        return errors.size() == 0;
    }

    public String[] getErrors() {
        return errors.toArray(new String[0]);
    }
}
