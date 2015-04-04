package liquid.accounting.persistence.domain;

import liquid.order.domain.OrderEntity;
import liquid.persistence.domain.BaseIdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.util.Date;

/**
 * Created by Tao Ma on 2/7/15.
 */
@Entity(name = "ACT_GROSS_PROFIT")
public class GrossProfitEntity extends BaseIdEntity {
    @OneToOne
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity order;

    @Column(name = "CREATED_AT")
    private Date createdAt;

    @Column(name = "REVENUE")
    private Long revenue;

    @Column(name = "COST")
    private Long cost;

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getRevenue() {
        return revenue;
    }

    public void setRevenue(Long revenue) {
        this.revenue = revenue;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }
}
