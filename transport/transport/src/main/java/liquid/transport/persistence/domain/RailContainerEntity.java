package liquid.transport.persistence.domain;

import liquid.operation.domain.ServiceProvider;

import javax.persistence.*;
import java.util.Date;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/12/13
 * Time: 10:22 AM
 */
@Entity(name = "TSP_RAIL_CONTAINER")
public class RailContainerEntity extends BaseLegContainer {
    @OneToOne
    @JoinColumn(name = "TRUCK_ID")
    private TruckEntity truck;

    @ManyToOne
    @JoinColumn(name = "FLEET_ID")
    private ServiceProvider fleet;

    @Column(name = "TRUCKER")
    private String trucker;

    @Column(name = "PLATE_NO")
    private String plateNo;

    @Column(name = "RELEASED_AT")
    private Date releasedAt;

    /**
     * Time of Completion of Loading
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LOADING_TOC")
    private Date loadingToc;

    /**
     * Time of Arrival at rail yard.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STATION_TOA")
    private Date stationToa;

    @Column(name = "TRANS_PLAN_NO")
    private String transPlanNo;

    /**
     * Estimated time of transport
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ETS")
    private Date ets;

    /**
     * Actual time of transport
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ATS")
    private Date ats;

    /**
     * Actual time of arrival
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ATA")
    private Date ata;

    @Transient
    private boolean batch;

    public TruckEntity getTruck() {
        return truck;
    }

    public void setTruck(TruckEntity truck) {
        this.truck = truck;
    }

    public ServiceProvider getFleet() {
        return fleet;
    }

    public void setFleet(ServiceProvider fleet) {
        this.fleet = fleet;
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

    public Date getReleasedAt() {
        return releasedAt;
    }

    public void setReleasedAt(Date releasedAt) {
        this.releasedAt = releasedAt;
    }

    public Date getLoadingToc() {
        return loadingToc;
    }

    public void setLoadingToc(Date loadingToc) {
        this.loadingToc = loadingToc;
    }

    public Date getStationToa() {
        return stationToa;
    }

    public void setStationToa(Date stationToa) {
        this.stationToa = stationToa;
    }

    public String getTransPlanNo() {
        return transPlanNo;
    }

    public void setTransPlanNo(String transPlanNo) {
        this.transPlanNo = transPlanNo;
    }

    public Date getEts() {
        return ets;
    }

    public void setEts(Date ets) {
        this.ets = ets;
    }

    public Date getAts() {
        return ats;
    }

    public void setAts(Date ats) {
        this.ats = ats;
    }

    public Date getAta() {
        return ata;
    }

    public void setAta(Date ata) {
        this.ata = ata;
    }

    public boolean isBatch() {
        return batch;
    }

    public void setBatch(boolean batch) {
        this.batch = batch;
    }
}
