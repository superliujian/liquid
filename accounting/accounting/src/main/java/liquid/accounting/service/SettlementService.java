package liquid.accounting.service;

import liquid.accounting.persistence.domain.SettlementEntity;
import liquid.accounting.persistence.repository.SettlementRepository;
import liquid.service.AbstractService;
import org.springframework.stereotype.Service;

/**
 * Created by Tao Ma on 1/10/15.
 */
@Service
public class SettlementService extends AbstractService<SettlementEntity, SettlementRepository> {
    @Override
    public void doSaveBefore(SettlementEntity entity) {}

    public Iterable<SettlementEntity> findByOrderId(Long orderId) {
        return repository.findByOrderId(orderId);
    }
}
