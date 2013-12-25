package liquid.persistence.repository;

import liquid.persistence.domain.Service;
import liquid.persistence.domain.ServiceProvider;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 * Created by tao on 12/25/13.
 */
public interface ServiceRepository extends CrudRepository<Service, Long> {
//    Iterable<Service> findByChangeTypeId(long changeTypeId);

    Collection<Service> findBySp(ServiceProvider serviceProvider);

    Collection<Service> findByName(String name);
}