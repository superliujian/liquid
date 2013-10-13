package liquid.persistence.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/12/13
 * Time: 10:22 AM
 */
@Entity(name = "RAIL_CONTAINER")
public class RailContainer extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "LEG_ID")
    private Leg leg;

    @OneToOne
    @JoinColumn(name = "SC_ID")
    private ShippingContainer sc;

    @Transient
    private String loadingTocStr;

    /**
     * Time of Completion of Loading
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LOADING_TOC")
    private Date loadingToc;

    @Transient
    private String stationToaStr;

    /**
     * Time of Arrival at station.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STATION_TOA")
    private Date stationToa;

    @Transient
    private boolean batch;

    @Column(name = "TRANS_PLAN_NO")
    private String transPlanNo;

    /**
     * Estimated time of shipping
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ETS")
    private Date ets;

    @Transient
    private String etsStr;

    /**
     * Actual time of shipping
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ATS")
    private Date ats;

    @Transient
    private String atsStr;

    /**
     * Actual time of arrival
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ATA")
    private Date ata;

    @Transient
    private String ataStr;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Leg getLeg() {
        return leg;
    }

    public void setLeg(Leg leg) {
        this.leg = leg;
    }

    public ShippingContainer getSc() {
        return sc;
    }

    public void setSc(ShippingContainer sc) {
        this.sc = sc;
    }

    public String getLoadingTocStr() {
        return loadingTocStr;
    }

    public void setLoadingTocStr(String loadingTocStr) {
        this.loadingTocStr = loadingTocStr;
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

    public boolean isBatch() {
        return batch;
    }

    public void setBatch(boolean batch) {
        this.batch = batch;
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

    public String getAtaStr() {
        return ataStr;
    }

    public void setAtaStr(String ataStr) {
        this.ataStr = ataStr;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("RailContainer{");
        sb.append("order=").append(order);
        sb.append(", leg=").append(leg);
        sb.append(", sc=").append(sc);
        sb.append(", loadingTocStr='").append(loadingTocStr).append('\'');
        sb.append(", loadingToc=").append(loadingToc);
        sb.append(", stationToaStr='").append(stationToaStr).append('\'');
        sb.append(", stationToa=").append(stationToa);
        sb.append(", batch=").append(batch);
        sb.append(", transPlanNo='").append(transPlanNo).append('\'');
        sb.append(", ets=").append(ets);
        sb.append(", etsStr='").append(etsStr).append('\'');
        sb.append(", ats=").append(ats);
        sb.append(", atsStr='").append(atsStr).append('\'');
        sb.append(", ata=").append(ata);
        sb.append(", ataStr='").append(ataStr).append('\'');
        sb.append('}');
        return sb.toString();
    }
}