package liquid.persistence.repository;

import liquid.persistence.PageRepository;
import liquid.persistence.domain.LocationEntity;

import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/5/13
 * Time: 10:40 AM
 */
public interface LocationRepository extends PageRepository<LocationEntity> {
    List<LocationEntity> findByType(int type);
}
