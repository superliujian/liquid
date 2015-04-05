package liquid.operation.service;

import liquid.operation.domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by Tao Ma on 4/5/15.
 */
public interface InternalCustomerService extends CustomerService {
    Page<Customer> findAll(Pageable pageable);

    Page<Customer> findByQueryNameLike(String name, Pageable pageable);

    Iterable<Customer> findByNameLike(String name);

    Customer save(Customer customer);
}
