package liquid.dto;

import liquid.metadata.DatePattern;
import liquid.validation.constraints.DateFormat;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/19/13
 * Time: 1:07 PM
 */
public class RailYardDto extends RailContainerDto {
    @DateFormat(DatePattern.UNTIL_MINUTE)
    private String railYardToa;

    public String getRailYardToa() {
        return railYardToa;
    }

    public void setRailYardToa(String railYardToa) {
        this.railYardToa = railYardToa;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("RailYardDto{");
        sb.append("railYardToa='").append(railYardToa).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
