package liquid.task;

import liquid.shipping.domain.TransMode;
import liquid.shipping.persistence.domain.LegEntity;
import liquid.shipping.persistence.domain.RouteEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by redbrick9 on 6/7/14.
 */
@DefinitionKey("planRoute")
@Service
public class PlanRouteTask extends AbstractTaskProxy {
    private static final Logger logger = LoggerFactory.getLogger(PlanRouteTask.class);

    @Override
    public void doBeforeComplete(String taskId, Map<String, Object> variableMap) {
        Map<String, Object> transTypes = getTransTypes(taskId);
        variableMap.putAll(transTypes);

        Long orderId = taskService.getOrderIdByTaskId(taskId);
        Iterable<RouteEntity> routes = routeService.findByOrderId(orderId);
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

    @Transactional("transactionManager")
    public Map<String, Object> getTransTypes(String taskId) {
        Map<String, Object> transTypes = new HashMap<String, Object>();
        transTypes.put("hasRailway", false);
        transTypes.put("hasBarge", false);
        transTypes.put("hasVessel", false);

        Long orderId = taskService.findOrderId(taskId);

        Iterable<RouteEntity> routes = routeService.findByOrderId(orderId);
        for (RouteEntity route : routes) {
            Collection<LegEntity> legs = route.getLegs();
            for (LegEntity leg : legs) {
                TransMode mode = TransMode.valueOf(leg.getTransMode());
                switch (mode) {
                    case RAIL:
                        transTypes.put("hasRailway", true);
                        break;
                    case BARGE:
                        transTypes.put("hasBarge", true);
                        break;
                    case VESSEL:
                        transTypes.put("hasVessel", true);
                        break;
                    default:
                        logger.warn("{} transportation mode is illegal.");
                        break;
                }
            }
        }

        logger.debug("The order has the transportation {}.", transTypes);
        return transTypes;
    }
}
