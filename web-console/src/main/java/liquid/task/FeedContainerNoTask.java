package liquid.task;

import liquid.order.persistence.domain.OrderEntity;
import liquid.shipping.persistence.domain.RouteEntity;
import liquid.shipping.persistence.domain.ShippingContainerEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

/**
 * Created by redbrick9 on 7/22/14.
 */
@DefinitionKey("feedContainerNo")
@Service
public class FeedContainerNoTask extends AbstractTaskProxy {
    @Override
    public void doBeforeComplete(String taskId, Map<String, Object> variableMap) {
        Long orderId = taskService.getOrderIdByTaskId(taskId);
        OrderEntity order = orderService.find(orderId);
        Iterable<RouteEntity> routes = routeService.findByOrderId(orderId);

        int allocatedContainerQuantity = 0;

        for (RouteEntity route : routes) {
            Collection<ShippingContainerEntity> scs = route.getContainers();
            for (ShippingContainerEntity shippingContainer : scs) {
                if (null != shippingContainer.getContainer() || null != shippingContainer.getBicCode()) {
                    allocatedContainerQuantity++;
                }
            }
        }

        if (allocatedContainerQuantity != order.getContainerQty()) {
            throw new NotCompletedException("container.allocation.is.not.completed");
        }
    }
}
