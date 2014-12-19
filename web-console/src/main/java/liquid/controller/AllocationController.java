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
import liquid.transport.persistence.domain.ShipmentEntity;
import liquid.transport.service.ShipmentService;
import liquid.transport.service.ShippingContainerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    private ShipmentService shipmentService;

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
        List<ShipmentContainerAllocation> shipmentContainerAllocations = containerAllocationFacade.computeContainerAllocation(order);

        model.addAttribute("shipmentContainerAllocations", shipmentContainerAllocations);

        if (ContainerType.RAIL.getType() == order.getContainerType()) {
            return "allocation/rail_container";
        } else {
            return "allocation/self_container";
        }
    }

    @RequestMapping(value = "/rail", method = RequestMethod.GET, params = "shipmentId")
    public String initRailAlloc(@PathVariable String taskId, @RequestParam Long shipmentId, Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("shipmentId: {}", shipmentId);

        model.addAttribute("taskId", taskId);

        ShipmentEntity shipmentEntity = shipmentService.find(shipmentId);
        ShipmentContainerAllocation shipmentContainerAllocation = containerAllocationFacade.getShipmentContainerAllocation(
                ContainerType.RAIL.getType(), containerSubtypeService.find(shipmentEntity.getOrder().getContainerSubtypeId()).getName(), shipmentEntity);
        // For showing the allocated containers.
        model.addAttribute("shipmentContainerAllocations", shipmentContainerAllocation);

        Iterable<ContainerSubtypeEntity> subtypes = containerSubtypeService.findByContainerType(ContainerType.RAIL);
        EnterContainerAllocForm containers = new EnterContainerAllocForm();
        containers.setShipmentId(shipmentId);

        // For the allocation form
        model.addAttribute("containerSubtypes", subtypes);
        return "allocation/rail_enter";
    }

    /**
     * Rail Container Allocation.
     *
     * @param taskId
     * @param shipmentContainerAllocation
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public String allocate(@PathVariable String taskId, ShipmentContainerAllocation shipmentContainerAllocation, Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("shipmentContainerAllocations: {}", shipmentContainerAllocation);

        containerAllocationFacade.allocate(shipmentContainerAllocation);

        Long orderId = taskService.getOrderIdByTaskId(taskId);
        model.addAttribute("shipmentContainerAllocations", containerAllocationFacade.computeContainerAllocation(orderId));

        model.addAttribute("alert", messageSource.getMessage("save.success", new String[]{}, Locale.CHINA));
        return "redirect:/task/" + taskId + "/allocation";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, params = "shipmentId")
    public String initContainerList(@PathVariable String taskId, @RequestParam Long shipmentId,
                                    @RequestParam(required = false, defaultValue = "0") Long ownerId,
                                    @RequestParam(required = false, defaultValue = "0") Long yardId,
                                    @RequestParam(required = false, defaultValue = "0") Integer number,
                                    Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("shipmentId: {}", shipmentId);

        ShipmentContainerAllocation shipmentContainerAllocation = new ShipmentContainerAllocation();
        ShipmentEntity shipmentEntity = shipmentService.find(shipmentId);
        shipmentContainerAllocation.setShipment(shipmentEntity);
        shipmentContainerAllocation.setType(shipmentEntity.getOrder().getContainerType());
        List<ContainerAllocation> containerAllocations = containerAllocationFacade.computeSelfContainerAllocations(containerSubtypeService.find(shipmentEntity.getOrder().getContainerSubtypeId()).getName(), shipmentEntity);
        shipmentContainerAllocation.setContainerAllocations(containerAllocations);
        // For showing the allocated containers.
        model.addAttribute("shipmentContainerAllocation", shipmentContainerAllocation);

        model.addAttribute("taskId", taskId);

        int size = 5;
        PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));
        Page<ContainerEntity> page = containerService.findAll(shipmentEntity.getOrder().getContainerSubtypeId(),
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
        model.addAttribute("contextPath", "/task/" + taskId + "/allocation?shipmentId=" + shipmentId + "&ownerId=" + ownerId + "&yardId=" + yardId + "&");

        // For the allocation form
        SelfContainerAllocation selfContainerAllocation = new SelfContainerAllocation();
        selfContainerAllocation.setShipmentId(shipmentId);
        model.addAttribute("selfContainerAllocation", selfContainerAllocation);
        return "allocation/list";
    }

    @RequestMapping(method = RequestMethod.POST, params = "self")
    public String allocateInList(@PathVariable String taskId, SelfContainerAllocation selfContainerAllocation,
                                 @RequestHeader(value = "referer", required = false) final String referer) {
        logger.debug("taskId: {}", taskId);
        logger.debug("containerAllocation: {}", selfContainerAllocation);
        containerAllocationFacade.allocate(selfContainerAllocation);
        return "redirect:" + referer;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET, params = "shipmentId")
    public String initContainerSearch(@PathVariable String taskId, @RequestParam Long shipmentId, Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("shipmentId: {}", shipmentId);

        ShipmentContainerAllocation shipmentContainerAllocation = new ShipmentContainerAllocation();
        ShipmentEntity shipmentEntity = shipmentService.find(shipmentId);
        shipmentContainerAllocation.setShipment(shipmentEntity);
        shipmentContainerAllocation.setType(shipmentEntity.getOrder().getContainerType());
        List<ContainerAllocation> containerAllocations = containerAllocationFacade.computeSelfContainerAllocations(containerSubtypeService.find(shipmentEntity.getOrder().getContainerSubtypeId()).getName(), shipmentEntity);
        shipmentContainerAllocation.setContainerAllocations(containerAllocations);
        // For showing the allocated containers.
        model.addAttribute("shipmentContainerAllocation", shipmentContainerAllocation);

        model.addAttribute("taskId", taskId);

        // For the allocation form
        SearchContainerAllocForm searchContainerAllocForm = new SearchContainerAllocForm();
        searchContainerAllocForm.setShipmentId(shipmentId);
        model.addAttribute("searchContainerAllocForm", searchContainerAllocForm);
        return "allocation/search";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String allocateInSearch(@PathVariable String taskId, SearchContainerAllocForm searchContainerAllocForm) {
        logger.debug("taskId: {}", taskId);
        logger.debug("searchContainerAllocForm: {}", searchContainerAllocForm);

        SelfContainerAllocation selfContainerAllocation = new SelfContainerAllocation();
        selfContainerAllocation.setShipmentId(searchContainerAllocForm.getShipmentId());
        selfContainerAllocation.setContainerIds(new Long[]{searchContainerAllocForm.getContainerId()});
        containerAllocationFacade.allocate(selfContainerAllocation);

        return "redirect:/task/" + taskId + "/allocation/search?shipmentId=" + searchContainerAllocForm.getShipmentId();
    }

    @RequestMapping(value = "/enter", method = RequestMethod.GET, params = "shipmentId")
    public String initContainerEnter(@PathVariable String taskId, @RequestParam Long shipmentId, Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("shipmentId: {}", shipmentId);

        model.addAttribute("taskId", taskId);

        ShipmentContainerAllocation shipmentContainerAllocation = new ShipmentContainerAllocation();
        ShipmentEntity shipmentEntity = shipmentService.find(shipmentId);
        shipmentContainerAllocation.setShipment(shipmentEntity);
        shipmentContainerAllocation.setType(shipmentEntity.getOrder().getContainerType());
        List<ContainerAllocation> containerAllocations = containerAllocationFacade.computeSelfContainerAllocations(containerSubtypeService.find(shipmentEntity.getOrder().getContainerSubtypeId()).getName(), shipmentEntity);
        shipmentContainerAllocation.setContainerAllocations(containerAllocations);
        // For showing the allocated containers.
        model.addAttribute("shipmentContainerAllocation", shipmentContainerAllocation);

        // Owner list
        List<ServiceProviderEntity> owners = serviceItemService.findContainerOwners();
        // Yard list
        List<LocationEntity> yards = locationService.findYards();
        // container subtypes
        Iterable<ContainerSubtypeEntity> subtypes = containerSubtypeService.findByContainerType(ContainerType.SELF);
        EnterContainerAllocForm containers = new EnterContainerAllocForm();
        containers.setShipmentId(shipmentId);
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
        selfContainerAllocation.setShipmentId(containers.getShipmentId());

        selfContainerAllocation.setContainerIds(containerIds.toArray(new Long[0]));
        containerAllocationFacade.allocate(selfContainerAllocation);

        return "redirect:/task/" + taskId + "/allocation/enter?shipmentId=" + containers.getShipmentId();
    }

    @RequestMapping(method = RequestMethod.POST, params = "undo")
    public String undo(@PathVariable String taskId, @RequestParam Long allocationId,
                       @RequestHeader(value = "referer", required = false) final String referer) {
        logger.debug("taskId: {}", taskId);
        logger.debug("allocationId: {}", allocationId);

        containerAllocationFacade.undo(allocationId);
        return "redirect:" + referer;
    }
}
