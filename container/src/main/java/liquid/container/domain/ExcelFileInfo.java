package liquid.container.domain;

import java.util.Date;

/**
 * Created by redbrick9 on 6/21/14.
 */
public class ExcelFileInfo {
    public enum State {UPLOADED, IMPORTING, IMPORTED}

    private String name;
    private Date modifiedDate;
    private State state;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
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
