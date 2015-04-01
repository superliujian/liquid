package liquid.purchase.web.domain;

import liquid.order.domain.Order;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by redbrick9 on 6/9/14.
 */
public class Charge {
    private Long id;
    private Long shipmentId;
    private Long legId;
    private Long serviceSubtypeId;
    private Integer containerQuantity;

    @NotNull
    private Long serviceProviderId;
    private String serviceProviderName;
    private Integer way;
    private Integer currency;

    @NotNull
    private BigDecimal unitPrice;
    private BigDecimal totalPrice = BigDecimal.ZERO;
    private BigDecimal totalCny = BigDecimal.ZERO;
    private BigDecimal totalUsd = BigDecimal.ZERO;
    private String taskId;
    private String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Long shipmentId) {
        this.shipmentId = shipmentId;
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

    public Integer getContainerQuantity() {
        return containerQuantity;
    }

    public void setContainerQuantity(Integer containerQuantity) {
        this.containerQuantity = containerQuantity;
    }

    public String getServiceProviderName() {
        return serviceProviderName;
    }

    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
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

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalCny() {
        return totalCny;
    }

    public void setTotalCny(BigDecimal totalCny) {
        this.totalCny = totalCny;
    }

    public BigDecimal getTotalUsd() {
        return totalUsd;
    }

    public void setTotalUsd(BigDecimal totalUsd) {
        this.totalUsd = totalUsd;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=Charge");
        sb.append(", id=").append(id);
        sb.append(", shipmentId=").append(shipmentId);
        sb.append(", legId=").append(legId);
        sb.append(", serviceSubtypeId=").append(serviceSubtypeId);
        sb.append(", containerQuantity=").append(containerQuantity);
        sb.append(", serviceProviderId=").append(serviceProviderId);
        sb.append(", serviceProviderName='").append(serviceProviderName).append('\'');
        sb.append(", way=").append(way);
        sb.append(", currency=").append(currency);
        sb.append(", unitPrice=").append(unitPrice);
        sb.append(", totalPrice=").append(totalPrice);
        sb.append(", totalCny=").append(totalCny);
        sb.append(", totalUsd=").append(totalUsd);
        sb.append(", taskId='").append(taskId).append('\'');
        sb.append(", comment='").append(comment).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
