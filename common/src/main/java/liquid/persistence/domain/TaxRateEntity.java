package liquid.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by redbrick9 on 8/29/14.
 */
@Entity(name = "OPS_TAX_RATE")
public class TaxRateEntity extends BaseUpdateEntity {
    @Column(name = "NAME")
    private String name;

    @Column(name = "VALUE")
    private Integer value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
