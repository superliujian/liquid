package liquid.dto;

/**
 * TODO: Comments.
 * User: tao
 * Date: 11/16/13
 * Time: 5:04 PM
 */
public class ExchangeRateDto {
    private double value;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
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
