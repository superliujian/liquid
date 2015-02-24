package liquid.order.domain;

import liquid.domain.BaseIdObject;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by Tao Ma on 2/24/15.
 */
public class BaseOrder extends BaseIdObject {
    private String orderNo;
    private Long serviceTypeId;
    private String serviceType;
    private Long customerId;

    @NotNull
    @NotEmpty
    private String customerName;

    private Long originId;

    @NotNull
    @NotEmpty
    private String origination;
    private Long destinationId;

    @NotNull
    @NotEmpty
    private String destination;
    private String consignee;
    private String consigneePhone;
    private String consigneeAddress;
    private Long goodsId;
    private String goodsName;
    @Min(1)
    @NotNull
    private Integer goodsWeight;
    private String goodsDimension;

    private Integer containerType = 0;
    private String containerTypeName;
    private Long railContainerSubtypeId;
    private Long selfContainerSubtypeId;
    private String containerSubtype;
    @NotNull
    @Min(1)
    private Integer containerQuantity = 0;
    private String containerAttribute;

    @Min(1)
    private Long cnyTotal;
    private Long usdTotal;

    private String createdBy;
    private String createdAt;
    private String updatedAt;

    private String role;
    private Integer status;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Long serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getOriginId() {
        return originId;
    }

    public void setOriginId(Long originId) {
        this.originId = originId;
    }

    public String getOrigination() {
        return origination;
    }

    public void setOrigination(String origination) {
        this.origination = origination;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getConsigneePhone() {
        return consigneePhone;
    }

    public void setConsigneePhone(String consigneePhone) {
        this.consigneePhone = consigneePhone;
    }

    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getGoodsWeight() {
        return goodsWeight;
    }

    public void setGoodsWeight(Integer goodsWeight) {
        this.goodsWeight = goodsWeight;
    }

    public String getGoodsDimension() {
        return goodsDimension;
    }

    public void setGoodsDimension(String goodsDimension) {
        this.goodsDimension = goodsDimension;
    }

    public Integer getContainerType() {
        return containerType;
    }

    public void setContainerType(Integer containerType) {
        this.containerType = containerType;
    }

    public String getContainerTypeName() {
        return containerTypeName;
    }

    public void setContainerTypeName(String containerTypeName) {
        this.containerTypeName = containerTypeName;
    }

    public Long getRailContainerSubtypeId() {
        return railContainerSubtypeId;
    }

    public void setRailContainerSubtypeId(Long railContainerSubtypeId) {
        this.railContainerSubtypeId = railContainerSubtypeId;
    }

    public Long getSelfContainerSubtypeId() {
        return selfContainerSubtypeId;
    }

    public void setSelfContainerSubtypeId(Long selfContainerSubtypeId) {
        this.selfContainerSubtypeId = selfContainerSubtypeId;
    }

    public String getContainerSubtype() {
        return containerSubtype;
    }

    public void setContainerSubtype(String containerSubtype) {
        this.containerSubtype = containerSubtype;
    }

    public Integer getContainerQuantity() {
        return containerQuantity;
    }

    public void setContainerQuantity(Integer containerQuantity) {
        this.containerQuantity = containerQuantity;
    }

    public String getContainerAttribute() {
        return containerAttribute;
    }

    public void setContainerAttribute(String containerAttribute) {
        this.containerAttribute = containerAttribute;
    }

    public Long getCnyTotal() {
        return cnyTotal;
    }

    public void setCnyTotal(Long cnyTotal) {
        this.cnyTotal = cnyTotal;
    }

    public Long getUsdTotal() {
        return usdTotal;
    }

    public void setUsdTotal(Long usdTotal) {
        this.usdTotal = usdTotal;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=BaseOrder");
        sb.append(", orderNo='").append(orderNo).append('\'');
        sb.append(", serviceTypeId=").append(serviceTypeId);
        sb.append(", serviceType='").append(serviceType).append('\'');
        sb.append(", customerId=").append(customerId);
        sb.append(", customerName='").append(customerName).append('\'');
        sb.append(", originId=").append(originId);
        sb.append(", origination='").append(origination).append('\'');
        sb.append(", destinationId=").append(destinationId);
        sb.append(", destination='").append(destination).append('\'');
        sb.append(", consignee='").append(consignee).append('\'');
        sb.append(", consigneePhone='").append(consigneePhone).append('\'');
        sb.append(", consigneeAddress='").append(consigneeAddress).append('\'');
        sb.append(", goodsId=").append(goodsId);
        sb.append(", goodsName='").append(goodsName).append('\'');
        sb.append(", goodsWeight=").append(goodsWeight);
        sb.append(", goodsDimension='").append(goodsDimension).append('\'');
        sb.append(", containerType=").append(containerType);
        sb.append(", containerTypeName='").append(containerTypeName).append('\'');
        sb.append(", railContainerSubtypeId=").append(railContainerSubtypeId);
        sb.append(", selfContainerSubtypeId=").append(selfContainerSubtypeId);
        sb.append(", containerSubtype='").append(containerSubtype).append('\'');
        sb.append(", containerQuantity=").append(containerQuantity);
        sb.append(", containerAttribute='").append(containerAttribute).append('\'');
        sb.append(", cnyTotal=").append(cnyTotal);
        sb.append(", usdTotal=").append(usdTotal);
        sb.append(", createdBy='").append(createdBy).append('\'');
        sb.append(", createdAt='").append(createdAt).append('\'');
        sb.append(", updatedAt='").append(updatedAt).append('\'');
        sb.append(", role='").append(role).append('\'');
        sb.append(", status=").append(status);
        sb.append(", ").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}
