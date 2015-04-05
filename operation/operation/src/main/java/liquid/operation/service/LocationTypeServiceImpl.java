package liquid.operation.service;

import liquid.operation.domain.LocationType;
import liquid.operation.repository.LocationTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Tao Ma on 4/5/15.
 */
@Service
public class LocationTypeServiceImpl implements LocationTypeService {

    @Autowired
    private LocationTypeRepository locationTypeRepository;

    @Override
    public Iterable<LocationType> findAll() {
        return locationTypeRepository.findAll();
    }
}
