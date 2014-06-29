package liquid.persistence.repository;

import liquid.persistence.domain.ServiceTypeEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by redbrick9 on 4/26/14.
 */
public interface ServiceTypeRepository extends CrudRepository<ServiceTypeEntity, Long> {
    Iterable<ServiceTypeEntity> findByState(int state);
}
