package liquid.facade;

import liquid.domain.ServiceProvider;
import liquid.persistence.domain.ServiceProviderEntity;
import liquid.persistence.domain.ServiceProviderTypeEnity;
import liquid.persistence.domain.ServiceSubtypeEntity;
import liquid.pinyin4j.PinyinHelper;
import liquid.service.ServiceProviderService;
import liquid.service.ServiceSubtypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by redbrick9 on 6/9/14.
 */
@Service
public class ServiceProviderFacade {

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private ServiceSubtypeService serviceSubtypeService;

    public ServiceProviderEntity save(ServiceProvider serviceProvider) {
        ServiceProviderEntity entity = convert(serviceProvider);
        return serviceProviderService.save(entity);
    }

    public ServiceProvider find(long id) {
        ServiceProviderEntity entity = serviceProviderService.find(id);
        ServiceProvider serviceProvider = convert(entity);
        return serviceProvider;
    }

    public List<ServiceProvider> findBySubtypeId(Long subtypeId) {
        List<ServiceProviderEntity> providerEntities = serviceProviderService.findByServiceSubtypeId(subtypeId);
        List<ServiceProvider> serviceProviders = new ArrayList<>();
        for (ServiceProviderEntity providerEntity : providerEntities) {
            serviceProviders.add(simplyConvert(providerEntity));
        }
        return serviceProviders;
    }

    @Transactional("transactionManager")
    public List<ServiceProvider> findByQueryNameLike(String name) {
        List<ServiceProvider> list = new ArrayList<>();
        Iterable<ServiceProviderEntity> entities = serviceProviderService.findByQueryNameLike(name);
        for (ServiceProviderEntity entity : entities) {
            list.add(convert(entity));
        }
        return list;
    }

    public void changeStatus(Long id, Integer status) {
        ServiceProviderEntity entity = serviceProviderService.find(id);
        entity.setStatus(status);
        serviceProviderService.save(entity);
    }

    private ServiceProviderEntity convert(ServiceProvider serviceProvider) {
        ServiceProviderEntity entity = new ServiceProviderEntity();
        entity.setId(serviceProvider.getId());
        entity.setCode(serviceProvider.getCode());
        entity.setName(serviceProvider.getName());
        String queryName = PinyinHelper.converterToFirstSpell(serviceProvider.getName()) + ";" + serviceProvider.getName();
        entity.setQueryName(queryName);
        entity.setType(ServiceProviderTypeEnity.newInstance(ServiceProviderTypeEnity.class, serviceProvider.getTypeId()));
        entity.setAddress(serviceProvider.getAddress());
        entity.setPostcode(serviceProvider.getPostcode());
        entity.setContact(serviceProvider.getContact());
        entity.setPhone(serviceProvider.getPhone());
        entity.setCell(serviceProvider.getCell());

        for (Long subtypeId : serviceProvider.getSubtypeIds()) {
            ServiceSubtypeEntity serviceSubtypeEntity = ServiceSubtypeEntity.newInstance(ServiceSubtypeEntity.class, subtypeId);
            entity.getServiceSubtypes().add(serviceSubtypeEntity);
        }
        return entity;
    }

    /**
     * For single service provider
     *
     * @param entity
     * @return
     */
    private ServiceProvider convert(ServiceProviderEntity entity) {
        ServiceProvider serviceProvider = simplyConvert(entity);
        Set<ServiceSubtypeEntity> serviceSubtypeEntities = entity.getServiceSubtypes();
        Long[] subtypeIds = new Long[serviceSubtypeEntities.size()];
        int i = 0;
        for (ServiceSubtypeEntity serviceSubtypeEntity : serviceSubtypeEntities) {
            subtypeIds[i] = serviceSubtypeEntity.getId();
            i++;
        }
        serviceProvider.setSubtypeIds(subtypeIds);
        return serviceProvider;
    }

    /**
     * For service provider set.
     *
     * @param entity
     * @return
     */
    private ServiceProvider simplyConvert(ServiceProviderEntity entity) {
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setId(entity.getId());
        serviceProvider.setCode(entity.getCode());
        serviceProvider.setName(entity.getName());
        serviceProvider.setTypeId(entity.getType().getId());
        serviceProvider.setAddress(entity.getAddress());
        serviceProvider.setPostcode(entity.getPostcode());
        serviceProvider.setContact(entity.getContact());
        serviceProvider.setPhone(entity.getPhone());
        serviceProvider.setCell(entity.getCell());
        serviceProvider.setStatus(entity.getStatus());
        return serviceProvider;
    }
}
