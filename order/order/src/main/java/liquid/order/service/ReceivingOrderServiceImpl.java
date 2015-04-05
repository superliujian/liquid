package liquid.order.service;

import liquid.order.domain.ReceivingOrderEntity;
import liquid.order.persistence.repository.ReceivingContainerRepository;
import liquid.order.persistence.repository.ReceivingOrderRepository;
import liquid.operation.domain.Location;
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
public class ReceivingOrderServiceImpl extends AbstractBaseOrderService<ReceivingOrderEntity, ReceivingOrderRepository> {
    private static Logger logger = LoggerFactory.getLogger(ReceivingOrderServiceImpl.class);

    // Repositories
    @Autowired
    private ReceivingOrderRepository recvOrderRepository;

    @Autowired
    private ReceivingContainerRepository recvContainerRepository;

    public ReceivingOrderEntity newOrder(List<Location> locationEntities) {
        ReceivingOrderEntity order = new ReceivingOrderEntity();
        Location second = CollectionUtil.tryToGet2ndElement(locationEntities);
        order.setSrcLocId(second.getId());
        return order;
    }

    @Override
    public void doSaveBefore(ReceivingOrderEntity entity) {}

    public List<ReceivingOrderEntity> findAllOrderByDesc() {
        return recvOrderRepository.findAll(new Sort(Sort.Direction.DESC, "id"));
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
