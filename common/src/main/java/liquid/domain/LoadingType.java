package liquid.domain;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/29/13
 * Time: 10:29 PM
 */
public enum LoadingType {
    YARD(0, "loading.yard"),
    TRUCK(1, "loading.truck");

    private final int type;

    private final String i18nKey;

    private LoadingType(int type, String i18nKey) {
        this.type = type;
        this.i18nKey = i18nKey;
    }

    public int getType() {
        return type;
    }

    public String getI18nKey() {
        return i18nKey;
    }

    public static LoadingType valueOf(int type) {
        switch (type) {
            case 0:
                return YARD;
            case 1:
                return TRUCK;
            default:
                throw new IllegalArgumentException(String.format("%s should be from %s and %s.", type, 0, 1));
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("LoadingType{");
        sb.append("type=").append(type);
        sb.append(", i18nKey='").append(i18nKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
