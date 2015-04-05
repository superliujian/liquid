package liquid.operation.service;

import liquid.operation.domain.ServiceProvider;
import liquid.operation.domain.ServiceProviderType;
import liquid.operation.domain.ServiceSubtype;
import liquid.operation.repository.ServiceProviderRepository;
import liquid.operation.repository.ServiceProviderTypeRepository;
import liquid.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/13/13
 * Time: 12:56 AM
 */
@Service
public class ServiceProviderServiceImpl extends AbstractService<ServiceProvider, ServiceProviderRepository>
        implements InternalServiceProviderService {

    @Autowired
    private ServiceSubtypeService serviceSubtypeService;

    @Autowired
    private ServiceProviderTypeRepository serviceProviderTypeRepository;

    @Override
    public void doSaveBefore(ServiceProvider serviceProvider) { }

    @Override
    public Page<ServiceProvider> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Iterable<ServiceProvider> findAll() {
        return repository.findOrderByName();
    }

    @Override
    @Transactional("transactionManager")
    public ServiceProvider find(Long id) {
        ServiceProvider entity = repository.findOne(id);
        Set<ServiceSubtype> serviceSubtypeEntitySet = entity.getSubtypes();
        serviceSubtypeEntitySet.size();
        return entity;
    }

    @Override
    public Iterable<ServiceProvider> findByType(Long typeId) {
        ServiceProviderType type = serviceProviderTypeRepository.findOne(typeId);
        return repository.findByType(type);
    }

    public Iterable<ServiceProvider> findByType(ServiceProviderType type) {
        return repository.findByType(type);
    }

    public Map<Long, String> getSpTypes() {
        Map<Long, String> spTypes = new TreeMap<Long, String>();
        Iterable<ServiceProviderType> iterable = serviceProviderTypeRepository.findAll();
        for (ServiceProviderType spType : iterable) {
            spTypes.put(spType.getId(), spType.getName());
        }
        return spTypes;
    }

    @Override
    public Iterable<ServiceProvider> findByQueryNameLike(String name) {
        return repository.findByQueryNameLike("%" + name + "%");
    }

    @Override
    public List<ServiceProvider> findByServiceSubtypeId(Long serviceSubtypeId) {
        HashSet<ServiceSubtype> serviceSubtypes = new HashSet<>();
        serviceSubtypes.add(ServiceSubtype.newInstance(ServiceSubtype.class, serviceSubtypeId));
        return repository.findBySubtypes(serviceSubtypes);
    }
}
