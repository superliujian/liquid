package liquid.facade;

import liquid.domain.ContainerAllocation;
import liquid.domain.RouteContainerAllocation;
import liquid.metadata.ContainerType;
import liquid.persistence.domain.Order;
import liquid.persistence.domain.Route;
import liquid.persistence.domain.ShippingContainer;
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

    public ContainerAllocation computeContainerAllocation(String taskId) {
        ContainerAllocation containerAllocation = new ContainerAllocation();

        Order order = orderService.findByTaskId(taskId);
        List<Route> routes = routeService.findByTaskId(taskId);

        List<RouteContainerAllocation> routeContainerAllocations = new ArrayList<>();
        for (int i = 0; i < routes.size(); i++) {
            Route route = routes.get(i);
            List<ShippingContainer> shippingContainers = shippingContainerService.findByRoute(route);
            int allocatedQuantity = shippingContainers == null ? 0 : shippingContainers.size();
            for (int j = 0; j < allocatedQuantity; j++) {
                RouteContainerAllocation routeContainerAllocation = new RouteContainerAllocation();
                routeContainerAllocation.setAllocationId(shippingContainers.get(j).getId());
                routeContainerAllocation.setRouteId(route.getId());
                routeContainerAllocation.setTypeNameKey(ContainerType.toMap().get(order.getContainerType()));
                routeContainerAllocation.setSubtypeName(order.getContainerSubtype().getName());
                routeContainerAllocation.setBicCode(shippingContainers.get(j).getBicCode());
                routeContainerAllocations.add(routeContainerAllocation);
            }
            for (int j = allocatedQuantity; j < route.getContainerQty(); j++) {
                RouteContainerAllocation routeContainerAllocation = new RouteContainerAllocation();
                routeContainerAllocation.setRouteId(route.getId());
                routeContainerAllocation.setTypeNameKey(ContainerType.toMap().get(order.getContainerType()));
                routeContainerAllocation.setSubtypeName(order.getContainerSubtype().getName());
                routeContainerAllocation.setBicCode("");
                routeContainerAllocations.add(routeContainerAllocation);
            }
        }

        containerAllocation.setRouteContainerAllocations(routeContainerAllocations.toArray(new RouteContainerAllocation[0]));
        return containerAllocation;
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
}
