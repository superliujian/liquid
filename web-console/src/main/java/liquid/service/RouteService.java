package liquid.service;

import liquid.order.persistence.domain.OrderEntity;
import liquid.shipping.persistence.domain.*;
import liquid.shipping.persistence.repository.LegRepository;
import liquid.shipping.persistence.repository.RailContainerRepository;
import liquid.shipping.persistence.repository.RouteRepository;
import liquid.shipping.persistence.repository.ShippingContainerRepository;
import liquid.shipping.service.BargeContainerService;
import liquid.shipping.service.RailContainerService;
import liquid.shipping.service.VesselContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
    private DeliveryContainerService deliveryContainerService;

    @Autowired
    private ShippingContainerRepository scRepository;

    public List<RouteEntity> findByTaskId(String taskId) {
        PlanningEntity planning = planningService.findByTaskId(taskId);
        List<RouteEntity> routes = planning.getRoutes();
        for (RouteEntity route : routes) {
            Collection<LegEntity> legs = legRepository.findByRoute(route);
            route.setLegs(legs);
            Collection<ShippingContainerEntity> containers = scRepository.findByRoute(route);
            route.setContainers(containers);
            Collection<RailContainer> railContainers = railContainerService.findByRoute(route);
            route.setRailContainers(railContainers);
            Collection<BargeContainer> bargeContainers = bargeContainerService.findByRoute(route);
            route.setBargeContainers(bargeContainers);
            Collection<VesselContainer> vesselContainers = vesselContainerService.findByRoute(route);
            route.setVesselContainers(vesselContainers);
            Collection<DeliveryContainer> deliveryContainers = deliveryContainerService.findByRoute(route);
            route.setDeliveryContainers(deliveryContainers);
        }
        return routes;
    }

    public List<RouteEntity> findByOrderId(Long orderId) {
        PlanningEntity planning = planningService.findByOrder(OrderEntity.newInstance(OrderEntity.class, orderId));
        List<RouteEntity> routes = planning.getRoutes();
        for (RouteEntity route : routes) {
            Collection<LegEntity> legs = legRepository.findByRoute(route);
            route.setLegs(legs);
            Collection<ShippingContainerEntity> containers = scRepository.findByRoute(route);
            route.setContainers(containers);
            Collection<RailContainer> railContainers = railContainerService.findByRoute(route);
            route.setRailContainers(railContainers);
            Collection<BargeContainer> bargeContainers = bargeContainerService.findByRoute(route);
            route.setBargeContainers(bargeContainers);
            Collection<VesselContainer> vesselContainers = vesselContainerService.findByRoute(route);
            route.setVesselContainers(vesselContainers);
            Collection<DeliveryContainer> deliveryContainers = deliveryContainerService.findByRoute(route);
            route.setDeliveryContainers(deliveryContainers);
        }
        return routes;
    }

    public Collection<RouteEntity> findByPlanning(PlanningEntity planning) {
        if (planning == null) {
            return Collections.EMPTY_LIST;
        }
        Collection<RouteEntity> routes = planning.getRoutes();
        for (RouteEntity route : routes) {
            Collection<LegEntity> legs = legRepository.findByRoute(route);
            route.setLegs(legs);
            Collection<ShippingContainerEntity> containers = scRepository.findByRoute(route);
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
    public RouteEntity save(RouteEntity formBean, PlanningEntity planning) {
        formBean.setPlanning(planning);
        RouteEntity route = routeRepository.save(formBean);
        return route;
    }
}
