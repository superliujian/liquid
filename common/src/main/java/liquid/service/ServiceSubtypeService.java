package liquid.service;

import liquid.persistence.domain.ServiceProviderEntity;
import liquid.persistence.domain.ServiceSubtypeEntity;
import liquid.persistence.repository.ServiceSubtypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Created by redbrick9 on 5/9/14.
 */
@Service
public class ServiceSubtypeService {

    @Autowired
    private ServiceSubtypeRepository serviceSubtypeRepository;

    public Iterable<ServiceSubtypeEntity> findEnabled() {
        return serviceSubtypeRepository.findAll();
    }

    public void add(ServiceSubtypeEntity serviceSubtype) {
        serviceSubtypeRepository.save(serviceSubtype);
    }

    @Transactional("transactionManager")
    public ServiceSubtypeEntity find(long id) {
        ServiceSubtypeEntity serviceSubtype = serviceSubtypeRepository.findOne(id);
        List<ServiceProviderEntity> serviceProviders = serviceSubtype.getServiceProviders();
        for (ServiceProviderEntity serviceProvider : serviceProviders) {
            Set<ServiceSubtypeEntity> serviceSubtypes = serviceProvider.getServiceSubtypes();
            for (ServiceSubtypeEntity subtype : serviceSubtypes) {}
        }
        return serviceSubtype;
    }
}
