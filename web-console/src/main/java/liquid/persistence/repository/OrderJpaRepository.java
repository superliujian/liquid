package liquid.persistence.repository;

import liquid.persistence.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/5/13
 * Time: 12:39 AM
 */
public interface OrderJpaRepository extends JpaRepository<Order, Long> {}
