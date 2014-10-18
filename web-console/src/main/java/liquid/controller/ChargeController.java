package liquid.controller;

import liquid.charge.persistence.domain.ChargeEntity;
import liquid.container.domain.ContainerType;
import liquid.domain.Charge;
import liquid.domain.LoadingType;
import liquid.domain.TradeType;
import liquid.dto.EarningDto;
import liquid.dto.ExchangeRateDto;
import liquid.facade.ChargeFacade;
import liquid.metadata.ChargeStatus;
import liquid.metadata.ChargeWay;
import liquid.metadata.ContainerCap;
import liquid.metadata.OrderStatus;
import liquid.order.persistence.domain.OrderEntity;
import liquid.order.service.OrderService;
import liquid.persistence.domain.ServiceProviderEntity;
import liquid.persistence.domain.ServiceSubtypeEntity;
import liquid.service.ChargeService;
import liquid.service.ServiceSubtypeService;
import liquid.service.TaskService;
import liquid.shipping.persistence.domain.LegEntity;
import liquid.shipping.service.LegService;
import liquid.shipping.web.domain.TransMode;
import liquid.web.domain.SearchBarForm;
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
public class ChargeController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ChargeController.class);

    @Autowired
    private TaskService taskService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ChargeService chargeService;

    @Autowired
    private ChargeFacade chargeFacade;

    @Autowired
    private LegService legService;

    @Autowired
    private ServiceSubtypeService serviceSubtypeService;

    @ModelAttribute("transModes")
    public Map<Integer, String> populateTransModes() {
        return TransMode.toMap();
    }

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

    @ModelAttribute("exchangeRate")
    public Double populateExchangeRate() {
        return chargeService.getExchangeRate();
    }

    @RequestMapping(method = RequestMethod.GET)
    public void init() {
        logger.debug("init");
    }

    @RequestMapping(method = RequestMethod.POST)
    public String addCharge(@Valid @ModelAttribute Charge charge, BindingResult bindingResult, Model model) {
        logger.debug("charge: {}", charge);
        LegEntity leg = legService.find(charge.getLegId());

        if (bindingResult.hasErrors()) {
            Iterable<ServiceSubtypeEntity> serviceSubtypes = serviceSubtypeService.findEnabled();
            Iterable<ServiceProviderEntity> sps = serviceSubtypeService.find(charge.getServiceSubtypeId()).getServiceProviders();

            Iterable<ChargeEntity> charges = chargeService.findByLegId(charge.getLegId());

            charge.setRouteId(charge.getRouteId());
            charge.setLegId(charge.getLegId());

            model.addAttribute("serviceSubtypes", serviceSubtypes);
            model.addAttribute("sps", sps);
            model.addAttribute("charge", charge);
            model.addAttribute("route", leg.getRoute());
            model.addAttribute("leg", leg);
            model.addAttribute("charges", charges);
            model.addAttribute("backToTask", taskService.computeTaskMainPath(charge.getTaskId()));
            return "charge/console";
        } else {
            charge.setRouteId(leg.getRoute().getId());
            chargeFacade.save(charge);
            String redirect = "/charge/console?taskId=" + charge.getTaskId() + "&legId=" + charge.getLegId();
            return "redirect:" + redirect;
        }
    }

    @RequestMapping(value = "/console", method = RequestMethod.GET, params = "legId")
    public String console(@RequestParam(value = "taskId", required = false) String taskId,
                          @RequestParam(value = "legId") Long legId,
                          Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("legId: {}", legId);

        LegEntity leg = legService.find(legId);
        Iterable<ChargeEntity> charges = chargeService.findByLegId(legId);

        Charge charge = new Charge();
        charge.setRouteId(leg.getRoute().getId());
        charge.setLegId(legId);
        charge.setWay(ChargeWay.PER_CONTAINER.getValue());
        if (null != leg.getSp()) charge.setServiceProviderId(leg.getSp().getId());

        Long defaultServiceSubtypeId = 1L;
        switch (TransMode.valueOf(leg.getTransMode())) {
            case RAIL:
                defaultServiceSubtypeId = 2L;
                break;
            case BARGE:
                defaultServiceSubtypeId = 4L;
                break;
            case VESSEL:
                defaultServiceSubtypeId = 3L;
                break;
            case ROAD:
                defaultServiceSubtypeId = 5L;
                break;
        }
        charge.setServiceSubtypeId(defaultServiceSubtypeId);

        Iterable<ServiceSubtypeEntity> serviceSubtypes = serviceSubtypeService.findEnabled();
        Iterable<ServiceProviderEntity> sps = serviceSubtypeService.find(defaultServiceSubtypeId).getServiceProviders();
        charge.setTaskId(taskId);

        model.addAttribute("serviceSubtypes", serviceSubtypes);
        model.addAttribute("sps", sps);
        model.addAttribute("charge", charge);
        model.addAttribute("route", leg.getRoute());
        model.addAttribute("leg", leg);
        model.addAttribute("charges", charges);
        model.addAttribute("backToTask", taskService.computeTaskMainPath(taskId));
        return "charge/console";
    }

    @RequestMapping(value = "/details", method = RequestMethod.GET, params = "number")
    public String details(@RequestParam int number, Model model) {
        PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));
        Page<ChargeEntity> page = chargeService.findAll(pageRequest);
        model.addAttribute("tab", "details");
        model.addAttribute("page", page);
        model.addAttribute("charges", page.getContent());
        return "charge/details";
    }

    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    public String summary(@RequestParam(defaultValue = "0", required = false) int number, Model model) {
        list(number, model, "/charge/summary");

        model.addAttribute("contextPath", "/charge/summary?");
        return "charge/summary";
    }

    @RequestMapping(value = "/summary", method = RequestMethod.GET, params = {"type", "text"})
    public String summarySearch(@RequestParam(defaultValue = "0", required = false) int number, SearchBarForm searchBarForm, Model model) {
        searchBarForm.setAction("/charge/summary");
        search(number, searchBarForm, model);

        model.addAttribute("contextPath", "/charge/summary?type=" + searchBarForm.getType() + "&content=" + searchBarForm.getText() + "&");
        return "charge/summary";
    }

    @RequestMapping(value = "/receivable", method = RequestMethod.GET)
    public String receivable(@RequestParam(defaultValue = "0", required = false) int number, Model model) {
        list(number, model, "/charge/receivable");

        model.addAttribute("contextPath", "/charge/receivable?");
        return "charge/receivable";
    }

    @RequestMapping(value = "/receivable", method = RequestMethod.GET, params = {"type", "text"})
    public String receivableSearch(@RequestParam(defaultValue = "0", required = false) int number, SearchBarForm searchBarForm, Model model) {
        searchBarForm.setAction("/charge/receivable");
        search(number, searchBarForm, model);

        model.addAttribute("contextPath", "/charge/receivable?type=" + searchBarForm.getType() + "&content=" + searchBarForm.getText() + "&");
        return "charge/receivable";
    }

    private void list(int number, Model model, String action) {
        PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));
        Page<OrderEntity> page = orderService.findByStatus(OrderStatus.SUBMITTED.getValue(), pageRequest);

        SearchBarForm searchBarForm = new SearchBarForm();
        searchBarForm.setAction(action);
        searchBarForm.setTypes(new String[][]{{"orderNo", "order.no"}, {"customerName", "customer.name"}});
        model.addAttribute("searchBarForm", searchBarForm);
        model.addAttribute("page", page);
    }

    private void search(int number, SearchBarForm searchBarForm, Model model) {
        PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));

        String orderNo = "orderNo".equals(searchBarForm.getType()) ? searchBarForm.getText() : null;
        String customerName = "customerName".equals(searchBarForm.getType()) ? searchBarForm.getText() : null;

        Page<OrderEntity> page = orderService.findAll(orderNo, customerName, null, pageRequest);

        searchBarForm.setTypes(new String[][]{{"orderNo", "order.no"}, {"customerName", "customer.name"}});
        model.addAttribute("searchBarForm", searchBarForm);
        model.addAttribute("page", page);
    }

    @RequestMapping(value = "/payable", method = RequestMethod.GET)
    public String payable(@RequestParam(defaultValue = "0", required = false) int number, Model model) {
        PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));
        Page<ChargeEntity> page = chargeService.findAll(pageRequest);
        model.addAttribute("page", page);
        model.addAttribute("contextPath", "/charge/payable?");

        SearchBarForm searchBarForm = new SearchBarForm();
        searchBarForm.setAction("/charge/payable");
        searchBarForm.setTypes(new String[][]{{"orderNo", "order.no"}, {"spName", "sp.name"}});
        model.addAttribute("searchBarForm", searchBarForm);
        return "charge/payable";
    }

    @RequestMapping(value = "/payable", method = RequestMethod.GET, params = {"type", "text"})
    public String payableSearch(@RequestParam(defaultValue = "0", required = false) int number, SearchBarForm searchBarForm, Model model) {
        PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));

        String orderNo = "orderNo".equals(searchBarForm.getType()) ? searchBarForm.getText() : null;
        String spName = "spName".equals(searchBarForm.getType()) ? searchBarForm.getText() : null;

        Page<ChargeEntity> page = chargeService.findAll(orderNo, spName, pageRequest);

        searchBarForm.setAction("/charge/payable");
        searchBarForm.setTypes(new String[][]{{"orderNo", "order.no"}, {"spName", "sp.name"}});
        model.addAttribute("searchBarForm", searchBarForm);
        model.addAttribute("page", page);

        model.addAttribute("contextPath", "/charge/payable?type=" + searchBarForm.getType() + "&content=" + searchBarForm.getText() + "&");
        return "charge/payable";
    }

//    @RequestMapping(method = RequestMethod.POST)
//    public String record(@Valid @ModelAttribute ChargeEntity charge,
//                         @RequestHeader(value = "referer") String referer,
//                         BindingResult bindingResult, Principal principal) {
//        logger.debug("charge: {}", charge);
//        logger.debug("referer: {}", referer);
//
//        long orderId = taskService.getOrderIdByTaskId(charge.getTaskId());
//        OrderEntity order = orderService.find(orderId);
//
//        charge.setOrder(order);
//
//        if (ChargeWay.PER_ORDER.getValue() == charge.getWay()) {
//            charge.setUnitPrice(0L);
//        } else if (ChargeWay.PER_CONTAINER.getValue() == charge.getWay()) {
//            charge.setTotalPrice(charge.getUnitPrice() * order.getContainerQty());
//        } else {
//            logger.warn("{} is out of charge way range.", charge.getWay());
//        }
//
//        chargeService.save(charge);
//
//        String redirect = "redirect:" + referer;
//        return redirect;
//    }

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

    @RequestMapping(value = "/{chargeId}", method = RequestMethod.POST, params = "delete")
    public String delete(@RequestHeader(value = "referer", required = false) final String referer,
                         @PathVariable Long chargeId) {
        logger.debug("chargeId: {}", chargeId);
        chargeService.removeCharge(chargeId);
        return "redirect:" + referer;
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
