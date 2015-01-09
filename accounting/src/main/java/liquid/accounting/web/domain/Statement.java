package liquid.accounting.web.domain;

/**
 * Created by Tao Ma on 1/8/15.
 */
public class Statement<A> {
    private Iterable<A> content;
    private Long cnyTotal;
    private Long usdTotal;

    public Iterable<A> getContent() {
        return content;
    }

    public void setContent(Iterable<A> content) {
        this.content = content;
    }

    public Long getCnyTotal() {
        return cnyTotal;
    }

    public void setCnyTotal(Long cnyTotal) {
        this.cnyTotal = cnyTotal;
    }

    public Long getUsdTotal() {
        return usdTotal;
    }

    public void setUsdTotal(Long usdTotal) {
        this.usdTotal = usdTotal;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=Statement");
        sb.append(", content=").append(content);
        sb.append(", cnyTotal=").append(cnyTotal);
        sb.append(", usdTotal=").append(usdTotal);
        sb.append('}');
        return sb.toString();
    }
}
