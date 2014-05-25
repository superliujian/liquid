package liquid.persistence.repository;

import liquid.persistence.domain.LocationEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/5/13
 * Time: 10:40 AM
 */
public interface LocationRepository extends CrudRepository<LocationEntity, Long> {
    List<LocationEntity> findByType(int type);
}
