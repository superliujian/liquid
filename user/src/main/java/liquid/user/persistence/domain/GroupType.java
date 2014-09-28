package liquid.user.persistence.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/6/13
 * Time: 11:40 AM
 */
public enum GroupType {
    SALES("sales", "role.sales"),
    MARKETING("marketing", "role.marketing"),
    OPERATING("operating", "role.operating"),
    CONTAINER("container", "role.container"),
    FIELD("field", "role.field"),
    COMMERCE("commerce", "role.commerce"),
    ADMIN("admin", "role.admin");

    private final String type;

    private final String i18nKey;

    private GroupType(String type, String i18nKey) {
        this.type = type;
        this.i18nKey = i18nKey;
    }

    public static Map<String, String> toMap() {
        Map<String, String> types = new HashMap<String, String>();
        GroupType[] typeArray = values();
        for (GroupType type : typeArray) {
            types.put(type.type, type.i18nKey);
        }
        return types;
    }

    public String getType() {
        return type;
    }

    public String getI18nKey() {
        return i18nKey;
    }
}
