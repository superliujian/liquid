package liquid.shipping.web.domain;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/19/13
 * Time: 2:21 PM
 */
public class RailContainer {
    private long railContainerId;

    private String bicCode;

    private boolean batch;

    public long getRailContainerId() {
        return railContainerId;
    }

    public void setRailContainerId(long railContainerId) {
        this.railContainerId = railContainerId;
    }

    public String getBicCode() {
        return bicCode;
    }

    public void setBicCode(String bicCode) {
        this.bicCode = bicCode;
    }

    public boolean isBatch() {
        return batch;
    }

    public void setBatch(boolean batch) {
        this.batch = batch;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("RailContainerDto{");
        sb.append("railContainerId=").append(railContainerId);
        sb.append(", bicCode='").append(bicCode).append('\'');
        sb.append(", batch=").append(batch);
        sb.append('}');
        return sb.toString();
    }
}
