package liquid.order.model;

/**
 * Created by Tao Ma on 2/25/15.
 */
public class TransportedContainer {
    private Long id;
    private String bicCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBicCode() {
        return bicCode;
    }

    public void setBicCode(String bicCode) {
        this.bicCode = bicCode;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=TransportedContainer");
        sb.append(", id=").append(id);
        sb.append(", bicCode='").append(bicCode).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
