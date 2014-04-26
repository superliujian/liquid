package liquid.service;

import liquid.persistence.domain.ServiceType;
import liquid.persistence.repository.ServiceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by redbrick9 on 4/26/14.
 */
@Service
public class ServiceTypeService {

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    public ServiceType save(ServiceType serviceType) {
        return serviceTypeRepository.save(serviceType);
    }

    public ServiceType find(long id) {
        return serviceTypeRepository.findOne(id);
    }

    public Iterable<ServiceType> findAll() {
        return serviceTypeRepository.findAll();
    }

    public Iterable<ServiceType> findEnabled() {
        return serviceTypeRepository.findByState(0);
    }
}
