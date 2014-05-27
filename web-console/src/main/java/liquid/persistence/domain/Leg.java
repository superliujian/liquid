package liquid.persistence.domain;

import javax.persistence.*;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/10/13
 * Time: 8:18 PM
 */
@Entity(name = "LEG")
public class Leg extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "ROUTE_ID")
    private Route route;

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
    private Leg prev;

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
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

    public Leg getPrev() {
        return prev;
    }

    public void setPrev(Leg prev) {
        this.prev = prev;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("Leg{");
        sb.append("route=").append(route);
        sb.append(", head=").append(head);
        sb.append(", transMode=").append(transMode);
        sb.append(", sp=").append(sp);
        sb.append(", spId=").append(spId);
        sb.append(", srcLoc=").append(srcLoc);
        sb.append(", dstLoc=").append(dstLoc);
        sb.append(", srcLocId=").append(srcLocId);
        sb.append(", dstLocId=").append(dstLocId);
        sb.append(", prev=").append(prev);
        sb.append('}');
        return sb.toString();
    }
}
