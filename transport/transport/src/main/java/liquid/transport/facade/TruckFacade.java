package liquid.transport.facade;

import liquid.transport.persistence.domain.ShipmentEntity;
import liquid.transport.persistence.domain.TruckEntity;
import liquid.transport.service.TruckService;
import liquid.transport.web.domain.TruckForm;
import liquid.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tao Ma on 1/1/15.
 */
@Service
public class TruckFacade {

    @Autowired
    private TruckService truckService;

    public TruckForm find(Long id) {
        return convert(truckService.find(id));
    }

    public List<TruckForm> findByShipmentId(Long shipmentId) {
        Iterable<TruckEntity> truckEntityIterable = truckService.findByShipmentId(shipmentId);
        List<TruckForm> truckList = new ArrayList<>();
        for (TruckEntity truckEntity : truckEntityIterable) {
            TruckForm truck = convert(truckEntity);
            truckList.add(truck);
        }

        return truckList;
    }

    private TruckForm convert(TruckEntity entity) {
        TruckForm truck = new TruckForm();
        truck.setId(entity.getId());
        truck.setShipmentId(entity.getShipment().getId());
        truck.setPickingAt(DateUtil.stringOf(entity.getPickingAt()));
        truck.setServiceProviderId(entity.getServiceProviderId());
        truck.setLicensePlate(entity.getLicensePlate());
        truck.setDriver(entity.getDriver());
        return truck;
    }

    public List<TruckForm> save(List<TruckForm> truckList) {
        List<TruckEntity> truckEntityList = new ArrayList<>();
        for (TruckForm truck : truckList) {
            TruckEntity truckEntity = convert(truck);
            truckEntityList.add(truckEntity);
        }

        List<TruckForm> newTruckList = new ArrayList<>();
        Iterable<TruckEntity> truckEntityIterable = truckService.save(truckEntityList);
        for (TruckEntity truckEntity : truckEntityIterable) {
            TruckForm truck = convert(truckEntity);
            newTruckList.add(truck);
        }
        return newTruckList;
    }

    private TruckEntity convert(TruckForm truck) {
        TruckEntity entity = new TruckEntity();
        entity.setId(truck.getId());
        entity.setShipment(ShipmentEntity.newInstance(ShipmentEntity.class, truck.getShipmentId()));
        entity.setPickingAt(DateUtil.dateOf(truck.getPickingAt()));
        entity.setServiceProviderId(truck.getServiceProviderId());
        entity.setLicensePlate(truck.getLicensePlate());
        entity.setDriver(truck.getDriver());
        return entity;
    }
}
