package liquid.order.persistence.domain;

import liquid.persistence.domain.BaseIdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.util.Date;

/**
 * Created by redbrick9 on 8/28/14.
 */
@Entity(name = "ACT_RECEIVABLE_SUMMARY")
public class ReceivableSummaryEntity extends BaseIdEntity {
    @OneToOne
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity order;

    @Column(name = "CNY")
    private Long cny;

    @Column(name = "USD")
    private Long usd;

    @Column(name = "PREPAID_TIME")
    private Date prepaidTime;

    @Column(name = "REM_BAL_CNY")
    private Long remainingBalanceCny;

    @Column(name = "REM_BAL_USD")
    private Long remainingBalanceUsd;

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
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
}
