package liquid.validation;

/**
 * Created by redbrick9 on 12/28/13.
 */
public class FormValidationResult {
    private final boolean isSuccessful;
    private final Long id;
    private final String name;

    private FormValidationResult(boolean isSuccessful, Long id, String name) {
        this.isSuccessful = isSuccessful;
        this.id = id;
        this.name = name;
    }

    public static FormValidationResult newSuccess(Long id, String name) {
        return new FormValidationResult(true, id, name);
    }

    public static FormValidationResult newFailure() {
        return new FormValidationResult(false, null, null);
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=FormValidationResult");
        sb.append(", isSuccessful=").append(isSuccessful);
        sb.append(", id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
