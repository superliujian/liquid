package liquid.operation.service;

import liquid.operation.domain.ServiceProviderType;
import liquid.operation.repository.ServiceProviderTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Tao Ma on 4/2/15.
 */
@Service
public class ServiceProviderTypeServiceImpl implements ServiceProviderTypeService {

    @Autowired
    private ServiceProviderTypeRepository serviceProviderTypeRepository;

    @Override
    public ServiceProviderType find(Long id) {
        return serviceProviderTypeRepository.findOne(id);
    }
}
