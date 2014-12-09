package liquid.order.domain;

/**
 * Created by Tao Ma on 12/9/14.
 */
public class ReceivableSummary {
    private Long id;

    private Long cny;

    private Long usd;

    private String prepaidTime;

    private Long remainingBalanceCny;

    private Long remainingBalanceUsd;

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=ReceivableSummary");
        sb.append(", id=").append(id);
        sb.append(", cny=").append(cny);
        sb.append(", usd=").append(usd);
        sb.append(", prepaidTime='").append(prepaidTime).append('\'');
        sb.append(", remainingBalanceCny=").append(remainingBalanceCny);
        sb.append(", remainingBalanceUsd=").append(remainingBalanceUsd);
        sb.append('}');
        return sb.toString();
    }
}
