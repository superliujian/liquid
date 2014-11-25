package liquid.controller;

import liquid.container.domain.Container;
import liquid.container.domain.ContainerType;
import liquid.container.facade.ContainerFacade;
import liquid.container.persistence.domain.ContainerEntity;
import liquid.container.persistence.domain.ContainerSubtypeEntity;
import liquid.container.service.ContainerService;
import liquid.container.service.ContainerSubtypeService;
import liquid.domain.*;
import liquid.facade.ContainerAllocationFacade;
import liquid.order.persistence.domain.OrderEntity;
import liquid.order.service.OrderService;
import liquid.order.service.ServiceItemService;
import liquid.persistence.domain.LocationEntity;
import liquid.persistence.domain.ServiceProviderEntity;
import liquid.service.LocationService;
import liquid.transport.persistence.domain.TransportEntity;
import liquid.transport.persistence.domain.ShippingContainerEntity;
import liquid.transport.service.RouteService;
import liquid.transport.service.ShippingContainerService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    private ContainerFacade containerFacade;

    @Autowired
    private ServiceItemService serviceItemService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ContainerSubtypeService containerSubtypeService;

    @Autowired
    private ContainerAllocationFacade containerAllocationFacade;

    @Autowired
    private ShippingContainerService shippingContainerService;

    @RequestMapping(method = RequestMethod.GET)
    public String init(@PathVariable String taskId, Model model) {
        logger.debug("taskId: {}", taskId);

        Long orderId = taskService.getOrderIdByTaskId(taskId);
        OrderEntity order = orderService.find(orderId);
        List<RouteContainerAllocation> routeContainerAllocations = containerAllocationFacade.computeContainerAllocation(order);

        model.addAttribute("routeContainerAllocations", routeContainerAllocations);

        if (ContainerType.RAIL.getType() == order.getContainerType()) {
            return "allocation/rail_container";
        } else {
            return "allocation/self_container";
        }
    }

    @RequestMapping(value = "/rail", method = RequestMethod.GET, params = "routeId")
    public String initRailAlloc(@PathVariable String taskId, @RequestParam Long routeId, Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("routeId: {}", routeId);

        model.addAttribute("taskId", taskId);

        TransportEntity transportEntity = routeService.find(routeId);
        RouteContainerAllocation routeContainerAllocation = containerAllocationFacade.getRouteContainerAllocation(
                ContainerType.RAIL.getType(), transportEntity.getOrder().getContainerSubtype().getName(), transportEntity);
        // For showing the allocated containers.
        model.addAttribute("routeContainerAllocation", routeContainerAllocation);

        Iterable<ContainerSubtypeEntity> subtypes = containerSubtypeService.findByContainerType(ContainerType.RAIL);
        EnterContainerAllocForm containers = new EnterContainerAllocForm();
        containers.setRouteId(routeId);

        // For the allocation form
        model.addAttribute("containerSubtypes", subtypes);
        return "allocation/rail_enter";
    }

    /**
     * Rail Container Allocation.
     *
     * @param taskId
     * @param routeContainerAllocation
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public String allocate(@PathVariable String taskId, RouteContainerAllocation routeContainerAllocation, Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("routeContainerAllocation: {}", routeContainerAllocation);

        containerAllocationFacade.allocate(routeContainerAllocation);

        Long orderId = taskService.getOrderIdByTaskId(taskId);
        model.addAttribute("routeContainerAllocation", containerAllocationFacade.computeContainerAllocation(orderId));

        model.addAttribute("alert", messageSource.getMessage("save.success", new String[]{}, Locale.CHINA));
        return "redirect:/task/" + taskId + "/allocation";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, params = "routeId")
    public String initContainerList(@PathVariable String taskId, @RequestParam Long routeId,
                                    @RequestParam(required = false, defaultValue = "0") Long ownerId,
                                    @RequestParam(required = false, defaultValue = "0") Long yardId,
                                    @RequestParam(required = false, defaultValue = "0") Integer number,
                                    Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("routeId: {}", routeId);

        RouteContainerAllocation routeContainerAllocation = new RouteContainerAllocation();
        TransportEntity transportEntity = routeService.find(routeId);
        routeContainerAllocation.setRoute(transportEntity);
        routeContainerAllocation.setType(transportEntity.getOrder().getContainerType());
        List<ContainerAllocation> containerAllocations = containerAllocationFacade.computeSelfContainerAllocations(transportEntity.getOrder().getContainerSubtype().getName(), transportEntity);
        routeContainerAllocation.setContainerAllocations(containerAllocations);
        // For showing the allocated containers.
        model.addAttribute("routeContainerAllocation", routeContainerAllocation);

        model.addAttribute("taskId", taskId);

        int size = 5;
        PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));
        Page<ContainerEntity> page = containerService.findAll(transportEntity.getOrder().getContainerSubtype().getId(),
                ownerId, yardId, pageRequest);
        // Owner list
        List<ServiceProviderEntity> owners = serviceItemService.findContainerOwners();
        // Yard list
        List<LocationEntity> yards = locationService.findYards();
        // For showing containers in stock.
        model.addAttribute("owners", owners);
        model.addAttribute("yards", yards);
        model.addAttribute("ownerId", ownerId);
        model.addAttribute("yardId", yardId);
        model.addAttribute("page", page);
        model.addAttribute("contextPath", "/task/" + taskId + "/allocation?" + "routeId=" + routeId + "&ownerId=" + ownerId + "&yardId=" + yardId + "&");

        // For the allocation form
        SelfContainerAllocation selfContainerAllocation = new SelfContainerAllocation();
        selfContainerAllocation.setRouteId(routeId);
        model.addAttribute("selfContainerAllocation", selfContainerAllocation);
        return "allocation/list";
    }

    @RequestMapping(method = RequestMethod.POST, params = "self")
    public String allocateInList(@PathVariable String taskId, SelfContainerAllocation selfContainerAllocation,
                                 @RequestHeader(value = "referer", required = false) final String referer) {
        logger.debug("taskId: {}", taskId);
        logger.debug("containerAllocation: {}", selfContainerAllocation);
        containerAllocationFacade.allocate(selfContainerAllocation);
//        return "redirect:/task/" + taskId + "/allocation/list?routeId=" + selfContainerAllocation.getRouteId();
        return "redirect:" + referer;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET, params = "routeId")
    public String initContainerSearch(@PathVariable String taskId, @RequestParam Long routeId, Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("routeId: {}", routeId);

        RouteContainerAllocation routeContainerAllocation = new RouteContainerAllocation();
        TransportEntity transportEntity = routeService.find(routeId);
        routeContainerAllocation.setRoute(transportEntity);
        routeContainerAllocation.setType(transportEntity.getOrder().getContainerType());
        List<ContainerAllocation> containerAllocations = containerAllocationFacade.computeSelfContainerAllocations(transportEntity.getOrder().getContainerSubtype().getName(), transportEntity);
        routeContainerAllocation.setContainerAllocations(containerAllocations);
        // For showing the allocated containers.
        model.addAttribute("routeContainerAllocation", routeContainerAllocation);

        model.addAttribute("taskId", taskId);

        // For the allocation form
        SearchContainerAllocForm searchContainerAllocForm = new SearchContainerAllocForm();
        searchContainerAllocForm.setRouteId(routeId);
        model.addAttribute("searchContainerAllocForm", searchContainerAllocForm);
        return "allocation/search";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String allocateInSearch(@PathVariable String taskId, SearchContainerAllocForm searchContainerAllocForm) {
        logger.debug("taskId: {}", taskId);
        logger.debug("searchContainerAllocForm: {}", searchContainerAllocForm);

        SelfContainerAllocation selfContainerAllocation = new SelfContainerAllocation();
        selfContainerAllocation.setRouteId(searchContainerAllocForm.getRouteId());
        selfContainerAllocation.setContainerIds(new Long[]{searchContainerAllocForm.getContainerId()});
        containerAllocationFacade.allocate(selfContainerAllocation);

        return "redirect:/task/" + taskId + "/allocation/search?routeId=" + searchContainerAllocForm.getRouteId();
    }

    @RequestMapping(value = "/enter", method = RequestMethod.GET, params = "routeId")
    public String initContainerEnter(@PathVariable String taskId, @RequestParam Long routeId, Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("routeId: {}", routeId);

        model.addAttribute("taskId", taskId);

        RouteContainerAllocation routeContainerAllocation = new RouteContainerAllocation();
        TransportEntity transportEntity = routeService.find(routeId);
        routeContainerAllocation.setRoute(transportEntity);
        routeContainerAllocation.setType(transportEntity.getOrder().getContainerType());
        List<ContainerAllocation> containerAllocations = containerAllocationFacade.computeSelfContainerAllocations(transportEntity.getOrder().getContainerSubtype().getName(), transportEntity);
        routeContainerAllocation.setContainerAllocations(containerAllocations);
        // For showing the allocated containers.
        model.addAttribute("routeContainerAllocation", routeContainerAllocation);

        // Owner list
        List<ServiceProviderEntity> owners = serviceItemService.findContainerOwners();
        // Yard list
        List<LocationEntity> yards = locationService.findYards();
        // container subtypes
        Iterable<ContainerSubtypeEntity> subtypes = containerSubtypeService.findByContainerType(ContainerType.SELF);
        EnterContainerAllocForm containers = new EnterContainerAllocForm();
        containers.setRouteId(routeId);
        for (int i = 0; i < 5; i++) {
            containers.getList().add(new Container());
        }
        // For the allocation form
        model.addAttribute("owners", owners);
        model.addAttribute("locations", yards);
        model.addAttribute("containerSubtypes", subtypes);
        model.addAttribute("containers", containers);
        return "allocation/enter";
    }

    @RequestMapping(value = "/enter", method = RequestMethod.POST)
    public String allocateInEnter(@PathVariable String taskId, @ModelAttribute(value = "containers") EnterContainerAllocForm containers) {
        logger.debug("taskId: {}", taskId);
        logger.debug("searchContainerAllocForm: {}", containers);

        Iterable<ContainerEntity> containerEntities = containerFacade.enter(containers);
        List<Long> containerIds = new ArrayList<>();
        for (ContainerEntity containerEntity : containerEntities) {
            containerIds.add(containerEntity.getId());
        }

        SelfContainerAllocation selfContainerAllocation = new SelfContainerAllocation();
        selfContainerAllocation.setRouteId(containers.getRouteId());

        selfContainerAllocation.setContainerIds(containerIds.toArray(new Long[0]));
        containerAllocationFacade.allocate(selfContainerAllocation);

        return "redirect:/task/" + taskId + "/allocation/enter?routeId=" + containers.getRouteId();
    }

    @RequestMapping(method = RequestMethod.POST, params = "undo")
    public String undo(@PathVariable String taskId, @RequestParam Long allocationId,
                       @RequestHeader(value = "referer", required = false) final String referer) {
        logger.debug("taskId: {}", taskId);
        logger.debug("allocationId: {}", allocationId);

        containerAllocationFacade.undo(allocationId);
//        return "redirect:/task/" + taskId + "/allocation";
        return "redirect:" + referer;
    }

    @RequestMapping(value = "/route/{routeId}/sc/{scId}", method = RequestMethod.GET)
    public String initAllocation(@PathVariable String taskId, @PathVariable Long routeId, @PathVariable Long scId, Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("routeId: {}", routeId);

        ShippingContainerEntity sc = scService.find(scId);

        // Set up pickup contact and his phone by default
        TransportEntity route = routeService.find(routeId);
        List<ShippingContainerEntity> shippingContainers = shippingContainerService.findByRouteId(route.getId());

        if (shippingContainers.iterator().hasNext()) {
            ShippingContainerEntity firstOne = shippingContainers.iterator().next();
            sc.setPickupContact(firstOne.getPickupContact());
            sc.setContactPhone(firstOne.getContactPhone());
        }

        model.addAttribute("sc", sc);
        model.addAttribute("routeId", routeId);
        model.addAttribute("containers", containerService.findAllInStock(route.getOrder().getContainerType()));
        return "allocation/allocating";
    }

    @RequestMapping(value = "/route/{routeId}/sc", method = RequestMethod.POST)
    public String allocate(@PathVariable String taskId, @PathVariable long routeId, ShippingContainerEntity sc,
                           BindingResult bindingResult, Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("routeId: {}", routeId);

        if (bindingResult.hasErrors()) {
            return "allocation/allocating";
        }

        scService.allocate(routeId, sc);

        Long orderId = taskService.getOrderIdByTaskId(taskId);
        Iterable<TransportEntity> routes = routeService.findByOrderId(orderId);
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
