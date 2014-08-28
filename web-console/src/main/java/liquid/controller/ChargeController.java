package liquid.controller;

import liquid.charge.persistence.domain.ChargeEntity;
import liquid.container.domain.ContainerType;
import liquid.dto.EarningDto;
import liquid.dto.ExchangeRateDto;
import liquid.metadata.*;
import liquid.order.persistence.domain.OrderEntity;
import liquid.persistence.domain.ServiceSubtypeEntity;
import liquid.service.ChargeService;
import liquid.order.service.OrderService;
import liquid.service.ServiceSubtypeService;
import liquid.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/2/13
 * Time: 9:17 PM
 */
@Controller
@RequestMapping("/charge")
public class ChargeController {
    private static final Logger logger = LoggerFactory.getLogger(ChargeController.class);

    @Autowired
    private TaskService taskService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ChargeService chargeService;

    @Autowired
    private ServiceSubtypeService serviceSubtypeService;

    @ModelAttribute("chargeWays")
    public ChargeWay[] populateChargeWays() {
        return ChargeWay.values();
    }

    @ModelAttribute("tradeTypes")
    public TradeType[] populateTradeTypes() {
        return TradeType.values();
    }

    @ModelAttribute("loadingTypes")
    public LoadingType[] populateLoadings() {
        return LoadingType.values();
    }

    @ModelAttribute("containerTypes")
    public ContainerType[] populateContainerTypes() {
        return ContainerType.values();
    }

    @ModelAttribute("containerCaps")
    public ContainerCap[] populateContainerCaps() {
        return ContainerCap.values();
    }

    @ModelAttribute("status")
    public OrderStatus[] populateStatus() {
        return OrderStatus.values();
    }

    @RequestMapping(method = RequestMethod.GET)
    public void init() {
        logger.debug("init");
    }

    @RequestMapping(method = RequestMethod.GET, params = "findByOrderNo")
    public String findByOrderNo(@RequestParam String param, Model model, Principal principal) {
        logger.debug("param: {}", param);

        model.addAttribute("charges", chargeService.findByOrderNo(param));
        return "/charge/details";
    }

    @RequestMapping(method = RequestMethod.GET, params = "findBySpName")
    public String findBySpName(@RequestParam String param, Model model, Principal principal) {
        logger.debug("param: {}", param);

        model.addAttribute("charges", chargeService.findBySpName(param));
        return "charge/details";
    }

    @RequestMapping(value = "/summary", method = RequestMethod.GET, params = "number")
    public String summary(@RequestParam int number,
                          Model model, Principal principal) {
        int size = 20;
        PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));
        Page<OrderEntity> page = orderService.findAll(pageRequest);
        model.addAttribute("tab", "summary");
        model.addAttribute("page", page);
        return "charge/summary";
    }

    @RequestMapping(value = "/details", method = RequestMethod.GET, params = "number")
    public String details(@RequestParam int number,
                          Model model, Principal principal) {
        int size = 20;
        PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));
        Page<OrderEntity> page = orderService.findAll(pageRequest);
        model.addAttribute("tab", "details");
        model.addAttribute("charges", chargeService.findAll());
        return "charge/details";
    }

    @RequestMapping(value = "/receivable", method = RequestMethod.GET, params = "number")
    public String receivable(@RequestParam int number,
                             Model model, Principal principal) {
        int size = 20;
        PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));
        Page<OrderEntity> page = orderService.findAll(pageRequest);
        model.addAttribute("tab", "summary");
        model.addAttribute("page", page);
        return "charge/receivable";
    }

    @RequestMapping(value = "/payable", method = RequestMethod.GET, params = "number")
    public String payable(@RequestParam int number,
                          Model model, Principal principal) {
        int size = 20;
        PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));
        Page<OrderEntity> page = orderService.findAll(pageRequest);
        model.addAttribute("tab", "summary");
        model.addAttribute("page", page);
        return "charge/payable";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String record(@Valid @ModelAttribute ChargeEntity charge,
                         @RequestHeader(value = "referer") String referer,
                         BindingResult bindingResult, Principal principal) {
        logger.debug("charge: {}", charge);
        logger.debug("referer: {}", referer);

        long orderId = taskService.getOrderIdByTaskId(charge.getTaskId());
        OrderEntity order = orderService.find(orderId);

        charge.setOrder(order);

        if (ChargeWay.PER_ORDER.getValue() == charge.getWay()) {
            charge.setUnitPrice(0L);
        } else if (ChargeWay.PER_CONTAINER.getValue() == charge.getWay()) {
            charge.setTotalPrice(charge.getUnitPrice() * order.getContainerQty());
        } else {
            logger.warn("{} is out of charge way range.", charge.getWay());
        }

        chargeService.save(charge);

        String redirect = "redirect:" + referer;
        return redirect;
    }

    @RequestMapping(value = "/{chargeId}", method = RequestMethod.GET)
    public String initDetail(@PathVariable long chargeId,
                             Model model, Principal principal) {
        logger.debug("chargeId: {}", chargeId);
        ChargeEntity charge = chargeService.find(chargeId);
        model.addAttribute("charge", charge);
        return "charge/detail";
    }

    @RequestMapping(value = "/{chargeId}", method = RequestMethod.POST)
    public String pay(@PathVariable long chargeId,
                      Model model, Principal principal) {
        logger.debug("chargeId: {}", chargeId);
        ChargeEntity charge = chargeService.find(chargeId);
        charge.setStatus(ChargeStatus.PAID.getValue());
        chargeService.save(charge);
        return "redirect:/charge";
    }

    @RequestMapping(value = "/order/{orderId}", method = RequestMethod.GET)
    public String order(@PathVariable long orderId,
                        Model model, Principal principal) {
        logger.debug("orderId: {}", orderId);

        OrderEntity order = orderService.find(orderId);
        Iterable<ServiceSubtypeEntity> serviceSubtypes = serviceSubtypeService.findEnabled();
        Iterable<ChargeEntity> charges = chargeService.findByOrderId(order.getId());
        model.addAttribute("charges", charges);

        model.addAttribute("chargeWays", ChargeWay.values());
        model.addAttribute("serviceSubtypes", serviceSubtypes);

        EarningDto earning = chargeService.calculateEarning(order, charges);
        model.addAttribute("earning", earning);
        return "charge/order_detail";
    }

    @RequestMapping(value = "/{chargeId}/settle", method = RequestMethod.GET)
    public String initSettlement(@PathVariable long chargeId,
                                 @RequestHeader(value = "referer") String referer,
                                 Model model, Principal principal) {
        logger.debug("chargeId: {}", chargeId);
        logger.debug("referer: {}", referer);

        ChargeEntity charge = chargeService.find(chargeId);
        Iterable<ServiceSubtypeEntity> serviceSubtypes = serviceSubtypeService.findEnabled();
        model.addAttribute("charge", charge);
        model.addAttribute("chargeWays", ChargeWay.values());
        model.addAttribute("serviceSubtypes", serviceSubtypes);
        model.addAttribute("redirectTo", referer);

        return "charge/settlement_detail";
    }

    @RequestMapping(value = "/{chargeId}/settle", method = RequestMethod.POST)
    public String settle(@PathVariable long chargeId,
                         @RequestParam("redirectTo") String redirectTo,
                         Model model, Principal principal) {
        logger.debug("chargeId: {}", chargeId);
        logger.debug("redirectTo: {}", redirectTo);

        ChargeEntity charge = chargeService.find(chargeId);
        charge.setStatus(ChargeStatus.PAID.getValue());
        chargeService.save(charge);

        return "redirect:" + redirectTo;
    }

    @RequestMapping(value = "/exchange_rate", method = RequestMethod.GET)
    public String getExchangeRate(@RequestParam(required = false) boolean done,
                                  Model model, Principal principal) {
        logger.debug("done: {}", done);

        double value = chargeService.getExchangeRate();
        ExchangeRateDto exchangeRate = new ExchangeRateDto();
        exchangeRate.setValue(value);

        model.addAttribute("exchangeRate", exchangeRate);
        model.addAttribute("done", done);
        return "charge/exchange_rate";
    }

    @RequestMapping(value = "/exchange_rate", method = RequestMethod.POST)
    public String setExchangeRate(@ModelAttribute("exchangeRate") ExchangeRateDto exchangeRate,
                                  Model model, Principal principal) {
        logger.debug("exchangeRate: {}", exchangeRate);

        chargeService.setExchangeRate(exchangeRate.getValue());
        model.addAttribute("done", true);

        return "redirect:/charge/exchange_rate";
    }
}
