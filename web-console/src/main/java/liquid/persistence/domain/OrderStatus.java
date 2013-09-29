package liquid.persistence.domain;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/29/13
 * Time: 10:16 PM
 */
public enum OrderStatus {
    NULL(0, ""),
    SAVED(1, "order.status.saved"),
    SUBMITTED(2, "order.status.submitted");

    private final int value;

    private final String i18nKey;

    private OrderStatus(int value, String i18nKey) {
        this.value = value;
        this.i18nKey = i18nKey;
    }

    public int getValue() {
        return value;
    }

    public String getI18nKey() {
        return i18nKey;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("OrderStatus{");
        sb.append("value=").append(value);
        sb.append(", i18nKey='").append(i18nKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
