package liquid.task;

import liquid.order.persistence.domain.OrderEntity;
import liquid.shipping.domain.TransMode;
import liquid.shipping.persistence.domain.LegEntity;
import liquid.shipping.persistence.domain.PlanningEntity;
import liquid.shipping.persistence.domain.RouteEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

/**
 * Created by redbrick9 on 6/7/14.
 */
@DefinitionKey("planRoute")
@Service
public class PlanRouteTask extends AbstractTaskProxy {
    @Override
    public void doBeforeComplete(String taskId, Map<String, Object> variableMap) {
        Map<String, Object> transTypes = planningService.getTransTypes(taskId);
        variableMap.putAll(transTypes);

        long orderId = taskService.getOrderIdByTaskId(taskId);
        OrderEntity order = orderService.find(orderId);
        PlanningEntity planning = planningService.findByOrder(order);
        Collection<RouteEntity> routes = routeService.findByPlanning(planning);
        boolean hasWaterTransport = false;
        for (RouteEntity route : routes) {
            Collection<LegEntity> legs = route.getLegs();
            for (LegEntity leg : legs) {
                switch (TransMode.valueOf(leg.getTransMode())) {
                    case BARGE:
                    case VESSEL:
                        hasWaterTransport = true;
                        break;
                    default:
                        break;
                }
                if (hasWaterTransport) break;
            }
            if (hasWaterTransport) break;
        }
        variableMap.put("hasWaterTransport", hasWaterTransport);
    }
}
