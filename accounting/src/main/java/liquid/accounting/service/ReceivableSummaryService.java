package liquid.accounting.service;

import liquid.accounting.persistence.domain.AccountingOperator;
import liquid.accounting.persistence.domain.AccountingType;
import liquid.accounting.persistence.domain.ReceivableSummaryEntity;
import liquid.accounting.persistence.repository.ReceivableSummaryRepository;
import liquid.service.AbstractService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Tao Ma on 1/10/15.
 */
@Service
public class ReceivableSummaryService extends AbstractService<ReceivableSummaryEntity, ReceivableSummaryRepository> {
    @Override
    public void doSaveBefore(ReceivableSummaryEntity entity) {}

    public ReceivableSummaryEntity findByOrderId(Long orderId) {
        return repository.findByOrderId(orderId);
    }

    @Transactional(value = "transactionManager")
    public void update(Long orderId, AccountingType type, AccountingOperator op, Long amountCny, Long amountUsd) {
        ReceivableSummaryEntity entity = repository.findByOrderId(orderId);
        switch (type) {
            case SETTLEMENT:
                switch (op) {
                    case PLUS:
                    case MINUS:
                        break;
                }
                break;
            case INVOICE:
                switch (op) {
                    case PLUS:
                        entity.setInvoicedCny(entity.getInvoicedCny() + amountCny);
                        entity.setInvoicedUsd(entity.getInvoicedUsd() + amountUsd);
                        break;
                    case MINUS:
                        entity.setInvoicedCny(entity.getInvoicedCny() - amountCny);
                        entity.setInvoicedUsd(entity.getInvoicedUsd() - amountUsd);
                }
                break;
            case PAYMENT:
                switch (op) {
                    case PLUS:
                        entity.setPaidCny(entity.getPaidCny() + amountCny);
                        entity.setPaidUsd(entity.getPaidUsd() + amountUsd);
                        break;
                    case MINUS:
                        entity.setPaidCny(entity.getPaidCny() - amountCny);
                        entity.setPaidUsd(entity.getPaidUsd() - amountUsd);
                        break;
                }
                break;
        }
        save(entity);
    }

    @Transactional(value = "transactionManager")
    public void update(Long orderId, AccountingType type, Long totalCny, Long totalUsd) {
        ReceivableSummaryEntity entity = repository.findByOrderId(orderId);
        switch (type) {
            case SETTLEMENT:
                break;
            case INVOICE:
                entity.setInvoicedCny(totalCny);
                entity.setInvoicedUsd(totalUsd);
                break;
            case PAYMENT:
                entity.setPaidCny(totalCny);
                entity.setPaidUsd(totalUsd);
                break;
        }
        save(entity);
    }

    public Page<ReceivableSummaryEntity> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
