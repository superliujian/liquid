package liquid.service;

import liquid.persistence.domain.ServiceProviderEntity;
import liquid.persistence.domain.ServiceSubtypeEntity;
import liquid.persistence.domain.SpTypeEnity;
import liquid.persistence.repository.ServiceProviderRepository;
import liquid.persistence.repository.ServiceProviderTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/13/13
 * Time: 12:56 AM
 */
@Service
public class ServiceProviderService extends AbstractService<ServiceProviderEntity, ServiceProviderRepository> {
    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    @Autowired
    private ServiceSubtypeService serviceSubtypeService;

    @Autowired
    private ServiceProviderTypeRepository serviceProviderTypeRepository;

    @Override
    public void doSaveBefore(ServiceProviderEntity serviceProvider) { }

    public Page<ServiceProviderEntity> findAll(Pageable pageable) {
        return serviceProviderRepository.findAll(pageable);
    }

    public Iterable<ServiceProviderEntity> findAll() {
        return serviceProviderRepository.findOrderByName();
    }

    @Transactional("transactionManager")
    public ServiceProviderEntity find(long id) {
        ServiceProviderEntity entity = serviceProviderRepository.findOne(id);
        Set<ServiceSubtypeEntity> serviceSubtypeEntitySet = entity.getServiceSubtypes();
        serviceSubtypeEntitySet.size();
        return entity;
    }

    public Iterable<ServiceProviderEntity> findByType(long typeId) {
        SpTypeEnity type = serviceProviderTypeRepository.findOne(typeId);
        return serviceProviderRepository.findByType(type);
    }

    public Iterable<ServiceProviderEntity> findByType(SpTypeEnity type) {
        return serviceProviderRepository.findByType(type);
    }

    public Map<Long, String> getSpTypes() {
        Map<Long, String> spTypes = new TreeMap<Long, String>();
        Iterable<SpTypeEnity> iterable = serviceProviderTypeRepository.findAll();
        for (SpTypeEnity spType : iterable) {
            spTypes.put(spType.getId(), spType.getName());
        }
        return spTypes;
    }
}
