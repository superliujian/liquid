package liquid.persistence.repository;

import liquid.persistence.domain.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/28/13
 * Time: 5:19 PM
 */
public interface OrderRepository extends CrudRepository<OrderEntity, Long>, JpaRepository<OrderEntity, Long> {
    Page<OrderEntity> findByCreateUser(String uid, Pageable pageable);

    Iterable<OrderEntity> findByOrderNoLike(String orderNo);

    Iterable<OrderEntity> findByCustomerNameLike(String cumtomerName);
}