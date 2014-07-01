package liquid.persistence.repository;

import liquid.persistence.domain.GoodsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/28/13
 * Time: 3:31 PM
 */

public interface GoodsRepository extends CrudRepository<GoodsEntity, Long> {
    Page<GoodsEntity> findAll(Pageable pageable);
}
