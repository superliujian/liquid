package liquid.controller;

import liquid.charge.persistence.domain.ChargeEntity;
import liquid.container.service.ContainerService;
import liquid.domain.ContainerAllocation;
import liquid.domain.SelfContainerAllocation;
import liquid.facade.ContainerAllocationFacade;
import liquid.metadata.ChargeWay;
import liquid.metadata.ContainerCap;
import liquid.container.domain.ContainerType;
import liquid.container.persistence.domain.ContainerEntity;
import liquid.order.service.ServiceItemService;
import liquid.persistence.domain.LocationEntity;
import liquid.persistence.domain.ServiceProviderEntity;
import liquid.service.*;
import liquid.shipping.persistence.domain.RouteEntity;
import liquid.shipping.persistence.domain.ShippingContainerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

/**
 * Container allocation controller.
 * User: tao
 * Date: 10/3/13
 * Time: 2:42 PM
 */
@Controller
@RequestMapping("/task/{taskId}/allocation")
public class AllocationController extends BaseTaskController {
    private static final Logger logger = LoggerFactory.getLogger(AllocationController.class);

    @Autowired
    private RouteService routeService;

    @Autowired
    private ShippingContainerService scService;

    @Autowired
    private ContainerService containerService;

    @Autowired
    private ChargeService chargeService;

    @Autowired
    private ServiceItemService serviceItemService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private ContainerAllocationFacade containerAllocationFacade;

    public String init(@PathVariable String taskId, Model model) {
        scService.initialize(taskId);
        Collection<RouteEntity> routes = routeService.findByTaskId(taskId);
        model.addAttribute("containerTypeMap", ContainerCap.toMap());
        model.addAttribute("routes", routes);

        model.addAttribute("chargeWays", ChargeWay.values());
        Iterable<ChargeEntity> charges = chargeService.findByTaskId(taskId);
        model.addAttribute("charges", charges);
        model.addAttribute("total", chargeService.total(charges));
        return "allocation/main";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String init(Model model, @PathVariable String taskId,
                       @RequestParam(required = false, defaultValue = "0") int number,
                       @RequestParam(required = false, defaultValue = "0") long routeId,
                       @RequestParam(required = false, defaultValue = "0") long ownerId,
                       @RequestParam(required = false, defaultValue = "0") long yardId) {
        logger.debug("taskId: {}", taskId);

        ContainerAllocation containerAllocation = containerAllocationFacade.computeContainerAllocation(taskId);
        model.addAttribute("containerAllocation", containerAllocation);
        if (routeId == 0) routeId = containerAllocation.getRoutes()[0].getId();

        if (ContainerType.RAIL.getType() == containerAllocation.getType())
            return "allocation/rail_container";
        else {
            // Owner list
            List<ServiceProviderEntity> owners = serviceItemService.findContainerOwners();

            // Yard list
            List<LocationEntity> yards = locationService.findYards();

            // Container list in stock
            int size = 10;
            PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));

            RouteEntity routeEntity = routeService.find(routeId);
            Page<ContainerEntity> page = containerService.findAll(routeEntity.getPlanning().getOrder().getContainerSubtype().getId(),
                    ownerId, yardId, pageRequest);

            model.addAttribute("ownerId", ownerId);
            model.addAttribute("owners", owners);

            model.addAttribute("yardId", yardId);
            model.addAttribute("yards", yards);

            model.addAttribute("routeId", routeId);
            model.addAttribute("page", page);

            SelfContainerAllocation selfContainerAllocation = new SelfContainerAllocation();
            selfContainerAllocation.setRouteId(routeId);
            model.addAttribute("selfContainerAllocation", selfContainerAllocation);
            return "allocation/self_container";
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public String allocate(@PathVariable String taskId, ContainerAllocation containerAllocation, Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("containerAllocation: {}", containerAllocation);
        containerAllocationFacade.allocate(containerAllocation);
        return "redirect:/task/" + taskId + "/allocation";
    }

    @RequestMapping(method = RequestMethod.POST, params = "self")
    public String allocate(@PathVariable String taskId, SelfContainerAllocation selfContainerAllocation, Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("containerAllocation: {}", selfContainerAllocation);
        containerAllocationFacade.allocate(selfContainerAllocation);
        return "redirect:/task/" + taskId + "/allocation";
    }

    @RequestMapping(method = RequestMethod.POST, params = "undo")
    public String undo(@PathVariable String taskId,
                       @RequestParam long allocationId) {
        logger.debug("taskId: {}", taskId);
        logger.debug("allocationId: {}", allocationId);
        containerAllocationFacade.undo(allocationId);
        return "redirect:/task/" + taskId + "/allocation";
    }

    @RequestMapping(value = "/route/{routeId}/sc/{scId}", method = RequestMethod.GET)
    public String initAllocation(@PathVariable String taskId,
                                 @PathVariable long routeId,
                                 @PathVariable long scId,
                                 Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("routeId: {}", routeId);

        ShippingContainerEntity sc = scService.find(scId);

        // Set up pickup contact and his phone by default
        RouteEntity route = routeService.find(routeId);
        Collection<ShippingContainerEntity> scs = scService.findByRoute(route);
        if (scs.iterator().hasNext()) {
            ShippingContainerEntity firstOne = scs.iterator().next();
            sc.setPickupContact(firstOne.getPickupContact());
            sc.setContactPhone(firstOne.getContactPhone());
        }

        model.addAttribute("sc", sc);
        model.addAttribute("routeId", routeId);
        model.addAttribute("containers", containerService.findAllInStock(route.getPlanning().getOrder().getContainerType()));
        return "allocation/allocating";
    }

    @RequestMapping(value = "/route/{routeId}/sc", method = RequestMethod.POST)
    public String allocate(@PathVariable String taskId,
                           @PathVariable long routeId,
                           ShippingContainerEntity sc,
                           BindingResult bindingResult, Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("routeId: {}", routeId);

        if (bindingResult.hasErrors()) {
            return "allocation/allocating";
        }

        scService.allocate(routeId, sc);

        Collection<RouteEntity> routes = routeService.findByTaskId(taskId);
        model.addAttribute("routes", routes);
        return "redirect:/task/" + taskId + "/allocation";
    }

    // TODO: change to form submit.
//    @RequestMapping(value = "/route/{routeId}/sc/{scId}", method = RequestMethod.GET)
//    public String remove(@PathVariable String taskId,
//                         @PathVariable long routeId,
//                         @PathVariable long scId,
//                         Model model, Principal principal) {
//        logger.debug("taskId: {}", taskId);
//        logger.debug("routeId: {}", routeId);
//
//        scService.remove(scId);
//
//        Collection<Route> routes = routeService.findByTaskId(taskId);
//        model.addAttribute("routes", routes);
//        return "redirect:/task/" + taskId + "/allocation";
//    }
}
