package liquid.accounting.service;

import liquid.order.persistence.domain.OrderEntity;
import liquid.accounting.persistence.domain.ReceivableSettlementEntity;
import liquid.accounting.persistence.domain.ReceivableSummaryEntity;
import liquid.accounting.persistence.repository.ReceivableSettlementRepository;
import liquid.order.service.OrderService;
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
    private ReceivableSummaryService receivableSummaryService;

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
        ReceivableSummaryEntity summary = receivableSummaryService.findByOrderId(orderId);
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

        // db
        save(settlement);
    }
}
