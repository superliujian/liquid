package liquid.transport.service;

import liquid.finance.service.PurchaseService;
import liquid.service.AbstractService;
import liquid.transport.persistence.domain.LegEntity;
import liquid.transport.persistence.domain.ShipmentEntity;
import liquid.transport.persistence.repository.TransportRepository;
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
public class TransportService extends AbstractService<ShipmentEntity, TransportRepository> {

    @Autowired
    private LegService legService;

    @Autowired
    private PurchaseService purchaseService;

    @Override
    public void doSaveBefore(ShipmentEntity shipmentEntity) { }

    @Transactional("transactionManager")
    public Iterable<ShipmentEntity> findByOrderId(Long orderId) {
        Iterable<ShipmentEntity> routes = repository.findByOrderId(orderId);
        for (ShipmentEntity route : routes) {
            route.getContainers().size();
            route.getRailContainers().size();
            route.getBargeContainers().size();
            route.getVesselContainers().size();
            route.getDeliveryContainers();
        }
        return routes;
    }

    public ShipmentEntity find(Long id) {
        ShipmentEntity route = repository.findOne(id);
        return route;
    }

    @Transactional("transactionManager")
    public void delete(Long id) {
        ShipmentEntity route = repository.findOne(id);
        Collection<LegEntity> legs = route.getLegs();
        for (LegEntity leg : legs) {
            purchaseService.deleteByLegId(leg.getId());
        }
        repository.delete(id);
    }
}
