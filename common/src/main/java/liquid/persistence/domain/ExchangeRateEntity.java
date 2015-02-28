package liquid.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;

/**
 * TODO: Comments.
 * User: tao
 * Date: 11/16/13
 * Time: 1:32 PM
 */
@Entity(name = "OPS_EXCHANGE_RATE")
public class ExchangeRateEntity extends BaseUpdateEntity {
    public static final BigDecimal DEFAULT_EXCHANGE_RATE = new BigDecimal("6.09");

    /**
     * 1 usd = ? cny
     */
    @Column(precision = 19, scale = 4, name = "VALUE")
    private BigDecimal value = DEFAULT_EXCHANGE_RATE;

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
