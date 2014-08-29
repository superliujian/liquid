package liquid.order.service;

import liquid.order.persistence.domain.ReceivableSettlementEntity;
import liquid.order.persistence.repository.ReceivableSettlementRepository;
import liquid.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by redbrick9 on 8/29/14.
 */
@Service
public class ReceivableSettlementService extends AbstractService<ReceivableSettlementEntity, ReceivableSettlementRepository> {

    @Autowired
    private ReceivableSettlementRepository receivableSettlementRepository;

    @Override
    public void doSaveBefore(ReceivableSettlementEntity receivableSettlementEntity) { }

    public Iterable<ReceivableSettlementEntity> findByOrderId(Long orderId) {
        return receivableSettlementRepository.findByOrderId(orderId);
    }
}
