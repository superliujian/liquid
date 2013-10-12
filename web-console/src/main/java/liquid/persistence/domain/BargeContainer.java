package liquid.persistence.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/12/13
 * Time: 2:42 PM
 */
@Entity(name = "BARGE_CONTAINER")
public class BargeContainer extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "LEG_ID")
    private Leg leg;

    @OneToOne
    @JoinColumn(name = "SC_ID")
    private ShippingContainer sc;

    /**
     * Bill of Lading No.
     */
    @Column(name = "BOL_NO")
    private String bolNo;

    @Column(name = "SLOT")
    private String slot;

    /**
     * Estimated time of shipping
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ETS")
    private Date ets;

    @Transient
    private String etsStr;

    @Transient
    private boolean batch;

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

    public String getBolNo() {
        return bolNo;
    }

    public void setBolNo(String bolNo) {
        this.bolNo = bolNo;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
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

    public boolean isBatch() {
        return batch;
    }

    public void setBatch(boolean batch) {
        this.batch = batch;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("BargeContainer{");
        sb.append("order=").append(order);
        sb.append(", leg=").append(leg);
        sb.append(", sc=").append(sc);
        sb.append(", bolNo='").append(bolNo).append('\'');
        sb.append(", slot='").append(slot).append('\'');
        sb.append(", ets=").append(ets);
        sb.append(", etsStr='").append(etsStr).append('\'');
        sb.append(", batch=").append(batch);
        sb.append('}');
        return sb.toString();
    }
}
