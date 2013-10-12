package liquid.utils;

import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/12/13
 * Time: 9:18 PM
 */
public class CollectionUtils {
    private CollectionUtils() {}

    public static <T> T tryToGet2ndElement(List<T> list) {
        int size = list.size();
        long id = 0;
        if (size < 2) {
            return list.get(0);
        } else {
            return list.get(1);
        }
    }
}
