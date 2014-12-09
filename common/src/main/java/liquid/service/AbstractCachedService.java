package liquid.service;

import liquid.persistence.domain.BaseUpdateEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Tao Ma on 12/8/14.
 */
public abstract class AbstractCachedService<E extends BaseUpdateEntity, R extends CrudRepository<E, Long>> extends AbstractService<E, R> {
    private final Map<Long, E> cache = new TreeMap<>();
    private Object lock = new Object();

    @Override
    public Iterable<E> findAll() {
        if (cache.size() == 0) {
            synchronized (lock) {
                if (cache.size() == 0) {
                    reload();
                }
            }
        }
        return cache.values();
    }

    private void reload() {
        Iterable<E> set = super.findAll();
        for (E e : set) {
            cache.put(e.getId(), e);
        }
    }

    public E save(E e) {
        e = super.save(e);
        synchronized (lock) {
            cache.put(e.getId(), e);
        }
        return e;
    }

    public E find(Long id) {
        E e = null;
        e = cache.get(id);
        synchronized (lock) {
            if (null == e) {
                reload();
                e = cache.get(id);
            }
        }
        return e;
    }
}
