package liquid.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Arrays;

/**
 * Created by redbrick9 on 7/9/14.
 */
public class LocationForm {
    @NotNull
    @NotEmpty
    private String name;

    @Size(min = 1, max = 4)
    private Byte[] types;

    private String queryName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte[] getTypes() {
        return types;
    }

    public void setTypes(Byte[] types) {
        this.types = types;
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Location{");
        sb.append("name='").append(name).append('\'');
        sb.append(", types=").append(Arrays.toString(types));
        sb.append(", queryName='").append(queryName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
