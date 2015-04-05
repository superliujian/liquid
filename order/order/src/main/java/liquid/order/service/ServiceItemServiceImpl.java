package liquid.order.service;

import liquid.operation.domain.ServiceProvider;
import liquid.operation.service.ServiceProviderService;
import liquid.order.domain.ServiceItemEntity;
import liquid.order.persistence.repository.ServiceItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by redbrick9 on 5/24/14.
 */
@Service
public class ServiceItemServiceImpl implements ServiceItemService {
    private static final Long CONTAINER_PROVIDER_TYPE_ID = 1L;

    @Autowired
    private ServiceItemRepository serviceItemRepository;

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Override
    public List<ServiceProvider> findContainerOwners() {
        return serviceProviderService.findByServiceSubtypeId(CONTAINER_PROVIDER_TYPE_ID);
    }

    public Iterable<ServiceItemEntity> findEnabled() {
        return serviceItemRepository.findAll();
    }

    public ServiceItemEntity find(long id) {
        return serviceItemRepository.findOne(id);
    }

    public void delete(Iterable<ServiceItemEntity> serviceItems) {
        serviceItemRepository.delete(serviceItems);
    }
}