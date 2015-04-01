package liquid.accounting.persistence.repository;

import liquid.accounting.persistence.domain.SettlementEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Tao Ma on 1/10/15.
 */
public interface SettlementRepository extends CrudRepository<SettlementEntity, Long> {
    Iterable<SettlementEntity> findByOrderId(Long orderId);
}
