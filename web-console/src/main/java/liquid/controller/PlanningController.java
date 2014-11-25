package liquid.controller;

import liquid.accounting.persistence.domain.ChargeEntity;
import liquid.metadata.ChargeWay;
import liquid.order.persistence.domain.OrderEntity;
import liquid.order.service.OrderService;
import liquid.service.ChargeService;
import liquid.transport.persistence.domain.ShipmentEntity;
import liquid.transport.service.TransportService;
import liquid.transport.web.domain.TransMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

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
    private TransportService transportService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ChargeService chargeService;

    @RequestMapping(method = RequestMethod.GET)
    public String init(@PathVariable String taskId, Model model) {
        logger.debug("taskId: {}", taskId);
        Long orderId = taskService.getOrderIdByTaskId(taskId);
        OrderEntity order = orderService.find(orderId);
        Iterable<ShipmentEntity> routes = transportService.findByOrderId(orderId);

        int containerUsage = 0;
        for (ShipmentEntity route : routes) {
            containerUsage += route.getContainerQty();
        }

        ShipmentEntity route = new ShipmentEntity();
        // set remaining container quantity as the default value for the next route planning.
        route.setContainerQty(order.getContainerQty() - containerUsage);

        // route planning bar
        model.addAttribute("route", route);
        model.addAttribute("containerTotality", order.getContainerQty());

        // route table
        model.addAttribute("routes", routes);
        model.addAttribute("transModes", TransMode.toMap());

        // charge table
        model.addAttribute("chargeWays", ChargeWay.values());
        Iterable<ChargeEntity> charges = chargeService.findByTaskId(taskId);
        model.addAttribute("charges", charges);
        model.addAttribute("total", chargeService.total(charges));
        return "planning/main";
    }

    @RequestMapping(value = "/route", method = RequestMethod.POST)
    public String addRoute(@PathVariable String taskId, @Valid @ModelAttribute(value = "route") ShipmentEntity route,
                           BindingResult result, Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("route: {}", route);

        Long orderId = taskService.getOrderIdByTaskId(taskId);
        OrderEntity order = orderService.find(orderId);

        int containerUsage = 0;
        Iterable<ShipmentEntity> routes = transportService.findByOrderId(orderId);
        for (ShipmentEntity r : routes) {
            containerUsage += r.getContainerQty();
        }

        if (result.hasErrors()) {
            model.addAttribute("containerTotality", order.getContainerQty());

            model.addAttribute("routes", routes);
            model.addAttribute("transModes", TransMode.toMap());

            // charge table
            model.addAttribute("chargeWays", ChargeWay.values());
            Iterable<ChargeEntity> charges = chargeService.findByTaskId(taskId);
            model.addAttribute("charges", charges);
            model.addAttribute("total", chargeService.total(charges));
            return "planning/main";
        } else if (route.getContainerQty() > (order.getContainerQty() - containerUsage)) {
            setFieldError(result, "route", "containerQty", route.getContainerQty(), (order.getContainerQty() - containerUsage));

            model.addAttribute("containerTotality", order.getContainerQty());

            model.addAttribute("routes", routes);
            model.addAttribute("transModes", TransMode.toMap());

            // charge table
            model.addAttribute("chargeWays", ChargeWay.values());
            Iterable<ChargeEntity> charges = chargeService.findByTaskId(taskId);
            model.addAttribute("charges", charges);
            model.addAttribute("total", chargeService.total(charges));
            return "planning/main";
        } else {
            route.setOrder(order);
            transportService.save(route);
            return "redirect:/task/" + taskId + "/planning";
        }
    }

    @RequestMapping(value = "/route/{routeId}", method = RequestMethod.POST, params = "delete")
    public String deleteRoute(@PathVariable String taskId,
                              @PathVariable Long routeId) {
        logger.debug("taskId: {}", taskId);
        logger.debug("routeId: {}", routeId);
        transportService.delete(routeId);
        return "redirect:/task/" + taskId + "/planning";
    }
}
