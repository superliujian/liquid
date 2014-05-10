package liquid.service;

import liquid.persistence.domain.ServiceSubtype;
import liquid.persistence.repository.ServiceSubtypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by redbrick9 on 5/9/14.
 */
@Service
public class ServiceSubtypeService {

    @Autowired
    private ServiceSubtypeRepository serviceSubtypeRepository;

    public Iterable<ServiceSubtype> findEnabled() {
        return serviceSubtypeRepository.findAll();
    }

    public void add(ServiceSubtype serviceSubtype) {
        serviceSubtypeRepository.save(serviceSubtype);
    }

    public ServiceSubtype find(long id) {
        return serviceSubtypeRepository.findOne(id);
    }
}
