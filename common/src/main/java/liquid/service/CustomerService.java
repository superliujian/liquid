package liquid.service;

import liquid.persistence.domain.CustomerEntity;
import liquid.persistence.repository.CustomerRepository;
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

    public Iterable<CustomerEntity> findByNameLike(String name) {
        Iterable<CustomerEntity> customers = customerRepository.findByQueryNameLike("%" + name + "%");
        return customers;
    }

    public CustomerEntity findByName(String name) {
        return customerRepository.findByName(name);
    }
}
