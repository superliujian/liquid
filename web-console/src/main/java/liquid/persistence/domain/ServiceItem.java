package liquid.persistence.domain;

import javax.persistence.*;

/**
 * Created by redbrick9 on 5/7/14.
 */
@Entity(name = "SERVICE_ITEM")
public class ServiceItem extends BaseEntity {

    @Transient
    private long chargeTypeId;

    @ManyToOne
    @JoinColumn(name = "CHARGE_TYPE_ID")
    private ChargeType chargeType;

    @Column(name = "UNIT_PRICE")
    private long unitPrice;

    public long getChargeTypeId() {
        return chargeTypeId;
    }

    public void setChargeTypeId(long chargeTypeId) {
        this.chargeTypeId = chargeTypeId;
    }

    public ChargeType getChargeType() {
        return chargeType;
    }

    public void setChargeType(ChargeType chargeType) {
        this.chargeType = chargeType;
    }

    public long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(long unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ServiceItem{");
        sb.append("chargeTypeId=").append(chargeTypeId);
        sb.append(", chargeType=").append(chargeType);
        sb.append(", unitPrice=").append(unitPrice);
        sb.append('}');
        return sb.toString();
    }
}
