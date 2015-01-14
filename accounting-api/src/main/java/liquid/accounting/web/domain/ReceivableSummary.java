package liquid.accounting.web.domain;

import liquid.order.domain.Order;

/**
 * Created by Tao Ma on 12/9/14.
 */
public class ReceivableSummary extends Order {
    private Long id;

    private Long cny = 0L;

    private Long usd = 0L;

    private String prepaidTime;

    private Long remainingBalanceCny = 0L;

    private Long remainingBalanceUsd = 0L;

    private Long paidCny = 0L;

    private Long paidUsd = 0L;

    private Long invoicedCny = 0L;

    private Long invoicedUsd = 0L;

    private Long orderId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPrepaidTime() {
        return prepaidTime;
    }

    public void setPrepaidTime(String prepaidTime) {
        this.prepaidTime = prepaidTime;
    }

    public Long getRemainingBalanceCny() {
        return remainingBalanceCny;
    }

    public void setRemainingBalanceCny(Long remainingBalanceCny) {
        this.remainingBalanceCny = remainingBalanceCny;
    }

    public Long getRemainingBalanceUsd() {
        return remainingBalanceUsd;
    }

    public void setRemainingBalanceUsd(Long remainingBalanceUsd) {
        this.remainingBalanceUsd = remainingBalanceUsd;
    }

    public Long getPaidCny() {
        return paidCny;
    }

    public void setPaidCny(Long paidCny) {
        this.paidCny = paidCny;
    }

    public Long getPaidUsd() {
        return paidUsd;
    }

    public void setPaidUsd(Long paidUsd) {
        this.paidUsd = paidUsd;
    }

    public Long getInvoicedCny() {
        return invoicedCny;
    }

    public void setInvoicedCny(Long invoicedCny) {
        this.invoicedCny = invoicedCny;
    }

    public Long getInvoicedUsd() {
        return invoicedUsd;
    }

    public void setInvoicedUsd(Long invoicedUsd) {
        this.invoicedUsd = invoicedUsd;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=ReceivableSummary");
        sb.append(", id=").append(id);
        sb.append(", cny=").append(cny);
        sb.append(", usd=").append(usd);
        sb.append(", prepaidTime='").append(prepaidTime).append('\'');
        sb.append(", remainingBalanceCny=").append(remainingBalanceCny);
        sb.append(", remainingBalanceUsd=").append(remainingBalanceUsd);
        sb.append(", paidCny=").append(paidCny);
        sb.append(", paidUsd=").append(paidUsd);
        sb.append(", invoicedCny=").append(invoicedCny);
        sb.append(", invoicedUsd=").append(invoicedUsd);
        sb.append(", orderId=").append(orderId);
        sb.append(", ").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}
