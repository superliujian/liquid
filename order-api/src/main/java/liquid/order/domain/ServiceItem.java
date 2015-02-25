package liquid.order.domain;

import liquid.domain.BaseIdObject;
import org.springframework.format.annotation.NumberFormat;

/**
 * Created by redbrick9 on 5/27/14.
 */
public class ServiceItem extends BaseIdObject {
    private Long serviceSubtypeId;
    private String serviceSubtype;

    @NumberFormat
    private Integer currency;
    private String currencyText;

    private Long quotation;
    private String comment;

    public Long getServiceSubtypeId() {
        return serviceSubtypeId;
    }

    public void setServiceSubtypeId(Long serviceSubtypeId) {
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

    public Long getQuotation() {
        return quotation;
    }

    public void setQuotation(Long quotation) {
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
        final StringBuilder sb = new StringBuilder("{Class=ServiceItem");
        sb.append(", serviceSubtypeId=").append(serviceSubtypeId);
        sb.append(", serviceSubtype='").append(serviceSubtype).append('\'');
        sb.append(", currency=").append(currency);
        sb.append(", currencyText='").append(currencyText).append('\'');
        sb.append(", quotation=").append(quotation);
        sb.append(", comment='").append(comment).append('\'');
        sb.append(", ").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}
