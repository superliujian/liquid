package liquid.purchase.web.domain;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/7/13
 * Time: 3:30 PM
 */
public enum ChargeStatus {
    UNPAID(0, "charge.status.unpaid"),
    PAID(1, "charge.status.paid");

    private final int value;

    private final String i18nKey;

    private ChargeStatus(int value, String i18nKey) {
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
