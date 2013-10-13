package liquid.service;

import liquid.persistence.domain.Charge;
import liquid.persistence.domain.ChargeType;
import liquid.persistence.domain.Leg;
import liquid.persistence.domain.ServiceProvider;
import liquid.persistence.repository.ChargeRepository;
import liquid.persistence.repository.ChargeTypeRepository;
import liquid.persistence.repository.LegRepository;
import liquid.persistence.repository.SpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/5/13
 * Time: 2:59 PM
 */
@Service
public class ChargeService {
    @Autowired
    private ChargeRepository chargeRepository;

    @Autowired
    private ChargeTypeRepository ctRepository;

    @Autowired
    private LegRepository legRepository;

    @Autowired
    private SpRepository spRepository;

    public Iterable<Charge> getChargesByOrderId(long orderId) {
        return chargeRepository.findByOrderId(orderId);
    }

    public List<Charge> getChargesByTaskId(String taskId) {
        return chargeRepository.findByTaskId(taskId);
    }

    public Charge newCharge(long legId) {
        Charge charge = new Charge();

        Leg leg = legRepository.findOne(legId);
        charge.setSp(leg.getSp());

        return charge;
    }

    public Charge addCharge(long legId, Charge charge) {
        ServiceProvider sp = spRepository.findOne(charge.getSpId());
        charge.setSp(sp);
        Leg leg = legRepository.findOne(legId);
        charge.setLeg(leg);
        charge.setOrder(leg.getRoute().getPlanning().getOrder());
        return chargeRepository.save(charge);
    }

    public void removeCharge(long chargeId) {
        chargeRepository.delete(chargeId);
    }

    public Iterable<Charge> findByLegId(long legId) {
        return chargeRepository.findByLegId(legId);
    }

    public Iterable<Charge> findByTaskId(String taskId) {
        return chargeRepository.findByTaskId(taskId);
    }

    public Map<Long, String> getChargeTypes() {
        Map<Long, String> cts = new TreeMap<Long, String>();
        Iterable<ChargeType> iterable = ctRepository.findAll();
        for (ChargeType chargeType : iterable) {
            cts.put(chargeType.getId(), chargeType.getName());
        }
        return cts;
    }
}
