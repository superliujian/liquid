package liquid.transport.web.domain;

import liquid.order.persistence.domain.OrderEntity;
import liquid.persistence.domain.ServiceProviderEntity;
import liquid.transport.persistence.domain.LegEntity;
import liquid.transport.persistence.domain.RailContainerEntity;
import liquid.transport.persistence.domain.TransportEntity;
import liquid.util.DateUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by redbrick9 on 9/11/14.
 */
public class RailTransport extends TransportBase {
    private Long id;

    public Long orderId;

    private Long routeId;

    private Long legId;

    private Long fleetId;

    private String trucker;

    private String plateNo;

    private String loadedAt;

    private String arrivedStationAt;

    private String planNo;

    private String plannedShippedAt;

    private String actualShippedAt;

    public static RailContainerEntity toEntity(RailTransport transport) {
        RailContainerEntity entity = new RailContainerEntity();
        entity.setId(transport.getId());
        entity.setOrder(OrderEntity.newInstance(OrderEntity.class, transport.getOrderId()));
        entity.setRoute(TransportEntity.newInstance(TransportEntity.class, transport.getRouteId()));
        entity.setLeg(LegEntity.newInstance(LegEntity.class, transport.getLegId()));
        entity.setFleet(ServiceProviderEntity.newInstance(ServiceProviderEntity.class, transport.getFleetId()));
        entity.setTrucker(transport.getTrucker());
        entity.setPlateNo(transport.getPlateNo());
        entity.setLoadingToc(DateUtil.dateOf(transport.getLoadedAt()));
        entity.setStationToa(DateUtil.dateOf(transport.getArrivedStationAt()));
        entity.setTransPlanNo(transport.getPlanNo());
        entity.setEts(DateUtil.dateOf(transport.getPlannedShippedAt()));
        entity.setAts(DateUtil.dateOf(transport.getActualShippedAt()));
        return entity;
    }

    public RailContainerEntity toEntity() {
        return toEntity(this);
    }

    public static Collection<RailContainerEntity> toEntities(RailTransport[] transportSet) {
        List<RailContainerEntity> entities = new ArrayList<RailContainerEntity>();
        for (RailTransport transport : transportSet) {
            RailContainerEntity entity = toEntity(transport);
            entities.add(entity);
        }
        return entities;
    }

    public static RailTransport valueOf(RailContainerEntity entity) {
        RailTransport value = new RailTransport();
        value.setId(entity.getId());
        value.setOrderId(entity.getOrder().getId());
        value.setRouteId(entity.getRoute().getId());
        value.setLegId(entity.getLeg().getId());
        value.setSource(entity.getLeg().getSrcLoc().getName());
        value.setDestination(entity.getLeg().getDstLoc().getName());
        value.setFleetId(entity.getFleet().getId());
        value.setTrucker(entity.getTrucker());
        value.setPlateNo(entity.getPlateNo());
        value.setLoadedAt(DateUtil.stringOf(entity.getLoadingToc()));
        value.setArrivedStationAt(DateUtil.stringOf(entity.getStationToa()));
        value.setPlanNo(entity.getTransPlanNo());
        value.setPlannedShippedAt(DateUtil.stringOf(entity.getEts()));
        value.setActualShippedAt(DateUtil.stringOf(entity.getAts()));
        return value;
    }

    public static RailTransport[] valueOf(Collection<RailContainerEntity> entities) {
        RailTransport[] railTransport = new RailTransport[entities.size()];
        int i = 0;
        for (RailContainerEntity entity : entities) {
            railTransport[i] = RailTransport.valueOf(entity);
            i++;
        }
        return railTransport;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public Long getLegId() {
        return legId;
    }

    public void setLegId(Long legId) {
        this.legId = legId;
    }

    public Long getFleetId() {
        return fleetId;
    }

    public void setFleetId(Long fleetId) {
        this.fleetId = fleetId;
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

    public String getLoadedAt() {
        return loadedAt;
    }

    public void setLoadedAt(String loadedAt) {
        this.loadedAt = loadedAt;
    }

    public String getArrivedStationAt() {
        return arrivedStationAt;
    }

    public void setArrivedStationAt(String arrivedStationAt) {
        this.arrivedStationAt = arrivedStationAt;
    }

    public String getPlanNo() {
        return planNo;
    }

    public void setPlanNo(String planNo) {
        this.planNo = planNo;
    }

    public String getPlannedShippedAt() {
        return plannedShippedAt;
    }

    public void setPlannedShippedAt(String plannedShippedAt) {
        this.plannedShippedAt = plannedShippedAt;
    }

    public String getActualShippedAt() {
        return actualShippedAt;
    }

    public void setActualShippedAt(String actualShippedAt) {
        this.actualShippedAt = actualShippedAt;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RailTransport{");
        sb.append("routeId=").append(routeId);
        sb.append(", fleetId=").append(fleetId);
        sb.append(", trucker='").append(trucker).append('\'');
        sb.append(", plateNo='").append(plateNo).append('\'');
        sb.append(", loadedAt='").append(loadedAt).append('\'');
        sb.append(", arrivedStationAt='").append(arrivedStationAt).append('\'');
        sb.append(", planNo='").append(planNo).append('\'');
        sb.append(", plannedShippedAt='").append(plannedShippedAt).append('\'');
        sb.append(", actualShippedAt='").append(actualShippedAt).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
