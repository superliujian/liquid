package liquid.service;

import liquid.persistence.domain.LocationEntity;
import liquid.persistence.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
public class LocationService extends AbstractService<LocationEntity, LocationRepository> {
    private static final int LOCATION_YARD_TYPE = 3;

    @Autowired
    private LocationRepository locationRepository;

    public Page<LocationEntity> findAll(Pageable pageable) {
        return locationRepository.findAll(pageable);
    }

    public Page<LocationEntity> findAll(final Integer type, Pageable pageable) {
        return locationRepository.findByType(type, pageable);
    }

    @Override
    public void doSaveBefore(LocationEntity entity) { }

    public LocationEntity find(Long id) {
        return locationRepository.findOne(id);
    }

    public List<LocationEntity> findYards() {
        return findByType(LOCATION_YARD_TYPE);
    }

    public List<LocationEntity> findByType(int type) {
        return locationRepository.findByType(type);
    }
}
