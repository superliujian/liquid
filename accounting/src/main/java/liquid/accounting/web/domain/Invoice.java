package liquid.accounting.web.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by Tao Ma on 1/8/15.
 */
public class Invoice {
    private Long id;

    @NotNull
    @NotEmpty
    private String invoiceNo;
    private Long orderId;
    private String orderNo;

    @NotNull
    private Long cny = 0L;

    @NotNull
    private Long usd = 0L;

    @NotNull
    @NotEmpty
    private String issuedAt;
    private Long buyerId;
    private String buyerName;
    private Long sellerId;
    private String sellerName;
    private String expectedPaymentAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
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

    public String getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(String issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getExpectedPaymentAt() {
        return expectedPaymentAt;
    }

    public void setExpectedPaymentAt(String expectedPaymentAt) {
        this.expectedPaymentAt = expectedPaymentAt;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=Invoice");
        sb.append(", id=").append(id);
        sb.append(", invoiceNo='").append(invoiceNo).append('\'');
        sb.append(", orderId=").append(orderId);
        sb.append(", orderNo='").append(orderNo).append('\'');
        sb.append(", cny=").append(cny);
        sb.append(", usd=").append(usd);
        sb.append(", issuedAt='").append(issuedAt).append('\'');
        sb.append(", buyerId=").append(buyerId);
        sb.append(", buyerName='").append(buyerName).append('\'');
        sb.append(", sellerId=").append(sellerId);
        sb.append(", sellerName='").append(sellerName).append('\'');
        sb.append(", expectedPaymentAt='").append(expectedPaymentAt).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
