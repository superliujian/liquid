package liquid.service;

import liquid.charge.persistence.domain.ChargeEntity;
import liquid.charge.persistence.repository.ChargeRepository;
import liquid.dto.EarningDto;
import liquid.metadata.ChargeWay;
import liquid.order.persistence.domain.OrderEntity;
import liquid.order.service.OrderService;
import liquid.persistence.domain.ExchangeRate;
import liquid.persistence.repository.ExchangeRateRepository;
import liquid.persistence.repository.ServiceProviderRepository;
import liquid.security.SecurityContext;
import liquid.shipping.persistence.domain.LegEntity;
import liquid.shipping.persistence.domain.RouteEntity;
import liquid.shipping.persistence.repository.LegRepository;
import liquid.shipping.service.RouteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/5/13
 * Time: 2:59 PM
 */
@Service
public class ChargeService extends AbstractService<ChargeEntity, ChargeRepository> {
    private static final Logger logger = LoggerFactory.getLogger(ChargeService.class);

    @Autowired
    private ChargeRepository chargeRepository;

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    private LegRepository legRepository;

    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RouteService routeService;

    // TODO: have to enhance this function.
    @Override
    public void doSaveBefore(ChargeEntity entity) {
        // update charge
        if (null != entity.getId()) return;

        // new charge
        RouteEntity route = null;
        if (null != entity.getRoute()) {
            route = routeService.find(entity.getRoute().getId());
            entity.setOrder(route.getOrder());
        }
        if (null != entity.getLeg()) {
            LegEntity leg = legRepository.findOne(entity.getLeg().getId());
            route = leg.getRoute();
            entity.setOrder(route.getOrder());
        }
        if (null == entity.getOrder()) {
            OrderEntity order = taskService.findOrderByTaskId(entity.getTaskId());
            entity.setOrder(order);
        }

        if (ChargeWay.PER_ORDER.getValue() == entity.getWay()) {
            entity.setTotalPrice(entity.getUnitPrice());
            entity.setUnitPrice(0L);
        } else if (ChargeWay.PER_CONTAINER.getValue() == entity.getWay()) {
            entity.setTotalPrice(entity.getUnitPrice() * route.getContainerQty());
        } else {
            logger.warn("{} is out of charge way range.", entity.getWay());
        }

        entity.setCreateRole(SecurityContext.getInstance().getRole());
        // Compute grand total
        OrderEntity order = orderService.find(entity.getOrder().getId());
        if (entity.getCurrency() == 0) {
            order.setGrandTotal(order.getGrandTotal() + entity.getTotalPrice());
        } else {
            order.setGrandTotal(order.getGrandTotal() + Math.round(entity.getTotalPrice() * getExchangeRate()));
        }
        orderService.save(order);

    }

    public Iterable<ChargeEntity> getChargesByOrderId(long orderId) {
        return chargeRepository.findByOrderId(orderId);
    }

    @Transactional("transactionManager")
    public void removeCharge(long chargeId) {
        ChargeEntity charge = chargeRepository.findOne(chargeId);
        OrderEntity order = charge.getOrder();

        if (charge.getCurrency() == 0) {
            order.setGrandTotal(order.getGrandTotal() - charge.getTotalPrice());
        } else {
            order.setGrandTotal(order.getGrandTotal() - Math.round(charge.getTotalPrice() * getExchangeRate()));
        }

        orderService.save(order);

        chargeRepository.delete(chargeId);
    }

    public Iterable<ChargeEntity> findByLegId(long legId) {
        return chargeRepository.findByLegId(legId);
    }

    public Iterable<ChargeEntity> findByRouteId(long routeId) {
        return chargeRepository.findByRouteId(routeId);
    }

    public Iterable<ChargeEntity> findByTaskId(String taskId) {
        return chargeRepository.findByTaskId(taskId);
    }

    public long total(Iterable<ChargeEntity> charges) {
        long total = 0L;
        for (ChargeEntity charge : charges) {
            if (charge.getCurrency() == 0) {
                total += charge.getTotalPrice();
            } else {
                total += Math.round(charge.getTotalPrice() * getExchangeRate());
            }
        }
        return total;
    }

    public Iterable<ChargeEntity> findByOrderId(long orderId) {
        return chargeRepository.findByOrderId(orderId);
    }

    public Page<ChargeEntity> findByOrderId(long orderId, Pageable pageable) {
        return chargeRepository.findByOrderId(orderId, pageable);
    }

    public Iterable<ChargeEntity> findByOrderNo(String orderNo) {
        return chargeRepository.findByOrderOrderNoLike("%" + orderNo + "%");
    }

    public Iterable<ChargeEntity> findBySpName(String spName) {
        return chargeRepository.findBySpNameLike("%" + spName + "%");
    }

    public ChargeEntity find(long id) {
        return chargeRepository.findOne(id);
    }

    public Iterable<ChargeEntity> findAll() {
        return chargeRepository.findAll();
    }

    public Page<ChargeEntity> findAll(Pageable pageable) {
        return chargeRepository.findAll(pageable);
    }

    public Iterable<ChargeEntity> findByOrderIdAndCreateRole(long orderId, String createRole) {
        return chargeRepository.findByOrderIdAndCreateRole(orderId, createRole);
    }

    public EarningDto calculateEarning(OrderEntity order, Iterable<ChargeEntity> charges) {
        EarningDto earning = new EarningDto();

        double exchangeRate = getExchangeRate();

        earning.setSalesPriceCny(order.getReceivableSummary().getCny());
        earning.setSalesPriceUsd(order.getReceivableSummary().getUsd());
        earning.setDistyPrice(order.getDistyPrice());
        earning.setGrandTotal(order.getGrandTotal());
        earning.setGrossMargin(earning.getSalesPriceCny() + Math.round(earning.getSalesPriceUsd() * exchangeRate) - order.getGrandTotal());
        earning.setSalesProfit(order.getReceivableSummary().getCny() + Math.round(earning.getSalesPriceUsd() * exchangeRate) - order.getDistyPrice());
        earning.setDistyProfit(earning.getDistyPrice() - order.getGrandTotal());
        return earning;
    }

    public double getExchangeRate() {
        ExchangeRate exchangeRate = exchangeRateRepository.findOne(1L);
        return null == exchangeRate ? 0.00 : exchangeRate.getValue();
    }

    public void setExchangeRate(double value) {
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setId(1L);
        exchangeRate.setValue(value);
        exchangeRateRepository.save(exchangeRate);
    }
}
