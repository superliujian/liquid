package liquid.controller;

import liquid.domain.TaxRates;
import liquid.model.Alert;
import liquid.persistence.domain.TaxRateEntity;
import liquid.service.TaxRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * Created by redbrick9 on 8/29/14.
 */
@Controller
@RequestMapping("/tax_rate")
public class TaxRateController {
    private static final Logger logger = LoggerFactory.getLogger(TaxRateController.class);

    @Autowired
    private TaxRateService taxRateService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        Collection<TaxRateEntity> list = taxRateService.findAll();
        TaxRates taxRates = new TaxRates();
        taxRates.setList(list);
        model.addAttribute("taxRates", taxRates);
        return "charge/tax_rate";
    }

    @RequestMapping(method = RequestMethod.POST, params = "addTaxRate")
    public String addTaxRate(@ModelAttribute TaxRates taxRates) {
        taxRates.getList().add(new TaxRateEntity());
        return "charge/tax_rate";
    }

    @RequestMapping(method = RequestMethod.POST, params = "removeTaxRate")
    public String removeRow(@ModelAttribute TaxRates taxRates, HttpServletRequest request) {
        final Integer rowId = Integer.valueOf(request.getParameter("removeTaxRate"));
        taxRates.getList().remove(rowId.intValue());
        return "charge/tax_rate";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String add(@ModelAttribute TaxRates taxRates,
                      RedirectAttributes redirectAttributes) {
        taxRateService.save(taxRates.getList());

        redirectAttributes.addFlashAttribute("alert", new Alert("save.success"));
        return "redirect:/tax_rate";
    }
}
