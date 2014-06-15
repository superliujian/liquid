package liquid.excel;

/**
 * Created by redbrick9 on 6/15/14.
 */
public class ExcelOperationException extends RuntimeException {
    public ExcelOperationException() {
        super();
    }

    public ExcelOperationException(String message) {
        super(message);
    }

    public ExcelOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcelOperationException(Throwable cause) {
        super(cause);
    }

    protected ExcelOperationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
