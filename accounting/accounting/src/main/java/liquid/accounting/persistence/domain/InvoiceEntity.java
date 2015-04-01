package liquid.accounting.persistence.domain;

import liquid.order.persistence.domain.OrderEntity;
import liquid.persistence.domain.BaseUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * Created by Tao Ma on 1/8/15.
 */
@Entity(name = "ACT_INVOICE")
public class InvoiceEntity extends BaseUpdateEntity {
    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity order;

    @Column(name = "INVOICE_NO")
    private String invoiceNo;

    @Column(name = "CNY")
    private Long cny;

    @Column(name = "usd")
    private Long usd;

    @Column(name = "ISSUED_AT")
    private Date issuedAt;

    @Column(name = "BUYER_ID")
    private Long buyerId;

    @Column(name = "SELLER_ID")
    private Long sellerId;

    @Column(name = "EXP_PAY_AT")
    private Date expectedPaymentAt;

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
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

    public Date getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Date issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public Date getExpectedPaymentAt() {
        return expectedPaymentAt;
    }

    public void setExpectedPaymentAt(Date expectedPaymentAt) {
        this.expectedPaymentAt = expectedPaymentAt;
    }
}
