package liquid.persistence.repository;

import liquid.persistence.domain.Customer;
import org.springframework.data.repository.CrudRepository;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/24/13
 * Time: 11:14 PM
 */
public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
