package liquid.web.domain;

/**
 * Created by Tao Ma on 11/30/14.
 */
public class Location {
    private Long id;
    private String name;
    private Integer type;
    private String typeText;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTypeText() {
        return typeText;
    }

    public void setTypeText(String typeText) {
        this.typeText = typeText;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=Location");
        sb.append(", id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", type=").append(type);
        sb.append(", typeText='").append(typeText).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
