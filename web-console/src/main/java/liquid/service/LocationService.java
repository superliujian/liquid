package liquid.service;

import liquid.persistence.domain.Location;
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

    @Autowired
    private LocationRepository locationRepository;

    public List<Location> findByType(int type) {
        return locationRepository.findByType(type);
    }

    public Location find(long id) {
        return locationRepository.findOne(id);
    }
}
