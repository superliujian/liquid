package liquid.domain;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/5/13
 * Time: 10:35 AM
 */
public enum LocationType {
    CITY(0, "city"),
    STATION(1, "station"),
    PORT(2, "port"),
    YARD(3, "yard");

    private final int type;

    private final String i18nKey;

    private LocationType(int type, String i18nKey) {
        this.type = type;
        this.i18nKey = i18nKey;
    }

    public static LocationType valueOf(int type) {
        switch (type) {
            case 0:
                return CITY;
            case 1:
                return STATION;
            case 2:
                return PORT;
            case 3:
                return YARD;
            default:
                break;
        }
        throw new RuntimeException("input transMode is illegal.");
    }

    public int getType() {
        return type;
    }

    public String getI18nKey() {
        return i18nKey;
    }
}
