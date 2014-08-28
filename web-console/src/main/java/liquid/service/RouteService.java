package liquid.service;

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

import java.util.Collection;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/12/13
 * Time: 12:26 AM
 */
@Service
public class RouteService extends AbstractService<RouteEntity, RouteRepository> {
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

    public Iterable<RouteEntity> findByOrderId(Long orderId) {
        Iterable<RouteEntity> routes = routeRepository.findByOrderId(orderId);
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

    public Iterable<RouteEntity> findByOrderId0(Long orderId) {
        return routeRepository.findByOrderId(orderId);
    }

    public RouteEntity find(long id) {
        RouteEntity route = routeRepository.findOne(id);
        return route;
    }

    @Override
    public void doSaveBefore(RouteEntity routeEntity) {
    }
}
