package liquid.order.persistence.repository;

import liquid.order.persistence.domain.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/28/13
 * Time: 5:19 PM
 */
public interface OrderRepository extends CrudRepository<OrderEntity, Long>, JpaRepository<OrderEntity, Long> {
    /**
     * Criteria Query
     *
     * @param specification
     * @param pageable
     * @return
     */
    Page<OrderEntity> findAll(Specification<OrderEntity> specification, Pageable pageable);

    /**
     * TODO: Low Efficiency
     *
     * @param uid
     * @param pageable
     * @return
     */
    @Query(value = "SELECT o FROM ORD_ORDER o LEFT JOIN FETCH o.railway r WHERE o.createdBy = ?1",
            countQuery = "SELECT count(o) FROM ORD_ORDER o WHERE o.createdBy = ?1")
    Page<OrderEntity> findByCreatedBy(String uid, Pageable pageable);

    /**
     * TODO: Low Efficiency
     *
     * @param status
     * @param pageable
     * @return
     */
    Page<OrderEntity> findByStatus(Integer status, Pageable pageable);

    Page<OrderEntity> findByOrderNoLike(String orderNo, Pageable pageable);

    Iterable<OrderEntity> findByOrderNoLike(String orderNo);

    Page<OrderEntity> findByCustomerIdAndCreatedBy(Long customerId, String createdBy, Pageable pageable);
}