package liquid.persistence.domain;

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

    public int getType() {
        return type;
    }

    public String getI18nKey() {
        return i18nKey;
    }
}
