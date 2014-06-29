package liquid.service;

import liquid.persistence.domain.*;
import liquid.persistence.repository.ReceivingContainerRepository;
import liquid.persistence.repository.ReceivingOrderRepository;
import liquid.utils.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/13/13
 * Time: 4:57 PM
 */
@Service
public class ReceivingOrderService extends AbstractBaseOrderService {
    private static Logger logger = LoggerFactory.getLogger(ReceivingOrderService.class);

    // Repositories
    @Autowired
    private ReceivingOrderRepository recvOrderRepository;

    @Autowired
    private ReceivingContainerRepository recvContainerRepository;

    // Services
    @Autowired
    private CustomerService customerService;

    @Autowired
    private CargoTypeService cargoTypeService;

    @Autowired
    private LocationService locationService;

    public ReceivingOrder newOrder(List<LocationEntity> locationEntities) {
        ReceivingOrder order = new ReceivingOrder();
        LocationEntity second = CollectionUtils.tryToGet2ndElement(locationEntities);
        order.setOrigination(second.getId());
        return order;
    }

    @Override
    public void doSaveBefore(OrderEntity entity) {

    }

    @Transactional(value = "transactionManager")
    public ReceivingOrder save(ReceivingOrder order) {
        ServiceTypeEntity serviceType = serviceTypeService.find(order.getServiceTypeId());
        CustomerEntity customer = customerService.find(order.getCustomerId());
        GoodsEntity goods = cargoTypeService.find(order.getGoodsId());
        LocationEntity srcLoc = locationService.find(order.getOrigination());
        LocationEntity dstLoc = locationService.find(order.getDestination());
        if (null != serviceType) order.setServiceType(serviceType);
        if (null != customer) order.setCustomer(customer);
        if (null != goods) order.setGoods(goods);
        if (null != srcLoc) order.setSrcLoc(srcLoc);
        if (null != dstLoc) order.setDstLoc(dstLoc);

        order.setOrderNo(computeOrderNo(order.getCreateRole(), order.getServiceType().getCode()));
        logger.debug("receivingOrder: {}", order);
        ReceivingOrder newOne = recvOrderRepository.save(order);

        Collection<ReceivingContainer> containers = recvContainerRepository.findByReceivingOrder(order);
        recvContainerRepository.delete(containers);

        containers.clear();
        for (String bicCode : order.getBicCodes()) {
            ReceivingContainer container = new ReceivingContainer();
            container.setReceivingOrder(newOne);
            container.setBicCode(bicCode);
            containers.add(container);
        }
        recvContainerRepository.save(containers);

        return newOne;
    }

    public Iterable<ReceivingOrder> findAll() {
        return recvOrderRepository.findAll();
    }

    public List<ReceivingOrder> findAllOrderByDesc() {
        return recvOrderRepository.findAll(new Sort(Sort.Direction.DESC, "id"));
    }

    public ReceivingOrder find(long id) {
        ReceivingOrder order = recvOrderRepository.findOne(id);

        order.setOrigination(order.getSrcLoc().getId());
        order.setDestination(order.getDstLoc().getId());
        order.setServiceTypeId(order.getServiceType().getId());
        order.setCustomerId(order.getCustomer().getId());
        order.setCustomerName0(order.getCustomer().getName());
        order.setGoodsId(order.getGoods().getId());

        Iterable<ReceivingContainer> containers = recvContainerRepository.findByReceivingOrder(order);
        for (ReceivingContainer container : containers) {
            order.getBicCodes().add(container.getBicCode());
        }

        return order;
    }

    public Iterable<ReceivingOrder> findByOrderNo(String orderNo) {
        return recvOrderRepository.findByOrderNoLike("%" + orderNo + "%");
    }

    public Iterable<ReceivingOrder> findByCustomerName(String customerName) {
        return recvOrderRepository.findByCustomerNameLike("%" + customerName + "%");
    }
}
