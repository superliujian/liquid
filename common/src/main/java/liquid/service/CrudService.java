package liquid.service;

/**
 * Created by Tao Ma on 4/2/15.
 */
public interface CrudService<E> {
    E find(Long id);
}
