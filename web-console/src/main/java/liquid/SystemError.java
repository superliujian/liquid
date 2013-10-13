package liquid;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/26/13
 * Time: 11:33 PM
 */
public class SystemError extends RuntimeException {
    public SystemError() {
        super();
    }

    public SystemError(String message) {
        super(message);
    }

    public SystemError(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemError(Throwable cause) {
        super(cause);
    }

    protected SystemError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
