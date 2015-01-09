package liquid.accounting.web.controller;

import liquid.accounting.facade.InvoiceFacade;
import liquid.accounting.web.domain.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Tao Ma on 1/8/15.
 */
@Controller
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceFacade invoiceFacade;

    @RequestMapping(method = RequestMethod.POST)
    public String add(@ModelAttribute(value = "invoice") Invoice invoice, @RequestHeader(value = "referer", required = false) final String referer) {
        invoiceFacade.save(invoice);
        return "redirect:" + referer;
    }
}
