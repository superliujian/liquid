package liquid.transport.service;

import liquid.finance.service.PurchaseService;
import liquid.service.AbstractService;
import liquid.transport.persistence.domain.LegEntity;
import liquid.transport.persistence.domain.ShipmentEntity;
import liquid.transport.persistence.repository.LegRepository;
import liquid.transport.persistence.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by redbrick9 on 9/4/14.
 */
@Service
public class LegService extends AbstractService<LegEntity, LegRepository> {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private PurchaseService purchaseService;

    @Override
    public void doSaveBefore(LegEntity legEntity) {}

    public LegEntity find(Long id) {
        return repository.findOne(id);
    }

    public List<LegEntity> findByShipmentAndTransMode(ShipmentEntity shipment, int transMode) {
        return repository.findByShipmentAndTransMode(shipment, transMode);
    }

    @Transactional("transactionManager")
    public void delete(Long id) {
        purchaseService.deleteByLegId(id);

        LegEntity leg = repository.findOne(id);
        ShipmentEntity shipment = leg.getShipment();
        shipment.getLegs().remove(leg);
        shipmentRepository.save(shipment);
    }
}
