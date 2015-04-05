package liquid.transport.persistence.domain;

import liquid.operation.domain.ServiceProvider;
import liquid.persistence.domain.BaseIdEntity;
import liquid.operation.domain.Location;

import javax.persistence.*;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/10/13
 * Time: 8:18 PM
 */
@Entity(name = "TSP_LEG")
public class LegEntity extends BaseIdEntity {

    @ManyToOne
    @JoinColumn(name = "SHIPMENT_ID")
    private ShipmentEntity shipment;

    @Column(name = "IS_HEAD")
    private Boolean head;

    @Column(name = "TRANS_MODE")
    private int transMode;

    @ManyToOne
    @JoinColumn(name = "SP_ID")
    private ServiceProvider sp;

    @ManyToOne
    @JoinColumn(name = "SRC_LOC_ID")
    private Location srcLoc;

    @ManyToOne
    @JoinColumn(name = "DST_LOC_ID")
    private Location dstLoc;

    @OneToOne
    @JoinColumn(name = "PREV")
    private LegEntity prev;

    public ShipmentEntity getShipment() {
        return shipment;
    }

    public void setShipment(ShipmentEntity shipment) {
        this.shipment = shipment;
    }

    public Boolean isHead() {
        return head;
    }

    public void setHead(Boolean head) {
        this.head = head;
    }

    public int getTransMode() {
        return transMode;
    }

    public void setTransMode(int transMode) {
        this.transMode = transMode;
    }

    public ServiceProvider getSp() {
        return sp;
    }

    public void setSp(ServiceProvider sp) {
        this.sp = sp;
    }

    public Location getSrcLoc() {
        return srcLoc;
    }

    public void setSrcLoc(Location srcLoc) {
        this.srcLoc = srcLoc;
    }

    public Location getDstLoc() {
        return dstLoc;
    }

    public void setDstLoc(Location dstLoc) {
        this.dstLoc = dstLoc;
    }

    public LegEntity getPrev() {
        return prev;
    }

    public void setPrev(LegEntity prev) {
        this.prev = prev;
    }
}
