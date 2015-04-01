package liquid.model;

/**
 * Created by Tao Ma on 4/1/15.
 */
public enum AlertType implements Css {
    SUCCESS,
    INFO,
    WARNING,
    DANGER;

    private static String CSS_PREFIX = "alert-";

    @Override
    public String css() {
        return CSS_PREFIX + name().toLowerCase();
    }
}
