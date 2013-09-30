package liquid.service;

import liquid.context.BusinessContext;
import liquid.persistence.domain.Cargo;
import liquid.persistence.domain.Customer;
import liquid.persistence.domain.Order;
import liquid.persistence.repository.CargoRepository;
import liquid.persistence.repository.CustomerRepository;
import liquid.persistence.repository.OrderRepository;
import liquid.service.bpm.ActivitiEngineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

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
    private ActivitiEngineService bpmEngineService;

    public void save(Order order) {
        Customer customer = customerRepository.findOne(order.getCustomerId());
        Cargo cargo = cargoRepository.findOne(order.getCargoId());
        order.setCustomer(customer);
        order.setCargo(cargo);
        logger.debug("Order: {}", order);
        orderRepository.save(order);
    }

    public void submit(Order order) {
        save(order);
        logger.debug("username: {}", businessContext.getUsername());
        bpmEngineService.startProcess(businessContext.getUsername(), new HashMap<String, Object>());
    }
}
