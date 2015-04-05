package liquid.operation.service;

import liquid.operation.domain.Customer;
import liquid.operation.repository.CustomerRepository;
import liquid.service.AbstractCachedService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/13/13
 * Time: 6:28 PM
 */
@Service
public class CustomerServiceImpl extends AbstractCachedService<Customer, CustomerRepository>
        implements InternalCustomerService {
    @Override
    public void doSaveBefore(Customer entity) { }

    @Override
    public Page<Customer> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Customer findByName(String name) {
        return repository.findByName(name);
    }

    public Iterable<Customer> findByNameLike(String name) {
        Iterable<Customer> customers = repository.findByQueryNameLike("%" + name + "%");
        return customers;
    }

    @Override
    public Page<Customer> findByQueryNameLike(String name, Pageable pageable) {
        return repository.findByQueryNameLike("%" + name + "%", pageable);
    }

    @Override
    public Iterable<Customer> findByQueryNameLike(String name) {
        List<Customer> result = new ArrayList<>();

        Iterable<Customer> entities = super.findAll();
        for (Customer entity : entities) {
            int index = entity.getQueryName().indexOf(name);
            if (index > -1) result.add(entity);
        }
        return result;
    }
}
