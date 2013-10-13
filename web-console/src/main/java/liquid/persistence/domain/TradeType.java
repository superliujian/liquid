package liquid.persistence.domain;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/29/13
 * Time: 10:24 PM
 */
public enum TradeType {
    DOMESTIC(0, "trade.domestic"),
    FOREIGN(1, "trade.foreign");

    private final int type;

    private final String i18nKey;

    private TradeType(int type, String i18nKey) {
        this.type = type;
        this.i18nKey = i18nKey;
    }

    public int getType() {
        return type;
    }

    public String getI18nKey() {
        return i18nKey;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("TradeType{");
        sb.append("type=").append(type);
        sb.append(", i18nKey='").append(i18nKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
