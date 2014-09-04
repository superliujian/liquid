package liquid.shipping.persistence.domain;

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
@Entity(name = "SHP_LEG")
public class LegEntity extends BaseUpdateEntity {

    @ManyToOne
    @JoinColumn(name = "ROUTE_ID")
    private RouteEntity route;

    @Column(name = "IS_HEAD")
    private boolean head;

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

    @Transient
    private long srcLocId;

    @Transient
    private long dstLocId;

    @OneToOne
    @JoinColumn(name = "PREV")
    private LegEntity prev;

    public RouteEntity getRoute() {
        return route;
    }

    public void setRoute(RouteEntity route) {
        this.route = route;
    }

    public boolean isHead() {
        return head;
    }

    public void setHead(boolean head) {
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

    public long getSrcLocId() {
        return srcLocId;
    }

    public void setSrcLocId(long srcLocId) {
        this.srcLocId = srcLocId;
    }

    public long getDstLocId() {
        return dstLocId;
    }

    public void setDstLocId(long dstLocId) {
        this.dstLocId = dstLocId;
    }

    public LegEntity getPrev() {
        return prev;
    }

    public void setPrev(LegEntity prev) {
        this.prev = prev;
    }
}
