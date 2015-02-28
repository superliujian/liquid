package liquid.domain;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/24/13
 * Time: 9:48 PM
 */
public class Disty {
    @NotNull
    @Min(0)
    private BigDecimal distyCny;

    @NotNull
    @Min(0)
    private BigDecimal distyUsd;

    public BigDecimal getDistyCny() {
        return distyCny;
    }

    public void setDistyCny(BigDecimal distyCny) {
        this.distyCny = distyCny;
    }

    public BigDecimal getDistyUsd() {
        return distyUsd;
    }

    public void setDistyUsd(BigDecimal distyUsd) {
        this.distyUsd = distyUsd;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Disty{");
        sb.append("distyCny=").append(distyCny);
        sb.append(", distyUsd=").append(distyUsd);
        sb.append('}');
        return sb.toString();
    }
}
