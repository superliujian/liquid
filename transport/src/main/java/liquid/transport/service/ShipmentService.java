package liquid.transport.service;

import liquid.purchase.service.PurchaseService;
import liquid.service.AbstractService;
import liquid.transport.persistence.domain.LegEntity;
import liquid.transport.persistence.domain.ShipmentEntity;
import liquid.transport.persistence.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class ShipmentService extends AbstractService<ShipmentEntity, ShipmentRepository> {

    @Autowired
    private LegService legService;

    @Autowired
    private PurchaseService purchaseService;

    @Override
    public void doSaveBefore(ShipmentEntity shipmentEntity) { }

    public Page<ShipmentEntity> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional("transactionManager")
    public Iterable<ShipmentEntity> findByOrderId(Long orderId) {
        Iterable<ShipmentEntity> shipmentSet = repository.findByOrderId(orderId);
        for (ShipmentEntity shipment : shipmentSet) {
            shipment.getContainers().size();
            shipment.getRailContainers().size();
            shipment.getBargeContainers().size();
            shipment.getVesselContainers().size();
            shipment.getDeliveryContainers();
        }
        return shipmentSet;
    }

    public ShipmentEntity find(Long id) {
        ShipmentEntity shipment = repository.findOne(id);
        return shipment;
    }

    @Transactional("transactionManager")
    public void delete(Long id) {
        ShipmentEntity shipment = repository.findOne(id);
        Collection<LegEntity> legs = shipment.getLegs();
        for (LegEntity leg : legs) {
            purchaseService.deleteByLegId(leg.getId());
        }
        repository.delete(id);
    }
}
