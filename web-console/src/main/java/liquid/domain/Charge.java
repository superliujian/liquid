package liquid.domain;

import javax.validation.constraints.NotNull;

/**
 * Created by redbrick9 on 6/9/14.
 */
public class Charge extends BaseIdObject {
    private Long routeId;
    private Long legId;
    private Long serviceSubtypeId;

    @NotNull
    private Long serviceProviderId;
    private Integer way;
    private Integer currency;

    @NotNull
    private Long unitPrice;
    private String taskId;

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

    public Long getServiceSubtypeId() {
        return serviceSubtypeId;
    }

    public void setServiceSubtypeId(Long serviceSubtypeId) {
        this.serviceSubtypeId = serviceSubtypeId;
    }

    public Long getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(Long serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public Integer getWay() {
        return way;
    }

    public void setWay(Integer way) {
        this.way = way;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public Long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Long unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Charge{");
        sb.append("super=").append(super.toString());
        sb.append(", routeId=").append(routeId);
        sb.append(", legId=").append(legId);
        sb.append(", serviceSubtypeId=").append(serviceSubtypeId);
        sb.append(", serviceProviderId=").append(serviceProviderId);
        sb.append(", way=").append(way);
        sb.append(", currency=").append(currency);
        sb.append(", unitPrice=").append(unitPrice);
        sb.append(", taskId='").append(taskId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
