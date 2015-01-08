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

    @Column(name = "CNY_OF_INVOICE")
    private Long cnyOfInvoice;

    @Column(name = "USD_OF_INVOICE")
    private Long usdOfInvoice;

    @Column(name = "ISSUED_AT")
    private Date issuedAt;

    @Column(name = "BUYER_ID")
    private Long buyerId;

    @Column(name = "SELLER_ID")
    private Long sellerId;

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

    public Long getCnyOfInvoice() {
        return cnyOfInvoice;
    }

    public void setCnyOfInvoice(Long cnyOfInvoice) {
        this.cnyOfInvoice = cnyOfInvoice;
    }

    public Long getUsdOfInvoice() {
        return usdOfInvoice;
    }

    public void setUsdOfInvoice(Long usdOfInvoice) {
        this.usdOfInvoice = usdOfInvoice;
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
}
