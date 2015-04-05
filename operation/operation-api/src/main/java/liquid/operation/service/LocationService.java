package liquid.operation.service;

import liquid.operation.domain.Location;

import java.util.List;

/**
 * Created by Tao Ma on 4/5/15.
 */
public interface LocationService {
    List<Location> findYards();

    List<Location> findByTypeId(Byte typeId);

    Location find(Long id);

    Location findByName(String name);

    Location findByTypeAndName(Integer type, String name);
}
