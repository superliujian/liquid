package liquid.operation.repository;

import liquid.operation.domain.Customer;
import liquid.persistence.PageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/24/13
 * Time: 11:14 PM
 */
public interface CustomerRepository extends PageRepository<Customer> {
    Customer findByName(String name);

    Iterable<Customer> findByQueryNameLike(String queryName);

    Page<Customer> findByQueryNameLike(String queryName, Pageable pageable);
}
