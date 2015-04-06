package liquid.operation.repository;

import liquid.operation.domain.Location;
import liquid.persistence.PageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/5/13
 * Time: 10:40 AM
 */
public interface LocationRepository extends PageRepository<Location> {
    List<Location> findByTypeId(Byte type);

    Page<Location> findByTypeId(Byte typeId, Pageable pageable);

    Iterable<Location> findByQueryNameLike(String queryName);

    Iterable<Location> findByTypeIdAndQueryNameLike(Byte typeId, String queryName);

    Location findByName(String name);

    Location findByTypeIdAndName(Byte typeId, String name);
}
