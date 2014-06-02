package liquid.service;

import liquid.domain.Order;
import liquid.persistence.domain.CustomerEntity;
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

    public Iterable<CustomerEntity> findAll() {
        return customerRepository.findOrderByName();
    }

    public CustomerEntity find(long id) {
        return customerRepository.findOne(id);
    }

    public Iterable<CustomerEntity> findByName(String name) {
        Iterable<CustomerEntity> customers = customerRepository.findByNameLike("%" + name + "%");
        return customers;
    }

    /**
     * If customer name is exactly equals to the one in database, the customer id is set.
     *
     * @param order
     * @return
     */
    public FormValidationResult validateCustomer(Order order) {
        long id = order.getCustomerId();
        String name = order.getCustomerName();
        if (id == 0L) {
            if (null != name && name.trim().length() > 0) {
                CustomerEntity customer = customerRepository.findByName(name);
                if (null != customer) {
                    order.setCustomerId(customer.getId());
                    return FormValidationResult.newSuccess();
                }
            } else {
                return FormValidationResult.newFailure("invalid.customer");
            }
        }

        CustomerEntity customer = find(id);
        if (name == null) return FormValidationResult.newFailure("invalid.customer");

        if (null == customer) return FormValidationResult.newFailure("invalid.customer");

        if (name.equals(customer.getName())) return FormValidationResult.newSuccess();
        else return FormValidationResult.newFailure("invalid.customer");
    }
}
