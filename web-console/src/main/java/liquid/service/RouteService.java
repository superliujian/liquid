package liquid.service;

import liquid.persistence.domain.*;
import liquid.persistence.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/12/13
 * Time: 12:26 AM
 */
@Service
public class RouteService {

    @Autowired
    private PlanningService planningService;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private LegRepository legRepository;

    @Deprecated
    @Autowired
    private RailContainerRepository railContainerRepository;

    @Autowired
    private RailContainerService railContainerService;

    @Autowired
    private BargeContainerService bargeContainerService;

    @Autowired
    private VesselContainerService vesselContainerService;

    @Autowired
    private DeliveryContainerRepository deliveryContainerRepository;

    @Autowired
    private ShippingContainerRepository scRepository;

    public List<RouteEntity> findByTaskId(String taskId) {
        Planning planning = planningService.findByTaskId(taskId);
        List<RouteEntity> routes = planning.getRoutes();
        for (RouteEntity route : routes) {
            Collection<Leg> legs = legRepository.findByRoute(route);
            route.setLegs(legs);
            Collection<ShippingContainer> containers = scRepository.findByRoute(route);
            route.setContainers(containers);
            Collection<RailContainer> railContainers = railContainerService.findByRoute(route);
            route.setRailContainers(railContainers);
            Collection<BargeContainer> bargeContainers = bargeContainerService.findByRoute(route);
            route.setBargeContainers(bargeContainers);
            Collection<VesselContainer> vesselContainers = vesselContainerService.findByRoute(route);
            route.setVesselContainers(vesselContainers);
            Collection<DeliveryContainer> deliveryContainers = deliveryContainerRepository.findByRoute(route);
            route.setDeliveryContainers(deliveryContainers);
        }
        return routes;
    }

    public Collection<RouteEntity> findByPlanning(Planning planning) {
        if (planning == null) {
            return Collections.EMPTY_LIST;
        }
        Collection<RouteEntity> routes = planning.getRoutes();
        for (RouteEntity route : routes) {
            Collection<Leg> legs = legRepository.findByRoute(route);
            route.setLegs(legs);
            Collection<ShippingContainer> containers = scRepository.findByRoute(route);
            route.setContainers(containers);
        }
        return routes;
    }

    public RouteEntity find(long id) {
        RouteEntity route = routeRepository.findOne(id);
        return route;
    }

    /**
     * Update for delivery date.
     *
     * @param formBean
     * @return
     */
    @Transactional("transactionManager")
    public void save(RouteEntity formBean) {
        RouteEntity oldOne = routeRepository.findOne(formBean.getId());

        if (formBean.isBatch()) {
            Collection<RouteEntity> routes = routeRepository.findByPlanning(oldOne.getPlanning());
            routeRepository.save(routes);
        } else {
            routeRepository.save(oldOne);
        }
    }

    @Transactional("transactionManager")
    public RouteEntity save(RouteEntity formBean, Planning planning) {
        formBean.setPlanning(planning);
        RouteEntity route = routeRepository.save(formBean);
        return route;
    }
}
