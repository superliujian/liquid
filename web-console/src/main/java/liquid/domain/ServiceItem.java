package liquid.domain;

/**
 * Created by redbrick9 on 5/27/14.
 */
public class ServiceItem {
    private long id;
    private long serviceSubtypeId;
    private String currency;
    private long quotation;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
}
