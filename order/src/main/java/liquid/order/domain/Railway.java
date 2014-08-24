package liquid.order.domain;

/**
 * Created by redbrick9 on 8/23/14.
 */
public class Railway {
    private Long id;
    private Long orderId;
    private String planReportTime;
    private Integer planType;
    private String programNo;
    private Long sourceId;
    private Long destinationId;
    private String comment;
    private Boolean sameDay;

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

    public String getPlanReportTime() {
        return planReportTime;
    }

    public void setPlanReportTime(String planReportTime) {
        this.planReportTime = planReportTime;
    }

    public Integer getPlanType() {
        return planType;
    }

    public void setPlanType(Integer planType) {
        this.planType = planType;
    }

    public String getProgramNo() {
        return programNo;
    }

    public void setProgramNo(String programNo) {
        this.programNo = programNo;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Railway{");
        sb.append("id=").append(id);
        sb.append(", orderId=").append(orderId);
        sb.append(", planReportTime='").append(planReportTime).append('\'');
        sb.append(", planType=").append(planType);
        sb.append(", programNo='").append(programNo).append('\'');
        sb.append(", sourceId=").append(sourceId);
        sb.append(", destinationId=").append(destinationId);
        sb.append(", comment='").append(comment).append('\'');
        sb.append(", sameDay=").append(sameDay);
        sb.append('}');
        return sb.toString();
    }
}
