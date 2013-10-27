package liquid.service;

import liquid.persistence.domain.ChargeType;
import liquid.persistence.domain.ServiceProvider;
import liquid.persistence.domain.SpType;
import liquid.persistence.repository.SpRepository;
import liquid.persistence.repository.SpTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;

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
    private SpTypeRepository spTypeRepository;

    public Iterable<ServiceProvider> findAll() {
        return spRepository.findAll();
    }

    public void save(ServiceProvider sp) {
        SpType type = spTypeRepository.findOne(sp.getTypeId());
        sp.setType(type);
        spRepository.save(sp);
    }

    public Map<Long, String> getSpTypes() {
        Map<Long, String> spTypes = new TreeMap<Long, String>();
        Iterable<SpType> iterable = spTypeRepository.findAll();
        for (SpType spType : iterable) {
            spTypes.put(spType.getId(), spType.getName());
        }
        return spTypes;
    }
}
