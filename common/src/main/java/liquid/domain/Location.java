package liquid.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Arrays;

/**
 * Created by redbrick9 on 7/9/14.
 */
public class Location {
    @NotNull
    @NotEmpty
    private String name;

    @Size(min = 1, max = 4)
    private Integer[] types;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer[] getTypes() {
        return types;
    }

    public void setTypes(Integer[] types) {
        this.types = types;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Location{");
        sb.append("super=").append(super.toString());
        sb.append(", name='").append(name).append('\'');
        sb.append(", types=").append(Arrays.toString(types));
        sb.append('}');
        return sb.toString();
    }
}
