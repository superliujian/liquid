package liquid.controller;

import liquid.charge.persistence.domain.ChargeEntity;
import liquid.metadata.ChargeWay;
import liquid.persistence.domain.ServiceSubtypeEntity;
import liquid.service.ChargeService;
import liquid.service.DeliveryContainerService;
import liquid.service.RouteService;
import liquid.service.ServiceSubtypeService;
import liquid.shipping.domain.TransMode;
import liquid.shipping.persistence.domain.DeliveryContainer;
import liquid.shipping.persistence.domain.RouteEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/16/13
 * Time: 10:28 PM
 */
@Controller
@RequestMapping("/task/{taskId}/delivery")
public class DeliveryController extends BaseTaskController {
    private static final Logger logger = LoggerFactory.getLogger(DeliveryController.class);

    @Autowired
    private DeliveryContainerService deliveryContainerService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private ChargeService chargeService;

    @Autowired
    private ServiceSubtypeService serviceSubtypeService;

    @RequestMapping(method = RequestMethod.GET)
    public String init(@PathVariable String taskId,
                       Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);

        Long orderId = taskService.findOrderId(taskId);
        Iterable<RouteEntity> routes = routeService.findByOrderId(orderId);
        model.addAttribute("routes", routes);

        model.addAttribute("containers", deliveryContainerService.initDeliveryContainers(taskId));

        // for charges
        Iterable<ServiceSubtypeEntity> serviceSubtypes = serviceSubtypeService.findEnabled();
        model.addAttribute("serviceSubtypes", serviceSubtypes);
        model.addAttribute("chargeWays", ChargeWay.values());
        model.addAttribute("transModes", TransMode.toMap());
        Iterable<ChargeEntity> charges = chargeService.findByTaskId(taskId);
        model.addAttribute("charges", charges);
        model.addAttribute("total", chargeService.total(charges));
        return "delivery/main";
    }

    @RequestMapping(value = "/{containerId}", method = RequestMethod.GET)
    public String initSave(@PathVariable String taskId,
                           @PathVariable long containerId,
                           Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("containerId: {}", containerId);

        DeliveryContainer container = deliveryContainerService.findDeliveryContainer(containerId);

        logger.debug("container: {}", container);
        model.addAttribute("container", container);
        return "delivery/edit";
    }

    @RequestMapping(value = "/{containerId}", method = RequestMethod.POST)
    public String save(@PathVariable String taskId,
                       @PathVariable long containerId,
                       @ModelAttribute("container") DeliveryContainer formBean,
                       Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("containerId: {}", containerId);

        deliveryContainerService.saveDeliveryContainer(containerId, formBean);

        return "redirect:/task/" + taskId + "/delivery";
    }
}
