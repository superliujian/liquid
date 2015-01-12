package liquid.web.domain;

/**
 * Created by Tao Ma on 1/12/15.
 */
public class PageForm {
    /**
     * Path of a URL.
     */
    private String path;

    /**
     * The number of page.
     */
    private int number;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=PageForm");
        sb.append(", path='").append(path).append('\'');
        sb.append(", number=").append(number);
        sb.append('}');
        return sb.toString();
    }
}
