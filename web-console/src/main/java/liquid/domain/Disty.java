package liquid.domain;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/24/13
 * Time: 9:48 PM
 */
public class Disty {
    @NotNull
    @Min(0)
    private Long distyCny;

    @NotNull
    @Min(0)
    private Long distyUsd;

    public Long getDistyCny() {
        return distyCny;
    }

    public void setDistyCny(Long distyCny) {
        this.distyCny = distyCny;
    }

    public Long getDistyUsd() {
        return distyUsd;
    }

    public void setDistyUsd(Long distyUsd) {
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
