package liquid.persistence.domain;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/28/13
 * Time: 11:24 PM
 */
public class Trade {
    private int type;
    private String name;
    private String i18nKey;

    public Trade(int type, String i18nKey) {
        this.type = type;
        this.i18nKey = i18nKey;
    }

    public static Trade[] defaultTrades() {
        return new Trade[]{
                new Trade(0, "trade.domestic"),
                new Trade(1, "trade.foreign")
        };
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getI18nKey() {
        return i18nKey;
    }

    public void setI18nKey(String i18nKey) {
        this.i18nKey = i18nKey;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Trade{");
        sb.append("type=").append(type);
        sb.append(", name='").append(name).append('\'');
        sb.append(", i18nKey='").append(i18nKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
