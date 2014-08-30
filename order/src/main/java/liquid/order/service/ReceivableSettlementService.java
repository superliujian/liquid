package liquid.order.service;

import liquid.order.persistence.domain.OrderEntity;
import liquid.order.persistence.domain.ReceivableSettlementEntity;
import liquid.order.persistence.domain.ReceivableSummaryEntity;
import liquid.order.persistence.repository.ReceivableSettlementRepository;
import liquid.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by redbrick9 on 8/29/14.
 */
@Service
public class ReceivableSettlementService extends AbstractService<ReceivableSettlementEntity, ReceivableSettlementRepository> {

    @Autowired
    private ReceivableSettlementRepository receivableSettlementRepository;

    @Autowired
    private OrderService orderService;

    @Override
    public void doSaveBefore(ReceivableSettlementEntity receivableSettlementEntity) { }

    public Iterable<ReceivableSettlementEntity> findByOrderId(Long orderId) {
        return receivableSettlementRepository.findByOrderId(orderId);
    }

    @Transactional("transactionManager")
    public void addSettlement(Long orderId, ReceivableSettlementEntity settlement) {
        // db
        OrderEntity order = orderService.find(orderId);
        // db
        ReceivableSummaryEntity summary = order.getReceivableSummary();
        Long remainingCny = summary.getCny();
        Long remainingUsd = summary.getUsd();
        // db
        Iterable<ReceivableSettlementEntity> settlements = findByOrderId(orderId);
        for (ReceivableSettlementEntity s : settlements) {
            remainingCny -= s.getCny();
            remainingUsd -= s.getUsd();
        }
        summary.setRemainingBalanceCny(remainingCny - settlement.getCny());
        summary.setRemainingBalanceUsd(remainingUsd - settlement.getUsd());
        order.setReceivableSummary(summary);
        // db
        orderService.save(order);

        // db
        save(settlement);
    }
}
