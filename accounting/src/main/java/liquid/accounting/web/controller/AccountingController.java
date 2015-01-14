package liquid.accounting.web.controller;

import liquid.accounting.facade.ReceivableFacadeImpl;
import liquid.accounting.persistence.domain.ChargeEntity;
import liquid.accounting.web.domain.ReceivableSummary;
import liquid.domain.TradeType;
import liquid.persistence.domain.ExchangeRate;
import liquid.persistence.repository.ExchangeRateRepository;
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
import org.springframework.web.bind.annotation.RequestParam;

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
    private ExchangeRateRepository exchangeRateRepository;

    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    public String summary(@Valid SearchBarForm searchBarForm,
                          BindingResult bindingResult, Model model) {
        model.addAttribute("tradeTypes", TradeType.values());
        model.addAttribute("exchangeRate", getExchangeRate());

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
        model.addAttribute("exchangeRate", getExchangeRate());

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

    public double getExchangeRate() {
        ExchangeRate exchangeRate = exchangeRateRepository.findOne(1L);
        return null == exchangeRate ? 0.00 : exchangeRate.getValue();
    }
}
