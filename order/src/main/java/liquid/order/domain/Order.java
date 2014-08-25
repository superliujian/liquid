package liquid.order.domain;

import liquid.domain.BaseIdObject;
import liquid.domain.ServiceItem;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by redbrick9 on 5/27/14.
 */
public class Order extends BaseIdObject {
    private String orderNo;
    private long serviceTypeId;
    private long customerId;

    @NotNull
    @NotEmpty
    private String customerName;
    private int tradeType;
    private long originId;

    @NotNull
    @NotEmpty
    private String origination;
    private long destinationId;

    @NotNull
    @NotEmpty
    private String destination;
    private String consignee;
    private String consigneePhone;
    private String consigneeAddress;
    private long goodsId;
    private String goodsName;

    @Min(1)
    private int goodsWeight;
    private String goodsDimension;
    private int loadingType;
    private String loadingAddress;
    private String loadingContact;
    private String loadingPhone;
    private String loadingEstimatedTime;
    private int containerType;
    private long railContainerSubtypeId;
    private long selfContainerSubtypeId;

    @Min(1)
    private int containerQuantity;
    private String containerAttribute;

    private Long railwayId;
    private String planReportTime;
    private Long railwayPlanTypeId;
    private String programNo;
    private Long railSourceId;
    private String railSource;
    private Long railDestinationId;
    private String railDestination;
    private String comment;
    private Boolean sameDay;

    private List<ServiceItem> serviceItems;

    @Min(1)
    private long cnyTotal;
    private long usdTotal;

    private String role;
    private int status;

    public Order() {
        serviceItems = new ArrayList<>();
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public long getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(long serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getTradeType() {
        return tradeType;
    }

    public void setTradeType(int tradeType) {
        this.tradeType = tradeType;
    }

    public long getOriginId() {
        return originId;
    }

    public void setOriginId(long originId) {
        this.originId = originId;
    }

    public String getOrigination() {
        return origination;
    }

    public void setOrigination(String origination) {
        this.origination = origination;
    }

    public long getDestinationId() {
        return destinationId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDestinationId(long destinationId) {
        this.destinationId = destinationId;
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

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getGoodsWeight() {
        return goodsWeight;
    }

    public void setGoodsWeight(int goodsWeight) {
        this.goodsWeight = goodsWeight;
    }

    public String getGoodsDimension() {
        return goodsDimension;
    }

    public void setGoodsDimension(String goodsDimension) {
        this.goodsDimension = goodsDimension;
    }

    public int getLoadingType() {
        return loadingType;
    }

    public void setLoadingType(int loadingType) {
        this.loadingType = loadingType;
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

    public int getContainerType() {
        return containerType;
    }

    public void setContainerType(int containerType) {
        this.containerType = containerType;
    }

    public long getRailContainerSubtypeId() {
        return railContainerSubtypeId;
    }

    public void setRailContainerSubtypeId(long railContainerSubtypeId) {
        this.railContainerSubtypeId = railContainerSubtypeId;
    }

    public long getSelfContainerSubtypeId() {
        return selfContainerSubtypeId;
    }

    public void setSelfContainerSubtypeId(long selfContainerSubtypeId) {
        this.selfContainerSubtypeId = selfContainerSubtypeId;
    }

    public int getContainerQuantity() {
        return containerQuantity;
    }

    public void setContainerQuantity(int containerQuantity) {
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

    public long getUsdTotal() {
        return usdTotal;
    }

    public void setUsdTotal(long usdTotal) {
        this.usdTotal = usdTotal;
    }

    public long getCnyTotal() {
        return cnyTotal;
    }

    public void setCnyTotal(long cnyTotal) {
        this.cnyTotal = cnyTotal;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Order{");
        sb.append("orderNo='").append(orderNo).append('\'');
        sb.append(", serviceTypeId=").append(serviceTypeId);
        sb.append(", customerId=").append(customerId);
        sb.append(", customerName='").append(customerName).append('\'');
        sb.append(", tradeType=").append(tradeType);
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
        sb.append(", loadingAddress='").append(loadingAddress).append('\'');
        sb.append(", loadingContact='").append(loadingContact).append('\'');
        sb.append(", loadingPhone='").append(loadingPhone).append('\'');
        sb.append(", loadingEstimatedTime='").append(loadingEstimatedTime).append('\'');
        sb.append(", containerType=").append(containerType);
        sb.append(", railContainerSubtypeId=").append(railContainerSubtypeId);
        sb.append(", selfContainerSubtypeId=").append(selfContainerSubtypeId);
        sb.append(", containerQuantity=").append(containerQuantity);
        sb.append(", containerAttribute='").append(containerAttribute).append('\'');
        sb.append(", railwayId=").append(railwayId);
        sb.append(", planReportTime='").append(planReportTime).append('\'');
        sb.append(", railwayPlanTypeId=").append(railwayPlanTypeId);
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
        sb.append(", role='").append(role).append('\'');
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}

