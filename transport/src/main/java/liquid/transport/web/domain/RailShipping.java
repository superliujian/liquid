package liquid.transport.web.domain;

import liquid.util.DatePattern;
import liquid.validation.constraints.DateFormat;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/19/13
 * Time: 3:18 PM
 */
public class RailShipping extends RailContainer {
    @DateFormat(DatePattern.UNTIL_MINUTE)
    private String ats;

    public String getAts() {
        return ats;
    }

    public void setAts(String ats) {
        this.ats = ats;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append("RailShippingDto{");
        sb.append("ats='").append(ats).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
