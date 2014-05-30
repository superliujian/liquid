package liquid.persistence.domain;

/**
 * Created by redbrick9 on 5/29/14.
 */
public class EntityInstantiationException extends RuntimeException {
    public EntityInstantiationException() {
        super();
    }

    public EntityInstantiationException(String message) {
        super(message);
    }

    public EntityInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityInstantiationException(Throwable cause) {
        super(cause);
    }

    protected EntityInstantiationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
