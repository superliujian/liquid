package liquid.controller;

import liquid.accounting.persistence.domain.ChargeEntity;
import liquid.accounting.service.ChargeService;
import liquid.accounting.web.domain.ChargeWay;
import liquid.dto.TruckingDto;
import liquid.user.domain.Role;
import liquid.operation.service.ServiceProviderService;
import liquid.operation.domain.ServiceSubtype;
import liquid.operation.service.ServiceSubtypeService;
import liquid.process.controller.BaseTaskController;
import liquid.transport.persistence.domain.ShipmentEntity;
import liquid.transport.service.ShipmentService;
import liquid.transport.service.ShippingContainerService;
import liquid.transport.domain.TransMode;
import liquid.transport.web.domain.Truck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/19/13
 * Time: 12:08 AM
 */
@Controller
@RequestMapping("/task/{taskId}/rail_truck")
public class RailTruckController extends BaseTaskController {
    private static final Logger logger = LoggerFactory.getLogger(RailTruckController.class);

    private static final String TASK_PATH = "rail_truck";

    @Autowired
    private ShippingContainerService scService;

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private ChargeService chargeService;

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private ServiceSubtypeService serviceSubtypeService;

    @RequestMapping(method = RequestMethod.GET)
    public String init(@PathVariable String taskId, Model model) {
        logger.debug("taskId: {}", taskId);
        Long orderId = taskService.getOrderIdByTaskId(taskId);
        model.addAttribute("containers", scService.initializeRailContainers(orderId));
        model.addAttribute("rail_task", TASK_PATH);
        Iterable<ShipmentEntity> shipmentSet = shipmentService.findByOrderId(orderId);
        model.addAttribute("shipmentSet", shipmentSet);

        // for charges
        Iterable<ServiceSubtype> serviceSubtypes = serviceSubtypeService.findEnabled();
        model.addAttribute("serviceSubtypes", serviceSubtypes);
        model.addAttribute("chargeWays", ChargeWay.values());
        model.addAttribute("transModes", TransMode.toMap());
        Iterable<ChargeEntity> charges = chargeService.findByTaskId(taskId);
        model.addAttribute("charges", charges);
        model.addAttribute("total", chargeService.total(charges));
        return "rail/main";
    }

    @RequestMapping(value = "/{containerId}", method = RequestMethod.GET)
    public String initRecord(@PathVariable String taskId,
                             @PathVariable long containerId,
                             Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("containerId: {}", containerId);

        Truck truck = scService.findTruckDto(containerId);
        logger.debug("truck: {}", truck);
        model.addAttribute("truck", truck);
        model.addAttribute("sps", serviceProviderService.findByType(4L));
        return TASK_PATH + "/edit";
    }

    @RequestMapping(value = "/{containerId}", method = RequestMethod.POST)
    public String record(@PathVariable String taskId,
                         @PathVariable long containerId,
                         @Valid @ModelAttribute("truck") Truck truck,
                         BindingResult bindingResult) {
        logger.debug("taskId: {}", taskId);
        logger.debug("containerId: {}", containerId);
        logger.debug("truck: {}", truck);

        if (bindingResult.hasErrors()) {
            return TASK_PATH + "/edit";
        } else {
            scService.saveTruck(truck);
        }

        return "redirect:/task/" + taskId + "/" + TASK_PATH;
    }

    @RequestMapping(value = "/sending", method = RequestMethod.GET)
    public String initTrucking(@PathVariable String taskId,
                               @RequestParam(required = false) boolean done,
                               Model model) {
        logger.debug("taskId: {}", taskId);

        TruckingDto trucking = new TruckingDto();
        Object role = taskService.getVariable(taskId, "truckingRole");
        if (null != role) trucking.setRole(role.toString());

        model.addAttribute("roles", new Role[]{Role.SALES, Role.MARKETING});
        model.addAttribute("done", done);
        model.addAttribute("trucking", trucking);
        return TASK_PATH + "/sending";
    }

    @RequestMapping(value = "/sending", method = RequestMethod.POST)
    public String trucking(@PathVariable String taskId,
                           @Valid @ModelAttribute("trucking") TruckingDto trucking,
                           Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("trucking: {}", trucking);

        taskService.setVariable(taskId, "truckingRole", trucking.getRole());

        model.addAttribute("done", true);
        return "redirect:/task/" + taskId + "/" + TASK_PATH + "/sending";
    }
}
