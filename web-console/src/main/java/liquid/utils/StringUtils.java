package liquid.utils;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/12/13
 * Time: 9:43 PM
 */
public class StringUtils {
    private StringUtils() {}

    public static boolean valuable(String value) {
        return value != null && value.trim().length() > 0;
    }
}
