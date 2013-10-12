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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
    private ActivitiEngineService bpmService;

    public void save(Order order) {
        Customer customer = customerRepository.findOne(order.getCustomerId());
        Cargo cargo = cargoRepository.findOne(order.getCargoId());
        Location srcLoc = locationRepository.findOne(Long.valueOf(order.getOrigination()));
        Location dstLoc = locationRepository.findOne(Long.valueOf(order.getDestination()));
        order.setCustomer(customer);
        order.setCargo(cargo);
        order.setSrcLoc(srcLoc);
        order.setDstLoc(dstLoc);
        logger.debug("Order: {}", order);
        orderRepository.save(order);
    }

    public void submit(Order order) {
        save(order);
        logger.debug("username: {}", businessContext.getUsername());

        Map<String, Object> variableMap = new HashMap<>();
        variableMap.put("loadingType", order.getLoadingType());
        variableMap.put("hasDelivery", order.isHasDelivery());
        bpmService.startProcess(businessContext.getUsername(), order.getId(), variableMap);
    }

    public Order findByTaskId(String taskId) {
        long orderId = bpmService.getOrderIdByTaskId(taskId);
        return find(orderId);
    }

    public Order find(long id) {
        Order order = orderRepository.findOne(id);

        order.setOrigination(String.valueOf(order.getSrcLoc().getId()));
        order.setDestination(String.valueOf(order.getDstLoc().getId()));
        order.setCustomerId(order.getCustomer().getId());
        order.setCargoId(order.getCargo().getId());

        return order;
    }
}
