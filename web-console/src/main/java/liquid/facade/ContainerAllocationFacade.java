package liquid.facade;

import liquid.domain.ContainerAllocation;
import liquid.domain.RouteContainerAllocation;
import liquid.persistence.domain.Route;
import liquid.persistence.domain.ShippingContainer;
import liquid.service.RouteService;
import liquid.service.ShippingContainerService;
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
    private RouteService routeService;

    public ContainerAllocation computeContainerAllocation(String taskId) {
        ContainerAllocation containerAllocation = new ContainerAllocation();

        List<Route> routes = routeService.findByTaskId(taskId);

        List<RouteContainerAllocation> routeContainerAllocations = new ArrayList<>();
        for (int i = 0; i < routes.size(); i++) {
            Route route = routes.get(i);
            List<ShippingContainer> shippingContainers = shippingContainerService.findByRoute(route);
            int allocatedQuantity = shippingContainers == null ? 0 : shippingContainers.size();
            for (int j = 0; j < allocatedQuantity; j++) {
                RouteContainerAllocation routeContainerAllocation = new RouteContainerAllocation();
                routeContainerAllocation.setRoute(route);
                routeContainerAllocation.setBicCode(shippingContainers.get(j).getBicCode());
                routeContainerAllocations.add(routeContainerAllocation);
            }
            for (int j = allocatedQuantity; j < route.getContainerQty(); j++) {
                RouteContainerAllocation routeContainerAllocation = new RouteContainerAllocation();
                routeContainerAllocation.setRoute(route);
                routeContainerAllocation.setBicCode("");
                routeContainerAllocations.add(routeContainerAllocation);
            }
        }

        containerAllocation.setRouteContainerAllocations(routeContainerAllocations.toArray(new RouteContainerAllocation[0]));
        return containerAllocation;
    }
}
