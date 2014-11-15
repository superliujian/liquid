package liquid.controller;

import liquid.accounting.facade.PayableSettlementFacade;
import liquid.accounting.persistence.domain.PayableSettlementEntity;
import liquid.accounting.service.PayableSettlementService;
import liquid.accounting.web.domain.PayableSettlement;
import liquid.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by mat on 11/13/14.
 */
@Controller
@RequestMapping("/payable/{orderId}")
public class PayableController {
    private static final Logger logger = LoggerFactory.getLogger(PayableController.class);

    @Autowired
    private PayableSettlementFacade payableSettlementFacade;

    @Autowired
    private PayableSettlementService payableSettlementService;

    @RequestMapping(method = RequestMethod.GET)
    public String initPanel(@PathVariable Long orderId, Model model) {

        Iterable<PayableSettlementEntity> records = payableSettlementService.findByOrderId(orderId);

        PayableSettlement formBean = new PayableSettlement();
        formBean.setAppliedAt(DateUtil.stringOf());
        formBean.setInvoiceNo("");
        formBean.setCnyOfInvoice(0L);
        formBean.setUsdOfInvoice(0L);
        formBean.setPayeeId(0L);
        formBean.setPayeeName("");
        formBean.setCny(0L);
        formBean.setUsd(0L);
        formBean.setPaidAt(DateUtil.stringOf());

        model.addAttribute("orderId", orderId);
        model.addAttribute("formBean", formBean);
        model.addAttribute("records", records);
        return "payable/panel";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String addPayableSettlement(@PathVariable Long orderId, @ModelAttribute("formBean") PayableSettlement formBean) {
        logger.debug("orderId: {}", orderId);
        logger.debug("PayableSettlement: {}", formBean);
        payableSettlementFacade.save(orderId, formBean);
        return "redirect:/payable/" + orderId;
    }
}
