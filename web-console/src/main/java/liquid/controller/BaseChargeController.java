package liquid.controller;

import liquid.persistence.domain.ChargeType;
import liquid.persistence.repository.ChargeTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/6/13
 * Time: 10:55 PM
 */
@Component
public abstract class BaseChargeController {

    @Autowired
    protected ChargeTypeRepository ctRepository;

    public Map<Long, String> chargeTypesToMap() {
        Map<Long, String> map = new HashMap<Long, String>();
        Iterable<ChargeType> iterable = ctRepository.findAll();
        for (ChargeType chargeType : iterable) {
            map.put(chargeType.getId(), chargeType.getName());
        }
        return map;
    }
}
