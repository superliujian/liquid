package liquid.accounting.web.controller;

import liquid.accounting.facade.ReceivableFacadeImpl;
import liquid.accounting.web.domain.ReceivableSummary;
import liquid.domain.TradeType;
import liquid.purchase.facade.ChargeFacade;
import liquid.purchase.web.domain.Charge;
import liquid.purchase.web.domain.ChargeWay;
import liquid.service.ExchangeRateService;
import liquid.web.domain.SearchBarForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.ArrayList;

/**
 * Created by Tao Ma on 1/10/15.
 */
@Controller
@RequestMapping("/accounting")
public class AccountingController {
    private static final int size = 20;

    @Autowired
    private ReceivableFacadeImpl receivableFacade;

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Autowired
    private ChargeFacade chargeFacade;

    @RequestMapping(value = "/gross_profit", method = RequestMethod.GET)
    public String grossProfit(@Valid SearchBarForm searchBarForm,
                              BindingResult bindingResult, Model model) {
        model.addAttribute("tradeTypes", TradeType.values());
        model.addAttribute("exchangeRate", exchangeRateService.getExchangeRate());

        model.addAttribute("contextPath", "/accounting/gross_profit" + SearchBarForm.toQueryStrings(searchBarForm));

        if (bindingResult.hasErrors()) {
            Page<ReceivableSummary> page = new PageImpl<ReceivableSummary>(new ArrayList<>());
            model.addAttribute("page", page);
            return "accounting/gross_profit";
        }

        searchBarForm.setAction("/accounting/gross_profit");
        model.addAttribute("searchBarForm", searchBarForm);

        PageRequest pageRequest = new PageRequest(searchBarForm.getNumber(), size, new Sort(Sort.Direction.DESC, "id"));
        Page<ReceivableSummary> page = receivableFacade.findAll(searchBarForm, pageRequest);
        model.addAttribute("page", page);
        return "accounting/gross_profit";
    }

    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    public String summary(@Valid SearchBarForm searchBarForm,
                          BindingResult bindingResult, Model model) {
        model.addAttribute("tradeTypes", TradeType.values());
        model.addAttribute("exchangeRate", exchangeRateService.getExchangeRate());

        model.addAttribute("contextPath", "/accounting/summary" + SearchBarForm.toQueryStrings(searchBarForm));

        if (bindingResult.hasErrors()) {
            Page<ReceivableSummary> page = new PageImpl<ReceivableSummary>(new ArrayList<>());
            model.addAttribute("page", page);
            return "charge/summary";
        }

        searchBarForm.setAction("/accounting/summary");
        model.addAttribute("searchBarForm", searchBarForm);

        PageRequest pageRequest = new PageRequest(searchBarForm.getNumber(), size, new Sort(Sort.Direction.DESC, "id"));
        Page<ReceivableSummary> page = receivableFacade.findAll(searchBarForm, pageRequest);
        model.addAttribute("page", page);

        return "charge/summary";
    }

    @RequestMapping(value = "/receivable", method = RequestMethod.GET)
    public String listReceivables(@Valid SearchBarForm searchBarForm,
                                  BindingResult bindingResult, Model model) {
        model.addAttribute("tradeTypes", TradeType.values());
        model.addAttribute("exchangeRate", exchangeRateService.getExchangeRate());

        model.addAttribute("contextPath", "/accounting/receivable" + SearchBarForm.toQueryStrings(searchBarForm));

        if (bindingResult.hasErrors()) {
            Page<ReceivableSummary> page = new PageImpl<ReceivableSummary>(new ArrayList<>());
            model.addAttribute("page", page);
            return "charge/receivable";
        }

        searchBarForm.setAction("/accounting/receivable");
        model.addAttribute("searchBarForm", searchBarForm);

        PageRequest pageRequest = new PageRequest(searchBarForm.getNumber(), size, new Sort(Sort.Direction.DESC, "id"));
        Page<ReceivableSummary> page = receivableFacade.findAll(searchBarForm, pageRequest);
        model.addAttribute("page", page);

        return "charge/receivable";
    }

    @RequestMapping(value = "/payable", method = RequestMethod.GET)
    public String payable(@Valid SearchBarForm searchBarForm,
                          BindingResult bindingResult, Model model) {
        model.addAttribute("chargeWays", ChargeWay.values());
        model.addAttribute("exchangeRate", exchangeRateService.getExchangeRate());

        model.addAttribute("contextPath", "/accounting/payable" + SearchBarForm.toQueryStrings(searchBarForm));

        if (bindingResult.hasErrors()) {
            Page<Charge> page = new PageImpl<Charge>(new ArrayList<>());
            model.addAttribute("page", page);
            return "charge/payable";
        }

        searchBarForm.setAction("/accounting/payable");
        model.addAttribute("searchBarForm", searchBarForm);

        PageRequest pageRequest = new PageRequest(searchBarForm.getNumber(), size, new Sort(Sort.Direction.DESC, "id"));
        Page<Charge> page = chargeFacade.findAll(searchBarForm, pageRequest);
        model.addAttribute("page", page);
        return "charge/payable";
    }
}
