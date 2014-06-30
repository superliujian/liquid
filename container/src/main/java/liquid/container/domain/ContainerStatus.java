package liquid.container.domain;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/3/13
 * Time: 4:26 PM
 */
public enum ContainerStatus {
    IN_STOCK(0, "container.in.stock"),
    ALLOCATED(1, "container.allocated"),
    LOADED(2, "container.loaded");

    private final int value;

    private final String i18nKey;

    private ContainerStatus(int value, String i18nKey) {
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
