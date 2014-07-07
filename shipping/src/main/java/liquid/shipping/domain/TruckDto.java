package liquid.shipping.domain;

import liquid.util.DatePattern;
import liquid.validation.constraints.DateFormat;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/18/13
 * Time: 11:53 PM
 */
public class TruckDto {

    private long railContainerId;

    private String bicCode;

    private long fleetId;

    @NotNull
    @NotEmpty
    private String trucker;

    @NotNull
    @NotEmpty
    private String plateNo;

    @DateFormat(DatePattern.UNTIL_MINUTE)
    private String loadingToc;

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

    public long getFleetId() {
        return fleetId;
    }

    public void setFleetId(long fleetId) {
        this.fleetId = fleetId;
    }

    public String getTrucker() {
        return trucker;
    }

    public void setTrucker(String trucker) {
        this.trucker = trucker;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public String getLoadingToc() {
        return loadingToc;
    }

    public void setLoadingToc(String loadingToc) {
        this.loadingToc = loadingToc;
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
        sb.append("TruckDto{");
        sb.append("railContainerId=").append(railContainerId);
        sb.append(", bicCode='").append(bicCode).append('\'');
        sb.append(", fleetId=").append(fleetId);
        sb.append(", trucker='").append(trucker).append('\'');
        sb.append(", plateNo='").append(plateNo).append('\'');
        sb.append(", loadingToc='").append(loadingToc).append('\'');
        sb.append(", batch=").append(batch);
        sb.append('}');
        return sb.toString();
    }
}
