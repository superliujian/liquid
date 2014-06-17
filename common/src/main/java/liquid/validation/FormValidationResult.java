package liquid.validation;

/**
 * Created by redbrick9 on 12/28/13.
 */
public class FormValidationResult {
    private final boolean isSuccessful;
    private final String messageKey;

    private FormValidationResult(boolean isSuccessful, String messageKey) {
        this.isSuccessful = isSuccessful;
        this.messageKey = messageKey;
    }

    public static FormValidationResult newSuccess() {
        return new FormValidationResult(true, "");
    }

    public static FormValidationResult newFailure(String messageKey) {
        return new FormValidationResult(false, messageKey);
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public String getMessageKey() {
        return messageKey;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FormValidationResult{");
        sb.append("isSuccessful=").append(isSuccessful);
        sb.append(", messageKey='").append(messageKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
