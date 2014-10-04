package liquid.container.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mat on 10/4/14.
 */
public class Containers {
    private List<Container> list = new ArrayList<>();

    public List<Container> getList() {
        return list;
    }

    public void setList(List<Container> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Containers{");
        sb.append("list=").append(list);
        sb.append('}');
        return sb.toString();
    }
}
