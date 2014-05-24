package liquid.service;

import liquid.persistence.domain.ChargeType;
import liquid.persistence.domain.ServiceItem;
import liquid.persistence.domain.ServiceProviderEntity;
import liquid.persistence.domain.SpType;
import liquid.persistence.repository.ChargeTypeRepository;
import liquid.persistence.repository.ServiceItemRepository;
import liquid.persistence.repository.ServiceProviderRepository;
import liquid.persistence.repository.ServiceProviderTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ServiceProviderService {
    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    @Autowired
    private ServiceItemRepository serviceItemRepository;

    @Autowired
    private ServiceProviderTypeRepository serviceProviderTypeRepository;

    @Autowired
    private ChargeService chargeService;

    @Autowired
    private ChargeTypeRepository chargeTypeRepository;

    public Iterable<ServiceProviderEntity> findAll() {
        return serviceProviderRepository.findOrderByName();
    }

    public ServiceProviderEntity find(long id) {
        ServiceProviderEntity serviceProviderEntity = serviceProviderRepository.findOne(id);
        serviceProviderEntity.setTypeId(serviceProviderEntity.getType().getId());

        Collection<ServiceItem> servicesCollection = serviceItemRepository.findByServiceProvider(serviceProviderEntity);

        if (servicesCollection != null) {
            ServiceItem[] serviceItems = servicesCollection.toArray(new ServiceItem[0]);
            long[] chargeTypeIds = new long[serviceItems.length];
            for (int i = 0; i < chargeTypeIds.length; i++) {
                chargeTypeIds[i] = serviceItems[i].getChargeType().getId();
            }
            serviceProviderEntity.setChargeTypeIds(chargeTypeIds);
        } else {
            serviceProviderEntity.setChargeTypeIds(new long[0]);
        }

        return serviceProviderEntity;
    }

    public Iterable<ServiceProviderEntity> findByType(long typeId) {
        SpType type = serviceProviderTypeRepository.findOne(typeId);
        return serviceProviderRepository.findByType(type);
    }

    public Iterable<ServiceProviderEntity> findByType(SpType type) {
        return serviceProviderRepository.findByType(type);
    }

    @Transactional("transactionManager")
    public void save(ServiceProviderEntity sp) {
        SpType type = serviceProviderTypeRepository.findOne(sp.getTypeId());
        sp.setType(type);
        serviceProviderRepository.save(sp);

        Iterable<ServiceItem> deletingServices = serviceItemRepository.findByServiceProvider(sp);
        serviceItemRepository.delete(deletingServices);

        long[] chargeTypeIds = sp.getChargeTypeIds();
        if (chargeTypeIds != null && chargeTypeIds.length > 0) {
            ServiceItem[] serviceItems = new ServiceItem[chargeTypeIds.length];
            for (int i = 0; i < serviceItems.length; i++) {
                ChargeType chargeType = chargeTypeRepository.findOne(chargeTypeIds[i]);

                serviceItems[i] = new ServiceItem();
                serviceItems[i].setServiceProvider(sp);
                serviceItems[i].setChargeType(chargeType);
                serviceItems[i].setName(chargeType.getName());
            }
            serviceItemRepository.save(Arrays.asList(serviceItems));
        }
    }

    public Map<Long, String> getSpTypes() {
        Map<Long, String> spTypes = new TreeMap<Long, String>();
        Iterable<SpType> iterable = serviceProviderTypeRepository.findAll();
        for (SpType spType : iterable) {
            spTypes.put(spType.getId(), spType.getName());
        }
        return spTypes;
    }

    public long spTypeByChargeType(int chargeType) {
        switch (chargeType) {
            case 1://驳船费
                return 2;//驳船
            case 2:  //大船费
            case 4: // 用箱费
            case 6: // 用柜费用
                return 3;
            case 3: //铁路运费
            case 5: //铁路包干费
                return 1;  //铁路
            case 7: // 码头费
            case 8: // THC
            case 9: // 装卸费
            case 11: //码头转场费
                return 5; // 码头
            case 10: // 报关报检费
                return 7;
            case 12: // 公路运费
            case 14: // 派车费用
                return 4; // 拖车
            case 13: // 堆存费
                return 6; //堆场
            default:
                return 0;
        }
    }

    public List<ServiceProviderEntity> findByChargeType(long chargeTypeId) {
        List<ServiceProviderEntity> serviceProviderEntities = new ArrayList<ServiceProviderEntity>();
        ChargeType chargeType = chargeTypeRepository.findOne(chargeTypeId);
        Iterable<ServiceItem> services = serviceItemRepository.findByChargeType(chargeType);
        for (ServiceItem serviceItem : services) {
            serviceProviderEntities.add(serviceItem.getServiceProvider());
        }
        return serviceProviderEntities;
    }
}
