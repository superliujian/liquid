package liquid.accounting.web.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by Tao Ma on 1/8/15.
 */
public class Receipt {
    private Long id;
    private Long orderId;
    private String orderNo;

    @NotNull
    private Long cny = 0L;

    @NotNull
    private Long usd = 0L;
    private Long recipientId;
    private String recipientName;
    private Long payerId;
    private String payerName;

    @NotNull
    @NotEmpty
    private String issuedAt;

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

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public Long getPayerId() {
        return payerId;
    }

    public void setPayerId(Long payerId) {
        this.payerId = payerId;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(String issuedAt) {
        this.issuedAt = issuedAt;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=Receipt");
        sb.append(", id=").append(id);
        sb.append(", orderId=").append(orderId);
        sb.append(", orderNo='").append(orderNo).append('\'');
        sb.append(", cny=").append(cny);
        sb.append(", usd=").append(usd);
        sb.append(", recipientId=").append(recipientId);
        sb.append(", recipientName='").append(recipientName).append('\'');
        sb.append(", payerId=").append(payerId);
        sb.append(", payerName='").append(payerName).append('\'');
        sb.append(", issuedAt='").append(issuedAt).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
