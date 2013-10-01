package liquid.persistence.domain;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/1/13
 * Time: 11:59 AM
 */
public enum TransMode {
    RAILWAY(0, "transportation.railway"),
    BARGE(1, "transportation.barge"),
    VESSEL(2, "transportation.vessel");

    private final int type;

    private final String i18nKey;

    private TransMode(int type, String i18nKey) {
        this.type = type;
        this.i18nKey = i18nKey;
    }

    public static TransMode valueOf(int type) {
        switch (type) {
            case 0:
                return RAILWAY;
            case 1:
                return BARGE;
            case 2:
                return VESSEL;
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
