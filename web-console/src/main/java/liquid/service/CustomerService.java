package liquid.service;

import liquid.persistence.domain.Customer;
import liquid.persistence.domain.OrderEntity;
import liquid.persistence.repository.CustomerRepository;
import liquid.validation.FormValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/13/13
 * Time: 6:28 PM
 */
@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Iterable<Customer> findAll() {
        return customerRepository.findOrderByName();
    }

    public Customer find(long id) {
        return customerRepository.findOne(id);
    }

    public Iterable<Customer> findByName(String name) {
        Iterable<Customer> customers = customerRepository.findByNameLike("%" + name + "%");
        return customers;
    }

    /**
     * If customer name is exactly equals to the one in database, the customer id is set.
     *
     * @param order
     * @return
     */
    public FormValidationResult validateCustomer(OrderEntity order) {
        long id = order.getCustomerId();
        String name = order.getCustomerName0();
        if (id == 0L) {
            if (null != name && name.trim().length() > 0) {
                Customer customer = customerRepository.findByName(name);
                if (null != customer) {
                    order.setCustomerId(customer.getId());
                    return FormValidationResult.newSuccess();
                }
            } else {
                return FormValidationResult.newFailure("invalid.customer");
            }
        }

        Customer customer = find(id);
        if (name == null) return FormValidationResult.newFailure("invalid.customer");

        if (null == customer) return FormValidationResult.newFailure("invalid.customer");

        if (name.equals(customer.getName())) return FormValidationResult.newSuccess();
        else return FormValidationResult.newFailure("invalid.customer");
    }
}
