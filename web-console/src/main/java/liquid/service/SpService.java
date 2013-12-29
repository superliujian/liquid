package liquid.service;

import liquid.persistence.domain.ChargeType;
import liquid.persistence.domain.ServiceProvider;
import liquid.persistence.domain.SpType;
import liquid.persistence.repository.ChargeTypeRepository;
import liquid.persistence.repository.ServiceRepository;
import liquid.persistence.repository.SpRepository;
import liquid.persistence.repository.SpTypeRepository;
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
public class SpService {
    @Autowired
    private SpRepository spRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private SpTypeRepository spTypeRepository;

    @Autowired
    private ChargeService chargeService;

    @Autowired
    private ChargeTypeRepository chargeTypeRepository;

    public Iterable<ServiceProvider> findAll() {
        return spRepository.findOrderByName();
    }

    public ServiceProvider find(long id) {
        ServiceProvider serviceProvider = spRepository.findOne(id);
        Collection<liquid.persistence.domain.Service> servicesCollection = serviceRepository.findBySp(serviceProvider);

        if (servicesCollection != null) {
            liquid.persistence.domain.Service[] services = servicesCollection.toArray(new liquid.persistence.domain.Service[0]);
            long[] chargeTypeIds = new long[services.length];
            for (int i = 0; i < chargeTypeIds.length; i++) {
                chargeTypeIds[i] = services[i].getChargeType().getId();
            }
            serviceProvider.setChargeTypeIds(chargeTypeIds);
        } else {
            serviceProvider.setChargeTypeIds(new long[0]);
        }

        return serviceProvider;
    }

    public Iterable<ServiceProvider> findByType(long typeId) {
        SpType type = spTypeRepository.findOne(typeId);
        return spRepository.findByType(type);
    }

    public Iterable<ServiceProvider> findByType(SpType type) {
        return spRepository.findByType(type);
    }

    @Transactional("transactionManager")
    public void save(ServiceProvider sp) {
        SpType type = spTypeRepository.findOne(sp.getTypeId());
        sp.setType(type);
        spRepository.save(sp);

        Iterable<liquid.persistence.domain.Service> deletingServices = serviceRepository.findBySp(sp);
        serviceRepository.delete(deletingServices);

        long[] chargeTypeIds = sp.getChargeTypeIds();
        if (chargeTypeIds != null && chargeTypeIds.length > 0) {
            liquid.persistence.domain.Service[] services = new liquid.persistence.domain.Service[chargeTypeIds.length];
            for (int i = 0; i < services.length; i++) {
                ChargeType chargeType = chargeTypeRepository.findOne(chargeTypeIds[i]);

                services[i] = new liquid.persistence.domain.Service();
                services[i].setSp(sp);
                services[i].setChargeType(chargeType);
                services[i].setName(chargeType.getName());
            }
            serviceRepository.save(Arrays.asList(services));
        }
    }

    public Map<Long, String> getSpTypes() {
        Map<Long, String> spTypes = new TreeMap<Long, String>();
        Iterable<SpType> iterable = spTypeRepository.findAll();
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

    public List<ServiceProvider> findByChargeType(long chargeTypeId) {
        List<ServiceProvider> serviceProviders = new ArrayList<ServiceProvider>();
        ChargeType chargeType = chargeTypeRepository.findOne(chargeTypeId);
        Iterable<liquid.persistence.domain.Service> services = serviceRepository.findByChargeType(chargeType);
        for (liquid.persistence.domain.Service service : services) {
            serviceProviders.add(service.getSp());
        }
        return serviceProviders;
    }
}
