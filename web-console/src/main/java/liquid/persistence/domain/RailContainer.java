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

    @Transient
    private boolean batch;

    /**
     * Time of Arrival at station.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STATION_TOA")
    private Date stationToa;

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
        sb.append(", batch=").append(batch);
        sb.append(", stationToa=").append(stationToa);
        sb.append('}');
        return sb.toString();
    }
}
