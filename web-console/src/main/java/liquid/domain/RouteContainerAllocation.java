package liquid.domain;

/**
 * Created by redbrick9 on 5/20/14.
 */
public class RouteContainerAllocation {
    private long allocationId;

    private long routeId;

    private String typeNameKey;

    private String subtypeName;

    private String bicCode;

    public RouteContainerAllocation() {}

    public long getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(long allocationId) {
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
}
