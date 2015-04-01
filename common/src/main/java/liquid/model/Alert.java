package liquid.model;

import java.util.Arrays;

/**
 * Created by Tao Ma on 4/1/15.
 */
public class Alert {
    final private String css;
    final private String code;
    final private String[] arguments;

    public Alert(String code, String... arguments) {
        this(AlertType.SUCCESS, code, arguments);
    }

    public Alert(AlertType type, String code, String... arguments) {
        this.css = type.css();
        this.code = code;
        this.arguments = arguments;
    }

    public String getCss() {
        return css;
    }

    public String getCode() {
        return code;
    }

    public String[] getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=Alert");
        sb.append(", code='").append(code).append('\'');
        sb.append(", arguments=").append(Arrays.toString(arguments));
        sb.append('}');
        return sb.toString();
    }
}
