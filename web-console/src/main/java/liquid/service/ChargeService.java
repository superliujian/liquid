package liquid.service;

import liquid.dto.EarningDto;
import liquid.metadata.ChargeWay;
import liquid.persistence.domain.*;
import liquid.persistence.repository.ChargeRepository;
import liquid.persistence.repository.ChargeTypeRepository;
import liquid.persistence.repository.LegRepository;
import liquid.persistence.repository.SpRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    private static final Logger logger = LoggerFactory.getLogger(ChargeService.class);

    @Autowired
    private ChargeRepository chargeRepository;

    @Autowired
    private ChargeTypeRepository ctRepository;

    @Autowired
    private LegRepository legRepository;

    @Autowired
    private SpRepository spRepository;

    @Autowired
    private OrderService orderService;

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
        ChargeType type = ctRepository.findOne(charge.getTypeId());
        charge.setType(type);

        if (ChargeWay.PER_ORDER.getValue() == charge.getWay()) {
            charge.setUnitPrice(0L);
        } else if (ChargeWay.PER_CONTAINER.getValue() == charge.getWay()) {
            charge.setTotalPrice(charge.getUnitPrice() * leg.getRoute().getContainerQty());
        } else {
            logger.warn("{} is out of charge way range.", charge.getWay());
        }

        return chargeRepository.save(charge);
    }

    public Charge addCharge(Charge charge, String uid) {
        Order order = orderService.findByTaskId(charge.getTaskId());
        charge.setOrder(order);

        ServiceProvider sp = spRepository.findOne(charge.getSpId());
        charge.setSp(sp);

        ChargeType type = ctRepository.findOne(charge.getTypeId());
        charge.setType(type);

        if (ChargeWay.PER_ORDER.getValue() == charge.getWay()) {
            charge.setUnitPrice(0L);
        } else if (ChargeWay.PER_CONTAINER.getValue() == charge.getWay()) {
            charge.setTotalPrice(charge.getUnitPrice() * order.getContainerQty());
        } else {
            logger.warn("{} is out of charge way range.", charge.getWay());
        }

        charge.setCreateUser(uid);
        charge.setCreateTime(new Date());
        charge.setUpdateUser(uid);
        charge.setUpdateTime(new Date());
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

    public long total(Iterable<Charge> charges) {
        long total = 0L;
        for (Charge charge : charges) {
            total += charge.getTotalPrice();
        }
        return total;
    }

    public Map<Long, String> getChargeTypes() {
        Map<Long, String> cts = new TreeMap<Long, String>();
        Iterable<ChargeType> iterable = ctRepository.findAll();
        for (ChargeType chargeType : iterable) {
            cts.put(chargeType.getId(), chargeType.getName());
        }
        return cts;
    }

    public Iterable<Charge> findByOrderId(long orderId) {
        return chargeRepository.findByOrderId(orderId);
    }

    public Iterable<Charge> findBySpName(String spName) {
        return chargeRepository.findBySpNameLike("%" + spName + "%");
    }

    public void save(Charge charge) {
        if (null == charge.getType()) {
            ChargeType type = ctRepository.findOne(charge.getTypeId());
            charge.setType(type);
        }
        chargeRepository.save(charge);
    }

    public Charge find(long id) {
        Charge charge = chargeRepository.findOne(id);
        charge.setTypeId(charge.getType().getId());
        return charge;
    }

    public Iterable<Charge> findAll() {
        return chargeRepository.findAll();
    }

    public Iterable<Charge> findByOrderIdAndCreateRole(long orderId, String createRole) {
        return chargeRepository.findByOrderIdAndCreateRole(orderId, createRole);
    }

    public EarningDto calculateEarning(Order order, Iterable<Charge> charges) {
        EarningDto earning = new EarningDto();
        earning.setCost(order.getDistyPrice());
        earning.setSalesPrice(order.getSalesPrice());

        long procurementCost = 0L;
        for (Charge charge : charges) {
            procurementCost += charge.getTotalPrice();
        }

        earning.setGrossMargin(earning.getSalesPrice() - procurementCost);
        earning.setNetProfit(earning.getCost() - procurementCost);
        return earning;
    }
}
