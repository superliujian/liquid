package liquid.accounting.web.controller;

import liquid.accounting.facade.ReceivableFacadeImpl;
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
    public String summary(@RequestParam(defaultValue = "0", required = false) int number, Model model) {
        PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));

        SearchBarForm searchBarForm = new SearchBarForm();
        searchBarForm.setAction("/accounting/summary");
        searchBarForm.setTypes(new String[][]{{"orderNo", "order.no"}, {"customerName", "customer.name"}});
        model.addAttribute("searchBarForm", searchBarForm);

//        Page<ReceivableSummary> page = receivableFacade.findAll(pageRequest);
        Page<ReceivableSummary> page = receivableFacade.findAll(pageRequest);
        model.addAttribute("page", page);
        model.addAttribute("contextPath", "/accounting/summary?");

        model.addAttribute("tradeTypes", TradeType.values());
        model.addAttribute("exchangeRate", getExchangeRate());

        return "charge/summary";
    }

    @RequestMapping(value = "/summary", method = RequestMethod.GET, params = {"type", "text"})
    public String summarySearch(@RequestParam(defaultValue = "0", required = false) int number, SearchBarForm searchBarForm, Model model) {
        searchBarForm.setAction("/charge/summary");
//        search(number, searchBarForm, model);

        model.addAttribute("contextPath", "/accounting/summary?type=" + searchBarForm.getType() + "&content=" + searchBarForm.getText() + "&");
        return "charge/summary";
    }

    @RequestMapping(value = "/receivable", method = RequestMethod.GET)
    public String listReceivables(@Valid SearchBarForm searchBarForm,
                                  BindingResult bindingResult, Model model) {
        model.addAttribute("tradeTypes", TradeType.values());
        model.addAttribute("exchangeRate", getExchangeRate());

        model.addAttribute("contextPath", "/accounting/receivable?");

        if (bindingResult.hasErrors()) {
            Page<ReceivableSummary> page = new PageImpl<ReceivableSummary>(new ArrayList<>());
            model.addAttribute("page", page);
            return "charge/receivable";
        }

        searchBarForm.setAction("/accounting/receivable");
        model.addAttribute("searchBarForm", searchBarForm);

        PageRequest pageRequest = new PageRequest(0, size, new Sort(Sort.Direction.DESC, "id"));
        Page<ReceivableSummary> page = receivableFacade.findAll(searchBarForm, pageRequest);
        model.addAttribute("page", page);

        return "charge/receivable";
    }

    public double getExchangeRate() {
        ExchangeRate exchangeRate = exchangeRateRepository.findOne(1L);
        return null == exchangeRate ? 0.00 : exchangeRate.getValue();
    }
}
