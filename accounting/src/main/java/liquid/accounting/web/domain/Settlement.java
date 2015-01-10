package liquid.accounting.web.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by Tao Ma on 1/10/15.
 */
public class Settlement {
    private Long id;
    private Long orderId;
    private String orderNo;

    @NotNull
    private Long cny = 0L;

    @NotNull
    private Long usd = 0L;

    @NotNull
    @NotEmpty
    private String settledAt;

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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getCny() {
        return cny;
    }

    public void setCny(Long cny) {
        this.cny = cny;
    }

    public Long getUsd() {
        return usd;
    }

    public void setUsd(Long usd) {
        this.usd = usd;
    }

    public String getSettledAt() {
        return settledAt;
    }

    public void setSettledAt(String settledAt) {
        this.settledAt = settledAt;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=Settlement");
        sb.append(", id=").append(id);
        sb.append(", orderId=").append(orderId);
        sb.append(", orderNo='").append(orderNo).append('\'');
        sb.append(", cny=").append(cny);
        sb.append(", usd=").append(usd);
        sb.append(", settledAt='").append(settledAt).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
