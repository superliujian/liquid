package liquid.accounting.service;

import liquid.accounting.persistence.domain.PayableSettlementEntity;
import liquid.accounting.persistence.repository.PayableSettlementRepository;
import liquid.service.AbstractService;
import org.springframework.stereotype.Service;

/**
 * Created by mat on 11/15/14.
 */
@Service
public class PayableSettlementService extends AbstractService<PayableSettlementEntity, PayableSettlementRepository> {
    @Override
    public void doSaveBefore(PayableSettlementEntity entity) { }

    public Iterable<PayableSettlementEntity> findByOrderId(Long orderId) {
        return repository.findByOrderId(orderId);
    }
}
