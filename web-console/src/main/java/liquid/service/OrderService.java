package liquid.service;

import liquid.context.BusinessContext;
import liquid.metadata.ContainerType;
import liquid.persistence.domain.OrderEntity;
import liquid.persistence.domain.OrderHistory;
import liquid.persistence.repository.*;
import liquid.service.bpm.ActivitiEngineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
    BusinessContext businessContext;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderHistoryRepository orderHistoryRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ActivitiEngineService bpmService;

    @Autowired
    private ContainerSubtypeService containerSubtypeService;

    @Autowired
    private ServiceItemService serviceItemService;

    public OrderEntity duplicate(OrderEntity oldOne) {
        OrderEntity order = new OrderEntity();

        order.setCustomer(oldOne.getCustomer());
        order.setSrcLoc(oldOne.getSrcLoc());
        order.setDstLoc(oldOne.getDstLoc());
        order.setConsignee(oldOne.getConsignee());
        order.setConsigneePhone(oldOne.getConsigneePhone());
        order.setConsigneeAddress(oldOne.getConsigneeAddress());
        order.setGoods(oldOne.getGoods());
        order.setGoodsWeight(oldOne.getGoodsWeight());
        order.setTradeType(oldOne.getTradeType());
        order.setContainerType(oldOne.getContainerType());
        order.setContainerCap(oldOne.getContainerCap());
        order.setContainerQty(oldOne.getContainerQty());
        order.setLoadingType(oldOne.getLoadingType());
        order.setLoadingAddress(oldOne.getLoadingAddress());
        order.setLoadingContact(oldOne.getLoadingContact());
        order.setLoadingPhone(oldOne.getLoadingPhone());
        order.setSalesPriceCny(oldOne.getSalesPriceCny());

        order.setOrigination(oldOne.getSrcLoc().getId());
        order.setDestination(oldOne.getDstLoc().getId());
        order.setServiceTypeId(oldOne.getServiceType().getId());
        order.setCustomerId(oldOne.getCustomer().getId());
        order.setCustomerName0(oldOne.getCustomer().getName());
        order.setGoodsId(oldOne.getGoods().getId());
        return order;
    }

    @Transactional("transactionManager")
    public OrderEntity save(OrderEntity order) {
        if (null != order.getId()) {
            OrderEntity oldOrder = find(order.getId());
            oldOrder.getServiceItems().removeAll(order.getServiceItems());
            if (oldOrder.getServiceItems().size() > 0)
                serviceItemService.delete(oldOrder.getServiceItems());
        }
        return orderRepository.save(order);
    }

    public OrderEntity findByTaskId(String taskId) {
        long orderId = taskService.getOrderIdByTaskId(taskId);
        return find(orderId);
    }

    public List<OrderEntity> findAllOrderByDesc() {
        return orderRepository.findAll(new Sort(Sort.Direction.DESC, "id"));
    }

    public Page<OrderEntity> findByUpdateUser(String uid, Pageable pageable) {
        return orderRepository.findByUpdateUser(uid, pageable);
    }

    public Page<OrderEntity> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public OrderEntity find(long id) {
        OrderEntity order = orderRepository.findOne(id);

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

        return order;
    }

    public Iterable<OrderEntity> findByOrderNo(String orderNo) {
        return orderRepository.findByOrderNoLike("%" + orderNo + "%");
    }

    public Iterable<OrderEntity> findByCustomerName(String customerName) {
        return orderRepository.findByCustomerNameLike("%" + customerName + "%");
    }

    @Transactional("transactionManager")
    public void moveToHistory(long id) {
        OrderEntity order = find(id);

        OrderHistory orderHistory = newOrderHistory(order);
        orderHistoryRepository.save(orderHistory);
    }

    private OrderHistory newOrderHistory(OrderEntity order) {
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setOrderId(order.getId());
        orderHistory.setCustomer(order.getCustomer());
        orderHistory.setSrcLoc(order.getSrcLoc());
        orderHistory.setDstLoc(order.getDstLoc());
        orderHistory.setConsignee(order.getConsignee());
        orderHistory.setConsigneePhone(order.getConsigneePhone());
        orderHistory.setConsigneeAddress(order.getConsigneeAddress());
        orderHistory.setGoods(order.getGoods());
        orderHistory.setGoodsWeight(order.getGoodsWeight());
        orderHistory.setTradeType(order.getTradeType());
        orderHistory.setContainerType(order.getContainerType());
        orderHistory.setContainerCap(order.getContainerCap());
        orderHistory.setContainerQty(order.getContainerQty());
        orderHistory.setLoadingType(order.getLoadingType());
        orderHistory.setLoadingAddress(order.getLoadingAddress());
        orderHistory.setLoadingContact(order.getLoadingContact());
        orderHistory.setLoadingEt(order.getLoadingEt());
        orderHistory.setSalesPriceCny(order.getSalesPriceCny());
        orderHistory.setGrandTotal(order.getGrandTotal());
        // Calculate earning
        orderHistory.setGrossMargin(order.getSalesPriceCny() - order.getGrandTotal());
        orderHistory.setSalesProfit(order.getSalesPriceCny() - order.getDistyPrice());
        orderHistory.setDistyProfit(orderHistory.getDistyPrice() - order.getGrandTotal());
        // End
        orderHistory.setCreateRole(order.getCreateRole());
        orderHistory.setCreateUser(order.getCreateUser());
        orderHistory.setCreateTime(order.getCreateTime());
        orderHistory.setFinishTime(new Date());
        return orderHistory;
    }

    public static void main(String[] args) {

    }
}
