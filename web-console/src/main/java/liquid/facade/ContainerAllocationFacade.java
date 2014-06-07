package liquid.facade;

import liquid.domain.ContainerAllocation;
import liquid.domain.RouteContainerAllocation;
import liquid.domain.SelfContainerAllocation;
import liquid.metadata.ContainerType;
import liquid.persistence.domain.ContainerEntity;
import liquid.persistence.domain.OrderEntity;
import liquid.persistence.domain.RouteEntity;
import liquid.persistence.domain.ShippingContainerEntity;
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

        List<RouteEntity> routes = routeService.findByTaskId(taskId);

        List<RouteContainerAllocation> routeContainerAllocations = null;
        if (ContainerType.RAIL.getType() == type)
            routeContainerAllocations = computeRailContainerAllocations(type, subtypeName, routes);
        else
            routeContainerAllocations = computeSelfContainerAllocations(type, subtypeName, routes);

        containerAllocation.setRoutes(routes.toArray(new RouteEntity[0]));
        containerAllocation.setType(type);
        containerAllocation.setRouteContainerAllocations(routeContainerAllocations.toArray(new RouteContainerAllocation[0]));
        return containerAllocation;
    }

    private List<RouteContainerAllocation> computeSelfContainerAllocations(int type, String subtypeName, List<RouteEntity> routes) {
        List<RouteContainerAllocation> routeContainerAllocations = new ArrayList<>();
        for (int i = 0; i < routes.size(); i++) {
            RouteEntity route = routes.get(i);
            List<ShippingContainerEntity> shippingContainers = shippingContainerService.findByRoute(route);
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

    private List<RouteContainerAllocation> computeRailContainerAllocations(int type, String subtypeName, List<RouteEntity> routes) {
        List<RouteContainerAllocation> routeContainerAllocations = new ArrayList<>();
        for (int i = 0; i < routes.size(); i++) {
            RouteEntity route = routes.get(i);
            List<ShippingContainerEntity> shippingContainers = shippingContainerService.findByRoute(route);
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
            if (!StringUtils.valid(routeContainerAllocations[i].getBicCode())) shippingContainer.setBicCode("");
            shippingContainer.setRoute(RouteEntity.newInstance(RouteEntity.class, routeContainerAllocations[i].getRouteId()));
            shippingContainer.setBicCode(routeContainerAllocations[i].getBicCode());

            shippingContainers.add(shippingContainer);
        }

        shippingContainerService.save(shippingContainers);
    }

    public void allocate(SelfContainerAllocation selfContainerAllocation) {
        List<ShippingContainerEntity> shippingContainers = new ArrayList<ShippingContainerEntity>();
        for (int i = 0; i < selfContainerAllocation.getContainerIds().length; i++) {
            ShippingContainerEntity shippingContainer = new ShippingContainerEntity();
            shippingContainer.setContainer(ContainerEntity.newInstance(ContainerEntity.class, selfContainerAllocation.getContainerIds()[i]));
            shippingContainer.setRoute(RouteEntity.newInstance(RouteEntity.class, selfContainerAllocation.getRouteId()));

            shippingContainers.add(shippingContainer);
        }
        containerAllocationService.allocate(shippingContainers);
    }

    public void undo(long allocationId) {
        ShippingContainerEntity shippingContainer = shippingContainerService.find(allocationId);
        containerAllocationService.undo(shippingContainer);
    }
}
