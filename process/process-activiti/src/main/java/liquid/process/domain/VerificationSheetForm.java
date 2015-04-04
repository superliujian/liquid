package liquid.process.domain;

import liquid.process.web.domain.TaskForm;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by Tao Ma on 12/31/14.
 */
public class VerificationSheetForm extends TaskForm {
    @NotNull
    @NotEmpty
    private String sn;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Class=VerificationSheetForm");
        sb.append(", sn='").append(sn).append('\'');
        sb.append(", ").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}
