package liquid.persistence.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * Created by tao on 12/24/13.
 */
@Entity(name = "SERVICE")
public class Service extends BaseEntity {
    @NotNull
    @NotEmpty
    @Column(name = "NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "CHARGE_TYPE_ID")
    private ChargeType chargeType;

    @ManyToOne
    @JoinColumn(name = "SP_ID")
    private ServiceProvider sp;

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

    public ServiceProvider getSp() {
        return sp;
    }

    public void setSp(ServiceProvider sp) {
        this.sp = sp;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Service{");
        sb.append("name='").append(name).append('\'');
        sb.append(", chargeType=").append(chargeType);
        sb.append(", sp=").append(sp);
        sb.append('}');
        return sb.toString();
    }
}
