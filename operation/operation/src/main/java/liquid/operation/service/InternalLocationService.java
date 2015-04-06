package liquid.operation.service;

import liquid.operation.domain.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by Tao Ma on 4/5/15.
 */
public interface InternalLocationService extends LocationService {
    Iterable<Location> findByNameLike(String name);

    Iterable<Location> findByTypeAndNameLike(Byte typeId, String name);

    Page<Location> findAll(Pageable pageable);

    Page<Location> findAll(Byte typeId, Pageable pageable);

    Location save(Location location);

    Iterable<Location> save(Iterable<Location> locations);
}
