package liquid.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by Tao Ma on 12/31/14.
 */
public class Truck {
    private Long id;
    private Long shipmentId;
    private Long serviceProviderId;
    private String serviceProviderName;

    @NotNull
    @NotEmpty
    private String pickingAt;

    @NotNull
    @NotEmpty
    private String licensePlate;

    @NotNull
    @NotEmpty
    private String driver;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Long shipmentId) {
        this.shipmentId = shipmentId;
    }

    public Long getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(Long serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public String getServiceProviderName() {
        return serviceProviderName;
    }

    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
    }

    public String getPickingAt() {
        return pickingAt;
    }

    public void setPickingAt(String pickingAt) {
        this.pickingAt = pickingAt;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=Truck");
        sb.append(", id=").append(id);
        sb.append(", shipmentId=").append(shipmentId);
        sb.append(", serviceProviderId=").append(serviceProviderId);
        sb.append(", serviceProviderName='").append(serviceProviderName).append('\'');
        sb.append(", pickingAt='").append(pickingAt).append('\'');
        sb.append(", licensePlate='").append(licensePlate).append('\'');
        sb.append(", driver='").append(driver).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
