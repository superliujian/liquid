package liquid.shipping.service;

import liquid.finance.service.PurchaseService;
import liquid.service.AbstractService;
import liquid.shipping.persistence.domain.LegEntity;
import liquid.shipping.persistence.domain.RouteEntity;
import liquid.shipping.persistence.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private LegService legService;

    @Autowired
    private PurchaseService purchaseService;

    @Override
    public void doSaveBefore(RouteEntity routeEntity) { }

    @Transactional("transactionManager")
    public Iterable<RouteEntity> findByOrderId(Long orderId) {
        Iterable<RouteEntity> routes = repository.findByOrderId(orderId);
        for (RouteEntity route : routes) {
            route.getContainers().size();
            route.getRailContainers().size();
            route.getBargeContainers().size();
            route.getVesselContainers().size();
            route.getDeliveryContainers();
        }
        return routes;
    }

    public RouteEntity find(Long id) {
        RouteEntity route = repository.findOne(id);
        return route;
    }

    @Transactional("transactionManager")
    public void delete(Long id) {
        RouteEntity route = repository.findOne(id);
        Collection<LegEntity> legs = route.getLegs();
        for (LegEntity leg : legs) {
            purchaseService.deleteByLegId(leg.getId());
        }
        repository.delete(id);
    }
}
