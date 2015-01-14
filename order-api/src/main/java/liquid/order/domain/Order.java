package liquid.order.domain;

import liquid.domain.BaseIdObject;
import liquid.domain.ServiceItem;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by redbrick9 on 5/27/14.
 */
public class Order extends BaseIdObject {
    private String orderNo;
    private Long serviceTypeId;
    private String serviceType;
    private Long customerId;

    @NotNull
    @NotEmpty
    private String customerName;
    private Integer tradeType = 0;
    private String verificationSheetSn;
    private String tradeTypeName;
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
    private Integer loadingType = 0;
    private String loadingTypeName;
    private String loadingAddress;
    private String loadingContact;
    private String loadingPhone;
    private String loadingEstimatedTime;
    private Integer containerType = 0;
    private String containerTypeName;
    private Long railContainerSubtypeId;
    private Long selfContainerSubtypeId;
    private String containerSubtype;

    @NotNull
    @Min(1)
    private Integer containerQuantity = 0;
    private String containerAttribute;

    private Long railwayId;
    private String planReportTime;
    private Long railwayPlanTypeId;
    private String railwayPlanType;
    private String programNo;
    private Long railSourceId;

    @NotNull
    @NotEmpty
    private String railSource;
    private Long railDestinationId;

    @NotNull
    @NotEmpty
    private String railDestination;
    private String comment;
    private Boolean sameDay;

    @Valid
    private List<ServiceItem> serviceItems;

    @Min(1)
    private Long cnyTotal;
    private Long usdTotal;
    private Long distyPrice = 0L;
    private Long distyUsd = 0L;
    private Long grandTotal = 0L;

    private String createdAt;
    private String updatedAt;

    private String role;
    private Integer status;

    public Order() {
        serviceItems = new ArrayList<>();
    }

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

    public Integer getTradeType() {
        return tradeType;
    }

    public void setTradeType(Integer tradeType) {
        this.tradeType = tradeType;
    }

    public String getTradeTypeName() {
        return tradeTypeName;
    }

    public void setTradeTypeName(String tradeTypeName) {
        this.tradeTypeName = tradeTypeName;
    }

    public String getVerificationSheetSn() {
        return verificationSheetSn;
    }

    public void setVerificationSheetSn(String verificationSheetSn) {
        this.verificationSheetSn = verificationSheetSn;
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

    public Integer getLoadingType() {
        return loadingType;
    }

    public void setLoadingType(Integer loadingType) {
        this.loadingType = loadingType;
    }

    public String getLoadingTypeName() {
        return loadingTypeName;
    }

    public void setLoadingTypeName(String loadingTypeName) {
        this.loadingTypeName = loadingTypeName;
    }

    public String getLoadingAddress() {
        return loadingAddress;
    }

    public void setLoadingAddress(String loadingAddress) {
        this.loadingAddress = loadingAddress;
    }

    public String getLoadingContact() {
        return loadingContact;
    }

    public void setLoadingContact(String loadingContact) {
        this.loadingContact = loadingContact;
    }

    public String getLoadingPhone() {
        return loadingPhone;
    }

    public void setLoadingPhone(String loadingPhone) {
        this.loadingPhone = loadingPhone;
    }

    public String getLoadingEstimatedTime() {
        return loadingEstimatedTime;
    }

    public void setLoadingEstimatedTime(String loadingEstimatedTime) {
        this.loadingEstimatedTime = loadingEstimatedTime;
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

    public Long getRailwayId() {
        return railwayId;
    }

    public void setRailwayId(Long railwayId) {
        this.railwayId = railwayId;
    }

    public String getPlanReportTime() {
        return planReportTime;
    }

    public void setPlanReportTime(String planReportTime) {
        this.planReportTime = planReportTime;
    }

    public Long getRailwayPlanTypeId() {
        return railwayPlanTypeId;
    }

    public void setRailwayPlanTypeId(Long railwayPlanTypeId) {
        this.railwayPlanTypeId = railwayPlanTypeId;
    }

    public String getRailwayPlanType() {
        return railwayPlanType;
    }

    public void setRailwayPlanType(String railwayPlanType) {
        this.railwayPlanType = railwayPlanType;
    }

    public String getProgramNo() {
        return programNo;
    }

    public void setProgramNo(String programNo) {
        this.programNo = programNo;
    }

    public Long getRailSourceId() {
        return railSourceId;
    }

    public void setRailSourceId(Long railSourceId) {
        this.railSourceId = railSourceId;
    }

    public String getRailSource() {
        return railSource;
    }

    public void setRailSource(String railSource) {
        this.railSource = railSource;
    }

    public Long getRailDestinationId() {
        return railDestinationId;
    }

    public void setRailDestinationId(Long railDestinationId) {
        this.railDestinationId = railDestinationId;
    }

    public String getRailDestination() {
        return railDestination;
    }

    public void setRailDestination(String railDestination) {
        this.railDestination = railDestination;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getSameDay() {
        return sameDay;
    }

    public void setSameDay(Boolean sameDay) {
        this.sameDay = sameDay;
    }

    public List<ServiceItem> getServiceItems() {
        return serviceItems;
    }

    public void setServiceItems(List<ServiceItem> serviceItems) {
        this.serviceItems = serviceItems;
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

    public Long getDistyPrice() {
        return distyPrice;
    }

    public void setDistyPrice(Long distyPrice) {
        this.distyPrice = distyPrice;
    }

    public Long getDistyUsd() {
        return distyUsd;
    }

    public void setDistyUsd(Long distyUsd) {
        this.distyUsd = distyUsd;
    }

    public Long getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(Long grandTotal) {
        this.grandTotal = grandTotal;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=Order");
        sb.append(", orderNo='").append(orderNo).append('\'');
        sb.append(", serviceTypeId=").append(serviceTypeId);
        sb.append(", serviceType='").append(serviceType).append('\'');
        sb.append(", customerId=").append(customerId);
        sb.append(", customerName='").append(customerName).append('\'');
        sb.append(", tradeType=").append(tradeType);
        sb.append(", verificationSheetSn='").append(verificationSheetSn).append('\'');
        sb.append(", tradeTypeName='").append(tradeTypeName).append('\'');
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
        sb.append(", loadingType=").append(loadingType);
        sb.append(", loadingTypeName='").append(loadingTypeName).append('\'');
        sb.append(", loadingAddress='").append(loadingAddress).append('\'');
        sb.append(", loadingContact='").append(loadingContact).append('\'');
        sb.append(", loadingPhone='").append(loadingPhone).append('\'');
        sb.append(", loadingEstimatedTime='").append(loadingEstimatedTime).append('\'');
        sb.append(", containerType=").append(containerType);
        sb.append(", containerTypeName='").append(containerTypeName).append('\'');
        sb.append(", railContainerSubtypeId=").append(railContainerSubtypeId);
        sb.append(", selfContainerSubtypeId=").append(selfContainerSubtypeId);
        sb.append(", containerSubtype='").append(containerSubtype).append('\'');
        sb.append(", containerQuantity=").append(containerQuantity);
        sb.append(", containerAttribute='").append(containerAttribute).append('\'');
        sb.append(", railwayId=").append(railwayId);
        sb.append(", planReportTime='").append(planReportTime).append('\'');
        sb.append(", railwayPlanTypeId=").append(railwayPlanTypeId);
        sb.append(", railwayPlanType='").append(railwayPlanType).append('\'');
        sb.append(", programNo='").append(programNo).append('\'');
        sb.append(", railSourceId=").append(railSourceId);
        sb.append(", railSource='").append(railSource).append('\'');
        sb.append(", railDestinationId=").append(railDestinationId);
        sb.append(", railDestination='").append(railDestination).append('\'');
        sb.append(", comment='").append(comment).append('\'');
        sb.append(", sameDay=").append(sameDay);
        sb.append(", serviceItems=").append(serviceItems);
        sb.append(", cnyTotal=").append(cnyTotal);
        sb.append(", usdTotal=").append(usdTotal);
        sb.append(", distyPrice=").append(distyPrice);
        sb.append(", distyUsd=").append(distyUsd);
        sb.append(", grandTotal=").append(grandTotal);
        sb.append(", createdAt='").append(createdAt).append('\'');
        sb.append(", updatedAt='").append(updatedAt).append('\'');
        sb.append(", role='").append(role).append('\'');
        sb.append(", status=").append(status);
        sb.append(", ").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}
