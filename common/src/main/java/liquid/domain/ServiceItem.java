package liquid.domain;

/**
 * Created by redbrick9 on 5/27/14.
 */
public class ServiceItem extends BaseIdObject {
    private long serviceSubtypeId;
    private String serviceSubtype;
    private Integer currency = 0;
    private String currencyText;
    private long quotation;
    private String comment;

    public long getServiceSubtypeId() {
        return serviceSubtypeId;
    }

    public void setServiceSubtypeId(long serviceSubtypeId) {
        this.serviceSubtypeId = serviceSubtypeId;
    }

    public String getServiceSubtype() {
        return serviceSubtype;
    }

    public void setServiceSubtype(String serviceSubtype) {
        this.serviceSubtype = serviceSubtype;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public String getCurrencyText() {
        return currencyText;
    }

    public void setCurrencyText(String currencyText) {
        this.currencyText = currencyText;
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
        sb.append("serviceSubtypeId=").append(serviceSubtypeId);
        sb.append(", serviceSubtype='").append(serviceSubtype).append('\'');
        sb.append(", currency=").append(currency);
        sb.append(", currencyText='").append(currencyText).append('\'');
        sb.append(", quotation=").append(quotation);
        sb.append(", comment='").append(comment).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
