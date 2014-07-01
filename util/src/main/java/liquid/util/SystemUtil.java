package liquid.util;

import java.util.Properties;
import java.util.Set;

/**
 * Created by redbrick9 on 6/16/14.
 */
public final class SystemUtil {
    private SystemUtil() { }

    public static void print() {
        StringBuilder builder = new StringBuilder();

        Properties properties = System.getProperties();
        Set keySet = properties.keySet();

        for (Object key : keySet) {
            builder.append(String.format("%s\t:%s\n", key, properties.get(key)));
        }

        System.out.println(builder.toString());
    }
}
