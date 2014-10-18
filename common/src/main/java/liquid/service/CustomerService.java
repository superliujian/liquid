package liquid.service;

import liquid.persistence.domain.CustomerEntity;
import liquid.persistence.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/13/13
 * Time: 6:28 PM
 */
@Service
public class CustomerService extends AbstractService<CustomerEntity, CustomerRepository> {
    public CustomerEntity find(Long id) {
        return repository.findOne(id);
    }

    public Iterable<CustomerEntity> findAll() {
        return repository.findAll();
    }

    public Page<CustomerEntity> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public CustomerEntity findByName(String name) {
        return repository.findByName(name);
    }

    public Iterable<CustomerEntity> findByNameLike(String name) {
        Iterable<CustomerEntity> customers = repository.findByQueryNameLike("%" + name + "%");
        return customers;
    }

    public Page<CustomerEntity> findByQueryNameLike(String name, Pageable pageable) {
        return repository.findByQueryNameLike("%" + name + "%", pageable);
    }

    @Override
    public void doSaveBefore(CustomerEntity entity) { }
}
