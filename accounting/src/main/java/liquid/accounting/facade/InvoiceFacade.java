package liquid.accounting.facade;

import liquid.accounting.persistence.domain.AccountingOperator;
import liquid.accounting.persistence.domain.AccountingType;
import liquid.accounting.persistence.domain.InvoiceEntity;
import liquid.accounting.service.InvoiceService;
import liquid.accounting.service.ReceivableSummaryService;
import liquid.accounting.web.domain.Statement;
import liquid.accounting.web.domain.Invoice;
import liquid.order.persistence.domain.OrderEntity;
import liquid.service.CustomerService;
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
public class InvoiceFacade {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ReceivableSummaryService receivableSummaryService;

    public Statement<Invoice> findByOrderId(Long orderId) {
        Statement<Invoice> statement = new Statement<>();
        List<Invoice> invoiceList = new ArrayList<>();
        Invoice total = new Invoice();

        Iterable<InvoiceEntity> invoiceEntities = invoiceService.findByOrderId(orderId);
        for (InvoiceEntity invoiceEntity : invoiceEntities) {
            Invoice invoice = new Invoice();
            invoice.setId(invoiceEntity.getId());
            invoice.setInvoiceNo(invoiceEntity.getInvoiceNo());
            invoice.setOrderId(orderId);
            invoice.setOrderNo(invoiceEntity.getOrder().getOrderNo());
            invoice.setCny(invoiceEntity.getCny());
            invoice.setUsd(invoiceEntity.getUsd());
            invoice.setBuyerId(invoiceEntity.getBuyerId());
            invoice.setBuyerName(customerService.find(invoice.getBuyerId()).getName());
            invoice.setIssuedAt(DateUtil.dayStrOf(invoiceEntity.getIssuedAt()));
            invoice.setExpectedPaymentAt(DateUtil.dayStrOf(invoiceEntity.getExpectedPaymentAt()));
            invoiceList.add(invoice);
            total.setCny(total.getCny() + invoice.getCny());
            total.setUsd(total.getUsd() + invoice.getUsd());
        }

        statement.setContent(invoiceList);
        statement.setTotal(total);

        return statement;
    }

    @Transactional(value = "transactionManager")
    public Invoice save(Invoice invoice) {
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setId(invoice.getId());
        invoiceEntity.setInvoiceNo(invoice.getInvoiceNo());
        invoiceEntity.setOrder(OrderEntity.newInstance(OrderEntity.class, invoice.getOrderId()));
        invoiceEntity.setCny(invoice.getCny());
        invoiceEntity.setUsd(invoice.getUsd());
        invoiceEntity.setBuyerId(invoice.getBuyerId());
        invoiceEntity.setIssuedAt(DateUtil.dayOf(invoice.getIssuedAt()));
        invoiceEntity.setExpectedPaymentAt(DateUtil.dayOf(invoice.getExpectedPaymentAt()));
        invoiceService.save(invoiceEntity);
        receivableSummaryService.update(invoice.getOrderId(), AccountingType.INVOICE, AccountingOperator.PLUS, invoice.getCny(), invoice.getUsd());
        return invoice;
    }

    @Transactional(value = "transactionManager")
    public Invoice update(Invoice invoice) {
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setId(invoice.getId());
        invoiceEntity.setInvoiceNo(invoice.getInvoiceNo());
        invoiceEntity.setOrder(OrderEntity.newInstance(OrderEntity.class, invoice.getOrderId()));
        invoiceEntity.setCny(invoice.getCny());
        invoiceEntity.setUsd(invoice.getUsd());
        invoiceEntity.setBuyerId(invoice.getBuyerId());
        invoiceEntity.setIssuedAt(DateUtil.dayOf(invoice.getIssuedAt()));
        invoiceEntity.setExpectedPaymentAt(DateUtil.dayOf(invoice.getExpectedPaymentAt()));
        invoiceService.save(invoiceEntity);

        Statement<Invoice> statement = findByOrderId(invoice.getOrderId());
        receivableSummaryService.update(invoice.getOrderId(), AccountingType.INVOICE, statement.getTotal().getCny(), statement.getTotal().getUsd());
        return invoice;
    }
}
