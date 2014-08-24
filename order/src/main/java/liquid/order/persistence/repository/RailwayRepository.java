package liquid.order.persistence.repository;

import liquid.order.persistence.domain.RailwayEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by redbrick9 on 8/22/14.
 */
public interface RailwayRepository extends CrudRepository<RailwayEntity, Long> {
    RailwayEntity findByOrderId(Long orderId);
}
