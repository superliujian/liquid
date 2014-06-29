package liquid.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * TODO: Comments.
 * User: tao
 * Date: 11/16/13
 * Time: 1:32 PM
 */
@Entity(name = "EXCHANGE_RATE")
public class ExchangeRate extends BaseUpdateEntity {
    /**
     * 1 usd = ? cny
     */
    @Column(name = "VALUE")
    private double value = 6.09;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("ExchangeRate{");
        sb.append("value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
