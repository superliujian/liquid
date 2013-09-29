package liquid.service;

import liquid.persistence.domain.Cargo;
import liquid.persistence.domain.Customer;
import liquid.persistence.domain.Order;
import liquid.persistence.repository.CargoRepository;
import liquid.persistence.repository.CustomerRepository;
import liquid.persistence.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private CustomerRepository customerRepository;

    @Autowired
    private CargoRepository cargoRepository;

    @Autowired
    private OrderRepository orderRepository;

    public void create(Order order) {
        Customer customer = customerRepository.findOne(order.getCustomerId());
        Cargo cargo = cargoRepository.findOne(order.getCargoId());
        order.setCustomer(customer);
        order.setCargo(cargo);
        logger.debug("Order: {}", order);
        orderRepository.save(order);
    }
}
