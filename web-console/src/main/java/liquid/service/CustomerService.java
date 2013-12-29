package liquid.service;

import liquid.persistence.domain.Customer;
import liquid.persistence.domain.Order;
import liquid.persistence.repository.CustomerRepository;
import liquid.validation.FormValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;

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
        return customerRepository.findByNameLike("%" + name + "%");
    }

    public FormValidationResult validateCustomer(long id, String name) {
        Customer customer = find(id);
        if (name == null) {
            return FormValidationResult.newFailure("");
        }

        if (null == customer) {
            return FormValidationResult.newFailure("");
        }

        if (name.equals(customer.getName())) {
            return FormValidationResult.newSuccess();
        } else {
            return FormValidationResult.newFailure("");
        }
    }
}
