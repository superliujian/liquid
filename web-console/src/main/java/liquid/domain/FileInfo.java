package liquid.domain;

/**
 * Created by redbrick9 on 6/21/14.
 */
public class FileInfo {
    public enum State {UPLOADED, IMPORTING, IMPORTED}

    private String name;
    private String modifiedDate;
    private State state;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FileInfo{");
        sb.append("super=").append(super.toString());
        sb.append(", name='").append(name).append('\'');
        sb.append(", modifiedDate='").append(modifiedDate).append('\'');
        sb.append(", state=").append(state);
        sb.append('}');
        return sb.toString();
    }
}
