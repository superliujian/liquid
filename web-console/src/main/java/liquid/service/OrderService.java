package liquid.service;

import liquid.context.BusinessContext;
import liquid.persistence.domain.*;
import liquid.persistence.repository.*;
import liquid.service.bpm.ActivitiEngineService;
import liquid.utils.CollectionUtils;
import liquid.utils.DateUtils;
import liquid.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/29/13
 * Time: 8:03 PM
 */
@Service
public class OrderService {
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
    private SequenceService sequenceService;

    @Autowired
    private ServiceTypeService serviceTypeService;

    public Order newOrder(List<Location> locations) {
        Order order = new Order();
        Location second = CollectionUtils.tryToGet2ndElement(locations);
        order.setDestination(second.getId());
        order.setLoadingEtStr(DateUtils.stringOf(new Date()));
        return order;
    }

    public Order duplicate(Order oldOne) {
        Order order = new Order();

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
        order.setLoadingEtStr(DateUtils.stringOf(new Date()));
        return order;
    }

    public void prepare(Order order) {
        ServiceType serviceType = serviceTypeService.find(order.getServiceTypeId());
        Customer customer = customerRepository.findOne(order.getCustomerId());
        Goods goods = goodsRepository.findOne(order.getGoodsId());
        Location srcLoc = locationRepository.findOne(order.getOrigination());
        Location dstLoc = locationRepository.findOne(order.getDestination());
        if (null != serviceType) order.setServiceType(serviceType);
        if (null != customer) order.setCustomer(customer);
        if (null != goods) order.setGoods(goods);
        if (null != srcLoc) order.setSrcLoc(srcLoc);
        if (null != dstLoc) order.setDstLoc(dstLoc);
        if (StringUtils.valuable(order.getLoadingEtStr()))
            order.setLoadingEt(DateUtils.dateOf(order.getLoadingEtStr()));
        logger.debug("Order: {}", order);
    }

    public void save(Order order) {
        prepare(order);
        orderRepository.save(order);
    }

    public void submit(Order order) {
        prepare(order);
        // compute order no.
        order.setOrderNo(computeOrderNo(order));
        logger.info("Order No: {}", order.getOrderNo());
        orderRepository.save(order);
        logger.debug("username: {}", businessContext.getUsername());

        Map<String, Object> variableMap = new HashMap<>();
        variableMap.put("loadingType", order.getLoadingType());
        variableMap.put("hasDelivery", order.isHasDelivery());
        variableMap.put("orderOwner", businessContext.getUsername());
        bpmService.startProcess(businessContext.getUsername(), order.getId(), variableMap);
    }

    public String computeOrderNo(Order order) {
        String sequenceName = String.format("%1$s%2$s%3$s%4$ty",
                salesOrMarketing(order.getCreateRole()),
                "C", // TODO
                order.getServiceType().getCode(),
                Calendar.getInstance());
        return String.format("%1$s%2$05d",
                sequenceName, sequenceService.getNextValue(sequenceName));
    }

    /**
     * @param role is like "ROLE_SALES" from LDAP.
     * @return
     */
    private String salesOrMarketing(String role) {
        return "ROLE_SALES".equals(role) ? "S" : "M";
    }

    public Order findByTaskId(String taskId) {
        long orderId = taskService.getOrderIdByTaskId(taskId);
        return find(orderId);
    }

    public List<Order> findAllOrderByDesc() {
        return orderRepository.findAll(new Sort(Sort.Direction.DESC, "id"));
    }

    public Page<Order> findByCreateUser(String uid, Pageable pageable) {
        return orderRepository.findByCreateUser(uid, pageable);
    }

    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Order find(long id) {
        Order order = orderRepository.findOne(id);

        order.setOrigination(order.getSrcLoc().getId());
        order.setDestination(order.getDstLoc().getId());
        order.setServiceTypeId(order.getServiceType().getId());
        order.setCustomerId(order.getCustomer().getId());
        order.setCustomerName0(order.getCustomer().getName());
        order.setGoodsId(order.getGoods().getId());
        order.setLoadingEtStr(order.getLoadingEt() == null
                ? DateUtils.stringOf(new Date())
                : DateUtils.stringOf(order.getLoadingEt()));

        return order;
    }

    public Iterable<Order> findByOrderNo(String orderNo) {
        return orderRepository.findByOrderNoLike("%" + orderNo + "%");
    }

    public Iterable<Order> findByCustomerName(String customerName) {
        return orderRepository.findByCustomerNameLike("%" + customerName + "%");
    }

    @Transactional("transactionManager")
    public void moveToHistory(long id) {
        Order order = find(id);

        OrderHistory orderHistory = newOrderHistory(order);
        orderHistoryRepository.save(orderHistory);
    }

    private OrderHistory newOrderHistory(Order order) {
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
