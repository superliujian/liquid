package liquid.transport.service;

import liquid.purchase.service.PurchaseService;
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
public class LegService {

    @Autowired
    private LegRepository legRepository;

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private PurchaseService purchaseService;

    public LegEntity save(LegEntity leg) {
        return legRepository.save(leg);
    }

    public LegEntity find(Long id) {
        return legRepository.findOne(id);
    }

    public List<LegEntity> findByShipmentAndTransMode(ShipmentEntity shipment, int transMode) {
        return legRepository.findByShipmentAndTransMode(shipment, transMode);
    }

    @Transactional("transactionManager")
    public void delete(Long id) {
        purchaseService.deleteByLegId(id);

        LegEntity leg = legRepository.findOne(id);
        ShipmentEntity shipment = leg.getShipment();
        shipment.getLegs().remove(leg);
        shipmentRepository.save(shipment);
    }
}
