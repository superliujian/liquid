package liquid.container.domain;

/**
 * Created by redbrick9 on 5/24/14.
 */
public class Container {
    private Long id;

    private String bicCode;

    private long ownerId;

    private long subtypeId;

    private long yardId;

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

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public long getSubtypeId() {
        return subtypeId;
    }

    public void setSubtypeId(long subtypeId) {
        this.subtypeId = subtypeId;
    }

    public long getYardId() {
        return yardId;
    }

    public void setYardId(long yardId) {
        this.yardId = yardId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Container{");
        sb.append("super=").append(super.toString());
        sb.append(", id=").append(id);
        sb.append(", bicCode='").append(bicCode).append('\'');
        sb.append(", ownerId=").append(ownerId);
        sb.append(", subtypeId=").append(subtypeId);
        sb.append(", yardId=").append(yardId);
        sb.append('}');
        return sb.toString();
    }
}
