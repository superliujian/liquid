package liquid.persistence.repository;

import liquid.persistence.domain.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/24/13
 * Time: 11:14 PM
 */
public interface CustomerRepository extends CrudRepository<Customer, Long> {
    @Query(value = "SELECT * FROM CUSTOMER ORDER BY CONVERT(NAME USING GBK)", nativeQuery = true) Iterable<Customer> findOrderByName();

    Iterable<Customer> findByNameLike(String name);
}
