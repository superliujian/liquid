package liquid.persistence.domain;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/1/13
 * Time: 9:40 PM
 */
public enum PlanningStatus {
    NULL(0, ""),
    ADDED(1, "order.status.saved"),
    FULL(2, "order.status.full");

    private final int value;

    private final String i18nKey;

    private PlanningStatus(int value, String i18nKey) {
        this.value = value;
        this.i18nKey = i18nKey;
    }

    public int getValue() {
        return value;
    }

    public String getI18nKey() {
        return i18nKey;
    }
}
