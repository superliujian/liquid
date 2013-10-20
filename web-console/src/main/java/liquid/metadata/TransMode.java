package liquid.metadata;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/1/13
 * Time: 11:59 AM
 */
public enum TransMode {
    RAIL(0, "rail"),
    BARGE(1, "barge"),
    VESSEL(2, "vessel");

    private final int type;

    private final String i18nKey;

    private TransMode(int type, String i18nKey) {
        this.type = type;
        this.i18nKey = i18nKey;
    }

    public static TransMode valueOf(int type) {
        switch (type) {
            case 0:
                return RAIL;
            case 1:
                return BARGE;
            case 2:
                return VESSEL;
            default:
                break;
        }
        throw new RuntimeException("input transMode is illegal.");
    }

    public static Map<Integer, String> toMap() {
        Map<Integer, String> types = new HashMap<Integer, String>();
        TransMode[] modeArray = values();
        for (TransMode mode : modeArray) {
            types.put(mode.type, mode.i18nKey);
        }
        return types;
    }

    public int getType() {
        return type;
    }

    public String getI18nKey() {
        return i18nKey;
    }

    public String toPath() {
        return toString().toLowerCase();
    }
}
