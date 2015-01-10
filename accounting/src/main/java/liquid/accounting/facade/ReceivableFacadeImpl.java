package liquid.accounting.facade;

import liquid.accounting.persistence.domain.ReceivableSummaryEntity;
import liquid.accounting.service.ReceivableSummaryService;
import liquid.accounting.web.domain.ReceivableSummary;
import liquid.order.facade.OrderFacade;
import liquid.order.persistence.domain.OrderEntity;
import liquid.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tao Ma on 1/10/15.
 */
@Service
public class ReceivableFacadeImpl implements ReceivableFacade {

    @Autowired
    private ReceivableSummaryService receivableSummaryService;

    @Autowired
    private OrderFacade orderFacade;

    public Page<ReceivableSummary> findAll(Pageable pageable) {
        List<ReceivableSummary> receivableList = new ArrayList<>();
        Page<ReceivableSummaryEntity> entityPage = receivableSummaryService.findAll(pageable);
        for (ReceivableSummaryEntity entity : entityPage) {
            ReceivableSummary receivable = convert(entity);
            receivable.setOrder(orderFacade.convertBasic(entity.getOrder()));
            receivableList.add(receivable);
        }
        return new PageImpl<ReceivableSummary>(receivableList, pageable, entityPage.getTotalElements());
    }

    public ReceivableSummary save(ReceivableSummary receivableSummary) {
        ReceivableSummaryEntity entity = convert(receivableSummary);
        receivableSummaryService.save(entity);
        return receivableSummary;
    }

    private ReceivableSummary convert(ReceivableSummaryEntity entity) {
        ReceivableSummary receivableSummary = new ReceivableSummary();
        receivableSummary.setId(entity.getId());
        receivableSummary.setCny(entity.getCny());
        receivableSummary.setUsd(entity.getUsd());
        receivableSummary.setPrepaidTime(DateUtil.stringOf(entity.getPrepaidTime()));
        receivableSummary.setRemainingBalanceCny(entity.getRemainingBalanceCny());
        receivableSummary.setRemainingBalanceUsd(entity.getRemainingBalanceUsd());
        receivableSummary.setPaidCny(entity.getPaidCny());
        receivableSummary.setPaidUsd(entity.getPaidUsd());
        receivableSummary.setInvoicedCny(entity.getInvoicedCny());
        receivableSummary.setInvoicedUsd(entity.getInvoicedUsd());
        return receivableSummary;
    }

    private ReceivableSummaryEntity convert(ReceivableSummary receivable) {
        ReceivableSummaryEntity entity = new ReceivableSummaryEntity();
        entity.setId(receivable.getId());
        entity.setCny(receivable.getCny());
        entity.setUsd(receivable.getUsd());
        entity.setPrepaidTime(DateUtil.dateOf(receivable.getPrepaidTime()));
        entity.setRemainingBalanceCny(receivable.getRemainingBalanceCny());
        entity.setRemainingBalanceUsd(receivable.getRemainingBalanceUsd());
        entity.setPaidCny(receivable.getPaidCny());
        entity.setPaidUsd(receivable.getPaidUsd());
        entity.setInvoicedCny(receivable.getInvoicedCny());
        entity.setInvoicedUsd(receivable.getInvoicedUsd());
        entity.setOrder(OrderEntity.newInstance(OrderEntity.class, receivable.getOrder().getId()));
        return entity;
    }
}
