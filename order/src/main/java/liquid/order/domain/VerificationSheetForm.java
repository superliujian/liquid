package liquid.order.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by Tao Ma on 12/31/14.
 */
public class VerificationSheetForm {
    private String definitionKey;
    private Long orderId;

    @NotNull
    @NotEmpty
    private String sn;

    public String getDefinitionKey() {
        return definitionKey;
    }

    public void setDefinitionKey(String definitionKey) {
        this.definitionKey = definitionKey;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=VerificationSheetForm");
        sb.append(", definitionKey='").append(definitionKey).append('\'');
        sb.append(", orderId=").append(orderId);
        sb.append(", sn='").append(sn).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
