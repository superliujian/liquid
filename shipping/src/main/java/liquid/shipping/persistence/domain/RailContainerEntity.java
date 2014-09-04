package liquid.shipping.persistence.domain;

import liquid.persistence.domain.ServiceProviderEntity;
import liquid.util.DatePattern;
import liquid.validation.constraints.DateFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/12/13
 * Time: 10:22 AM
 */
@Entity(name = "SHP_RAIL_CONTAINER")
public class RailContainerEntity extends BaseLegContainer {
    @ManyToOne
    @JoinColumn(name = "FLEET_ID")
    private ServiceProviderEntity fleet;

    @Column(name = "TRUCKER")
    private String trucker;

    @Column(name = "PLATE_NO")
    private String plateNo;

    /**
     * Time of Completion of Loading
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LOADING_TOC")
    private Date loadingToc;

    @DateFormat(DatePattern.UNTIL_MINUTE)
    @Transient
    private String stationToaStr;

    /**
     * Time of Arrival at rail yard.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STATION_TOA")
    private Date stationToa;

    @Column(name = "TRANS_PLAN_NO")
    private String transPlanNo;

    /**
     * Estimated time of shipping
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ETS")
    private Date ets;

    @DateFormat(DatePattern.UNTIL_DAY)
    @Transient
    private String etsStr;

    /**
     * Actual time of shipping
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ATS")
    private Date ats;

    @DateFormat(DatePattern.UNTIL_MINUTE)
    @Transient
    private String atsStr;

    /**
     * Actual time of arrival
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ATA")
    private Date ata;

    @Transient
    private boolean batch;

    public ServiceProviderEntity getFleet() {
        return fleet;
    }

    public void setFleet(ServiceProviderEntity fleet) {
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

    public Date getLoadingToc() {
        return loadingToc;
    }

    public void setLoadingToc(Date loadingToc) {
        this.loadingToc = loadingToc;
    }

    public String getStationToaStr() {
        return stationToaStr;
    }

    public void setStationToaStr(String stationToaStr) {
        this.stationToaStr = stationToaStr;
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

    public String getEtsStr() {
        return etsStr;
    }

    public void setEtsStr(String etsStr) {
        this.etsStr = etsStr;
    }

    public Date getAts() {

        return ats;
    }

    public void setAts(Date ats) {
        this.ats = ats;
    }

    public String getAtsStr() {
        return atsStr;
    }

    public void setAtsStr(String atsStr) {
        this.atsStr = atsStr;
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