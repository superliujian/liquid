package liquid.domain;

/**
 * Created by redbrick9 on 6/10/14.
 */
public abstract class BaseIdObject {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BaseObject{");
        sb.append("id=").append(id);
        sb.append('}');
        return sb.toString();
    }
}

