package liquid.service;

import liquid.persistence.domain.ServiceTypeEntity;
import liquid.persistence.repository.ServiceTypeRepository;
import org.springframework.stereotype.Service;

/**
 * Created by redbrick9 on 4/26/14.
 */
@Service
public class ServiceTypeServiceImpl extends AbstractCachedService<ServiceTypeEntity, ServiceTypeRepository> {
    @Override
    public void doSaveBefore(ServiceTypeEntity entity) { }

    public Iterable<ServiceTypeEntity> findEnabled() {
        return repository.findByState(0);
    }
}
