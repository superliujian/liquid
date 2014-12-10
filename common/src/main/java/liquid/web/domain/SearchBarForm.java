package liquid.web.domain;

import java.util.Arrays;

/**
 * Created by mat on 10/18/14.
 */
public class SearchBarForm {
    private String action;
    private String types[][];
    private Long id;
    private String type;
    private String text;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String[][] getTypes() {
        return types;
    }

    public void setTypes(String[][] types) {
        this.types = types;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=SearchBarForm");
        sb.append(", action='").append(action).append('\'');
        sb.append(", types=").append(Arrays.toString(types));
        sb.append(", id=").append(id);
        sb.append(", type='").append(type).append('\'');
        sb.append(", text='").append(text).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
