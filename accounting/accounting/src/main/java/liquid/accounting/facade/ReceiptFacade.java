package liquid.accounting.facade;

import liquid.accounting.persistence.domain.AccountingOperator;
import liquid.accounting.persistence.domain.AccountingType;
import liquid.accounting.persistence.domain.ReceiptEntity;
import liquid.accounting.service.ReceiptService;
import liquid.accounting.service.ReceivableSummaryService;
import liquid.accounting.web.domain.Receipt;
import liquid.accounting.web.domain.Statement;
import liquid.order.domain.OrderEntity;
import liquid.order.service.OrderService;
import liquid.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tao Ma on 1/8/15.
 */
@Service
public class ReceiptFacade {
    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ReceivableSummaryService receivableSummaryService;

    public Statement<Receipt> findByOrderId(Long orderId) {
        Statement<Receipt> statement = new Statement<>();
        List<Receipt> receiptList = new ArrayList<>();
        Receipt total = new Receipt();

        Iterable<ReceiptEntity> receiptEntities = receiptService.findByOrderId(orderId);
        for (ReceiptEntity receiptEntity : receiptEntities) {
            Receipt receipt = new Receipt();
            receipt.setId(receiptEntity.getId());
            receipt.setOrderId(receiptEntity.getOrder().getId());
            receipt.setOrderNo(receiptEntity.getOrder().getOrderNo());
            receipt.setCny(receiptEntity.getCny());
            receipt.setUsd(receiptEntity.getUsd());
            receipt.setIssuedAt(DateUtil.dayStrOf(receiptEntity.getIssuedAt()));
            receiptList.add(receipt);
            total.setCny(total.getCny() + receipt.getCny());
            total.setUsd(total.getUsd() + receipt.getUsd());
        }
        statement.setContent(receiptList);
        statement.setTotal(total);

        return statement;
    }

    @Transactional(value = "transactionManager")
    public Receipt save(Receipt receipt) {
        OrderEntity orderEntity = orderService.find(receipt.getOrderId());

        ReceiptEntity receiptEntity = new ReceiptEntity();
        receiptEntity.setId(receipt.getId());
        receiptEntity.setOrder(orderEntity);
        receiptEntity.setPayerId(orderEntity.getCustomerId());
        receiptEntity.setCny(receipt.getCny());
        receiptEntity.setUsd(receipt.getUsd());
        receiptEntity.setIssuedAt(DateUtil.dayOf(receipt.getIssuedAt()));
        receiptService.save(receiptEntity);
        receivableSummaryService.update(receipt.getOrderId(), AccountingType.PAYMENT, AccountingOperator.PLUS, receipt.getCny(), receipt.getUsd());
        return receipt;
    }

    @Transactional(value = "transactionManager")
    public Receipt update(Receipt receipt) {
        OrderEntity orderEntity = orderService.find(receipt.getOrderId());

        ReceiptEntity receiptEntity = new ReceiptEntity();
        receiptEntity.setId(receipt.getId());
        receiptEntity.setOrder(orderEntity);
        receiptEntity.setPayerId(orderEntity.getCustomerId());
        receiptEntity.setCny(receipt.getCny());
        receiptEntity.setUsd(receipt.getUsd());
        receiptEntity.setIssuedAt(DateUtil.dayOf(receipt.getIssuedAt()));
        receiptService.save(receiptEntity);

        Statement<Receipt> statement = findByOrderId(receipt.getOrderId());
        receivableSummaryService.update(receipt.getOrderId(), AccountingType.PAYMENT, statement.getTotal().getCny(), statement.getTotal().getUsd());
        return receipt;
    }
}
