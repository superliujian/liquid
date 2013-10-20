package liquid.metadata;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/2/13
 * Time: 4:44 PM
 */
public enum SpType {
    BARGE(0, "sp.barge"),
    VESSEL(1, "sp.vessel");

    private final int type;

    private final String i18nKey;

    private SpType(int type, String i18nKey) {
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
