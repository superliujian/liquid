package liquid.shipping.domain;

import liquid.util.DatePattern;
import liquid.validation.constraints.DateFormat;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/19/13
 * Time: 2:21 PM
 */
public class RailPlanDto extends RailContainerDto {
    @NotNull
    @NotEmpty
    private String planNo;

    @DateFormat(DatePattern.UNTIL_DAY)
    private String ets;

    public String getPlanNo() {
        return planNo;
    }

    public void setPlanNo(String planNo) {
        this.planNo = planNo;
    }

    public String getEts() {
        return ets;
    }

    public void setEts(String ets) {
        this.ets = ets;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("RailPlanDto{");
        sb.append("planNo='").append(planNo).append('\'');
        sb.append(", ets='").append(ets).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
