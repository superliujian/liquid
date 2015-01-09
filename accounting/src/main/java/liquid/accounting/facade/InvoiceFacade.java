package liquid.accounting.facade;

import liquid.accounting.persistence.domain.InvoiceEntity;
import liquid.accounting.service.InvoiceService;
import liquid.accounting.web.domain.Invoice;
import liquid.accounting.web.domain.Statement;
import liquid.order.persistence.domain.OrderEntity;
import liquid.service.CustomerService;
import liquid.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Statement<Invoice> findByOrderId(Long orderId) {
        Statement<Invoice> statement = new Statement<>();
        List<Invoice> invoiceList = new ArrayList<>();
        Long cnyTotal = 0L, usdTotal = 0L;

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
            cnyTotal += invoice.getCny();
            usdTotal += invoice.getUsd();
        }

        statement.setContent(invoiceList);
        statement.setCnyTotal(cnyTotal);
        statement.setUsdTotal(usdTotal);

        return statement;
    }

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
        return invoice;
    }
}
