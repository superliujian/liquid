package liquid.service;

import liquid.metadata.TransMode;
import liquid.persistence.domain.Leg;
import liquid.persistence.domain.OrderEntity;
import liquid.persistence.domain.Planning;
import liquid.persistence.domain.RouteEntity;
import liquid.persistence.repository.LegRepository;
import liquid.persistence.repository.PlanningRepository;
import liquid.service.bpm.ActivitiEngineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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

    @Autowired
    private LegRepository legRepository;

    public void editPlanning(String taskId, long planningId, boolean sameRoute) {
        Planning oldOne = planningRepository.findOne(planningId);
        oldOne.setSameRoute(sameRoute);
        planningRepository.save(oldOne);
    }

    @Transactional("transactionManager")
    public Planning findOne(Long id) {
        Planning planning = planningRepository.findOne(id);
        List<RouteEntity> routes = planning.getRoutes();
        for (RouteEntity route : routes) { }
        return planning;
    }

    @Transactional("transactionManager")
    public Map<String, Object> getTransTypes(String taskId) {
        Map<String, Object> transTypes = new HashMap<String, Object>();
        transTypes.put("hasRailway", false);
        transTypes.put("hasBarge", false);
        transTypes.put("hasVessel", false);

        Collection<RouteEntity> routes = routeService.findByTaskId(taskId);
        for (RouteEntity route : routes) {
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

    @Transactional(value = "transactionManager")
    public Planning findByTaskId(String taskId) {
        OrderEntity order = orderService.findByTaskId(taskId);
        return findByOrder(order);
    }

    public Leg findLeg(long legId) {
        if (0L == legId) return null;
        Leg leg = legRepository.findOne(legId);
        if (null != leg.getSp()) leg.setSpId(leg.getSp().getId());
        return leg;
    }

    @Transactional(value = "transactionManager")
    public Planning findByOrder(OrderEntity order) {
        Planning planning = planningRepository.findByOrder(order);
        List<RouteEntity> routes = planning.getRoutes();
        for (RouteEntity route : routes) { }
        return planning;
    }
}
