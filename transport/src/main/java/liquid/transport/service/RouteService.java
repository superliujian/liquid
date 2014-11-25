package liquid.transport.service;

import liquid.finance.service.PurchaseService;
import liquid.service.AbstractService;
import liquid.transport.persistence.domain.LegEntity;
import liquid.transport.persistence.domain.TransportEntity;
import liquid.transport.persistence.repository.RouteRepository;
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
public class RouteService extends AbstractService<TransportEntity, RouteRepository> {

    @Autowired
    private LegService legService;

    @Autowired
    private PurchaseService purchaseService;

    @Override
    public void doSaveBefore(TransportEntity transportEntity) { }

    @Transactional("transactionManager")
    public Iterable<TransportEntity> findByOrderId(Long orderId) {
        Iterable<TransportEntity> routes = repository.findByOrderId(orderId);
        for (TransportEntity route : routes) {
            route.getContainers().size();
            route.getRailContainers().size();
            route.getBargeContainers().size();
            route.getVesselContainers().size();
            route.getDeliveryContainers();
        }
        return routes;
    }

    public TransportEntity find(Long id) {
        TransportEntity route = repository.findOne(id);
        return route;
    }

    @Transactional("transactionManager")
    public void delete(Long id) {
        TransportEntity route = repository.findOne(id);
        Collection<LegEntity> legs = route.getLegs();
        for (LegEntity leg : legs) {
            purchaseService.deleteByLegId(leg.getId());
        }
        repository.delete(id);
    }
}
