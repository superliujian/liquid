package liquid.operation.repository;

import liquid.operation.domain.LocationType;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.Cacheable;

/**
 * Created by Tao Ma on 4/5/15.
 */
public interface LocationTypeRepository extends CrudRepository<LocationType, Byte> {}
