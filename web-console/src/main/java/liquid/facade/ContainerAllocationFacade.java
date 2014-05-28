package liquid.facade;

import liquid.domain.ContainerAllocation;
import liquid.domain.RouteContainerAllocation;
import liquid.domain.SelfContainerAllocation;
import liquid.metadata.ContainerType;
import liquid.persistence.domain.ContainerEntity;
import liquid.persistence.domain.OrderEntity;
import liquid.persistence.domain.Route;
import liquid.persistence.domain.ShippingContainer;
import liquid.service.ContainerAllocationService;
import liquid.service.OrderService;
import liquid.service.RouteService;
import liquid.service.ShippingContainerService;
import liquid.utils.StringUtils;
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
    private OrderService orderService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private ContainerAllocationService containerAllocationService;

    public ContainerAllocation computeContainerAllocation(String taskId) {
        ContainerAllocation containerAllocation = new ContainerAllocation();

        OrderEntity order = orderService.findByTaskId(taskId);
        int type = order.getContainerType();
        String subtypeName = order.getContainerSubtype().getName();

        List<Route> routes = routeService.findByTaskId(taskId);

        List<RouteContainerAllocation> routeContainerAllocations = null;
        if (ContainerType.RAIL.getType() == type)
            routeContainerAllocations = computeRailContainerAllocations(type, subtypeName, routes);
        else
            routeContainerAllocations = computeSelfContainerAllocations(type, subtypeName, routes);

        containerAllocation.setRoutes(routes.toArray(new Route[0]));
        containerAllocation.setType(type);
        containerAllocation.setRouteContainerAllocations(routeContainerAllocations.toArray(new RouteContainerAllocation[0]));
        return containerAllocation;
    }

    private List<RouteContainerAllocation> computeSelfContainerAllocations(int type, String subtypeName, List<Route> routes) {
        List<RouteContainerAllocation> routeContainerAllocations = new ArrayList<>();
        for (int i = 0; i < routes.size(); i++) {
            Route route = routes.get(i);
            List<ShippingContainer> shippingContainers = shippingContainerService.findByRoute(route);
            int allocatedQuantity = shippingContainers == null ? 0 : shippingContainers.size();
            for (int j = 0; j < allocatedQuantity; j++) {
                RouteContainerAllocation routeContainerAllocation = new RouteContainerAllocation();
                routeContainerAllocation.setAllocationId(shippingContainers.get(j).getId());
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
        return routeContainerAllocations;
    }

    private List<RouteContainerAllocation> computeRailContainerAllocations(int type, String subtypeName, List<Route> routes) {
        List<RouteContainerAllocation> routeContainerAllocations = new ArrayList<>();
        for (int i = 0; i < routes.size(); i++) {
            Route route = routes.get(i);
            List<ShippingContainer> shippingContainers = shippingContainerService.findByRoute(route);
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

        List<ShippingContainer> shippingContainers = new ArrayList<ShippingContainer>();
        for (int i = 0; i < routeContainerAllocations.length; i++) {
            if (!StringUtils.valid(routeContainerAllocations[i].getBicCode())) continue;
            ShippingContainer shippingContainer = new ShippingContainer();
            shippingContainer.setId(routeContainerAllocations[i].getAllocationId());
            shippingContainer.setRoute(new Route(routeContainerAllocations[i].getRouteId()));
            shippingContainer.setBicCode(routeContainerAllocations[i].getBicCode());

            shippingContainers.add(shippingContainer);
        }

        shippingContainerService.save(shippingContainers);
    }

    public void allocate(SelfContainerAllocation selfContainerAllocation) {
        List<ShippingContainer> shippingContainers = new ArrayList<ShippingContainer>();
        for (int i = 0; i < selfContainerAllocation.getContainerIds().length; i++) {
            ShippingContainer shippingContainer = new ShippingContainer();
            shippingContainer.setContainer(new ContainerEntity(selfContainerAllocation.getContainerIds()[i]));
            shippingContainer.setRoute(new Route(selfContainerAllocation.getRouteId()));

            shippingContainers.add(shippingContainer);
        }
        containerAllocationService.allocate(shippingContainers);
    }

    public void undo(long allocationId) {
        ShippingContainer shippingContainer = shippingContainerService.find(allocationId);
        containerAllocationService.undo(shippingContainer);
    }
}
