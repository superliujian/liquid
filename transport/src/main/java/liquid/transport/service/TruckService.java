package liquid.transport.service;

import liquid.service.AbstractService;
import liquid.transport.persistence.domain.TruckEntity;
import liquid.transport.persistence.repository.TruckRepository;
import org.springframework.stereotype.Service;

/**
 * Created by Tao Ma on 1/1/15.
 */
@Service
public class TruckService extends AbstractService<TruckEntity, TruckRepository> {
    @Override
    public void doSaveBefore(TruckEntity entity) {}

    public Iterable<TruckEntity> findByShipmentId(Long shipmentId) {
        return repository.findByShipmentId(shipmentId);
    }
}
