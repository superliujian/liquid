package liquid.service;

import liquid.persistence.domain.ServiceItemEntity;
import liquid.persistence.domain.ServiceProviderEntity;
import liquid.persistence.repository.ServiceItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created by redbrick9 on 5/24/14.
 */
@Service
public class ServiceItemService {
    private static final int CONTAINER_PROVIDER_TYPE_ID = 1;

    @Autowired
    private ServiceItemRepository serviceItemRepository;

    @Autowired
    private ServiceSubtypeService serviceSubtypeService;

    public Set<ServiceProviderEntity> findContainerOwners() {
        return serviceSubtypeService.find(CONTAINER_PROVIDER_TYPE_ID).getServiceProviders();
    }

    public Iterable<ServiceItemEntity> findEnabled() {
        return serviceItemRepository.findAll();
    }

    public ServiceItemEntity find(long id) {
        return serviceItemRepository.findOne(id);
    }

    public void delete(Set<ServiceItemEntity> serviceItems) {
        serviceItemRepository.delete(serviceItems);
    }
}