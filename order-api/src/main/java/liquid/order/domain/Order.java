package liquid.order.domain;

import liquid.domain.ServiceItem;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by redbrick9 on 5/27/14.
 */
public class Order extends BaseOrder {
    private Integer tradeType = 0;
    private String verificationSheetSn;
    private String tradeTypeName;

    private Integer loadingType = 0;
    private String loadingTypeName;
    private String loadingAddress;
    private String loadingContact;
    private String loadingPhone;
    private String loadingEstimatedTime;

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

    private Long distyPrice = 0L;
    private Long distyUsd = 0L;
    private Long grandTotal = 0L;

    public Order() {
        serviceItems = new ArrayList<>();
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
        sb.append(", tradeType=").append(tradeType);
        sb.append(", verificationSheetSn='").append(verificationSheetSn).append('\'');
        sb.append(", tradeTypeName='").append(tradeTypeName).append('\'');
        sb.append(", loadingType=").append(loadingType);
        sb.append(", loadingTypeName='").append(loadingTypeName).append('\'');
        sb.append(", loadingAddress='").append(loadingAddress).append('\'');
        sb.append(", loadingContact='").append(loadingContact).append('\'');
        sb.append(", loadingPhone='").append(loadingPhone).append('\'');
        sb.append(", loadingEstimatedTime='").append(loadingEstimatedTime).append('\'');
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
        sb.append(", distyPrice=").append(distyPrice);
        sb.append(", distyUsd=").append(distyUsd);
        sb.append(", grandTotal=").append(grandTotal);
        sb.append(", ").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}
