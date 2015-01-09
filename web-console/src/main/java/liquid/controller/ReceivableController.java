package liquid.controller;

import liquid.accounting.facade.InvoiceFacade;
import liquid.accounting.facade.ReceiptFacade;
import liquid.accounting.web.domain.Invoice;
import liquid.accounting.web.domain.Receipt;
import liquid.accounting.web.domain.Statement;
import liquid.order.domain.ReceivableSettlement;
import liquid.order.persistence.domain.OrderEntity;
import liquid.order.persistence.domain.ReceivableSettlementEntity;
import liquid.order.service.OrderService;
import liquid.order.service.ReceivableSettlementService;
import liquid.persistence.domain.CustomerEntity;
import liquid.service.CustomerService;
import liquid.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Created by redbrick9 on 8/29/14.
 */
@Controller
@RequestMapping("/receivable/{orderId}")
public class ReceivableController {
    private static final Logger logger = LoggerFactory.getLogger(ReceivableController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ReceivableSettlementService receivableSettlementService;

    @Autowired
    private InvoiceFacade invoiceFacade;

    @Autowired
    private ReceiptFacade receiptFacade;

    @RequestMapping(method = RequestMethod.GET)
    public String initPanel(@PathVariable Long orderId, Model model) {
        OrderEntity order = orderService.find(orderId);

        ReceivableSettlement formBean = new ReceivableSettlement();

        Long cnyOfInvoice = order.getReceivableSummary().getCny();
        Long usdOfInvoice = order.getReceivableSummary().getUsd();
        Iterable<ReceivableSettlementEntity> records = receivableSettlementService.findByOrderId(orderId);
        for (ReceivableSettlementEntity record : records) {
            cnyOfInvoice -= record.getCny();
            usdOfInvoice -= record.getUsd();
        }

        formBean.setCnyOfInvoice(cnyOfInvoice);
        formBean.setUsdOfInvoice(usdOfInvoice);
        formBean.setPayerId(order.getCustomerId());
        formBean.setPayerName(customerService.find(order.getCustomerId()).getName());
        formBean.setDateOfInvoice(DateUtil.dayStrOf());
        formBean.setExpectedDateOfReceivable(DateUtil.dayStrOf());
        formBean.setCny(cnyOfInvoice);
        formBean.setUsd(usdOfInvoice);
        formBean.setDateOfReceivable(DateUtil.dayStrOf());

        model.addAttribute("orderId", orderId);
        model.addAttribute("formBean", formBean);
        model.addAttribute("records", records);

        Statement<Invoice> statement = invoiceFacade.findByOrderId(orderId);
        model.addAttribute("statement", statement);
        Invoice invoice = new Invoice();
        invoice.setOrderId(orderId);
        invoice.setIssuedAt(DateUtil.dayStrOf());
        invoice.setBuyerId(order.getCustomerId());
        invoice.setBuyerName(customerService.find(order.getCustomerId()).getName());
        invoice.setExpectedPaymentAt(DateUtil.dayStrOf());
        model.addAttribute("invoice", invoice);

        Statement<Receipt> receiptStatement = receiptFacade.findByOrderId(orderId);
        model.addAttribute("receiptStatement", receiptStatement);
        Receipt receipt = new Receipt();
        receipt.setOrderId(orderId);
        receipt.setIssuedAt(DateUtil.dayStrOf());
        model.addAttribute("receipt", receipt);

        return "receivable/panel";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String addReceivableSettlement(@PathVariable Long orderId, @Valid @ModelAttribute("formBean") ReceivableSettlement formBean,
                                          BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {

            return "receivable/panel";
        }

        ReceivableSettlementEntity entity = new ReceivableSettlementEntity();
        entity.setOrder(OrderEntity.newInstance(OrderEntity.class, orderId));
        entity.setInvoiceNo(formBean.getInvoiceNo());
        entity.setCnyOfInvoice(formBean.getCnyOfInvoice());
        entity.setUsdOfInvoice(formBean.getUsdOfInvoice());
        entity.setPayer(CustomerEntity.newInstance(CustomerEntity.class, formBean.getPayerId()));
        entity.setDateOfInvoice(DateUtil.dayOf(formBean.getDateOfInvoice()));
        entity.setExpectedDateOfReceivable(DateUtil.dayOf(formBean.getExpectedDateOfReceivable()));
        entity.setCny(formBean.getCny());
        entity.setUsd(formBean.getUsd());
        entity.setDateOfReceivable(DateUtil.dayOf(formBean.getDateOfReceivable()));

        receivableSettlementService.addSettlement(orderId, entity);
        return "redirect:/receivable/" + orderId;
    }
}
