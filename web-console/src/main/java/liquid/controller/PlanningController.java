package liquid.controller;

import liquid.accounting.persistence.domain.ChargeEntity;
import liquid.metadata.ChargeWay;
import liquid.order.persistence.domain.OrderEntity;
import liquid.order.service.OrderService;
import liquid.service.ChargeService;
import liquid.transport.persistence.domain.LegEntity;
import liquid.transport.persistence.domain.PathEntity;
import liquid.transport.persistence.domain.RouteEntity;
import liquid.transport.persistence.domain.ShipmentEntity;
import liquid.transport.service.RouteService;
import liquid.transport.service.ShipmentService;
import liquid.transport.web.domain.Shipment;
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
import java.util.ArrayList;
import java.util.List;

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
    private ShipmentService shipmentService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ChargeService chargeService;

    @Autowired
    private RouteService routeService;

    @RequestMapping(method = RequestMethod.GET)
    public String init(@PathVariable String taskId, Model model) {
        logger.debug("taskId: {}", taskId);

        Long orderId = taskService.getOrderIdByTaskId(taskId);
        OrderEntity order = orderService.find(orderId);

        Iterable<ShipmentEntity> shipmentSet = shipmentService.findByOrderId(orderId);

        int containerUsage = 0;
        for (ShipmentEntity shipment : shipmentSet) {
            containerUsage += shipment.getContainerQty();
        }

        Shipment shipment = new Shipment();
        // set remaining container quantity as the default value for the next shipment planning.
        shipment.setContainerQuantity(order.getContainerQty() - containerUsage);

        // shipment planning bar
        model.addAttribute("shipment", shipment);
        model.addAttribute("containerTotality", order.getContainerQty());
        model.addAttribute("routes", routeService.find(order.getSrcLoc().getId(), order.getDstLoc().getId()));

        // shipment table
        model.addAttribute("shipmentSet", shipmentSet);
        model.addAttribute("transModes", TransMode.toMap());

        // charge table
        model.addAttribute("chargeWays", ChargeWay.values());
        Iterable<ChargeEntity> charges = chargeService.findByTaskId(taskId);
        model.addAttribute("charges", charges);
        model.addAttribute("total", chargeService.total(charges));
        return "planning/main";
    }

    @RequestMapping(value = "/shipment", method = RequestMethod.POST)
    public String addShipment(@PathVariable String taskId, @Valid @ModelAttribute(value = "shipment") Shipment shipment,
                              BindingResult result, Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("shipment: {}", shipment);

        Long orderId = taskService.getOrderIdByTaskId(taskId);
        OrderEntity order = orderService.find(orderId);

        int containerUsage = 0;
        Iterable<ShipmentEntity> shipmentSet = shipmentService.findByOrderId(orderId);
        for (ShipmentEntity r : shipmentSet) {
            containerUsage += r.getContainerQty();
        }

        if (result.hasErrors()) {
            model.addAttribute("containerTotality", order.getContainerQty());

            model.addAttribute("shipmentSet", shipmentSet);
            model.addAttribute("transModes", TransMode.toMap());

            // charge table
            model.addAttribute("chargeWays", ChargeWay.values());
            Iterable<ChargeEntity> charges = chargeService.findByTaskId(taskId);
            model.addAttribute("charges", charges);
            model.addAttribute("total", chargeService.total(charges));
            return "planning/main";
        } else if (shipment.getContainerQuantity() > (order.getContainerQty() - containerUsage)) {
            setFieldError(result, "shipment", "containerQty", shipment.getContainerQuantity(), (order.getContainerQty() - containerUsage));

            model.addAttribute("containerTotality", order.getContainerQty());

            model.addAttribute("shipmentSet", shipmentSet);
            model.addAttribute("transModes", TransMode.toMap());

            // charge table
            model.addAttribute("chargeWays", ChargeWay.values());
            Iterable<ChargeEntity> charges = chargeService.findByTaskId(taskId);
            model.addAttribute("charges", charges);
            model.addAttribute("total", chargeService.total(charges));
            return "planning/main";
        } else {
            ShipmentEntity entity = new ShipmentEntity();
            entity.setOrder(order);
            entity.setContainerQty(shipment.getContainerQuantity());
            if (null != shipment.getRouteId()) {
                RouteEntity route = routeService.findOne(shipment.getRouteId());
                List<PathEntity> paths = route.getPaths();
                List<LegEntity> legs = new ArrayList<>(paths.size());
                for (PathEntity path : paths) {
                    LegEntity leg = new LegEntity();
                    leg.setShipment(entity);
                    leg.setTransMode(path.getTransportMode());
                    leg.setSrcLoc(path.getFrom());
                    leg.setDstLoc(path.getTo());
                    legs.add(leg);
                }
                entity.setLegs(legs);
            }

            shipmentService.save(entity);
            return "redirect:/task/" + taskId + "/planning";
        }
    }

    @RequestMapping(value = "/shipment/{shipmentId}", method = RequestMethod.POST, params = "delete")
    public String deleteShipment(@PathVariable String taskId,
                                 @PathVariable Long shipmentId) {
        logger.debug("taskId: {}", taskId);
        logger.debug("shipmentId: {}", shipmentId);
        shipmentService.delete(shipmentId);
        return "redirect:/task/" + taskId + "/planning";
    }
}
