package liquid.operation.service;

import liquid.operation.domain.Location;
import liquid.operation.domain.LocationType;
import liquid.operation.repository.LocationRepository;
import liquid.service.AbstractCachedService;
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
public class LocationServiceImpl extends AbstractCachedService<Location, LocationRepository>
        implements InternalLocationService {
    @Override
    public void doSaveBefore(Location entity) { }

    @Override
    public Page<Location> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Location> findAll(final Byte typeId, Pageable pageable) {
        return repository.findByTypeId(typeId, pageable);
    }

    @Override
    public List<Location> findYards() {
        return findByTypeId(LocationType.YARD);
    }

    @Override
    public List<Location> findByTypeId(Byte typeId) {
        return repository.findByTypeId(typeId);
    }

    @Override
    public Iterable<Location> findByNameLike(String name) {
        Iterable<Location> locations = repository.findByQueryNameLike("%" + name + "%");
        return locations;
    }

    @Override
    public Iterable<Location> findByTypeAndNameLike(Integer type, String name) {
        Iterable<Location> locations = repository.findByTypeAndQueryNameLike(type, "%" + name + "%");
        return locations;
    }

    @Override
    public Location findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public Location findByTypeAndName(Integer type, String name) {
        return repository.findByTypeAndName(type, name);
    }
}
