package liquid.persistence.domain;

import javax.persistence.*;

/**
 * Created by redbrick9 on 5/7/14.
 */
@Entity(name = "SERVICE_ITEM")
public class ServiceItem extends BaseEntity {
    @Column(name = "CODE")
    private String code;

    @Column(name = "NAME")
    private String name;

    @Transient
    private long chargeTypeId;

    @ManyToOne
    @JoinColumn(name = "CHARGE_TYPE_ID")
    private ChargeType chargeType;

    @Transient
    private long serviceProviderId;

    @ManyToOne
    @JoinColumn(name = "SP_ID")
    private ServiceProvider serviceProvider;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "PRICE")
    private long price;

    @Column(name = "COST")
    private long cost;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public long getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(long serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ServiceItem{");
        sb.append("code='").append(code).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", chargeTypeId=").append(chargeTypeId);
        sb.append(", chargeType=").append(chargeType);
        sb.append(", serviceProviderId=").append(serviceProviderId);
        sb.append(", serviceProvider=").append(serviceProvider);
        sb.append(", currency='").append(currency).append('\'');
        sb.append(", price=").append(price);
        sb.append(", cost=").append(cost);
        sb.append('}');
        return sb.toString();
    }
}
