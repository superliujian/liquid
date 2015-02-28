package liquid.web.domain;

import java.math.BigDecimal;

/**
 * TODO: Comments.
 * User: tao
 * Date: 11/16/13
 * Time: 5:04 PM
 */
public class ExchangeRateDto {
    private BigDecimal value;

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("ExchangeRateDto{");
        sb.append("value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
