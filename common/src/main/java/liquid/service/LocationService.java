package liquid.service;

import liquid.persistence.domain.LocationEntity;
import liquid.persistence.repository.LocationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/13/13
 * Time: 4:59 PM
 */
@Service
public class LocationService extends AbstractCachedService<LocationEntity, LocationRepository> {
    private static final int LOCATION_YARD_TYPE = 3;

    @Override
    public void doSaveBefore(LocationEntity entity) { }

    public Page<LocationEntity> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<LocationEntity> findAll(final Integer type, Pageable pageable) {
        return repository.findByType(type, pageable);
    }

    public List<LocationEntity> findYards() {
        return findByType(LOCATION_YARD_TYPE);
    }

    public List<LocationEntity> findByType(int type) {
        return repository.findByType(type);
    }

    public Iterable<LocationEntity> findByNameLike(String name) {
        Iterable<LocationEntity> locations = repository.findByQueryNameLike("%" + name + "%");
        return locations;
    }

    public Iterable<LocationEntity> findByTypeAndNameLike(Integer type, String name) {
        Iterable<LocationEntity> locations = repository.findByTypeAndQueryNameLike(type, "%" + name + "%");
        return locations;
    }

    public LocationEntity findByName(String name) {
        return repository.findByName(name);
    }

    public LocationEntity findByTypeAndName(Integer type, String name) {
        return repository.findByTypeAndName(type, name);
    }
}
