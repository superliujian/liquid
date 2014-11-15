package liquid.accounting.facade;

import liquid.accounting.persistence.domain.PayableSettlementEntity;
import liquid.accounting.service.PayableSettlementService;
import liquid.accounting.web.domain.PayableSettlement;
import liquid.order.persistence.domain.OrderEntity;
import liquid.persistence.domain.ServiceProviderEntity;
import liquid.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by mat on 11/15/14.
 */
@Service
public class PayableSettlementFacade {

    @Autowired
    private PayableSettlementService payableSettlementService;

    public void save(Long orderId, PayableSettlement payableSettlement) {
        PayableSettlementEntity entity = new PayableSettlementEntity();
        entity.setOrder(OrderEntity.newInstance(OrderEntity.class, orderId));
        entity.setAppliedAt(DateUtil.dateOf(payableSettlement.getAppliedAt()));
        entity.setInvoiceNo(payableSettlement.getInvoiceNo());
        entity.setCnyOfInvoice(payableSettlement.getCnyOfInvoice());
        entity.setUsdOfInvoice(payableSettlement.getUsdOfInvoice());
        entity.setPayee(ServiceProviderEntity.newInstance(ServiceProviderEntity.class, payableSettlement.getPayeeId()));
        entity.setCny(payableSettlement.getCny());
        entity.setUsd(payableSettlement.getUsd());
        entity.setPaidAt(DateUtil.dateOf(payableSettlement.getPaidAt()));
        payableSettlementService.save(entity);
    }
}
