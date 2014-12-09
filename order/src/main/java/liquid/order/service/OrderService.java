package liquid.order.service;

import liquid.container.domain.ContainerType;
import liquid.container.service.ContainerSubtypeService;
import liquid.order.domain.OrderStatus;
import liquid.order.persistence.domain.OrderEntity;
import liquid.order.persistence.domain.OrderEntity_;
import liquid.order.persistence.repository.OrderHistoryRepository;
import liquid.persistence.domain.CustomerEntity_;
import liquid.persistence.repository.CustomerRepository;
import liquid.persistence.repository.LocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.domain.Specifications.where;

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

    public OrderEntity complete(Long orderId) {
        OrderEntity order = find(orderId);
        order.setStatus(OrderStatus.COMPLETED.getValue());
        order = save(order);
        return order;
    }

    public Page<OrderEntity> findByCreateUser(String username, Pageable pageable) {
        return repository.findByCreatedBy(username, pageable);
    }

    public Page<OrderEntity> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<OrderEntity> findByStatus(Integer status, Pageable pageable) {
        return repository.findByStatus(status, pageable);
    }

    @Transactional("transactionManager")
    public OrderEntity find(Long id) {
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
        order.setGoodsId(order.getGoodsId());
        // Initialize one to many children
        order.getServiceItems().size();
        return order;
    }

    /**
     * Criteria Query for order.
     *
     * @param orderNo
     * @param customerName
     * @param username
     * @param pageable
     * @return
     */
    public Page<OrderEntity> findAll(final String orderNo, final String customerName, final String username, final Pageable pageable) {
        List<Specification<OrderEntity>> specList = new ArrayList<>();

        if (null != orderNo) {
            Specification<OrderEntity> orderNoSpec = new Specification<OrderEntity>() {
                @Override
                public Predicate toPredicate(Root<OrderEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
                    return builder.like(root.get(OrderEntity_.orderNo), "%" + orderNo + "%");
                }
            };
            specList.add(orderNoSpec);
        }

        if (null != customerName) {
            Specification<OrderEntity> customerNameSpec = new Specification<OrderEntity>() {
                @Override
                public Predicate toPredicate(Root<OrderEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
                    return builder.like(root.get(OrderEntity_.customer).get(CustomerEntity_.name), "%" + customerName + "%");
                }
            };
            specList.add(customerNameSpec);
        }

        if (null != username) {
            Specification<OrderEntity> usernameSpec = new Specification<OrderEntity>() {
                @Override
                public Predicate toPredicate(Root<OrderEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
                    return builder.equal(root.get(OrderEntity_.createdBy.getName()), username);
                }
            };
        }

        if (specList.size() > 0) {
            Specifications<OrderEntity> specifications = where(specList.get(0));
            for (int i = 1; i < specList.size(); i++) {
                specifications.and(specList.get(i));
            }
            return repository.findAll(specifications, pageable);
        }

        return repository.findAll(pageable);
    }
}
