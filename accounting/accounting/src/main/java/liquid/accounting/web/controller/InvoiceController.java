package liquid.accounting.web.controller;

import liquid.accounting.facade.InvoiceFacade;
import liquid.accounting.facade.ReceiptFacade;
import liquid.accounting.web.domain.Invoice;
import liquid.accounting.web.domain.Receipt;
import liquid.accounting.web.domain.Statement;
import liquid.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Created by Tao Ma on 1/8/15.
 */
@Controller
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceFacade invoiceFacade;

    @Autowired
    private ReceiptFacade receiptFacade;

    @RequestMapping(method = RequestMethod.POST)
    public String add(@Valid @ModelAttribute Invoice invoice,
                      BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Statement<Invoice> statement = invoiceFacade.findByOrderId(invoice.getOrderId());
            model.addAttribute("statement", statement);

            Statement<Receipt> receiptStatement = receiptFacade.findByOrderId(invoice.getOrderId());
            model.addAttribute("receiptStatement", receiptStatement);
            Receipt receipt = new Receipt();
            receipt.setOrderId(invoice.getOrderId());
            receipt.setIssuedAt(DateUtil.dayStrOf());
            model.addAttribute("receipt", receipt);
            return "receivable/panel";
        }
        invoiceFacade.save(invoice);
        return "redirect:" + "/receivable/120";
    }
}
