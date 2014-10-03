package liquid.order.persistence.domain;

import liquid.persistence.domain.BaseIdEntity;
import liquid.persistence.domain.LocationEntity;
import liquid.persistence.domain.RailPlanTypeEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by redbrick9 on 8/22/14.
 */
@Entity(name = "RAILWAY")
public class RailwayEntity extends BaseIdEntity {
    @OneToOne
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity order;

    @Column(name = "PLAN_REPORT_TIME")
    private Date planReportTime;

    @ManyToOne
    @JoinColumn(name = "PLAN_TYPE")
    private RailPlanTypeEntity planType;

    @Column(name = "PROGRAM_NO")
    private String programNo;

    @ManyToOne
    @JoinColumn(name = "SOURCE_ID")
    private LocationEntity source;

    @ManyToOne
    @JoinColumn(name = "DESTINATION_ID")
    private LocationEntity destination;

    @Column(name = "COMMENT")
    private String comment;

    @Column(name = "SAME_DAY")
    private Boolean sameDay;

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public Date getPlanReportTime() {
        return planReportTime;
    }

    public void setPlanReportTime(Date planReportTime) {
        this.planReportTime = planReportTime;
    }

    public RailPlanTypeEntity getPlanType() {
        return planType;
    }

    public void setPlanType(RailPlanTypeEntity planType) {
        this.planType = planType;
    }

    public String getProgramNo() {
        return programNo;
    }

    public void setProgramNo(String programNo) {
        this.programNo = programNo;
    }

    public LocationEntity getSource() {
        return source;
    }

    public void setSource(LocationEntity source) {
        this.source = source;
    }

    public LocationEntity getDestination() {
        return destination;
    }

    public void setDestination(LocationEntity destination) {
        this.destination = destination;
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
}
