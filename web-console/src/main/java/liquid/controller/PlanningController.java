package liquid.controller;

import liquid.charge.persistence.domain.ChargeEntity;
import liquid.metadata.ChargeWay;
import liquid.order.persistence.domain.OrderEntity;
import liquid.order.service.OrderService;
import liquid.persistence.repository.LocationRepository;
import liquid.persistence.repository.ServiceProviderRepository;
import liquid.service.ChargeService;
import liquid.service.RouteService;
import liquid.shipping.domain.TransMode;
import liquid.shipping.persistence.domain.LegEntity;
import liquid.shipping.persistence.domain.RouteEntity;
import liquid.shipping.persistence.repository.LegRepository;
import liquid.shipping.persistence.repository.RouteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.Collection;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/1/13
 * Time: 12:04 PM
 */
@Controller
@RequestMapping("/task/{taskId}/planning")
public class PlanningController extends BaseTaskController {
    private static final Logger logger = LoggerFactory.getLogger(PlanningController.class);

    @Autowired
    private RouteService routeService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private LegRepository legRepository;

    @Autowired
    private ChargeService chargeService;

    public PlanningController() {
    }

    @RequestMapping(method = RequestMethod.GET)
    public String init(@PathVariable String taskId,
                       Model model) {
        logger.debug("taskId: {}", taskId);
        Long orderId = taskService.findOrderId(taskId);
        OrderEntity order = orderService.find(orderId);
        Iterable<RouteEntity> routes = routeService.findByOrderId(orderId);

        int containerUsage = 0;
        for (RouteEntity route : routes) {
            containerUsage += route.getContainerQty();
            Collection<LegEntity> legs = legRepository.findByRoute(route);
            route.setLegs(legs);
        }
        RouteEntity route = new RouteEntity();
        route.setContainerQty(order.getContainerQty() - containerUsage);

        model.addAttribute("transModes", TransMode.toMap());
        model.addAttribute("order", order);
        model.addAttribute("route", route);
        model.addAttribute("routes", routes);

        model.addAttribute("chargeWays", ChargeWay.values());
        Iterable<ChargeEntity> charges = chargeService.findByTaskId(taskId);
        model.addAttribute("charges", charges);
        model.addAttribute("total", chargeService.total(charges));
        return "planning/main";
    }

    @RequestMapping(value = "/route", method = RequestMethod.POST)
    public String addRoute(@PathVariable String taskId,
                           @Valid RouteEntity route,
                           BindingResult result, Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("route: {}", route);

        Long orderId = taskService.findOrderId(taskId);
        OrderEntity order = orderService.find(orderId);

        int containerUsage = 0;
        Iterable<RouteEntity> routes = routeService.findByOrderId0(orderId);
        for (RouteEntity r : routes) {
            containerUsage += r.getContainerQty();
            Collection<LegEntity> legs = legRepository.findByRoute(r);
            r.setLegs(legs);
        }

        if (result.hasErrors()) {
            return "planning/main";
        } else if (route.getContainerQty() > (order.getContainerQty() - containerUsage)) {
            setFieldError(result, "route", "containerQty", route.getContainerQty(), (order.getContainerQty() - containerUsage));
            model.addAttribute("transModes", TransMode.toMap());
            return "planning/main";
        } else {
            route.setOrder(order);
            routeService.save(route);
            return "redirect:/task/" + taskId + "/planning";
        }
    }

    @RequestMapping(value = "/route/{routeId}", method = RequestMethod.POST, params = "delete")
    public String deleteRoute(@PathVariable String taskId,
                              @PathVariable Long routeId) {
        logger.debug("taskId: {}", taskId);
        logger.debug("routeId: {}", routeId);
        routeRepository.delete(routeId);
        return "redirect:/task/" + taskId + "/planning";
    }
}
