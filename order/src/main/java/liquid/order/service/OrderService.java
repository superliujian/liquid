package liquid.order.service;

import liquid.container.domain.ContainerType;
import liquid.container.service.ContainerSubtypeService;
import liquid.order.persistence.domain.OrderEntity;
import liquid.order.persistence.repository.OrderHistoryRepository;
import liquid.persistence.repository.CustomerRepository;
import liquid.persistence.repository.GoodsRepository;
import liquid.persistence.repository.LocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/29/13
 * Time: 8:03 PM
 */
@Service
public class OrderService extends AbstractBaseOrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private OrderHistoryRepository orderHistoryRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ContainerSubtypeService containerSubtypeService;

    @Autowired
    private ServiceItemService serviceItemService;

    @Transactional("transactionManager")
    public void doSaveBefore(OrderEntity order) {
        if (null != order.getId()) {
            OrderEntity oldOrder = find(order.getId());
            oldOrder.getServiceItems().removeAll(order.getServiceItems());
            if (oldOrder.getServiceItems().size() > 0)
                serviceItemService.delete(oldOrder.getServiceItems());
        }
    }

    public List<OrderEntity> findAllOrderByDesc() {
        return repository.findAll(new Sort(Sort.Direction.DESC, "id"));
    }

    public Page<OrderEntity> findByCreateUser(String username, Pageable pageable) {
        return repository.findByCreateUser(username, pageable);
    }

    public Page<OrderEntity> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<OrderEntity> findByStatus(Integer status, Pageable pageable) {
        return repository.findByStatus(status, pageable);
    }

    @Transactional("transactionManager")
    public OrderEntity find(long id) {
        OrderEntity order = repository.findOne(id);

        order.setOrigination(order.getSrcLoc().getId());
        order.setDestination(order.getDstLoc().getId());
        order.setServiceTypeId(order.getServiceType().getId());
        order.setCustomerId(order.getCustomer().getId());
        order.setContainerSubtypeId(order.getContainerSubtype().getId());
        if (order.getContainerType() == ContainerType.SELF.getType()) {
            order.setOwnContainerSubtypeId(order.getContainerSubtypeId());
        } else {
            order.setRailContainerSubtypeId(order.getContainerSubtypeId());
        }
        order.setCustomerName0(order.getCustomer().getName());
        order.setGoodsId(order.getGoods().getId());
        // Initialize one to many children
        order.getServiceItems().size();

        return order;
    }

    public Iterable<OrderEntity> findByOrderNo(String orderNo) {
        return repository.findByOrderNoLike("%" + orderNo + "%");
    }

    public Iterable<OrderEntity> findByCustomerName(String customerName) {
        return repository.findByCustomerNameLike("%" + customerName + "%");
    }
}
