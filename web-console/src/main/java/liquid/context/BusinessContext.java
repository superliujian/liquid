package liquid.context;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/30/13
 * Time: 2:12 PM
 */
public class BusinessContext {

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("BusinessContext{");
        sb.append("username='").append(username).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
