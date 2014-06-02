package liquid.domain;

/**
 * Created by redbrick9 on 5/27/14.
 */
public class ServiceItem {
    private Long id;
    private long serviceSubtypeId;
    private String currency;
    private long quotation;
    private String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getServiceSubtypeId() {
        return serviceSubtypeId;
    }

    public void setServiceSubtypeId(long serviceSubtypeId) {
        this.serviceSubtypeId = serviceSubtypeId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public long getQuotation() {
        return quotation;
    }

    public void setQuotation(long quotation) {
        this.quotation = quotation;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ServiceItem{");
        sb.append("id=").append(id);
        sb.append(", serviceSubtypeId=").append(serviceSubtypeId);
        sb.append(", currency='").append(currency).append('\'');
        sb.append(", quotation=").append(quotation);
        sb.append(", comment='").append(comment).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
