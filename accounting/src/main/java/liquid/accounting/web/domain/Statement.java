package liquid.accounting.web.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Tao Ma on 1/13/15.
 */
public class Statement<T> implements Iterable<T> {
    private List<T> content = new ArrayList<>();
    private T total;

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public T getTotal() {
        return total;
    }

    public void setTotal(T total) {
        this.total = total;
    }

    @Override
    public Iterator<T> iterator() {
        return content.iterator();
    }
}
