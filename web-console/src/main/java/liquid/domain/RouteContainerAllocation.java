package liquid.domain;

/**
 * Created by redbrick9 on 5/20/14.
 */
public class RouteContainerAllocation {
    private Long allocationId;

    private long routeId;

    private String typeNameKey;

    private String subtypeName;

    /**
     * For the rail container.
     */
    private String bicCode;

    /**
     * For the non rail container.
     */
    private long containerId;

    private String owner;

    private String yard;

    public RouteContainerAllocation() {}

    public Long getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(Long allocationId) {
        this.allocationId = allocationId;
    }

    public long getRouteId() {
        return routeId;
    }

    public void setRouteId(long routeId) {
        this.routeId = routeId;
    }

    public String getTypeNameKey() {
        return typeNameKey;
    }

    public void setTypeNameKey(String typeNameKey) {
        this.typeNameKey = typeNameKey;
    }

    public String getSubtypeName() {
        return subtypeName;
    }

    public void setSubtypeName(String subtypeName) {
        this.subtypeName = subtypeName;
    }

    public String getBicCode() {
        return bicCode;
    }

    public void setBicCode(String bicCode) {
        this.bicCode = bicCode;
    }

    public long getContainerId() {
        return containerId;
    }

    public void setContainerId(long containerId) {
        this.containerId = containerId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getYard() {
        return yard;
    }

    public void setYard(String yard) {
        this.yard = yard;
    }
}
