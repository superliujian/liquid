package liquid.metadata;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/30/13
 * Time: 10:31 PM
 */
public enum ContainerCap {
    FT20(0, "20`GP"),
    FT40(1, "40`HQ");

    private final int type;

    private final String name;

    private ContainerCap(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static Map<Integer, String> toMap() {
        Map<Integer, String> types = new HashMap<Integer, String>();
        ContainerCap[] typeArray = values();
        for (ContainerCap type : typeArray) {
            types.put(type.type, type.name);
        }
        return types;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("ContainerCap{");
        sb.append("type=").append(type);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
