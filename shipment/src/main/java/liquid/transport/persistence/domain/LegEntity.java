package liquid.transport.persistence.domain;

import liquid.persistence.domain.BaseUpdateEntity;
import liquid.persistence.domain.LocationEntity;
import liquid.persistence.domain.ServiceProviderEntity;

import javax.persistence.*;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/10/13
 * Time: 8:18 PM
 */
@Entity(name = "TSP_LEG")
public class LegEntity extends BaseUpdateEntity {

    @ManyToOne
    @JoinColumn(name = "SHIPMENT_ID")
    private ShipmentEntity shipment;

    @Column(name = "IS_HEAD")
    private Boolean head;

    @Column(name = "TRANS_MODE")
    private int transMode;

    @ManyToOne
    @JoinColumn(name = "SP_ID")
    private ServiceProviderEntity sp;

    @Transient
    private long spId;

    @ManyToOne
    @JoinColumn(name = "SRC_LOC_ID")
    private LocationEntity srcLoc;

    @ManyToOne
    @JoinColumn(name = "DST_LOC_ID")
    private LocationEntity dstLoc;

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

    public ServiceProviderEntity getSp() {
        return sp;
    }

    public void setSp(ServiceProviderEntity sp) {
        this.sp = sp;
    }

    public long getSpId() {
        return spId;
    }

    public void setSpId(long spId) {
        this.spId = spId;
    }

    public LocationEntity getSrcLoc() {
        return srcLoc;
    }

    public void setSrcLoc(LocationEntity srcLoc) {
        this.srcLoc = srcLoc;
    }

    public LocationEntity getDstLoc() {
        return dstLoc;
    }

    public void setDstLoc(LocationEntity dstLoc) {
        this.dstLoc = dstLoc;
    }

    public LegEntity getPrev() {
        return prev;
    }

    public void setPrev(LegEntity prev) {
        this.prev = prev;
    }
}
