package liquid.util;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/17/13
 * Time: 11:51 PM
 */
public enum DatePattern {
    UNTIL_SECOND("yyyy-MM-dd HH:mm:ss"),
    UNTIL_MINUTE("yyyy-MM-dd HH:mm"),
    UNTIL_DAY("yyyy-MM-dd");

    private final String pattern;

    private DatePattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
}
