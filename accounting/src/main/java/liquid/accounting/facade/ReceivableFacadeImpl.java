package liquid.accounting.facade;

import liquid.accounting.persistence.domain.ReceivableSummaryEntity;
import liquid.accounting.service.ReceivableSummaryService;
import liquid.accounting.web.domain.ReceivableSummary;
import liquid.order.facade.OrderFacade;
import liquid.order.persistence.domain.OrderEntity;
import liquid.util.DateUtil;
import liquid.web.domain.EnhancedPageImpl;
import liquid.web.domain.SearchBarForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    public Page<ReceivableSummary> findAll(SearchBarForm searchBar, Pageable pageable) {
        List<ReceivableSummary> receivableList = new ArrayList<>();
        Page<ReceivableSummaryEntity> entityPage = null;
        if ("customer".equals(searchBar.getType())) {
            entityPage = receivableSummaryService.findAll(DateUtil.dayOf(searchBar.getStartDate()), DateUtil.dayOf(searchBar.getEndDate()), null, searchBar.getId(), pageable);
        } else if ("order".equals(searchBar.getType())) {
            entityPage = receivableSummaryService.findAll(DateUtil.dayOf(searchBar.getStartDate()), DateUtil.dayOf(searchBar.getEndDate()), searchBar.getId(), null, pageable);
        } else {
            entityPage = receivableSummaryService.findAll(DateUtil.dayOf(searchBar.getStartDate()), DateUtil.dayOf(searchBar.getEndDate()), null, null, pageable);
        }

        ReceivableSummary sum = new ReceivableSummary();
        for (ReceivableSummaryEntity entity : entityPage) {
            ReceivableSummary receivable = convert(entity);
            orderFacade.convert(entity.getOrder(), receivable);
            receivableList.add(receivable);

            sum.setContainerQuantity(sum.getContainerQuantity() + receivable.getContainerQuantity());
            sum.setCny(sum.getCny() + receivable.getCny());
            sum.setUsd(sum.getUsd() + receivable.getUsd());
            sum.setRemainingBalanceCny(sum.getRemainingBalanceCny() + receivable.getRemainingBalanceCny());
            sum.setRemainingBalanceUsd(sum.getRemainingBalanceUsd() + receivable.getRemainingBalanceUsd());
            sum.setPaidCny(sum.getPaidCny() + receivable.getPaidCny());
            sum.setPaidUsd(sum.getPaidUsd() + receivable.getPaidUsd());
            sum.setInvoicedCny(sum.getInvoicedCny() + receivable.getInvoicedCny());
            sum.setInvoicedUsd(sum.getInvoicedUsd() + receivable.getInvoicedUsd());

            sum.setDistyPrice(sum.getDistyPrice() + receivable.getDistyPrice());
            sum.setDistyUsd(sum.getDistyUsd() + receivable.getDistyUsd());
            sum.setDistyUsd(sum.getDistyUsd() + receivable.getDistyUsd());
            sum.setGrandTotal(sum.getGrandTotal() + receivable.getGrandTotal());
        }
        return new EnhancedPageImpl<ReceivableSummary>(receivableList, pageable, entityPage.getTotalElements(), sum);
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
        receivableSummary.setOrderId(entity.getOrder().getId());
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
        entity.setOrder(OrderEntity.newInstance(OrderEntity.class, receivable.getOrderId()));
        return entity;
    }
}
