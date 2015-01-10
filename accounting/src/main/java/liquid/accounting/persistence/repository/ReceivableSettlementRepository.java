package liquid.accounting.persistence.repository;

import liquid.accounting.persistence.domain.ReceivableSettlementEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by redbrick9 on 8/29/14.
 */
public interface ReceivableSettlementRepository extends CrudRepository<ReceivableSettlementEntity, Long> {

    Iterable<ReceivableSettlementEntity> findByOrderId(Long orderId);
}
