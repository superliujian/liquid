package liquid.task;

import liquid.persistence.domain.OrderEntity;
import liquid.persistence.domain.Planning;
import liquid.persistence.domain.RouteEntity;
import liquid.persistence.domain.ShippingContainer;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

/**
 * Created by redbrick9 on 6/7/14.
 */
@DefinitionKey("allocateContainers")
@Service
public class AllocationContainerTask extends AbstractTaskProxy {

    @Override
    public void doBeforeComplete(String taskId, Map<String, Object> variableMap) {
        long orderId = taskService.getOrderIdByTaskId(taskId);
        OrderEntity order = orderService.find(orderId);
        Planning planning = planningService.findByOrder(order);
        Collection<RouteEntity> routes = routeService.findByPlanning(planning);
        int allocatedContainerQty = 0;
        for (RouteEntity route : routes) {
            Collection<ShippingContainer> scs = shippingContainerService.findByRoute(route);
            allocatedContainerQty += scs.size();
        }
        if (allocatedContainerQty != order.getContainerQty()) {
            throw new NotCompletedException("container.allocation.is.not.completed");
        }
    }
}