package liquid.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by redbrick9 on 7/3/14.
 */
public interface PageRepository<E> extends CrudRepository<E, Long> {
    Page<E> findAll(Pageable pageable);
}
