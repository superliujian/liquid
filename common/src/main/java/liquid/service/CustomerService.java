package liquid.service;

import liquid.persistence.domain.CustomerEntity;
import liquid.persistence.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/13/13
 * Time: 6:28 PM
 */
@Service
public class CustomerService extends AbstractCachedService<CustomerEntity, CustomerRepository> {
    @Override
    public void doSaveBefore(CustomerEntity entity) { }

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

    public Iterable<CustomerEntity> findByQueryNameLike(String name) {
        List<CustomerEntity> result = new ArrayList<>();

        Iterable<CustomerEntity> entities = super.findAll();
        for (CustomerEntity entity : entities) {
            int index = entity.getQueryName().indexOf(name);
            if (index > -1) result.add(entity);
        }
        return result;
    }
}
