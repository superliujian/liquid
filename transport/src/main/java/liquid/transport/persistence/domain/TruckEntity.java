package liquid.transport.persistence.domain;

import liquid.persistence.domain.BaseUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * Created by Tao Ma on 12/31/14.
 */
@Entity(name = "TSP_TRUCK")
public class TruckEntity extends BaseUpdateEntity {
    @ManyToOne
    @JoinColumn(name = "SHIPMENT_ID")
    private ShipmentEntity shipment;

    @Column(name = "SP_ID")
    private Long serviceProviderId;

    @Column(name = "PICKING_AT")
    private Date pickingAt;

    @Column(name = "LICENSE_PLATE")
    private String licensePlate;

    @Column(name = "DRIVER")
    private String driver;

    public ShipmentEntity getShipment() {
        return shipment;
    }

    public void setShipment(ShipmentEntity shipment) {
        this.shipment = shipment;
    }

    public Date getPickingAt() {
        return pickingAt;
    }

    public void setPickingAt(Date pickingAt) {
        this.pickingAt = pickingAt;
    }
    
    public Long getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(Long serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
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
}
