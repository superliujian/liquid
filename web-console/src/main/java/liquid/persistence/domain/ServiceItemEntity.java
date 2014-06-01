package liquid.persistence.domain;

import javax.persistence.*;

/**
 * Created by redbrick9 on 5/7/14.
 */
@Entity(name = "SERVICE_ITEM")
public class ServiceItemEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "SERVICE_SUBTYPE_ID")
    private ServiceSubtypeEntity serviceSubtype;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "QUOTATION")
    private long quotation;

    @Column(name = "COMMENT")
    private String comment;

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public ServiceSubtypeEntity getServiceSubtype() {
        return serviceSubtype;
    }

    public void setServiceSubtype(ServiceSubtypeEntity serviceSubtype) {
        this.serviceSubtype = serviceSubtype;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public long getQuotation() {
        return quotation;
    }

    public void setQuotation(long quotation) {
        this.quotation = quotation;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
