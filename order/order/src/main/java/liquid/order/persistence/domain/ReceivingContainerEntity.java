package liquid.order.persistence.domain;

import liquid.order.domain.ReceivingOrderEntity;
import liquid.persistence.domain.BaseUpdateEntity;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/13/13
 * Time: 5:48 PM
 */
@Entity(name = "TSP_RECV_CONTAINER")
public class ReceivingContainerEntity extends BaseUpdateEntity {
    @ManyToOne
    @JoinColumn(name = "RECV_ORDER_ID")
    private ReceivingOrderEntity receivingOrder;

    @NotNull
    @NotEmpty
    @Column(name = "BIC_CODE")
    private String bicCode;

    public ReceivingOrderEntity getReceivingOrder() {
        return receivingOrder;
    }

    public void setReceivingOrder(ReceivingOrderEntity receivingOrder) {
        this.receivingOrder = receivingOrder;
    }

    public String getBicCode() {
        return bicCode;
    }

    public void setBicCode(String bicCode) {
        this.bicCode = bicCode;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("ReceivingContainer{");
        sb.append("receivingOrder=").append(receivingOrder);
        sb.append(", bicCode='").append(bicCode).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
