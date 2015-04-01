package liquid.order.persistence.repository;

import liquid.order.persistence.domain.ServiceItemEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by tao on 12/25/13.
 */
public interface ServiceItemRepository extends CrudRepository<ServiceItemEntity, Long> {}