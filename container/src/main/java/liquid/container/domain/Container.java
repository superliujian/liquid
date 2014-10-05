package liquid.container.domain;

/**
 * Created by redbrick9 on 5/24/14.
 */
public class Container {
    private Long id;

    private String bicCode;

    private Long ownerId;

    private String owner;

    private Long subtypeId;

    private String subtype;

    private Long yardId;

    private String yard;

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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Long getSubtypeId() {
        return subtypeId;
    }

    public void setSubtypeId(Long subtypeId) {
        this.subtypeId = subtypeId;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public Long getYardId() {
        return yardId;
    }

    public void setYardId(Long yardId) {
        this.yardId = yardId;
    }

    public String getYard() {
        return yard;
    }

    public void setYard(String yard) {
        this.yard = yard;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Container{");
        sb.append("id=").append(id);
        sb.append(", bicCode='").append(bicCode).append('\'');
        sb.append(", ownerId=").append(ownerId);
        sb.append(", owner='").append(owner).append('\'');
        sb.append(", subtypeId=").append(subtypeId);
        sb.append(", subtype='").append(subtype).append('\'');
        sb.append(", yardId=").append(yardId);
        sb.append(", yard='").append(yard).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
