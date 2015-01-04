package liquid.persistence.repository;

import liquid.persistence.PageRepository;
import liquid.persistence.domain.LocationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/5/13
 * Time: 10:40 AM
 */
public interface LocationRepository extends PageRepository<LocationEntity> {
    List<LocationEntity> findByType(int type);

    Page<LocationEntity> findByType(final Integer type, Pageable pageable);

    Iterable<LocationEntity> findByQueryNameLike(String queryName);

    Iterable<LocationEntity> findByTypeAndQueryNameLike(Integer type, String queryName);

    LocationEntity findByName(String name);

    LocationEntity findByTypeAndName(Integer type, String name);
}
