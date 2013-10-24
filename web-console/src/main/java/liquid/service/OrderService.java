package liquid.service;

import liquid.context.BusinessContext;
import liquid.persistence.domain.Cargo;
import liquid.persistence.domain.Customer;
import liquid.persistence.domain.Location;
import liquid.persistence.domain.Order;
import liquid.persistence.repository.CargoRepository;
import liquid.persistence.repository.CustomerRepository;
import liquid.persistence.repository.LocationRepository;
import liquid.persistence.repository.OrderRepository;
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private CargoRepository cargoRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ActivitiEngineService bpmService;

    public Order newOrder(List<Location> locations) {
        Order order = new Order();
        Location second = CollectionUtils.tryToGet2ndElement(locations);
        order.setDestination(second.getId());
        order.setLoadingEtStr(DateUtils.stringOf(new Date()));
        return order;
    }

    public void save(Order order) {
        Customer customer = customerRepository.findOne(order.getCustomerId());
        Cargo cargo = cargoRepository.findOne(order.getCargoId());
        Location srcLoc = locationRepository.findOne(order.getOrigination());
        Location dstLoc = locationRepository.findOne(order.getDestination());
        order.setCustomer(customer);
        order.setCargo(cargo);
        order.setSrcLoc(srcLoc);
        order.setDstLoc(dstLoc);
        if (StringUtils.valuable(order.getLoadingEtStr()))
            order.setLoadingEt(DateUtils.dateOf(order.getLoadingEtStr()));
        logger.debug("Order: {}", order);
        orderRepository.save(order);
    }

    public void submit(Order order) {
        save(order);
        logger.debug("username: {}", businessContext.getUsername());

        Map<String, Object> variableMap = new HashMap<>();
        variableMap.put("loadingType", order.getLoadingType());
        variableMap.put("hasDelivery", order.isHasDelivery());
        variableMap.put("orderOwner", businessContext.getUsername());
        bpmService.startProcess(businessContext.getUsername(), order.getId(), variableMap);
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
        order.setCustomerId(order.getCustomer().getId());
        order.setCargoId(order.getCargo().getId());
        order.setLoadingEtStr(order.getLoadingEt() == null
                ? DateUtils.stringOf(new Date())
                : DateUtils.stringOf(order.getLoadingEt()));

        return order;
    }

    public Iterable<Order> findByCustomerName(String customerName) {
        return orderRepository.findByCustomerNameLike("%" + customerName + "%");
    }
}
