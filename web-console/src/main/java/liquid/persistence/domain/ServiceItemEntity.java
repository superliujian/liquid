package liquid.persistence.domain;

import javax.persistence.*;

/**
 * Created by redbrick9 on 5/7/14.
 */
@Entity(name = "SERVICE_ITEM")
public class ServiceItemEntity extends BaseEntity {
    @Column(name = "CODE")
    private String code;

    @Column(name = "NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "CHARGE_TYPE_ID")
    private ChargeType chargeType;

    @ManyToOne
    @JoinColumn(name = "SP_ID")
    private ServiceProviderEntity serviceProvider;

    @Transient
    private long serviceSubtypeId;

    @ManyToOne
    @JoinColumn(name = "SERVICE_SUBTYPE_ID")
    private ServiceSubtype serviceSubtype;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "PRICE")
    private long price;

    @Column(name = "STATUS")
    private int status;

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

    public ChargeType getChargeType() {
        return chargeType;
    }

    public void setChargeType(ChargeType chargeType) {
        this.chargeType = chargeType;
    }

    public ServiceProviderEntity getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProviderEntity serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public long getServiceSubtypeId() {
        return serviceSubtypeId;
    }

    public void setServiceSubtypeId(long serviceSubtypeId) {
        this.serviceSubtypeId = serviceSubtypeId;
    }

    public ServiceSubtype getServiceSubtype() {
        return serviceSubtype;
    }

    public void setServiceSubtype(ServiceSubtype serviceSubtype) {
        this.serviceSubtype = serviceSubtype;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ServiceItem{");
        sb.append("code='").append(code).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", serviceSubtypeId=").append(serviceSubtypeId);
        sb.append(", serviceSubtype=").append(serviceSubtype);
        sb.append(", currency='").append(currency).append('\'');
        sb.append(", price=").append(price);
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
