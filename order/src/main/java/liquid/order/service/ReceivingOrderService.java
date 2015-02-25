package liquid.order.service;

import liquid.order.persistence.domain.ReceivingContainer;
import liquid.order.persistence.domain.ReceivingOrderEntity;
import liquid.order.persistence.repository.ReceivingContainerRepository;
import liquid.order.persistence.repository.ReceivingOrderRepository;
import liquid.persistence.domain.LocationEntity;
import liquid.service.CustomerService;
import liquid.service.GoodsService;
import liquid.service.LocationService;
import liquid.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/13/13
 * Time: 4:57 PM
 */
@Service
public class ReceivingOrderService extends AbstractBaseOrderService<ReceivingOrderEntity, ReceivingOrderRepository> {
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
    private GoodsService goodsService;

    @Autowired
    private LocationService locationService;

    public ReceivingOrderEntity newOrder(List<LocationEntity> locationEntities) {
        ReceivingOrderEntity order = new ReceivingOrderEntity();
        LocationEntity second = CollectionUtil.tryToGet2ndElement(locationEntities);
        order.setSrcLocId(second.getId());
        return order;
    }

    @Override
    public void doSaveBefore(ReceivingOrderEntity entity) {}

    public List<ReceivingOrderEntity> findAllOrderByDesc() {
        return recvOrderRepository.findAll(new Sort(Sort.Direction.DESC, "id"));
    }

    public ReceivingOrderEntity find(long id) {
        ReceivingOrderEntity order = recvOrderRepository.findOne(id);

        order.setCustomerId(order.getCustomerId());
        order.setGoodsId(order.getGoodsId());

        Iterable<ReceivingContainer> containers = recvContainerRepository.findByReceivingOrder(order);
        for (ReceivingContainer container : containers) {
            order.getBicCodes().add(container.getBicCode());
        }

        return order;
    }

    public Iterable<ReceivingOrderEntity> findByOrderNo(String orderNo) {
        return recvOrderRepository.findByOrderNoLike("%" + orderNo + "%");
    }

    public Page<ReceivingOrderEntity> findByOrderNoLike(String orderNo, Pageable pageable) {
        return recvOrderRepository.findByOrderNoLike("%" + orderNo + "%", pageable);
    }

    public Page<ReceivingOrderEntity> findAll(Pageable pageable) {
        return recvOrderRepository.findAll(pageable);
    }

    public Page<ReceivingOrderEntity> findByCustomerId(Long customerId, String createdBy, Pageable pageable) {
        return recvOrderRepository.findByCustomerIdAndCreatedBy(customerId, createdBy, pageable);
    }
}
