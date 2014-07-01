package liquid.service;

import liquid.persistence.domain.ServiceTypeEntity;
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

    public ServiceTypeEntity save(ServiceTypeEntity serviceType) {
        return serviceTypeRepository.save(serviceType);
    }

    public ServiceTypeEntity find(long id) {
        return serviceTypeRepository.findOne(id);
    }

    public Iterable<ServiceTypeEntity> findAll() {
        return serviceTypeRepository.findAll();
    }

    public Iterable<ServiceTypeEntity> findEnabled() {
        return serviceTypeRepository.findByState(0);
    }
}
