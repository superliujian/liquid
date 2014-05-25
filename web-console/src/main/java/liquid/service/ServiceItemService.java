package liquid.service;

import liquid.persistence.domain.ChargeType;
import liquid.persistence.domain.ServiceItem;
import liquid.persistence.domain.ServiceProviderEntity;
import liquid.persistence.repository.ServiceItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by redbrick9 on 5/24/14.
 */
@Service
public class ServiceItemService {
    private static final int CONTAINER_PROVIDER_TYPE_ID = 4;

    @Autowired
    private ServiceItemRepository serviceItemRepository;

    public List<ServiceProviderEntity> findByChargeType(long chargeTypeId) {
        List<ServiceProviderEntity> serviceProviderEntities = new ArrayList<>();
        Iterable<ServiceItem> serviceItems = serviceItemRepository.findByChargeType(new ChargeType(chargeTypeId));
        for (ServiceItem serviceItem : serviceItems) {
            serviceProviderEntities.add(serviceItem.getServiceProvider());
        }
        return serviceProviderEntities;
    }

    public List<ServiceProviderEntity> findContainerOwners() {
        return findByChargeType(CONTAINER_PROVIDER_TYPE_ID);
    }
}
