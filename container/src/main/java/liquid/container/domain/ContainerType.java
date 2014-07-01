package liquid.container.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/30/13
 * Time: 10:26 PM
 */
public enum ContainerType {
    RAIL(0, "container.rail"),
    //    MARINE(1, "container.marine"),
    SELF(2, "container.owned");

    private final int type;

    private final String i18nKey;

    private ContainerType(int type, String i18nKey) {
        this.type = type;
        this.i18nKey = i18nKey;
    }

    public static Map<Integer, String> toMap() {
        Map<Integer, String> types = new HashMap<Integer, String>();
        ContainerType[] typeArray = values();
        for (ContainerType type : typeArray) {
            types.put(type.type, type.i18nKey);
        }
        return types;
    }

    public int getType() {
        return type;
    }

    public String getI18nKey() {
        return i18nKey;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("ContainerType{");
        sb.append("type=").append(type);
        sb.append(", i18nKey='").append(i18nKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
