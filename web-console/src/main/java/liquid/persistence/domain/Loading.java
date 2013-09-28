package liquid.persistence.domain;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/28/13
 * Time: 11:49 PM
 */
public class Loading {
    private int type;
    private String name;
    private String i18nKey;

    public Loading(int type, String i18nKey) {
        this.type = type;
        this.i18nKey = i18nKey;
    }

    public static Loading[] defaultLoadings() {
        return new Loading[]{
                new Loading(0, "loading.yard"),
                new Loading(1, "loading.truck")
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
}
