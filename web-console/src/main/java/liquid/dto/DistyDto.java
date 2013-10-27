package liquid.dto;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/24/13
 * Time: 9:48 PM
 */
public class DistyDto {
    private long distyPrice;

    public long getDistyPrice() {
        return distyPrice;
    }

    public void setDistyPrice(long distyPrice) {
        this.distyPrice = distyPrice;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("DistyDto{");
        sb.append("distyPrice=").append(distyPrice);
        sb.append('}');
        return sb.toString();
    }
}
