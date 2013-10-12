package liquid.service;

import liquid.persistence.domain.*;
import liquid.persistence.repository.*;
import liquid.service.bpm.ActivitiEngineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/1/13
 * Time: 9:03 PM
 */
@Service
public class PlanningService {
    private static final Logger logger = LoggerFactory.getLogger(PlanningService.class);

    @Autowired
    private ActivitiEngineService bpmService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private PlanningRepository planningRepository;

    public void editPlanning(String taskId, long planningId, boolean sameRoute) {
        Planning oldOne = planningRepository.findOne(planningId);
        oldOne.setSameRoute(sameRoute);
        planningRepository.save(oldOne);
    }

    @Transactional("transactionManager")
    public Map<String, Object> getTransTypes(String taskId) {
        Map<String, Object> transTypes = new HashMap<String, Object>();
        transTypes.put("hasRailway", false);
        transTypes.put("hasBarge", false);
        transTypes.put("hasVessel", false);

        Collection<Route> routes = routeService.findByTaskId(taskId);
        for (Route route : routes) {
            Collection<Leg> legs = route.getLegs();
            for (Leg leg : legs) {
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

    public Planning findByTaskId(String taskId) {
        Order order = orderService.findByTaskId(taskId);
        return planningRepository.findByOrder(order);
    }
}
