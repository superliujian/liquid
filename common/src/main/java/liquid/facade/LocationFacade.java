package liquid.facade;

import liquid.domain.Location;
import liquid.persistence.domain.LocationEntity;
import liquid.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by redbrick9 on 7/9/14.
 */
@Service
public class LocationFacade {

    @Autowired
    private LocationService locationService;

    public Iterable<LocationEntity> save(Location location) {
        List<LocationEntity> entities = new ArrayList<>();
        for (Integer type : location.getTypes()) {
            entities.add(new LocationEntity(location.getName(), type));
        }
        return locationService.save(entities);
    }
}
