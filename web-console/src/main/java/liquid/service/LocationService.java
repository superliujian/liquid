package liquid.service;

import liquid.persistence.domain.LocationEntity;
import liquid.persistence.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/13/13
 * Time: 4:59 PM
 */
@Service
public class LocationService {
    private static final int LOCATION_YARD_TYPE = 3;

    @Autowired
    private LocationRepository locationRepository;

    public List<LocationEntity> findByType(int type) {
        return locationRepository.findByType(type);
    }

    public LocationEntity find(long id) {
        return locationRepository.findOne(id);
    }

    public List<LocationEntity> findYards() {
        return findByType(LOCATION_YARD_TYPE);
    }
}
