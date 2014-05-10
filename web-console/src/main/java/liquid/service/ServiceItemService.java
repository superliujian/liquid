package liquid.service;

import liquid.persistence.domain.ServiceItem;
import liquid.persistence.domain.ServiceSubtype;
import liquid.persistence.repository.ServiceItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by redbrick9 on 5/9/14.
 */
@Service
public class ServiceItemService {
    private static final Logger logger = LoggerFactory.getLogger(ServiceItemService.class);

    @Autowired
    private ServiceItemRepository serviceItemRepository;

    @Autowired
    private ServiceSubtypeService serviceSubtypeService;

    public Iterable<ServiceItem> findEnabled() {
        return serviceItemRepository.findAll();
    }

    public void add(ServiceItem serviceItem) {
        ServiceSubtype serviceSubtype = serviceSubtypeService.find(serviceItem.getServiceSubtypeId());
        serviceItem.setServiceSubtype(serviceSubtype);
        serviceItemRepository.save(serviceItem);
    }

    public ServiceItem find(long id) {
        return serviceItemRepository.findOne(id);
    }
}
