package liquid.controller;

import liquid.dto.EarningDto;
import liquid.dto.TaskDto;
import liquid.metadata.*;
import liquid.persistence.domain.*;
import liquid.persistence.repository.ChargeRepository;
import liquid.persistence.repository.ChargeTypeRepository;
import liquid.service.ChargeService;
import liquid.service.OrderService;
import liquid.service.TaskService;
import liquid.service.bpm.ActivitiEngineService;
import liquid.utils.RoleHelper;
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
import java.util.Map;

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
    private ChargeTypeRepository ctRepository;

    @Autowired
    private ChargeService chargeService;

    @ModelAttribute("chargeWays")
    public ChargeWay[] populateChargeWays() {
        return ChargeWay.values();
    }

    @ModelAttribute("cts")
    public Map<Long, String> populateChargeTypes() {
        return chargeService.getChargeTypes();
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

    @RequestMapping(method = RequestMethod.GET, params = "findByOrderId")
    public String findByOrderId(@RequestParam String param, Model model, Principal principal) {
        logger.debug("param: {}", param);
        if (null == param || param.trim().length() == 0) {

        } else {
            try {
                model.addAttribute("charges", chargeService.findByOrderId(Long.parseLong(param)));
            } catch (Exception e) {
                logger.warn("An exception was thrown when calling findById", e);
            }
        }
        return "charge";
    }

    @RequestMapping(method = RequestMethod.GET, params = "findBySpName")
    public String findBySpName(@RequestParam String param, Model model, Principal principal) {
        logger.debug("param: {}", param);

        model.addAttribute("charges", chargeService.findBySpName(param));
        return "charge";
    }

    @RequestMapping(value = "/summary", method = RequestMethod.GET, params = "number")
    public String summary(@RequestParam int number,
                          Model model, Principal principal) {
        int size = 20;
        PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));
        Page<Order> page = orderService.findAll(pageRequest);
        model.addAttribute("tab", "summary");
        model.addAttribute("page", page);
        return "/charge/summary";
    }

    @RequestMapping(value = "/details", method = RequestMethod.GET, params = "number")
    public String details(@RequestParam int number,
                          Model model, Principal principal) {
        int size = 20;
        PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));
        Page<Order> page = orderService.findAll(pageRequest);
        model.addAttribute("tab", "details");
        model.addAttribute("charges", chargeService.findAll());
        return "/charge/details";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String record(@Valid @ModelAttribute Charge charge,
                         @RequestHeader(value = "referer") String referer,
                         BindingResult bindingResult, Principal principal) {
        logger.debug("charge: {}", charge);
        logger.debug("referer: {}", referer);

        long orderId = taskService.getOrderIdByTaskId(charge.getTaskId());
        Order order = orderService.find(orderId);

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
        Charge charge = chargeService.find(chargeId);
        model.addAttribute("charge", charge);
        return "charge/detail";
    }

    @RequestMapping(value = "/{chargeId}", method = RequestMethod.POST)
    public String pay(@PathVariable long chargeId,
                      Model model, Principal principal) {
        logger.debug("chargeId: {}", chargeId);
        Charge charge = chargeService.find(chargeId);
        charge.setStatus(ChargeStatus.PAID.getValue());
        chargeService.save(charge);
        return "redirect:/charge";
    }

    @RequestMapping(value = "/order/{orderId}", method = RequestMethod.GET)
    public String order(@PathVariable long orderId,
                        Model model, Principal principal) {
        logger.debug("orderId: {}", orderId);

        Order order = orderService.find(orderId);

        Iterable<Charge> charges = chargeService.findByOrderId(order.getId());
        model.addAttribute("charges", charges);

        model.addAttribute("chargeWays", ChargeWay.values());
        model.addAttribute("cts", chargeService.getChargeTypes());

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

        Charge charge = chargeService.find(chargeId);
        model.addAttribute("charge", charge);
        model.addAttribute("chargeWays", ChargeWay.values());
        model.addAttribute("cts", chargeService.getChargeTypes());
        model.addAttribute("redirectTo", referer);

        return "charge/settlement_detail";
    }

    @RequestMapping(value = "/{chargeId}/settle", method = RequestMethod.POST)
    public String settle(@PathVariable long chargeId,
                         @RequestParam("redirectTo") String redirectTo,
                         Model model, Principal principal) {
        logger.debug("chargeId: {}", chargeId);
        logger.debug("redirectTo: {}", redirectTo);

        Charge charge = chargeService.find(chargeId);
        charge.setStatus(ChargeStatus.PAID.getValue());
        chargeService.save(charge);

        return "redirect:" + redirectTo;
    }
}
