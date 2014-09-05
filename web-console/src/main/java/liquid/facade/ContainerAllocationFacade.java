package liquid.facade;

import liquid.container.domain.ContainerType;
import liquid.container.persistence.domain.ContainerEntity;
import liquid.domain.ContainerAllocation;
import liquid.domain.RouteContainerAllocation;
import liquid.domain.SelfContainerAllocation;
import liquid.order.persistence.domain.OrderEntity;
import liquid.shipping.service.ContainerAllocationService;
import liquid.shipping.service.RouteService;
import liquid.shipping.service.ShippingContainerService;
import liquid.service.TaskService;
import liquid.shipping.persistence.domain.RouteEntity;
import liquid.shipping.persistence.domain.ShippingContainerEntity;
import liquid.util.CollectionUtil;
import liquid.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by redbrick9 on 5/20/14.
 */
@Service
public class ContainerAllocationFacade {

    @Autowired
    private ShippingContainerService shippingContainerService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private ContainerAllocationService containerAllocationService;

    public ContainerAllocation computeContainerAllocation(String taskId) {
        ContainerAllocation containerAllocation = new ContainerAllocation();

        OrderEntity order = taskService.findOrderByTaskId(taskId);
        int type = order.getContainerType();
        String subtypeName = order.getContainerSubtype().getName();

        Iterable<RouteEntity> routes = routeService.findByOrderId(order.getId());

        List<RouteContainerAllocation> routeContainerAllocations = null;
        if (ContainerType.RAIL.getType() == type)
            routeContainerAllocations = computeRailContainerAllocations(type, subtypeName, routes);
        else
            routeContainerAllocations = computeSelfContainerAllocations(type, subtypeName, routes);

        containerAllocation.setRoutes(routes);
        containerAllocation.setType(type);
        containerAllocation.setRouteContainerAllocations(routeContainerAllocations.toArray(new RouteContainerAllocation[0]));
        return containerAllocation;
    }

    private List<RouteContainerAllocation> computeSelfContainerAllocations(int type, String subtypeName, Iterable<RouteEntity> routes) {
        List<RouteContainerAllocation> routeContainerAllocations = new ArrayList<>();
        for (RouteEntity route : routes) {
            List<ShippingContainerEntity> shippingContainers = route.getContainers();
            int allocatedQuantity = shippingContainers == null ? 0 : shippingContainers.size();
            for (int j = 0; j < allocatedQuantity; j++) {
                RouteContainerAllocation routeContainerAllocation = new RouteContainerAllocation();
                ShippingContainerEntity shippingContainer = shippingContainers.get(j);
                if (null != shippingContainer.getContainer()) {
                    routeContainerAllocation.setAllocationId(shippingContainer.getId());
                    routeContainerAllocation.setRouteId(route.getId());
                    routeContainerAllocation.setTypeNameKey(ContainerType.toMap().get(type));
                    routeContainerAllocation.setSubtypeName(subtypeName);

                    routeContainerAllocation.setContainerId(shippingContainers.get(j).getContainer().getId());
                    routeContainerAllocation.setBicCode(shippingContainers.get(j).getContainer().getBicCode());
                    routeContainerAllocation.setOwner(shippingContainers.get(j).getContainer().getOwner().getName());
                    routeContainerAllocation.setYard(shippingContainers.get(j).getContainer().getYard().getName());
                    routeContainerAllocations.add(routeContainerAllocation);
                }
            }
        }
        return routeContainerAllocations;
    }

    private List<RouteContainerAllocation> computeRailContainerAllocations(int type, String subtypeName, Iterable<RouteEntity> routes) {
        List<RouteContainerAllocation> routeContainerAllocations = new ArrayList<>();
        for (RouteEntity route : routes) {
            List<ShippingContainerEntity> shippingContainers = route.getContainers();
            int allocatedQuantity = shippingContainers == null ? 0 : shippingContainers.size();
            for (int j = 0; j < allocatedQuantity; j++) {
                RouteContainerAllocation routeContainerAllocation = new RouteContainerAllocation();
                routeContainerAllocation.setAllocationId(shippingContainers.get(j).getId());
                routeContainerAllocation.setRouteId(route.getId());
                routeContainerAllocation.setTypeNameKey(ContainerType.toMap().get(type));
                routeContainerAllocation.setSubtypeName(subtypeName);
                routeContainerAllocation.setBicCode(shippingContainers.get(j).getBicCode());
                routeContainerAllocations.add(routeContainerAllocation);
            }
            for (int j = allocatedQuantity; j < route.getContainerQty(); j++) {
                RouteContainerAllocation routeContainerAllocation = new RouteContainerAllocation();
                routeContainerAllocation.setRouteId(route.getId());
                routeContainerAllocation.setTypeNameKey(ContainerType.toMap().get(type));
                routeContainerAllocation.setSubtypeName(subtypeName);
                routeContainerAllocation.setBicCode("");
                routeContainerAllocations.add(routeContainerAllocation);
            }
        }
        return routeContainerAllocations;
    }

    public void allocate(ContainerAllocation containerAllocation) {
        RouteContainerAllocation[] routeContainerAllocations = containerAllocation.getRouteContainerAllocations();

        List<ShippingContainerEntity> shippingContainers = new ArrayList<ShippingContainerEntity>();
        for (int i = 0; i < routeContainerAllocations.length; i++) {
            ShippingContainerEntity shippingContainer = new ShippingContainerEntity();
            shippingContainer.setId(routeContainerAllocations[i].getAllocationId());
            if (!StringUtil.valid(routeContainerAllocations[i].getBicCode())) shippingContainer.setBicCode("");
            shippingContainer.setRoute(RouteEntity.newInstance(RouteEntity.class, routeContainerAllocations[i].getRouteId()));
            shippingContainer.setBicCode(routeContainerAllocations[i].getBicCode());

            shippingContainers.add(shippingContainer);
        }

        shippingContainerService.save(shippingContainers);
    }

    public void allocate(SelfContainerAllocation selfContainerAllocation) {
        RouteEntity route = routeService.find(selfContainerAllocation.getRouteId());

        List<ShippingContainerEntity> shippingContainers = route.getContainers();
        if (CollectionUtil.isEmpty(shippingContainers))
            shippingContainers = new ArrayList<ShippingContainerEntity>();
        for (int i = shippingContainers.size(); i < route.getContainerQty(); i++) {
            ShippingContainerEntity shippingContainer = new ShippingContainerEntity();
            shippingContainer.setRoute(route);
            shippingContainers.add(shippingContainer);
        }

        for (int i = 0, j = 0; i < shippingContainers.size() && j < selfContainerAllocation.getContainerIds().length; i++) {
            ShippingContainerEntity shippingContainer = shippingContainers.get(i);
            if (null == shippingContainer.getContainer()) {
                shippingContainer.setContainer(ContainerEntity.newInstance(ContainerEntity.class,
                        selfContainerAllocation.getContainerIds()[j]));
                shippingContainer.setRoute(route);
                j++;
            }
        }
        containerAllocationService.allocate(shippingContainers);
    }

    public void undo(long allocationId) {
        ShippingContainerEntity shippingContainer = shippingContainerService.find(allocationId);
        containerAllocationService.undo(shippingContainer);
    }
}
