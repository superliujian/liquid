package liquid.service;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/19/13
 * Time: 10:11 PM
 */
public class NotCompletedException extends RuntimeException {
    private final String code;

    private final Object[] arguments;

    public NotCompletedException(String code, Object... arguments) {
        this.code = code;
        this.arguments = arguments;
    }

    public String getCode() {
        return code;
    }

    public Object[] getArguments() {
        return arguments;
    }
}
