package liquid.accounting.persistence.domain;

import liquid.order.persistence.domain.OrderEntity;
import liquid.persistence.domain.BaseUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by redbrick9 on 8/28/14.
 */
@Entity(name = "ACT_RECEIVABLE_SUMMARY")
public class ReceivableSummaryEntity extends BaseUpdateEntity {
    @OneToOne
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity order;

    @Column(precision = 19, scale = 4, name = "CNY")
    private BigDecimal cny;

    @Column(precision = 19, scale = 4, name = "USD")
    private BigDecimal usd;

    @Column(name = "PREPAID_TIME")
    private Date prepaidTime;

    @Deprecated
    @Column(name = "REM_BAL_CNY")
    private Long remainingBalanceCny;

    @Deprecated
    @Column(name = "REM_BAL_USD")
    private Long remainingBalanceUsd;

    @Column(name = "PAID_CNY")
    private Long paidCny = 0L;

    @Column(name = "PAID_USD")
    private Long paidUsd = 0L;

    @Column(name = "INVOICED_CNY")
    private Long invoicedCny = 0L;

    @Column(name = "INVOICED_USD")
    private Long invoicedUsd = 0L;

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public BigDecimal getCny() {
        return cny;
    }

    public void setCny(BigDecimal cny) {
        this.cny = cny;
    }

    public BigDecimal getUsd() {
        return usd;
    }

    public void setUsd(BigDecimal usd) {
        this.usd = usd;
    }

    public Date getPrepaidTime() {
        return prepaidTime;
    }

    public void setPrepaidTime(Date prepaidTime) {
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
}
