package liquid.service;

import liquid.domain.ServiceItem;
import liquid.persistence.domain.ChargeType;
import liquid.persistence.domain.ServiceItemEntity;
import liquid.persistence.domain.ServiceProviderEntity;
import liquid.persistence.domain.ServiceSubtypeEntity;
import liquid.persistence.repository.ServiceItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by redbrick9 on 5/24/14.
 */
@Service
public class ServiceItemService {
    private static final int CONTAINER_PROVIDER_TYPE_ID = 4;

    @Autowired
    private ServiceItemRepository serviceItemRepository;

    @Autowired
    private ServiceSubtypeService serviceSubtypeService;

    public List<ServiceProviderEntity> findByChargeType(long chargeTypeId) {
        List<ServiceProviderEntity> serviceProviderEntities = new ArrayList<>();
//        Iterable<ServiceItemEntity> serviceItems = serviceItemRepository.findByChargeType(new ChargeType(chargeTypeId));
//        for (ServiceItemEntity serviceItem : serviceItems) {
//            serviceProviderEntities.add(serviceItem.getServiceProvider());
//        }
        return serviceProviderEntities;
    }

    public List<ServiceProviderEntity> findContainerOwners() {
        return findByChargeType(CONTAINER_PROVIDER_TYPE_ID);
    }

    public Iterable<ServiceItemEntity> findEnabled() {
        return serviceItemRepository.findAll();
    }

    public void add(ServiceItemEntity serviceItem) {
//        ServiceSubtypeEntity serviceSubtype = serviceSubtypeService.find(serviceItem.getServiceSubtypeId());
//        serviceItem.setServiceSubtype(serviceSubtype);
//        serviceItemRepository.save(serviceItem);
    }

    public ServiceItemEntity find(long id) {
        return serviceItemRepository.findOne(id);
    }

    public void delete(Set<ServiceItemEntity> serviceItems) {
        serviceItemRepository.delete(serviceItems);
    }
}