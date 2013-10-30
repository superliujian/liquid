package liquid.metadata;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/30/13
 * Time: 12:56 PM
 */
public enum IncomeType {
    ORDER(0, "income.type.order"),
    OTHER(1, "income.type.other");

    private final int type;

    private final String i18nKey;

    private IncomeType(int type, String i18nKey) {
        this.type = type;
        this.i18nKey = i18nKey;
    }

    public static Map<Integer, String> toMap() {
        Map<Integer, String> types = new HashMap<Integer, String>();
        IncomeType[] typeArray = values();
        for (IncomeType type : typeArray) {
            types.put(type.type, type.i18nKey);
        }
        types.remove(0);
        return types;
    }

    public int getType() {
        return type;
    }

    public String getI18nKey() {
        return i18nKey;
    }
}
