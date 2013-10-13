package liquid.persistence.repository;

import liquid.persistence.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/28/13
 * Time: 5:19 PM
 */
public interface OrderRepository extends CrudRepository<Order, Long>, JpaRepository<Order, Long> {
    Iterable<Order> findByCustomerNameLike(String cumtomerName);
}