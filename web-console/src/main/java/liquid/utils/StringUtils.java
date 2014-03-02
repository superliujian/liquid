package liquid.utils;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/12/13
 * Time: 9:43 PM
 */
public class StringUtils {
    private StringUtils() {
    }

    public static boolean valuable(String value) {
        return value != null && value.trim().length() > 0;
    }

    public static String toString(String[] values) {
        if (null == values) return "null";
        if (0 == values.length) return "";
        if (1 == values.length) return values[0];

        StringBuilder sb = new StringBuilder();
        sb.append(values[0]);
        for (int i = 1; i < values.length; i++) {
            sb.append(",");
            sb.append(values[i]);
        }
        return sb.toString();
    }
}
